package es.upm;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Arrays;

public class DevicesCreator {

    public static String inputThingType;// Switch for example
    public static String propertyName;
    public static Boolean writable;// true or false
    public static String name;// device name and/or location and/or function
    public static String propType;// OnOffState
    public static String inputType;
    public static String outputType;
    public static String devHref;
    public static String serverChoice;//verificar si cumple con JSONLD
   public static long devSerial;
   public static boolean devStatus;
    public static Document ThingsDescriptor;

   /* public static Document assembleLL(){

         ThingsDescriptor = new Document();
            ThingsDescriptor.append("@context",Arrays.asList("http://w3c.github.io/wot/w3c-wot-td-context.jsonld",
                    "http://w3c.github.io/wot/w3c-wot-common-context.jsonld",
                    "http://iot.schema.org"));
        return ThingsDescriptor;}*/

    public static Document  assembleDevice(DevicesCreator gen) {

        Document LLthing = new Document();
        Document link = new Document("href", devSerial+"/properties/status")
                            .append("mediaType", "application/json");
        Document property = new Document("@type", Arrays.asList("Property", gen.propType))
                            .append("inputType", new Document("type", gen.inputType))
                            .append("outputType", new Document("type", gen.outputType))
                            .append("writable", gen.writable)
                            .append("link", Arrays.asList(link));

        Document properties = new Document(gen.propertyName, property);

        Document security = new Document( new Document(new Document("authorizationUrl", "/auth")
                                                                        .append("scheme", "bearer")
                                                                        .append("format", "JWT")));



        LLthing.append("@context",Arrays.asList("http://w3c.github.io/wot/w3c-wot-td-context.jsonld",
                "http://w3c.github.io/wot/w3c-wot-common-context.jsonld",
                "http://iot.schema.org"))
                .append("@type",Arrays.asList("Thing", gen.inputThingType))
                .append("name", gen.name)
                .append("properties",properties)
                .append("href",gen.devHref )
                .append("security",security)
                .append("status",gen.devStatus);

            return LLthing;

    }


}




