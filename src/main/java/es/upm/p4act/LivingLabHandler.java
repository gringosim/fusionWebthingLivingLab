package es.upm.p4act;
import es.upm.ll.LlBridge;
import es.upm.ll.device.RemoteDevice;
import es.upm.ll.device.RemoteDeviceConstants;
/*
 * This class manager the devices of the Living Lab, it allows to change state of VLC player and Remote 2 devices
 */
public class LivingLabHandler {
	
	
	public static final String CMD_GET = "get_status"; 
	public static final String CMD_SET = "set_status"; 
	
	public String getErrorMessageFromRequest(Plan4ActRequest req){
		Plan4ActResponse resp = new Plan4ActResponse();
		System.out.println("getErrorMessageFromRequest");
		resp.cmd = req.cmd;
		resp.code = "500";
		resp.description = "Generic server error";
		resp.device_id = req.device_id;
		resp.device_name = Plan4ActConstants.getNames(req.device_id);
		resp.version = req.version;
		resp.value = req.value;
		resp.sequence_number = req.sequence_number;
		return Plan4ActMessageParser.fromP4AResponseToJson(resp);
	}	
	
	public String getErrorMessage(){
		Plan4ActResponse resp = new Plan4ActResponse();
		resp.cmd = "unknown";
		resp.code = "500";
		resp.description = "Generic server error";
		resp.device_id = "-1";
		resp.device_name = "undefined";
		resp.version = "venus";
		resp.value = "-1";
		resp.sequence_number = "-1";
		return Plan4ActMessageParser.fromP4AResponseToJson(resp);
	}
	
	public String getPlayerSetSuccessMessage(Plan4ActRequest req){
		Plan4ActResponse resp = new Plan4ActResponse();
		resp.cmd = CMD_SET;
		resp.code = "200";
		resp.description =Plan4ActConstants.get(Plan4ActConstants.MEDIA_PLAYER)+" state changed.";
		resp.device_id =  Plan4ActConstants.MEDIA_PLAYER;
		resp.device_name = Plan4ActConstants.get(Plan4ActConstants.MEDIA_PLAYER);
		resp.value = req.value;
		resp.sequence_number = req.sequence_number;
		return Plan4ActMessageParser.fromP4AResponseToJson(resp);
	}
	
	public String getDeviceSetSuccessMessage(Plan4ActRequest req){
		Plan4ActResponse resp = new Plan4ActResponse();
		resp.cmd = CMD_SET;
		resp.code = "200";
		resp.description =Plan4ActConstants.get(Plan4ActConstants.HOUSE_LIGHT)+" state changed.";
		resp.device_id =  Plan4ActConstants.HOUSE_LIGHT;
		resp.device_name = Plan4ActConstants.get(Plan4ActConstants.HOUSE_LIGHT);
		resp.version = "venus";
		resp.value = req.value;
		resp.sequence_number = req.sequence_number;
		return Plan4ActMessageParser.fromP4AResponseToJson(resp);
	}
	
	public String handleDevice(Plan4ActRequest req){
		try{
			System.out.println("handleDevice");
			//Handle requests to Media Player
			if(req.device_name.equalsIgnoreCase(Plan4ActConstants.MEDIA_PLAYER)){
				if(req.cmd.equalsIgnoreCase("set_status")){
					if(req.value.equalsIgnoreCase("1")||req.value.equalsIgnoreCase("true")||req.value.equalsIgnoreCase("on")){
						Plan4ActVLCDecoder.fadeInMusicEffect();
					}
					else {
						Plan4ActVLCDecoder.fadeOutMusicEffect();
					}
				  
				  return getPlayerSetSuccessMessage(req);	
				}
				
			}
			
			//Handle requests to living lab devices
			
			
			//thought remote
			if(RemoteDeviceConstants.isARemoteDevice(req.device_name)){
				if(req.cmd.equalsIgnoreCase("set_status")){
					RemoteDevice device = new RemoteDevice(new LlBridge(),req.device_name);
					if(req.value.equalsIgnoreCase("1")||req.value.equalsIgnoreCase("true")||req.value.equalsIgnoreCase("on")){
						device.fadeInDevice();
					}
					else {
						device.fadeOutDevice();
					}
				  
				  return getDeviceSetSuccessMessage(req);	
				}
				
			}
			
			
			return getErrorMessage();
			}catch(Exception e){
				System.out.println("handleDevice - Error: "+e.getMessage());
				return getErrorMessageFromRequest(req);
			}
			
	}


	
	public String handleDeviceJsonRequest(String postData) {
		Plan4ActRequest req = Plan4ActMessageParser.fromJsonToP4ARequest(postData);
		return handleDevice(req);
	}	

}
