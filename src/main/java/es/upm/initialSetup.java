package es.upm;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import es.upm.ll.ContextDefinition;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

public class initialSetup {
    public static long docCount;
    public static MongoCollection<Document> collection;
    public static DevicesCreator newDevice = new DevicesCreator();
    public static String dataBaseName = "ThingsDtaBase";

    public static long dbInit() {
        ContextDefinition newInstance = new ContextDefinition();
        MongoClient mongoClient = new MongoClient(newInstance.generateDbDomain());
        MongoDatabase database = mongoClient.getDatabase(dataBaseName);
        collection = database.getCollection("Devices");
        docCount=collection.countDocuments();
        return docCount;
    };

    public static void deviceDBInit()  {
       			//This block is temporary and
        newDevice.devHref="/"+initialSetup.docCount;								//its only purpose is to build devices
        newDevice.inputThingType="Door";							//for test. Once W3C standard is released
        newDevice.inputType="integer";								//it must be removed and implemented in uAAL
        newDevice.outputType="integer";								//
        newDevice.name="lab_light";
        newDevice.propertyName="on";
        newDevice.propType="On/Off";
        newDevice.writable=true;
        newDevice.devSerial=initialSetup.docCount;
        newDevice.devStatus= true;//initial status
        try {
            if (newDevice.name != "") {
                 DBObject compareFilter = new BasicDBObject();
                 compareFilter.put("name", newDevice.name);
                 Document seek = new Document((initialSetup.collection).find((Bson) compareFilter).first());
                 JSONObject seekObj = new JSONObject(seek.toJson());

                 if (seekObj.length() != 0) {

                System.out.println( "Device already exists in "+dataBaseName+".");
                }
            }
        }
        catch (NullPointerException e) {
            //no ejecutar secuencias de trabajo aqui
            Document device = new Document(DevicesCreator.assembleDevice(newDevice));
            initialSetup.collection.insertOne(device);
          System.out.println("Excepción");
        }

    };


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