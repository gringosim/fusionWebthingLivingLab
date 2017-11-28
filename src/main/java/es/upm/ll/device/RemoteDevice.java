package es.upm.ll.device;


import es.upm.ll.LlBridge;

public  class RemoteDevice {
	

	private String device = "LUZ_CASA";
	private String url = "http://138.4.10.224/remote.htm?id="+device+"&value=";
	private static boolean on_off_mode = false;
	
	LlBridge bridge;
	
	public RemoteDevice(LlBridge b){
		 bridge = b;
	}
	
	public RemoteDevice(LlBridge b, String p4a_dev){
		 bridge = b;
		 device = RemoteDeviceConstants.devices.get(p4a_dev);
		 url = "http://138.4.10.224/remote.htm?id="+device+"&value=";
	}
	
	public void setMode(boolean onOff){
		on_off_mode = onOff;
	}
	
	public String setNewDeviceState(int newValue) throws Exception{
		if(on_off_mode) return setOnOffState(newValue);
		else if(newValue==1){
			 return fadeInDevice();
			}
			else {
				return fadeOutDevice();
			} 
	}
	
	
	
	public String setOnOffState(int newValue) throws Exception{
		String result = "";
		bridge.downloadUrl("http://138.4.10.224/remote.htm?id="+device+"_ONOFF&value="+newValue);
		return result;	
	}
	
	public String fadeInDevice() throws Exception{
		String result = "";
		for (int i =0; i<100; i=i+10){
			String url_to_call = url+i;
			System.out.println("url_to_call: "+url_to_call);
			result = result + bridge.downloadUrl(url_to_call);
			Thread.sleep(2000);
		}
		bridge.downloadUrl("http://138.4.10.224/remote.htm?id="+device+"_ONOFF&value=1");
		return result;
		
	}
	
	public String fadeOutDevice() throws Exception{
		String result = "";
		System.out.println("fadeOutDevice: "+url);
		for (int i = 100; i>=0; i = i-10){
			String url_to_call = url+i;
			System.out.println("url_to_call: "+url_to_call);
			result = result + bridge.downloadUrl(url_to_call);
			Thread.sleep(2000);
		}
		return result;	
	}

}
