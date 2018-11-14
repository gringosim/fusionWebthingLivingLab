package es.upm;

import es.upm.auth.JWTAuthHandler;
import es.upm.auth.User;
import es.upm.interfaces.UserUtils;

public class AuthManager {
	
	public static String handleRequest(spark.Request request, spark.Response response, UserUtils userUtils)  {
		response.header("Content-Type", "application/json");
		User user = JWTAuthHandler.fromJsonToUser(request.body());
		try {
		if(userUtils != null ){
			if(userUtils.checkUser(user.username, user.password)){
				user = new User();
				user.username = "plan4act";
			    user.password = "proactive";
			}
		}
		}catch(Exception e) {
		 System.out.println("Remote loggin is not available");
		 
		}
		try {		return JWTAuthHandler.getTokenForUser(user);
		
		}catch(Exception e) {
			return JWTAuthHandler.getGenericErrorMessage();
		}
		
	}

}
