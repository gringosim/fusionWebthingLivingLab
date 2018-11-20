package es.upm;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import es.upm.ll.ContextDefinition;
import es.upm.ll.uAALBridge;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class dataBaseManager {
    static long docCount;
    static MongoCollection<Document> collection;
    static DevicesCreator newDevice = new DevicesCreator();
    static String dataBaseName = "ThingsDtaBase";
    static MongoDatabase database;
    public static MongoCollection<Document> initCollection;
    public static boolean isRemote = true;


    public static long dbInit() {
        ContextDefinition newInstance = new ContextDefinition();
        MongoClient mongoClient = new MongoClient("192.168.1.68");
       // MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("ThingsDataBase");
        //aqui colocar un bloque que averigue si la Base de datos existe para ver si la crea o no.
        collection = database.getCollection("Devices");
        initCollection = database.getCollection("TD");
        docCount=collection.countDocuments();
        return docCount;
    };

public static void  RetrieveLLDevList (){

     uAALBridge getDevices = new uAALBridge();
     String vURL = "http://192.168.1.68:8181/uAALServices";//Only to ask uAAL server for list of devices (Temporary)
        String allDevices =getDevices.sendSyncRedirectToLL(vURL+"?rawDevices");
  
    System.out.println(allDevices);
    ArrayList<String> allDevicesArray= new ArrayList<>();
    String parts[]=allDevices.split(",");
    int i=0;
    Hashtable devicesTable = new Hashtable();
    for (String val: parts){
        allDevicesArray.add(val);//recibe todos los dispositivos disponibles
        devicesTable.put(i,val);// crea la tabla de correspondencias entre nombres y id de cada dispositivo.
        val.toLowerCase();
        if (val.contains("door")) {
            newDevice.devHref="/";								//its only purpose is to build devices
            newDevice.inputThingType="Door";							//for test. Once W3C standard is released
            newDevice.inputType="Boolean";								//it must be removed and implemented in uAAL
            newDevice.outputType="Boolean";								//
            newDevice.name=val;
            newDevice.propertyName="status";
            newDevice.propType="Open/Close";
            newDevice.writable=true;

           newDevice.devSerial=docCount;
            // newDevice.devStatus= ;//initial status that Server should provide
            Document device = new Document(DevicesCreator.assembleDevice(newDevice));
            collection.insertOne(device);


        }
        else if (val.contains("blind")){
            newDevice.devHref="/";									//its only purpose is to build devices
            newDevice.inputThingType="Blind";							//for test. Once W3C standard is released
            newDevice.inputType="Boolean";								//it must be removed and implemented in uAAL
            newDevice.outputType="Boolean";								//
            newDevice.name=val;
            newDevice.propertyName="status";
            newDevice.propType="Open/Close";
            newDevice.writable=true;

            newDevice.devSerial=docCount;
            // newDevice.devStatus= ;//initial status that Server should provide
            Document device = new Document(DevicesCreator.assembleDevice(newDevice));
            collection.insertOne(device);

        }
        else {
            newDevice.devHref="/";								//its only purpose is to build devices
            newDevice.inputThingType = "Light";                            //for test. Once W3C standard is released
            newDevice.inputType = "Boolean";                                //it must be removed and implemented in uAAL
            newDevice.outputType = "Boolean";                                //
            newDevice.name = val;
            newDevice.propertyName = "status";
            newDevice.propType = "On / Off";
            newDevice.writable = true;

             newDevice.devSerial=docCount;
            // newDevice.devStatus= ;//initial status that Server should provide
            Document device = new Document(DevicesCreator.assembleDevice(newDevice));

            collection.insertOne(device);
         /*   DBObject propFilter = new BasicDBObject();
            propFilter.put( "name", val);
            device =  collection.find((Bson) propFilter).first();
            collection.updateOne(new Document("name",val), new Document("$set", new Document("href",device.getObjectId("_id"))));*/
        }

        ++i;
    }



}

public static void buildDataBase(){
    RetrieveLLDevList();

    List<Document> devs = (List<Document>)collection.find().into(
            new ArrayList<Document>());

    for (Document device : devs) {
        Document link = new Document("href", "/things/"+device.getObjectId("_id")+"/properties/status")
                .append("mediaType", "application/json");
        Document property = new Document("@type", Arrays.asList("Property", DevicesCreator.propType))
                .append("inputType", new Document("type", DevicesCreator.inputType))
                .append("outputType", new Document("type", DevicesCreator.outputType))
                .append("writable", DevicesCreator.writable)
                .append("link", Arrays.asList(link));
        Document properties = new Document(DevicesCreator.propertyName, property);
        Bson euge = new Document("href", "/things/"+device.getObjectId("_id")).append("properties", new Document(DevicesCreator.propertyName, property));
        //Bson newValue = new Document("href", "/"+device.getObjectId("_id"));
        Bson updateOperationDocument = new Document("$set", euge);
        collection.updateOne(device, updateOperationDocument);


    }

}
}
//=====================================================
//SECUENCIA TEMPORAL PARA CREAR LISTADO DE DISPOSITIVOS
		/*uAALBridge getDevices = new uAALBridge();

		String vURL = newInstance.generateDomain()+"/"+newInstance.defineService();
		//String vURL = "http://192.168.1.130:8181/uAALServices";//Only to ask uAAL server for list of devices (Temporary)
		String allDevices =getDevices.sendSyncRedirectToLL(vURL+"?devices");
		System.out.println(allDevices);
		ArrayList<String> allDevicesArray= new ArrayList<>();
		String parts[]=allDevices.split(",");
		int i=0;
		Hashtable devicesTable = new Hashtable();
		for (String val: parts){
			allDevicesArray.add(val);//recibe todos los dispositivos disponibles
			devicesTable.put(i,val);// crea la tabla de correspondencias entre nombres y id de cada dispositivo.
			i=i+1;
			 }
			System.out.println(devicesTable);*/
//======================================================