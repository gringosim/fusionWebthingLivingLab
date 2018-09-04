package es.upm;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.port;
import static spark.Spark.secure;

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
import org.mozilla.iot.webthing.Property;
import org.mozilla.iot.webthing.Thing;
import org.mozilla.iot.webthing.Value;
import org.mozilla.iot.webthing.example.MultipleThings;

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
//import static sun.plugin2.util.PojoUtil.toJson;

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





		//initializing DataBase

		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("ThingsDataBase");
		MongoCollection<Document> collection = database.getCollection("Devices");

		long docCount=collection.countDocuments();
		//==========================================================
		DevicesCreator newDevice = new DevicesCreator();
		newDevice.devHref="/"+docCount;
		newDevice.inputThingType="Switch";
		newDevice.inputType="boolean";
		newDevice.outputType="boolean";
		newDevice.name="light01";
		newDevice.propertyName="on";
		newDevice.propType="On/Off";
		newDevice.writable=true;
		newDevice.devSerial=docCount;
		newDevice.devStatus= true;//initial status

		//===============================================================
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


		System.out.println("Current dir: " + System.getProperty("user.dir"));
		//=====================================================

		uAALBridge getDevices = new uAALBridge();
		String vURL = "http://192.168.1.102:8181/uAALServices?devices";
		String allDevices ="lights_ControlRoom,lightDetector_BedRoom,lightDetector_BathRoom,presenceSensor_BedRoom,blind_Kitchen,presenceSensor_Entrance,presenceSensor_DiningRoom,lights_Porch,door_Kitchen,door_LivingRoom,lights_VirtualRealityRoom,lights_DiningRoom,blind_ControlRoom,lights_DiningRoomexterior,electrovalve,aerator,door_BathRoom,lights_BathRoom,lights_ControlRoomExterior,smokeSensor_Kitchen,blind_UserArea,blind_DiningRoom,fountain,magneticContact_BathRoom,blind_BedRoom,light09,lights_DishwasherRefrigerator,light08,thermometer,light03,light02,light01,waterfall,blind_KitchenLeft,lightDetector_Kitchen,lights_BedRoom,light07,light06,light05,light04,light10,emergencyButton,presenceSensor_Kitchen,lights_TVRoom,presenceSensor_BathRoom,blind_KitchenRight,light14,light13,light12,light11,light15,door_Entrance,temperatureLevelSensor_Kitchen,lights_Kitchen,magneticContact_BedRoom,lights_UserArea,lights_OvenWindow,lightDetector_DiningRoom,lightDetector_Entrance,blind_Entrance,lights_DiningRoominterior,lights_Street";//getDevices.sendSyncRedirectToLL(vURL);
		System.out.println(allDevices);
		ArrayList<String> allDevicesArray= new ArrayList<>();
		String parts[]=allDevices.split(",");
		int i=0;
		for (String val: parts){
			allDevicesArray.add(val);
			 }

		//======================================================


		//set https connection
		secure("password.jks", "password", null, null);
		System.out.println("Secure load certificate from keystore2.jks");
		//handle HTTPS GET to path /device
		get("/info", (request, response) -> "Welcome to LivingLab!!! Currently "+docCount+" devices Created.");

		//set JWT authentication for request to the path /device and plan4act authentication type is Bearer expected header Authorization: Bearer <token>
		before("/device",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/plan4act",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		System.out.println("Adding JWTAuthenticationFilter to device and plan4act path");
		//set JWT authentication for request to the path /analizetext
		before("/analizetext",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		System.out.println("Adding JWTAuthenticationFilter to analizetext path");



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
				//view.put("Status",showProp.get("Current Status"));
				//return view;
				//======================================================================
				//Implementación de Bridge a uAAL

				uAALBridge bridgeTouAAL = new uAALBridge();
				String vDevice=alias;
				String vadURL = "http://192.168.1.102:8181/uAALServices?device="+vDevice;
				JSONObject getSt = new JSONObject(bridgeTouAAL.sendSyncRedirectToLL(vadURL));
				System.out.println(getSt);
				view.put("on",getSt.get(alias));// esta línea debería devolver el estado de la propiedad del dispositivo de acuerdo a WoT Mozilla

				return view;


				//=======================================================================


					} catch (JSONException e) {
						return "error a determinar";
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

			} catch (JSONException e) {
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
			JSON format for this request: { "Toggle Status": true/false }
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

				//AGREGAR IF para consultar por el caso de PUT en el que se escribe mal la propertyName
				String toggleStatus=o.get(newDevice.propertyName).toString().toLowerCase();
				if (toggleStatus=="true"){toggleStatus="100";}else{toggleStatus="0";}
				String vDevice=alias;
				if (vDevice.contains("door")){if (toggleStatus!="0"){toggleStatus="1";}}
				JSONObject reply = new JSONObject(bridgeTouAAL.sendSyncRequestToLL(toggleStatus, vDevice));

				System.out.println(reply);
				if (vDevice.contains("door")){
					String rep=reply.get(newDevice.name).toString();
					JSONObject resp=new JSONObject();
					resp.put(alias,rep);
					return resp;
				}
				else {String rep=reply.getString(newDevice.name);
					/*if (reply.getString(newDevice.name) == "0") {

						rep = "false";
					} else {
						rep = "true";
					}*/
					JSONObject resp = new JSONObject();
					resp.put(alias, rep);
							// DEBE CORREGIRSE LO QUE SE MUESTRA COMO RESPONSE para que sea consistente con WOT

				/*

				JSONObject rep=new JSONObject();
				rep.put(newDevice.propertyName,inpValue);*/
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
        });
	}
}