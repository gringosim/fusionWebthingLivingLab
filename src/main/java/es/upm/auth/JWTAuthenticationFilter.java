package es.upm.auth;

import static spark.Spark.halt;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qmetric.spark.authentication.AuthenticationDetails;
import com.qmetric.spark.authentication.BasicAuthenticationFilter;

import spark.Request;
import spark.Response;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	public JWTAuthenticationFilter(String path, AuthenticationDetails authenticationDetails) {
		super(path, authenticationDetails);
		System.out.println("Service path: "+path);
	}

	public String refreshToken() throws Exception {
		String token = JWT.create()
		        .withIssuer("auth0")
		        .withExpiresAt(new Date(System.currentTimeMillis()+33000))
		        .withIssuedAt(new Date(System.currentTimeMillis()))
		        .sign(Algorithm.HMAC256("secret"));
		    //System.out.println("refreshToken: "+token);
		  return token;  
	}
	
	
	@Override
    public void handle(final Request request, final Response response)
    {
		System.out.println("Current dir: "+System.getProperty("user.dir"));
		System.out.println("Current path: "+request.pathInfo());
		response.header("Content-Type", "application/json");
        response.header("Access-Control-Allow-Origin","*");
        response.header("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH,OPTIONS");
        response.header("Access-Control-Allow-Headers","Authorization, authorization");	
		try {
			System.out.println("Test Token: "+this.refreshToken());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//final String encodedHeader = StringUtils.substringAfter(request.headers("Authorization"), "Basic");
        //final String jwtHeader = request.headers("JWT");
        final String jwtHeader = StringUtils.substringAfter(request.headers("Authorization"), "Bearer");
        System.out.println("jwtHeader: "+jwtHeader);
        if(jwtHeader!=null){
        	boolean valid=false;
        	try{
        	JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret"))
        			.acceptExpiresAt(30)   
        		    .build();
        	System.out.println("Try to verify token. ");
        	DecodedJWT jwt = verifier.verify(jwtHeader.trim());
        	System.out.println(jwt.toString());
        	System.out.println("Expire: "+jwt.getExpiresAt().getTime());
        	//System.out.println("Issued: "+jwt.getIssuedAt().getTime());
        	System.out.println("interval: "+(jwt.getExpiresAt().getTime()-System.currentTimeMillis()));
        	long interval=jwt.getExpiresAt().getTime()-System.currentTimeMillis();
        	valid=(interval>0)&&(interval<30000);
        	valid = true;
        	//allow also expired token: just for testing
        	}catch(Exception e){
        	System.out.println("Error: "+e.getMessage());
        	valid = false;
        	}
        	if(!valid){
        		System.out.println("JWT auth Failed send: halt(402)");
            	halt(402, AuthErrorHandler.getNoValidTokenErrorMessage());
        	}
        }
        else {
        	System.out.println("JWT Header missing send: halt(401)");
        	halt(401, AuthErrorHandler.getAuthHeaderMissedErrorMessage());
        }
        
    }
	
	
	
}
