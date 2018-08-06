package es.upm;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.port;
import static spark.Spark.secure;

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
		newDevice.name="Bathroom Lamp";
		newDevice.propertyName="On";
		newDevice.propType="OnOffState";
		newDevice.writable=true;
		newDevice.devSerial=docCount;

		System.out.println(DevicesCreator.assembleDevice(newDevice));
		//===============================================================



		Document device_con_status= new Document(DevicesCreator.assembleDevice(newDevice));
		//Document device_con_status=new Document();
		collection.insertOne(device_con_status);

		ObjectId id = (ObjectId)device_con_status.get( "_id" );
	/*
		device_con_status= new Document("@type", new Document("Thing","Switch"));
		device_con_status.append("name","My .... Lamp");
		//device_con_status.append("href","/"+id.toString());
		device_con_status.append("href","/"+devSerial);
		device_con_status.append("@context",Arrays.asList("http://w3c.github.io/wot/w3c-wot-td-context.jsonld",
				"http://w3c.github.io/wot/w3c-wot-common-context.jsonld",
				"http://iot.schema.org"));
		Document link= new Document();
		link.append("href","/"+devSerial+"/properties/on").append("mediaType","application/json");
		//link.append("href","/"+id.toString()+"/properties/on").append("mediaType","application/json");
		device_con_status.append("properties",new Document("on", new Document("name", "B")
														.append("outputType",new Document("type","boolean"))
														.append("inputType",new Document("type","boolean"))
				   										.append("link",link)
														.append("@type",Arrays.asList( "Property",
																"Light", "OnOffState"))
														.append("writable",false)
													)
		);
		device_con_status.append("Current Status",null);


*/


		DBObject filter = new BasicDBObject();
		filter.put( "_id", new ObjectId(id.toString()));
		collection.updateOne(Filters.eq("_id", id), new Document("$set", device_con_status));


		Document projection = new Document();
		projection.append("_id",0).append("Current Status",0);
		Document device =  collection.find((Bson) filter).projection(exclude("Current Status","_id")).first();

		//device.remove("_id");
		//device.remove("Current Status");
		System.out.println(device.toJson());
		System.out.println(id);




		System.out.println("Current dir: " + System.getProperty("user.dir"));



		//set https connection
		secure("password.jks", "password", null, null);
		System.out.println("Secure load certificate from keystore2.jks");
		//handle HTTPS GET to path /device
		get("/info", (request, response) -> "Welcome to LivingLab!!!");

		//set JWT authentication for request to the path /device and plan4act authentication type is Bearer expected header Authorization: Bearer <token>
		/*before("/device",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		before("/plan4act",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		System.out.println("Adding JWTAuthenticationFilter to device and plan4act path");
		//set JWT authentication for request to the path /analizetext
		before("/analizetext",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		System.out.println("Adding JWTAuthenticationFilter to analizetext path");
		*/


		//manage authentication and generates token for valid users
		//example : post {"username":"eugenio","password":"gaeta"} it create a token for the authentication
		post("/auth", (request, response) -> {
			return AuthManager.handleRequest(request, response, userUtils);
		});


	//	before("/things",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("/things", (request, response) -> {
			//return DeviceManager.handleRequest(request, response);
			response.header("Content-Type", "application/json");


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
		//before("things/:thingId/properties/:propertyName",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("things/:thingId/properties/:propertyName", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			response.header("Content-Type", "application/json");
			DBObject propFilter = new BasicDBObject();
			propFilter.put( "href", "/"+idx);

					try {
						Document show =  collection.find((Bson) propFilter).first();
						JSONObject showProp= new JSONObject(show.toJson());
						return null;

					} catch (JSONException e) {
						return "error a determinar";
					}



				}
		);



	//	before("/:thingId/properties",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
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
				/*
				Thing cosa = things_obj.get(idx);
				JSONObject obj = new JSONObject();
				return obj.put("properties",cosa.getPropertyDescriptions());
				*/
				//===========================================================
				//método alternativo
				// JSONObject propiedad_de_la_cosa = ((JSONObject) things.get(idx)).getJSONObject("properties");
				// obj.put("properties",propiedad_de_la_cosa);
				//	return obj;
			} catch (JSONException e) {
				return "error a determinar";
			}


				}

		);
	//	before("/things/:thingId",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("/things/:thingId", (request, response) -> {
			response.header("Content-Type", "application/json");
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			return null;//things.get(idx);
				}
		);




		put("things/:thingId/properties/:propertyName", (request, response) -> {
			/*
			JSONObject obj_value = new JSONObject(request.body());
					System.out.println(request.body());
					String index = request.params(":thingId");
					int idx = Integer.parseInt(index);
					String propertyName = request.params(":propertyName");
					response.header("Content-Type", "application/json");
					JSONObject obj = new JSONObject();
					*/
			response.header("Content-Type", "application/json");
					try {
					    JSONObject o = new JSONObject(request.body());
						System.out.println(request.body());
						JSONArray inputDev = new JSONArray();
						inputDev.put(o.get("Current Status"));
						collection.updateOne(Filters.eq("_id", device.get("_id")), new Document("$set", new Document("Current Status",inputDev.toString())));
                       	Document uDevice=collection.find().first();

						return uDevice.toJson();

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
                Document LLDev = new Document(DevicesCreator.assembleDevice(addDev));
                collection.insertOne(LLDev);
                return "Device "+addDev.name+" has been created successfully";


            } catch (JSONException e) {
                return "Unable to create the Device";
            }
        });
	}
}