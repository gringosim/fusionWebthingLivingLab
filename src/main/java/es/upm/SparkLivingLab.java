package es.upm;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.port;
import static spark.Spark.secure;

import com.mongodb.client.model.Filters;
import es.upm.ll.uAALBridge;
import com.mongodb.*;

import static com.mongodb.client.model.Projections.*;

import com.mongodb.DBObject;
import org.bson.Document;
import com.qmetric.spark.authentication.AuthenticationDetails;
import es.upm.auth.JWTAuthenticationFilter;
import es.upm.interfaces.UserUtils;
import es.upm.ll.regex.TextRecognizer;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SparkLivingLab {

	public static boolean up = true;
	private static UserUtils userUtils;
	public static void setUserUtils(Object service) {
		userUtils = (UserUtils) service;
	}

	public static void main(String[] args)  {

		dataBaseManager.dbInit();

		long docCount= dataBaseManager.collection.countDocuments();
		if (dataBaseManager.docCount==0) {
			dataBaseManager.buildDataBase();

		}


		System.out.println("Current dir: " + System.getProperty("user.dir"));

		secure("password.jks", "password", null, null);


		post("/auth", (request, response) -> { return AuthManager.handleRequest(request, response, userUtils); });

		before("/device",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/plan4act",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/things",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/analizetext",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("things/:thingId/properties/:propertyName",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/things/:thingId",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/:thingId/properties",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("things/add",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));

		//example : post {"username":"plan4act","password":"proactive"} it create a token for the authentication


		get("/info", (request, response) -> "Welcome to LivingLab!!! Currently "+dataBaseManager.docCount+" devices available.");


		get("/things", (request, response) -> {
			response.header("Content-Type", "application/json");
			//set schema context, name and type
			JSONObject obj = new JSONObject();
			JSONArray ctx = new JSONArray();
			ctx.put("http://iot.schema.org");
			obj.put("@context", ctx);
			JSONArray type = new JSONArray();
			type.put("Thing");
			obj.put("@type", type);
			obj.put("name", "Smart Home Living Lab Devices");

			//set security scheme
			JSONArray sec = new JSONArray();
			JSONObject bar = new JSONObject();
			bar.put("authorizationUrl", "/auth");
			bar.put("scheme", "bearer");
			bar.put("format", "JWT");
			sec.put(bar);
			obj.put("security", sec);

			//set properties
			JSONObject props = new JSONObject();
			JSONArray devlist = new JSONArray();
			props.put("devices", devlist);
			obj.put("properties", props);
			//set devices
			List<Document> devices = (List<Document>) dataBaseManager.collection.find().into(
					new ArrayList<Document>());

			for (Document device : devices) {
				JSONObject dev = new JSONObject();
				dev.put("type", "Thing");
				dev.put("name", device.getString("name"));
				JSONObject oo = new JSONObject();
				oo.put("http:methodName", "GET");
				oo.put("href", "/things/"+device.getObjectId("_id").toString());
				JSONArray forms = new JSONArray();
				forms.put(oo);
				dev.put("forms", forms);
				System.out.println("device name = " + device.getString("name"));
				System.out.println("device href = " + device.getString("href"));
				System.out.println("obj id = " + device.getObjectId("_id").toString());
				devlist.put(dev);
			}

			return obj.toString();

		});






		// These are matched in the order they are added.

		get("things/:thingId/properties/:propertyName", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			//int idx = Integer.parseInt(index);
			DBObject propFilter = new BasicDBObject();
			propFilter.put( "href", "/things/"+index);
			try {
				Document show =  dataBaseManager.collection.find((Bson) propFilter).first();
				JSONObject showProp= new JSONObject(show.toJson());
				String alias = showProp.get("name").toString();
				JSONObject view= new JSONObject();
				//Implementación de Bridge a uAAL
				uAALBridge bridgeTouAAL = new uAALBridge();
				String vDevice=alias;
				System.out.println("este es vDevice"+vDevice);
				String vadURL = "http://192.168.1.68:8181/uAALServices?device="+vDevice;
				String res=bridgeTouAAL.sendSyncRedirectToLL(vadURL);
				System.out.println("Esto es res "+res);
				JSONObject getSt = new JSONObject(bridgeTouAAL.sendSyncRedirectToLL(vadURL));
				System.out.println("esto es getSt "+getSt.toString());
				view.put("status",getSt.get(alias));// esta línea debería devolver el estado de la propiedad del dispositivo de acuerdo a WoT Mozilla
				System.out.println("este es view"+view.toString());
				return view;

			}catch (Exception e) {
				e.printStackTrace();
				return "Unable to reach server or Error in server response interpretation";

						}
				}
		);


		get("things/:thingId/properties", (request, response) -> {
			String index = request.params(":thingId");
			//int idx = Integer.parseInt(index);
			response.header("Content-Type", "application/json");
			DBObject propFilter = new BasicDBObject();
			propFilter.put( "href", "/things/"+index);

			try {
				Document show =  dataBaseManager.collection.find((Bson) propFilter).first();
				JSONObject showProp= new JSONObject(show.toJson());
				return showProp.get("properties");
				}

			catch (JSONException e) {
				return "error a determinar";
				}
			}
		);


		get("/things/:thingId", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			//int idx = Integer.parseInt(index);
			DBObject thingFilter = new BasicDBObject();
			thingFilter.put( "href", "/things/"+index);
			Document aThing = new Document(dataBaseManager.collection.find((Bson)thingFilter).first());
			Document projection = new Document();
			projection.append("_id",0).append("Status",0);
			aThing = dataBaseManager.collection.find((Bson) thingFilter).projection(exclude("status","_id")).first();
			JSONObject showThing= new JSONObject(aThing);
			return showThing;
				}
		);




		put("things/:thingId/properties/:propertyName", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			//int idx = Integer.parseInt(index);

			try {
				DBObject thingFilter = new BasicDBObject();
				thingFilter.put( "href", "/things/"+index);
				Document thisThing = new Document(dataBaseManager.collection.find((Bson)thingFilter).first());
				JSONObject o = new JSONObject(request.body());
				JSONObject inputDev = new JSONObject();
				//inputDev.put("status",o.get(thisThing.get("name").toString()));
				inputDev.put("status",o.get("status"));

				dataBaseManager.collection.updateOne(Filters.eq("href","/things/"+index), new Document("$set", new Document("status",inputDev.get("status"))));
				Document uDevice=dataBaseManager.collection.find((Bson) thingFilter).first();
				JSONObject alias_obj =new JSONObject(uDevice.toJson());
				String alias = alias_obj.get("name").toString();


				uAALBridge bridgeTouAAL = new uAALBridge();
				String toggleStatus=o.get("status").toString().toLowerCase();
				if (toggleStatus=="true"){toggleStatus="100";}else{toggleStatus="0";}
				String vDevice=alias;
				if (vDevice.contains("door")){if (toggleStatus!="0"){toggleStatus="1";}}
				JSONObject reply = new JSONObject(bridgeTouAAL.sendSyncRequestToLL(toggleStatus, vDevice));
				System.out.println(reply);
				if (vDevice.contains("door")){
					String rep=reply.get(thisThing.get("name").toString()).toString();//COLOCAR ALIAS SEGURAMENTE SALE DE LA BASE DE DATOS!!!!!!!!
					JSONObject resp=new JSONObject();
					resp.put(alias,rep);
					System.out.println(resp);
					return resp;
				}
				else {String rep=reply.getString(thisThing.get("name").toString());
					JSONObject resp = new JSONObject();
					resp.put(alias, rep);
					System.out.println(resp);
					return resp;
				}

				//return alias;

			} catch (JSONException e) {
						return "error a determinar";
					}
				}
		);



		put("things/add", (request, response) -> {
            /*
            ---------------------------
            JSON Body request
            ---------------------------
            {
            "Device Name": "String value",
            "Initial Status": boolean,
            "Thing Type": "String value",
            "Input Type":"String value",
            "Output Type":"String value",
            "Property name":"String value",
            "Property Type":"String value",
            "Writable Device": boolean
            }
            ================================================
             */
			long countDocs= dataBaseManager.collection.countDocuments();
            DevicesCreator addDev = new DevicesCreator();
            try {
                JSONObject vars = new JSONObject(request.body()) {
                };
                System.out.println(request.body());

                addDev.name=vars.get("Device Name").toString();
               	addDev.devHref="/"+countDocs;
                addDev.inputThingType=vars.get("Thing Type").toString();
                addDev.inputType=vars.get("Input Type").toString();
                addDev.outputType=vars.get("Output Type").toString();
                addDev.propertyName=vars.get("Property name").toString();
                addDev.propType=vars.get("Property Type").toString();
                addDev.writable=vars.getBoolean("Writable Device");
                addDev.devSerial=countDocs;
                addDev.devStatus=vars.getBoolean("status");
                Document LLDev = new Document(DevicesCreator.assembleDevice(addDev));

                dataBaseManager.collection.insertOne(LLDev);

                return "Device "+addDev.name+" has been created successfully";


            } catch (JSONException e) {
                return "Unable to create the Device";
            }
        }


        );
	}
}