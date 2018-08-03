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

	public static class ExampleDimmableLight extends Thing {
		public String[] onType = {"Property",
				"Light",
				"OnOffState"};


		public ExampleDimmableLight() {
			super("My Lamp",
					Arrays.asList("Thing", "Switch")// Eze: modificado según JSON modelo
			);

			Map<String, Object> onDescription = new HashMap<>();
			onDescription.put("name", "Bathroom light");

			JSONObject inputType = new JSONObject();
			JSONObject outputType = new JSONObject();
			JSONObject link = new JSONObject();
			inputType.put("type", "boolean");
			onDescription.put("inputType", inputType);
			outputType.put("type", "boolean");
			onDescription.put("outputType", outputType);
			onDescription.put("@type", onType);
			link.put("href", String.format("/properties/%s", "on"));
			link.put("mediaType", "application/json");
			//onDescription.put("link",link);
			// onDescription.put("@type", "OnOffProperty");
			//  onDescription.put("label", "On/Off");
			//  onDescription.put("inputType", "boolean");
			onDescription.put("writable", "true");

			Value<Boolean> on = new Value<>(true,
					// Here, you could send a signal to
					// the GPIO that switches the lamp
					// off
					v -> System.out.printf(
							"On-State is now %s\n",
							v));

			this.addProperty(new Property(this, "on", on, onDescription));

           /* Map<String, Object> brightnessDescription = new HashMap<>();
            brightnessDescription.put("@type", "BrightnessProperty");
            brightnessDescription.put("label", "Brightness");
            brightnessDescription.put("type", "number");
            brightnessDescription.put("description",
                                      "The level of light from 0-100");
            brightnessDescription.put("minimum", 0);
            brightnessDescription.put("maximum", 100);
            brightnessDescription.put("unit", "percent");


            Value<Integer> brightness = new Value<>(50,
                                                    // Here, you could send a signal
                                                    // to the GPIO that controls the
                                                    // brightness
                                                    l -> System.out.printf(
                                                            "Brightness is now %s\n",
                                                            l));

            this.addProperty(new Property(this,
                                          "brightness",
                                          brightness,
                                          brightnessDescription));*/

			Map<String, Object> fadeMetadata = new HashMap<>();
			Map<String, Object> fadeInput = new HashMap<>();
			Map<String, Object> fadeProperties = new HashMap<>();
			Map<String, Object> fadeBrightness = new HashMap<>();
			Map<String, Object> fadeDuration = new HashMap<>();
			fadeMetadata.put("label", "Fade");
			fadeMetadata.put("description", "Fade the lamp to a given level");
			fadeInput.put("type", "object");
			fadeInput.put("required", new String[]{"brightness", "duration"});
			fadeBrightness.put("type", "number");
			fadeBrightness.put("minimum", 0);
			fadeBrightness.put("maximum", 100);
			fadeBrightness.put("unit", "percent");
			fadeDuration.put("type", "number");
			fadeDuration.put("minimum", 1);
			fadeDuration.put("unit", "milliseconds");
			fadeProperties.put("brightness", fadeBrightness);
			fadeProperties.put("duration", fadeDuration);
			fadeInput.put("properties", fadeProperties);
			fadeMetadata.put("input", fadeInput);
			// this.addAvailableAction("fade", fadeMetadata, FadeAction.class);

			Map<String, Object> overheatedMetadata = new HashMap<>();
			overheatedMetadata.put("description",
					"The lamp has exceeded its safe operating temperature");
			overheatedMetadata.put("type", "number");
			overheatedMetadata.put("unit", "celsius");
			//   this.addAvailableEvent("overheated", overheatedMetadata);
		}




	}

	public static void main(String[] args) {
		Thing light = new ExampleDimmableLight();
		//List<Property> propList = new ArrayList<>(Thing.properties.values());

		JSONArray things = new JSONArray();
		things.put(light.asThingDescription());


		List<Thing> things_obj = new ArrayList<>();
		things_obj.add(light);




		//initializing DataBase

		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("ThingsDataBase");
		MongoCollection<Document> collection = database.getCollection("Devices");
		//==========================================================
		DevicesCreator bathroom_light = new DevicesCreator();
		bathroom_light.devStatus=null;
		bathroom_light.inputThingType="Switch";
		bathroom_light.inputType="boolean";
		bathroom_light.outputType="boolean";
		bathroom_light.name="Bathroom Lamp";
		bathroom_light.propertyName="On";
		bathroom_light.propType="OnOffState";
		bathroom_light.writable="true";
		//===============================================================



		Document device_con_status= new Document();
		collection.insertOne(device_con_status);
		ObjectId id = (ObjectId)device_con_status.get( "_id" );
		device_con_status= new Document("@type", new Document("Thing","Switch"));
		device_con_status.append("name","My Lamp");
		device_con_status.append("href","/"+id.toString());
		device_con_status.append("@context",Arrays.asList("http://w3c.github.io/wot/w3c-wot-td-context.jsonld",
				"http://w3c.github.io/wot/w3c-wot-common-context.jsonld",
				"http://iot.schema.org"));
		Document link= new Document();
		link.append("href","/"+id.toString()+"/properties/on").append("mediaType","application/json");
		device_con_status.append("properties",new Document("on", new Document("name", "Bathroom Light")
														.append("outputType",new Document("type","boolean"))
														.append("inputType",new Document("type","boolean"))
				   										.append("link",link)
														.append("@type",Arrays.asList( "Property",
																"Light", "OnOffState"))
														.append("writable",true)
													)
		);
		device_con_status.append("Current Status",null);


		//device_con_status.put("href", "000000000000000000000023");

		//collection.insertOne(device_con_status);
		//ObjectId id = (ObjectId)device_con_status.get( "_id" );

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


		//handle HTTP/HTTPS GET to path /device http://localhost:8080/device?cmd=set_status&device_id=BATHROOM_DOOR&device_name=BATHROOM_DOOR&value=1&sequence_number=35345
		/*
		    String version = request.queryParams("version");
	        String cmd = request.queryParams("cmd");
	        String device_id = request.queryParams("device_id");
	        String device_name = request.queryParams("device_name");
	        String value = request.queryParams("value");
	        String sequence = request.queryParams("sequence_number");
		 */
	//	before("/things",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("/things", (request, response) -> {
			//return DeviceManager.handleRequest(request, response);
			response.header("Content-Type", "application/json");


			BasicDBObject view= new BasicDBObject() ;

			view.put("Things",collection.find());



				return view;
		});
		//handle HTTP/HTTPS GET to path /plan4act http://localhost:8080/plan4act?cmd=set_status&device_id=BATHROOM_DOOR&device_name=BATHROOM_DOOR&value=1&sequence_number=35345
			/*
			    String version = request.queryParams("version");
		        String cmd = request.queryParams("cmd");
		        String device_id = request.queryParams("device_id");
		        String device_name = request.queryParams("device_name");
		        String value = request.queryParams("value");
		        String sequence = request.queryParams("sequence_number");
			 */

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

			String index = request.params(":thingId");

				int idx = Integer.parseInt(index);
					String propertyName = request.params(":propertyName");
					response.header("Content-Type", "application/json");
					JSONObject obj = new JSONObject();

					try {
						JSONArray ver= new JSONArray();
						ver.put(device.get("Current status"));

						return "Current status"+ver;

						/*
						Object val = things_obj.get(idx).getProperty(propertyName);
						Object value = devices_obj.get(idx).getDeviceStatus(val);
						if (value == null) {
							obj.put(propertyName, JSONObject.NULL);
						} else {
							obj.putOpt(propertyName, value);
						}
						return obj.toString();
						*/
					} catch (JSONException e) {
						return "error a determinar";
					}

					//return DeviceManager.handleRequest(request, response);

				}
		);



	//	before("/:thingId/properties",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		get("things/:thingId/properties", (request, response) -> {
			String index = request.params(":thingId");
			int idx = Integer.parseInt(index);
			response.header("Content-Type", "application/json");


			try {

						JSONArray ver= new JSONArray();
						ver.put(device.get("properties"));

						return ver;
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
			return things.get(idx);
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

						/*
						Object vin = obj_value.get(propertyName);
						System.out.println(vin);
						Object val = things_obj.get(idx).getProperty(propertyName);
						Object value = devices_obj.get(idx).setDeviceStatus("",vin);
						if (value == null) {
							obj.put(propertyName, JSONObject.NULL);
						} else {
							obj.putOpt(propertyName, value);
						}
						return obj.toString();
						*/
					} catch (JSONException e) {
						return "error a determinar";
					}

					//return DeviceManager.handleRequest(request, response);

				}
		);
	}
}