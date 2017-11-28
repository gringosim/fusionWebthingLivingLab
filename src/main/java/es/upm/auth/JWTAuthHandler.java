package es.upm.auth;

import java.util.Date;
import java.util.Hashtable;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JWTAuthHandler {
	
 

   public static final Hashtable<String, String> users;
   
   static {
   	Hashtable<String, String> aMap = new Hashtable<String, String>();
   	aMap.put("alejandro", "medrano");
   	aMap.put("eugenio", "gaeta");
   	aMap.put("plan4act", "secret");
       users = aMap;
   }
	
   public static String createTokenForUser(String user) throws Exception {
		String token = JWT.create()
		        .withIssuer(user)
		        .withClaim("sequence_number",(int) Math.round(Math.random()*1000000))
		        .withExpiresAt(new Date(System.currentTimeMillis()+3300000))
		        .withIssuedAt(new Date(System.currentTimeMillis()))
		        .sign(Algorithm.HMAC256("secret"));
		  return token;  
	}
 
   
	
    
	public static String getTokenForUser(User usr) throws Exception{
		if(usr.password.equals(users.get(usr.username))){
			return fromUserToJsonToken(usr);
		}
		else {
			Token token = new Token();
			token.token = "No valid user";
			token.code = "500";
			Gson gson = new Gson();
			String json = gson.toJson(token);
			System.out.println(json);
			return json;
		}
	}
	

	
	public static String fromUserToJson(User user){
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(user);
		System.out.println(json);
		return json;
	}
	


	public static User fromJsonToUser(String user){
		Gson gson = new Gson();
		User p4a_res= gson.fromJson(user, User.class);
		return p4a_res;
	}
	
	public static String fromUserToJsonToken(User user) throws Exception{
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Token token = new Token();
		token.token = createTokenForUser(user.username);
		token.code = "200";
		String json = gson.toJson(token);
		System.out.println(json);
		return json;
	}
	

}
