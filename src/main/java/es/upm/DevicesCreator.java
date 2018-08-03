package es.upm;

import org.bson.Document;


import java.util.Arrays;



public class DevicesCreator {

    public String inputThingType;// Switch for example
    public String propertyName;
    public String writable;// true or false
    public String name;// device name and/or location and/or function
    public String propType;// OnOffState
    public String inputType;
    public String outputType;
    public String devStatus;

    Document LLthing=new Document();
    Document link=new Document("href","0/properties/on")
                                .append("mediaType","application/json");//
    Document property=new Document("@type",Arrays.asList("Property", propType))
                                    .append("inputType",new Document("type",inputType))
                                    .append("outputType",new Document("type",outputType))
                                    .append("writable",writable)
                                    .append("link",Arrays.asList(link));
    Document properties=new Document(propertyName,Arrays.asList(property));
    Document thingType=new Document("Thing",inputThingType);
    Document jContext=new Document("@context", Arrays.asList("http://w3c.github.io/wot/w3c-wot-td-context.jsonld",
                                                             "http://w3c.github.io/wot/w3c-wot-common-context.jsonld",
                                                             "http://iot.schema.org"));
    Document security=new Document("security",new Document(new Document("authorizationUrl","https://mythingserver.org/auth")
                                                                        .append("scheme", "bearer")
                                                                        .append("format","JWT")));

    public Document assembleDevice() {

        LLthing.append("@context", jContext);
        LLthing.append("@type", thingType);
        LLthing.append("name", name);
        LLthing.append("properties", properties);
        LLthing.append("Status", devStatus);
        LLthing.append("Security", security);

        return LLthing;

    }

    }




