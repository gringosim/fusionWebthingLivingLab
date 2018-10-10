package es.upm;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.port;
import static spark.Spark.secure;

import es.upm.ll.LlBridge;
import es.upm.ll.ServiceInterface;
import es.upm.ll.uAALBridge;
import org.bson.BSONObject;
import org.bson.types.ObjectId;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Sorts;
import java.math.BigInteger;
import java.sql.Array;
import java.util.Arrays;
import com.mongodb.DBObject;
import org.bson.Document;
import com.qmetric.spark.authentication.AuthenticationDetails;
import es.upm.auth.JWTAuthenticationFilter;
import es.upm.interfaces.UserUtils;
import es.upm.ll.regex.TextRecognizer;
import es.upm.p4act.Plan4ActConstants;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import java.util.*;
import com.mongodb.DBCursor;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;
import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class SparkLivingLab {

	public static boolean up = true;
	private static UserUtils userUtils;
	public static void setUserUtils(Object service) {
		userUtils = (UserUtils) service;
	}

	public static void main(String[] args) {
		LlBridge newInstance = new LlBridge();
		//initializing DataBase
		MongoClient mongoClient = new MongoClient(newInstance.generateDbDomain());
		MongoDatabase database = mongoClient.getDatabase("ThingsDataBase");
		MongoCollection<Document> collection = database.getCollection("Devices");
		long docCount=collection.countDocuments();
		//=======================================================================================================
		//=======================================================================================================
		DevicesCreator newDevice = new DevicesCreator();			//This block is temporary and
		newDevice.devHref="/"+docCount;								//its only purpose is to build devices
		newDevice.inputThingType="Door";							//for test. Once W3C standard is released
		newDevice.inputType="integer";								//it must be removed and implemented in uAAL
		newDevice.outputType="integer";								//
		newDevice.name="lab_light";
		newDevice.propertyName="on";
		newDevice.propType="On/Off";
		newDevice.writable=true;
		newDevice.devSerial=docCount;
		newDevice.devStatus= true;//initial status
		try {
			if (newDevice.name != "") {

				DBObject compareFilter = new BasicDBObject();
				compareFilter.put("name", newDevice.name);
				Document seek = new Document(collection.find((Bson) compareFilter).first());
				JSONObject seekObj = new JSONObject(seek.toJson());

				if (seekObj.length() != 0) {

					System.out.println(" existe el dispositivo");
				}
			}
		}
		catch (java.lang.NullPointerException e) {
			System.out.println("no existe el dispositivo y se ha creado");
			Document device = new Document(DevicesCreator.assembleDevice(newDevice));
			collection.insertOne(device);
			return ;
		}
//======================================================================================================
//======================================================================================================
		System.out.println("Current dir: " + System.getProperty("user.dir"));
		//=====================================================
		//SECUENCIA TEMPORAL PARA CREAR LISTADO DE DISPOSITIVOS
		uAALBridge getDevices = new uAALBridge();

		String vURL = newInstance.generateDomain()+"/"+newInstance.defineService();
		//String vURL = "http://192.168.1.130:8181/uAALServices";//Only to ask uAAL server for list of devices (Temporary)
		String allDevices =getDevices.sendSyncRedirectToLL(vURL+"?devices");
		System.out.println(allDevices);
		ArrayList<String> allDevicesArray= new ArrayList<>();
		String parts[]=allDevices.split(",");
		int i=0;
		Hashtable devicesTable = new Hashtable();
		for (String val: parts){
			allDevicesArray.add(val);//recibe todos los dispositivos disponibles
			devicesTable.put(i,val);// crea la tabla de correspondencias entre nombres y id de cada dispositivo.
			i=i+1;
			 }
			System.out.println(devicesTable);
		//======================================================
		//set https connection
		secure("password.jks", "password", null, null);
		System.out.println("Secure load certificate from keystore2.jks");
		//handle HTTPS GET to path /device
		get("/info", (request, response) -> "Welcome to LivingLab!!! Currently "+docCount+" devices Created.");
		//set JWT authentication for request to the path /device and plan4act authentication type is Bearer expected header Authorization: Bearer <token>
		before("/device",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/plan4act",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		//set JWT authentication for request to the path /analizetext
		//manage authentication and generates token for valid users
		//example : post {"username":"eugenio","password":"gaeta"} it create a token for the authentication
		post("/auth", (request, response) -> {
			return AuthManager.handleRequest(request, response, userUtils);
		});

		before("/things",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("/things", (request, response) -> {
			//return DeviceManager.handleRequest(request, response);
			response.header("Content-Type", "application/json");
			System.out.println(allDevicesArray);
			BasicDBObject view= new BasicDBObject() ;
			view.put("Things",collection.find());
			JSONObject showDev= new JSONObject(view.toJson());
			return showDev.get("Things");
		});

		//handle HTTPS GET to path /device
		before("/analizetext",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("/analizetext", (request, response) -> {
					response.header("Content-Type", "application/json");
					response.header("Access-Control-Allow-Origin", "*");
					response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
					response.header("Access-Control-Allow-Headers", "Authorization, authorization");
					String input = request.queryParams("message");
					return (new TextRecognizer()).getJsonMessage(input);
				}
		);
		/* agregado de webthing*/


		// These are matched in the order they are added.
		before("things/:thingId/properties/:propertyName",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("things/:thingId/properties/:propertyName", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			DBObject propFilter = new BasicDBObject();
			propFilter.put( "href", "/"+idx);
			try {
				Document show =  collection.find((Bson) propFilter).first();
				JSONObject showProp= new JSONObject(show.toJson());
				String alias = showProp.get("name").toString();
				JSONObject view= new JSONObject();
				//======================================================================
				//Implementación de Bridge a uAAL
				uAALBridge bridgeTouAAL = new uAALBridge();
				String vDevice=alias;
				String vadURL = "http://192.168.1.130:8181/uAALServices?device="+vDevice;
				JSONObject getSt = new JSONObject(bridgeTouAAL.sendSyncRedirectToLL(vadURL));
				System.out.println(getSt);
				view.put("on",getSt.get(alias));// esta línea debería devolver el estado de la propiedad del dispositivo de acuerdo a WoT Mozilla
				return view;
				//=======================================================================
			}catch (JSONException e) {
				return "Error en la interpretación del formato de respuesta de uAAL";
						}
				}
		);

		before("/:thingId/properties",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("things/:thingId/properties", (request, response) -> {
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			response.header("Content-Type", "application/json");
			DBObject propFilter = new BasicDBObject();
			propFilter.put( "href", "/"+idx);

			try {
				Document show =  collection.find((Bson) propFilter).first();
				JSONObject showProp= new JSONObject(show.toJson());
				return showProp.get("properties");
				}

			catch (JSONException e) {
				return "error a determinar";
				}
			}
		);

		before("/things/:thingId",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("/things/:thingId", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			DBObject thingFilter = new BasicDBObject();
			thingFilter.put( "href", "/"+idx);
			Document aThing = new Document(collection.find((Bson)thingFilter).first());

			Document projection = new Document();
			projection.append("_id",0).append("Current Status",0);
			aThing =  collection.find((Bson) thingFilter).projection(exclude("Current Status","_id")).first();
			JSONObject showThing= new JSONObject(aThing);
			return showThing;
				}
		);




		put("things/:thingId/properties/:propertyName", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			/*
			JSON format for this request: { "on": true/false }
			 */
			try {
				JSONObject o = new JSONObject(request.body());
				JSONObject inputDev = new JSONObject();
				inputDev.put("Current Status",o.get(newDevice.propertyName.toLowerCase()));
				DBObject propFilter = new BasicDBObject();
				propFilter.put( "href", "/"+idx);
				collection.updateOne(Filters.eq("href","/"+idx), new Document("$set", new Document("Current Status",inputDev.get("Current Status"))));
				Document uDevice=collection.find((Bson) propFilter).first();
				JSONObject alias_obj =new JSONObject(uDevice.toJson());
				String alias = alias_obj.get("name").toString();
//=========================================================================================================================================
				uAALBridge bridgeTouAAL = new uAALBridge();
				String toggleStatus=o.get(newDevice.propertyName).toString().toLowerCase();
				if (toggleStatus=="true"){toggleStatus="100";}else{toggleStatus="0";}
				String vDevice=alias;
				if (vDevice.contains("door")){if (toggleStatus!="0"){toggleStatus="1";}}
				JSONObject reply = new JSONObject(bridgeTouAAL.sendSyncRequestToLL(toggleStatus, vDevice));
				System.out.println(reply);
				if (vDevice.contains("door")){
					String rep=reply.get(newDevice.name).toString();//COLOCAR ALIAS SEGURAMENTE SALE DE LA BASE DE DATOS!!!!!!!!
					JSONObject resp=new JSONObject();
					resp.put(alias,rep);
					System.out.println(resp);
					return resp;
				}
				else {String rep=reply.getString(newDevice.name);
					JSONObject resp = new JSONObject();
					resp.put(alias, rep);
					System.out.println(resp);
					return resp;
				}

			} catch (JSONException e) {
						return "error a determinar";
					}
				}
		);


		before("things/add",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
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
			long countDocs=collection.countDocuments();
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
                addDev.devStatus=vars.getBoolean("Initial Status");
                Document LLDev = new Document(DevicesCreator.assembleDevice(addDev));

                collection.insertOne(LLDev);

                return "Device "+addDev.name+" has been created successfully";


            } catch (JSONException e) {
                return "Unable to create the Device";
            }
        }


        );
	}
}