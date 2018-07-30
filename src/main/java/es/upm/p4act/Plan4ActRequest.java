package es.upm.p4act;


import org.mozilla.iot.webthing.Property;


import java.util.Map;

public class Plan4ActRequest {
	public Map<String, Property> properties;
	public String version;
	public String cmd;
	public String device_id;
	public String device_name;
	public String value;
	public String sequence_number;

}

/*
Request schema:

{
"version":"string/enum[mercury, venus, earth, etc..]",     
"cmd" : "string/enum[getstatus, setstatus]",
"device_id":"string", 
"device_name":"string/enum[door1, door2, blind, light1, etc...]",
"value":"string/optional [true/false] required when cmd=setstatus" 
}


*/