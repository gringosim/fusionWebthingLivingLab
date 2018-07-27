/**
 * Java Web Thing server implementation.
 */
package org.mozilla.iot.webthing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.net.ssl.SSLServerSocketFactory;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;
import fi.iki.elonen.router.RouterNanoHTTPD;

/**
 * Server to represent a Web Thing over HTTP.
 */
public class WebThingServer extends RouterNanoHTTPD {
    private int port;
    private String ip;
    private ThingsType things;
    private String name;
    private String hostname;
    private List<String> hosts;
    private boolean isTls;
    private JmDNS jmdns;



    public WebThingServer(ThingsType things,
                          Integer port,
                          String hostname,
                          SSLOptions sslOptions)
            throws IOException, NullPointerException {
        super(port);
        this.port = port;
        this.things = things;
        this.name = things.getName();
        this.ip = Utils.getIP();
        this.isTls = sslOptions != null;
        this.hostname = hostname;

        this.hosts = new ArrayList<>();
        this.hosts.add("127.0.0.1");
        this.hosts.add(String.format("127.0.0.1:%d", this.port));
        this.hosts.add("localhost");
        this.hosts.add(String.format("localhost:%d", this.port));
        this.hosts.add(this.ip);
        this.hosts.add(String.format("%s:%d", this.ip, this.port));

        if (this.hostname != null) {
            this.hosts.add(this.hostname);
            this.hosts.add(String.format("%s:%d", this.hostname, this.port));
        }

        if (this.isTls) {
            super.makeSecure(sslOptions.getSocketFactory(),
                             sslOptions.getProtocols());
        }

        this.setRoutePrioritizer(new InsertionOrderRoutePrioritizer());

        if (MultipleThings.class.isInstance(things)) {
            String wsBase = String.format("%s://%s:%d/",
                                          this.isTls ? "wss" : "ws",
                                          this.ip,
                                          this.port);

            List<Thing> list = things.getThings();
            for (int i = 0; i < list.size(); ++i) {
                Thing thing = list.get(i);
                thing.setHrefPrefix(String.format("/%d", i));
                thing.setWsHref(wsBase + Integer.toString(i));
            }

            // These are matched in the order they are added.
            addRoute("/:thingId/properties/:propertyName",
                     PropertyHandler.class,
                     this.things,
                     this.hosts);
            addRoute("/:thingId/properties",
                     PropertiesHandler.class,
                     this.things,
                     this.hosts);


            addRoute("/", ThingsHandler.class, this.things, this.hosts);
        } else {
            String wsHref = String.format("%s://%s:%d",
                                          this.isTls ? "wss" : "ws",
                                          this.ip,
                                          this.port);

            Thing thing = things.getThing(0);
            thing.setWsHref(wsHref);

            // These are matched in the order they are added.
            addRoute("/properties/:propertyName",
                     PropertyHandler.class,
                     this.things,
                     this.hosts);
            addRoute("/properties",
                     PropertiesHandler.class,
                     this.things,
                     this.hosts);


        }
    }

    /**
     * Start listening for incoming connections.
     *
     * @param daemon Whether or not to daemonize the server
     * @throws IOException on failure to listen on port
     */
    public void start(boolean daemon) throws IOException {
        this.jmdns = JmDNS.create(InetAddress.getLocalHost());

        String systemHostname = this.jmdns.getHostName();
        if (systemHostname.endsWith(".")) {
            systemHostname =
                    systemHostname.substring(0, systemHostname.length() - 1);
        }
        this.hosts.add(systemHostname);
        this.hosts.add(String.format("%s:%d", systemHostname, this.port));

        ServiceInfo serviceInfo = ServiceInfo.create("_webthing._tcp.local",
                                                     this.name,
                                                     null,
                                                     this.port,
                                                     "path=/");
        this.jmdns.registerService(serviceInfo);

        super.start(NanoHTTPD.SOCKET_READ_TIMEOUT, daemon);
    }

    /**
     * Stop listening.
     */
    public void stop() {
        this.jmdns.unregisterAllServices();
        super.stop();
    }

    interface ThingsType {
        /**
         * Get the thing at the given index.
         *
         * @return The thing, or null.
         */
        Thing getThing(int idx);

        /**
         * Get the list of things.
         *
         * @return The list of things.
         */
        List<Thing> getThings();

        /**
         * Get the mDNS server name.
         *
         * @return The server name.
         */
        String getName();
    }


    /**
     * Class to hold options required by SSL server.
     */
    public static class SSLOptions {
        private String path;
        private String password;
        private String[] protocols;

        /**
         * Initialize the object.
         *
         * @param keystorePath     Path to the Java keystore (.jks) file
         * @param keystorePassword Password to open the keystore
         */
        public SSLOptions(String keystorePath, String keystorePassword) {
            this(keystorePath, keystorePassword, null);
        }

        /**
         * Initialize the object.
         *
         * @param keystorePath     Path to the Java keystore (.jks) file
         * @param keystorePassword Password to open the keystore
         * @param protocols        List of protocols to enable. Documentation
         *                         found here: https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLServerSocket.html#setEnabledProtocols-java.lang.String:A-
         */
        public SSLOptions(String keystorePath,
                          String keystorePassword,
                          String[] protocols) {
            this.path = keystorePath;
            this.password = keystorePassword;
            this.protocols = protocols;
        }

        /**
         * Create an SSLServerSocketFactory as required by NanoHTTPD.
         *
         * @return The socket factory.
         * @throws IOException If server fails to bind.
         */
        public SSLServerSocketFactory getSocketFactory() throws IOException {
            return NanoHTTPD.makeSSLSocketFactory(this.path,
                                                  this.password.toCharArray());
        }

        /**
         * Get the list of enabled protocols.
         *
         * @return The list of protocols.
         */
        public String[] getProtocols() {
            return this.protocols;
        }
    }

    /**
     * Base handler that responds to every request with a 405 Method Not
     * Allowed.
     */
    public static class BaseHandler implements UriResponder {
        /**
         * Add necessary CORS headers to response.
         *
         * @param response Response to add headers to
         * @return The Response object.
         */
        public Response corsResponse(Response response) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Headers",
                               "Origin, X-Requested-With, Content-Type, Accept");
            response.addHeader("Access-Control-Allow-Methods",
                               "GET, HEAD, PUT, POST, DELETE");
            return response;
        }

        /**
         * Handle a GET request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return 405 Method Not Allowed response.
         */
        public Response get(UriResource uriResource,
                            Map<String, String> urlParams,
                            IHTTPSession session) {
            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED,
                                                                 null,
                                                                 null));
        }

        /**
         * Handle a PUT request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return 405 Method Not Allowed response.
         */
        public Response put(UriResource uriResource,
                            Map<String, String> urlParams,
                            IHTTPSession session) {
            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED,
                                                                 null,
                                                                 null));
        }

        /**
         * Handle a POST request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return 405 Method Not Allowed response.
         */
        public Response post(UriResource uriResource,
                             Map<String, String> urlParams,
                             IHTTPSession session) {
            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED,
                                                                 null,
                                                                 null));
        }

        /**
         * Handle a DELETE request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return 405 Method Not Allowed response.
         */
        public Response delete(UriResource uriResource,
                               Map<String, String> urlParams,
                               IHTTPSession session) {
            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED,
                                                                 null,
                                                                 null));
        }

        /**
         * Handle any other request.
         *
         * @param method      The HTTP method
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return 405 Method Not Allowed response.
         */
        public Response other(String method,
                              UriResource uriResource,
                              Map<String, String> urlParams,
                              IHTTPSession session) {
            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED,
                                                                 null,
                                                                 null));
        }

        /**
         * Get a parameter from the URI.
         *
         * @param uri   The URI
         * @param index Index of the parameter
         * @return The URI parameter, or null if index was invalid.
         */
        public String getUriParam(String uri, int index) {
            String[] parts = uri.split("/");
            if (parts.length <= index) {
                return null;
            }

            return parts[index];
        }

        /**
         * Parse a JSON body.
         *
         * @param session The HTTP session
         * @return The parsed JSON body as a JSONObject, or null on error.
         */
        public JSONObject parseBody(IHTTPSession session) {
            Integer contentLength = Integer.parseInt(session.getHeaders()
                                                            .get("content-length"));
            byte[] buffer = new byte[contentLength];
            try {
                session.getInputStream().read(buffer, 0, contentLength);
                JSONObject obj = new JSONObject(new String(buffer));
                return obj;
            } catch (IOException e) {
                return null;
            }
        }

        /**
         * Get the thing this request is for.
         *
         * @param uriResource The URI resource that was matched
         * @param session     The HTTP session
         * @return The thing, or null if not found.
         */
        public Thing getThing(UriResource uriResource, IHTTPSession session) {
            ThingsType things = uriResource.initParameter(0, ThingsType.class);

            String thingId = this.getUriParam(session.getUri(), 1);
            int id;
            try {
                id = Integer.parseInt(thingId);
            } catch (NumberFormatException e) {
                id = 0;
            }

            return things.getThing(id);
        }

        /**
         * Validate Host header.
         *
         * @param uriResource The URI resource that was matched
         * @param session     The HTTP session
         */
        public boolean validateHost(UriResource uriResource,
                                    IHTTPSession session) {
            List<String> hosts = uriResource.initParameter(1, List.class);

            String host = session.getHeaders().get("host");
            if (host != null && hosts.contains(host)) {
                return true;
            }

            return false;
        }
    }

    /**
     * Handle a request to / when the server manages multiple things.
     */
    public static class ThingsHandler extends BaseHandler {
        /**
         * Handle a GET request, including websocket requests.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return The appropriate response.
         */
        @Override
        public Response get(UriResource uriResource,
                            Map<String, String> urlParams,
                            IHTTPSession session) {
            if (!validateHost(uriResource, session)) {
                return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN,
                                                        null,
                                                        null);
            }

            ThingsType things = uriResource.initParameter(0, ThingsType.class);

            JSONArray list = new JSONArray();
            for (Thing thing : things.getThings()) {
                list.put(thing.asThingDescription());
            }

            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.OK,
                                                                 "application/json",
                                                                 list.toString()));

        }
    }


    /**
     * Handle a request to /properties.
     */
    public static class PropertiesHandler extends BaseHandler {
        /**
         * Handle a GET request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return The appropriate response.
         */
        @Override
        public Response get(UriResource uriResource,
                            Map<String, String> urlParams,
                            IHTTPSession session) {
            if (!validateHost(uriResource, session)) {
                return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN,
                                                        null,
                                                        null);
            }

            Thing thing = this.getThing(uriResource, session);
            if (thing == null) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND,
                                                                     null,
                                                                     null));
            }

            // TODO: this is not yet defined in the spec

            return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.OK,
                                                                 "application/json",
                                                                 ""));
        }
    }

    /**
     * Handle a request to /properties/&lt;property&gt;.
     */
    public static class PropertyHandler extends BaseHandler {
        /**
         * Get the property name from the URI.
         *
         * @param uriResource The URI resource that was matched
         * @param session     The HTTP session
         * @return The property name.
         */
        public String getPropertyName(UriResource uriResource,
                                      IHTTPSession session) {
            ThingsType things = uriResource.initParameter(0, ThingsType.class);

            if (MultipleThings.class.isInstance(things)) {
                return this.getUriParam(session.getUri(), 3);
            } else {
                return this.getUriParam(session.getUri(), 2);
            }
        }

        /**
         * Handle a GET request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return The appropriate response.
         */
        @Override
        public Response get(UriResource uriResource,
                            Map<String, String> urlParams,
                            IHTTPSession session) {
            if (!validateHost(uriResource, session)) {
                return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN,
                                                        null,
                                                        null);
            }

            Thing thing = this.getThing(uriResource, session);
            if (thing == null) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND,
                                                                     null,
                                                                     null));
            }

            String propertyName = this.getPropertyName(uriResource, session);
            if (!thing.hasProperty(propertyName)) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND,
                                                                     null,
                                                                     null));
            }

            JSONObject obj = new JSONObject();
            try {
                Object value = thing.getProperty(propertyName);
                if (value == null) {
                    obj.put(propertyName, JSONObject.NULL);
                } else {
                    obj.putOpt(propertyName, value);
                }
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.OK,
                                                                     "application/json",
                                                                     obj.toString()));
            } catch (JSONException e) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR,
                                                                     null,
                                                                     null));
            }
        }

        /**
         * Handle a PUT request.
         *
         * @param uriResource The URI resource that was matched
         * @param urlParams   Map of URL parameters
         * @param session     The HTTP session
         * @return The appropriate response.
         */
        @Override
        public Response put(UriResource uriResource,
                            Map<String, String> urlParams,
                            IHTTPSession session) {
            if (!validateHost(uriResource, session)) {
                return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN,
                                                        null,
                                                        null);
            }

            Thing thing = this.getThing(uriResource, session);
            if (thing == null) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND,
                                                                     null,
                                                                     null));
            }

            String propertyName = this.getPropertyName(uriResource, session);
            if (!thing.hasProperty(propertyName)) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND,
                                                                     null,
                                                                     null));
            }

            JSONObject json = this.parseBody(session);
            if (json == null) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST,
                                                                     null,
                                                                     null));
            }

            if (!json.has(propertyName)) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST,
                                                                     null,
                                                                     null));
            }

            try {
                thing.setProperty(propertyName, json.get(propertyName));

                JSONObject obj = new JSONObject();
                obj.putOpt(propertyName, thing.getProperty(propertyName));
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.OK,
                                                                     "application/json",
                                                                     obj.toString()));
            } catch (JSONException e) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR,
                                                                     null,
                                                                     null));
            } catch (IllegalArgumentException e) {
                return corsResponse(NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN,
                                                                     null,
                                                                     null));
            }
        }
    }



    /**
     * A container for multiple things.
     */
    public static class MultipleThings implements ThingsType {
        private List<Thing> things;
        private String name;

        /**
         * Initialize the container.
         *
         * @param things The things to store
         * @param name   The mDNS server name
         */
        public MultipleThings(List<Thing> things, String name) {
            this.things = things;
            this.name = name;
        }

        /**
         * Get the thing at the given index.
         *
         * @param idx The index.
         */
        public Thing getThing(int idx) {
            if (idx < 0 || idx >= this.things.size()) {
                return null;
            }

            return this.things.get(idx);
        }

        /**
         * Get the list of things.
         *
         * @return The list of things.
         */
        public List<Thing> getThings() {
            return this.things;
        }

        /**
         * Get the mDNS server name.
         *
         * @return The server name.
         */
        public String getName() {
            return this.name;
        }
    }
}