package es.upm.auth;

import es.upm.p4act.Plan4ActMessageParser;
import es.upm.p4act.Plan4ActResponse;

public class AuthErrorHandler {

	public static String getAuthHeaderMissedErrorMessage(){
		Plan4ActResponse resp = new Plan4ActResponse();
		resp.cmd = "unknown";
		resp.code = "401";
		resp.description = "Np authentication header.";
		resp.device_id = "-1";
		resp.device_name = "undefined";
		resp.version = "venus";
		resp.value = "-1";
		return Plan4ActMessageParser.fromP4AResponseToJson(resp);
	}
	
	public static String getNoValidTokenErrorMessage(){
		Plan4ActResponse resp = new Plan4ActResponse();
		resp.cmd = "unknown";
		resp.code = "402";
		resp.description = "No valid token.";
		resp.device_id = "-1";
		resp.device_name = "undefined";
		resp.version = "venus";
		resp.value = "-1";
		return Plan4ActMessageParser.fromP4AResponseToJson(resp);
	}
	
}
