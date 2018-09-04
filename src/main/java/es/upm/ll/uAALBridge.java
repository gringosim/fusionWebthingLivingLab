package es.upm.ll;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

/**
 * Created by Eugenio on 02/06/2017.
           String url = "http://192.168.2.3:4200/device?id=LUZ_CASA_ONOFF&value=1";
   Usage: (new LLConnector(this)).sendRequestToLL(url);


 */

public class uAALBridge {
    /*
 Utilities functions Living Lab
 This class define the devices of the lab.

    Description                       | Device name      | Device id           |
    ---------------------------------------------------------------------------
    Puerta principal                  | doorHome         | MAIN_DOOR
    Puerta/Ventanal Salon             | doorSalon        | LIVINGROOM_WINDOW
    Puerta del bano                   | doorBath         | BATHROOM_DOOR
    Puerta/Ventanal Cocina            | doorKitchen      | KITCHEN_WINDOW
    Persiana Dormitorio               | RollupBed        | BEDROOM_BLIND
    Persiana Cocina                   | RollupKit        | KITCHEN_BLIND
    Persiana Salon                    | RollupSal        | LIVINGROOM_BLIND
    Luz Cocina                        | KitchenLight     | KITCHEN_LIGHT
    Luz Salon                         | SalaLight        | LIVINGROOM_LIGHT
    Luz Dormitorio                    | BedroomLight     | BEDROOM_LIGHT
    Luz Servicio                      | BathroomLight    | BATHROOM_LIGHT
    Luz Sala Control                  | SCLight          | CONTROLROOM_LIGHT
    Luz Habitacion Realidad Virtual   | RVLight          | VR_ROOM_LIGHT
    Persiana Sala Control             | RollupSC         | CONTROLROOM_BLIND
    Puerta Sala Control               | doorSC           | CONTROLROOM_DOOR
    ---------------------------------------------------------------------------
    MediaPlayer                       | Media Player     | MEDIA_PLAYER
    Test light device                 | Test Light       | HOUSE_LIGHT
     */

	public static boolean connectToRemote = true;

    public static final Hashtable<String, String> devices;

    static {
    	Hashtable<String, String> aMap = new Hashtable<String, String>();
    	aMap.put("MEDIA_PLAYER", "Media Player");
    	aMap.put("HOUSE_LIGHT", "Test Light device");
        devices = aMap;
    }



    public static String BLIND = "PERSIANA_SALON";
    public static String WINDOW = "VENTANAL_SALON";
    //public static String HOUSE_LIGHT = "LUZ_CASA_ONOFF";
    public static String STREET_LIGHT = "LUZ_CALLE_ONOFF";
    public static String GARDEN_LIGHT = "LUZ_PORCHE_ONOFF";
    public static String SCEXT_LIGHT = "LUZ_SCEXT_ONOFF";
    public static String ENTRY_LIGHT = "LUZ_ENTRADA_ONOFF";
    public static String MAIN_DOOR = "PUERTA_PRINCIPAL";
    public static String MAIN_BLIND = "PERSIANAS_CASA";
    public static String ENTRY_ROOM_LIGHT = "LUZ_HABITACION_ENTRADA_ONOFF";
    public static String HEADBOARD_ROOM_LIGHT = "LUZ_HABITACION_CABECERA_ONOFF";
    public static String ENTRY_LIVINGROOM_LIGHT = "LUZ_SALON_ENTRADA_ONOFF";

    public static String MEDIA_PLAYER = "MEDIA_PLAYER";
    public static String HOUSE_LIGHT = "HOUSE_LIGHT";

    public static String OPEN="1";
    public static String CLOSE="0";
    public static String ON="1";
    public static String OFF="0";


    public uAALBridge() {
      
    }

    public String sendSyncRequestToLL(String state, String dev){
        // params comes from the execute() call: params[0] is the url.
        //device [VENTANAL_SALON, PERSIANA_SALON] state [Open 1,Close 0]
 
        if (true) {
            try {
                return downloadUrl(encodeURL(state, dev));
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        } else {
            return "Unable to retrieve web page. No connection.";
        }

    }

    public String sendSyncRedirectToLL(String uri){
        // params comes from the execute() call: params[0] is the url.
        //device [VENTANAL_SALON, PERSIANA_SALON] state [Open 1,Close 0]
        if (true) {
            try {
                return downloadUrl(uri);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        } else {
            return "Unable to retrieve web page. No connection.";
        }

    }


    public String encodeURL(String state, String dev){
        String url = "http://192.168.1.102:8181/uAALServices?device="+dev+"&value="+state;
        System.out.println("La URL para Vadym es http://192.168.1.102:8181/uAALServices?device="+dev+"&value="+state);
        //String url = "http://138.4.10.224/remote.htm?id="+dev+"&value="+state;
        if (!connectToRemote){
        	//change url request to uAAL service
        }
        System.out.println(this.getClass().getSimpleName()+" Connecting to: "+url);
        return url;
    }

    public void sendRequestToLL(String state,String dev) {
        //device [VENTANAL_SALON, PERSIANA_SALON] state [Open 1,Close 0]
        if (true) {
           //new SendGetRequestTask().execute(encodeURL(state, dev));
        } else {
            //log network error
        }

    }


   private abstract class SendGetRequestTask extends SwingWorker<String, Void> {
        
    	private String[] urls_tocall;
    	
    	SendGetRequestTask(String... urls){
    		this.urls_tocall = urls;
    	}
    	
    	
    	public abstract void callback(String response);
    	
    	@Override
    	protected String doInBackground() {

            // params comes from the execute() call: params[0] is the url.
            try {
            	String response = downloadUrl(this.urls_tocall[0]);
            	this.callback(response);
            	return response;
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        
	
    }



    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
           System.out.println("The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            //String contentAsString = readIt(is, len);
            String contentAsString = readUTF8Text(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Reads all the InputStream and converts it to a String.
    public String readUTF8Text(InputStream stream)  throws IOException, UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

}


