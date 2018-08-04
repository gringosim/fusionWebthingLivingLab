package es.upm;

import org.bson.Document;
import org.json.JSONObject;


import java.util.Arrays;



public class DevicesCreator {

    public static String inputThingType;// Switch for example
    public static String propertyName;
    public static String writable;// true or false
    public static String name;// device name and/or location and/or function
    public static String propType;// OnOffState
    public static String inputType;
    public static String outputType;
    public static String devStatus;

    public static JSONObject assembleDevice(DevicesCreator gen) {
     Document LLthing = new Document();
   Document link = new Document("href", "0/properties/on")
            .append("mediaType", "application/json");//
    Document property = new Document("@type", Arrays.asList("Property", gen.propType))
            .append("inputType", new Document("type", gen.inputType))
            .append("outputType", new Document("type", gen.outputType))
            .append("writable", gen.writable)
            .append("link", Arrays.asList(link));
     Document properties = new Document(gen.propertyName, Arrays.asList(property));
    Document thingType = new Document("Thing", gen.inputThingType);
    Document jContext = new Document("@context", Arrays.asList("http://w3c.github.io/wot/w3c-wot-td-context.jsonld",
            "http://w3c.github.io/wot/w3c-wot-common-context.jsonld",
            "http://iot.schema.org"));
    Document security = new Document("security", new Document(new Document("authorizationUrl", "https://mythingserver.org/auth")
            .append("scheme", "bearer")
            .append("format", "JWT")));



        LLthing.append("@context", jContext)
                .append("@type", thingType)
                .append("name", gen.name)
                .append("properties",properties)
                .append("Status", gen.devStatus)
                .append("Security", security);
        JSONObject cosa=new JSONObject();
        cosa.put(gen.name,LLthing);
        return cosa;

    }

    }




