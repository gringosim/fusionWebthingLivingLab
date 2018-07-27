package es.upm.p4act;


import org.mozilla.iot.webthing.Property;
import org.mozilla.iot.webthing.Thing;

import java.util.Map;

public class Plan4ActRequest {
	public Map<String, Property> properties;
	public String version;
	public String cmd;
	public String device_id;
	public String device_name;
	public String value;
	public String sequence_number;

	/**
	 * Find a property by name.
	 *
	 * @param propertyName Name of the property to find
	 * @return Property if found, else null.
	 */
	public Property findProperty(String propertyName) {
		if (properties.containsKey(propertyName)) {

			return properties.get(propertyName);
		}

		return null;
	}

	/**
	 * Get a property's value.
	 *
	 * @param propertyName Name of the property to get the value of
	 * @param <T>          Type of the property value
	 * @return Current property value if found, else null.
	 */
	public  <T> T getProperty(String propertyName) {
		Property<T> prop = findProperty(propertyName);
		if (prop != null) {
			return prop.getValue();
		}

		return null;
	}
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