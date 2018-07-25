package es.upm;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.secure;



import com.auth0.jwt.JWT;
import com.google.gson.JsonObject;
import com.qmetric.spark.authentication.AuthenticationDetails;

import es.upm.auth.JWTAuthHandler;
import es.upm.auth.JWTAuthenticationFilter;
import es.upm.auth.User;
import es.upm.interfaces.UserUtils;
import es.upm.ll.regex.TextRecognizer;
import es.upm.p4act.LivingLabHandler;
import es.upm.p4act.Plan4ActRequest;


public class SparkLivingLab {
	
	public static boolean up = true;
	private static UserUtils userUtils;
	
	public static void setUserUtils(Object service){
		userUtils = (UserUtils) service;
	}
	
	
	public static void main(String[] args) {
		System.out.println("Current dir: "+System.getProperty("user.dir"));
		//set https connection
		//secure("keystore.jks", "password", null, null);
		//System.out.println("Secure load certificate from keystore2.jks");
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
		post("/auth", (request, response) -> {return AuthManager.handleRequest(request, response, userUtils);});

		
		//handle HTTP/HTTPS GET to path /device http://localhost:8080/device?cmd=set_status&device_id=BATHROOM_DOOR&device_name=BATHROOM_DOOR&value=1&sequence_number=35345
		/*
		    String version = request.queryParams("version");
	        String cmd = request.queryParams("cmd");
	        String device_id = request.queryParams("device_id");
	        String device_name = request.queryParams("device_name");
	        String value = request.queryParams("value");
	        String sequence = request.queryParams("sequence_number");
		 */
		 get("/device", (request, response) -> {return DeviceManager.handleRequest(request, response);});
		//handle HTTP/HTTPS GET to path /plan4act http://localhost:8080/plan4act?cmd=set_status&device_id=BATHROOM_DOOR&device_name=BATHROOM_DOOR&value=1&sequence_number=35345
			/*
			    String version = request.queryParams("version");
		        String cmd = request.queryParams("cmd");
		        String device_id = request.queryParams("device_id");
		        String device_name = request.queryParams("device_name");
		        String value = request.queryParams("value");
		        String sequence = request.queryParams("sequence_number");
			 */
		 get("/plan4act", (request, response) -> {return DeviceManager.handleRequest(request, response);});
		 
		//handle HTTPS GET to path /device
		 get("/analizetext", (request, response) -> {
	        response.header("Content-Type", "application/json");
	        response.header("Access-Control-Allow-Origin","*");
	        response.header("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH,OPTIONS");
	        response.header("Access-Control-Allow-Headers","Authorization, authorization");	
	        String input =  request.queryParams("message");
	        return (new TextRecognizer()).getJsonMessage(input);
		      }
		     );
		/* agregado de webthing*/



		// These are matched in the order they are added.
		get("/:thingId/properties/:propertyName", (request,response)->{return DeviceManager.handleRequest(request, response);

				}
		);
		get("/:thingId/properties", (request,response)->{
					return "/:thingId/properties";
				}
		);
		get("/:thingId", (request,response)->{
					return "/:thingId";
				}
		);

		get("/", (request,response)->{
					return "/";
				}
		);

		get("/properties/:propertyName", (request,response)->{
					return "/properties/:propertyName";
				}
		);

		get("/properties", (request,response)->{
					return "/properties";
				}
		);
    }

}
