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
		
		//set JWT authentication for request to the path /device
		before("/device",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		System.out.println("Adding JWTAuthenticationFilter to device path");
		//set JWT authentication for request to the path /analizetext
		before("/analizetext",new JWTAuthenticationFilter("/*", new AuthenticationDetails("", "")));
		System.out.println("Adding JWTAuthenticationFilter to analizetext path");
		
		
		//manage authentication and generates token for valid users
		post("/auth", (request, response) -> {
			response.header("Content-Type", "application/json");
			User user = JWTAuthHandler.fromJsonToUser(request.body());
			if(userUtils != null ){
				if(userUtils.checkUser(user.username, user.password)){
					user = new User();
					user.username = "eugenio";
				    user.password = "gaeta";			
				}
			}
			return JWTAuthHandler.getTokenForUser(user);
		});

		
		
		//handle HTTPS GET to path /device
		 get("/device", (request, response) -> {
	        	response.header("Content-Type", "application/json");
	            response.header("Access-Control-Allow-Origin","*");
	            response.header("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH,OPTIONS");
	            response.header("Access-Control-Allow-Headers","Authorization, authorization");	
	            
                String version = request.queryParams("version");
                String cmd = request.queryParams("cmd");
                String device_id = request.queryParams("device_id");
                String device_name = request.queryParams("device_name");
                String value = request.queryParams("value");
                String sequence = request.queryParams("sequence_number");
                
                Plan4ActRequest r = new Plan4ActRequest();
                r.version = version;
                r.cmd = cmd;
                r.device_id = device_id;
                r.device_name = device_name;
                r.value = value;
                r.sequence_number = sequence;
	            return (new LivingLabHandler()).handleDevice(r);
		      }
		     );
		 
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
		
    }

}
