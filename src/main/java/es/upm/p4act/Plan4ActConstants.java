package es.upm.p4act;

import java.util.Hashtable;

import es.upm.ll.device.RemoteDeviceConstants;

public class Plan4ActConstants {  
	 /*
    Utilities functions Living Lab
    This class define the devices of the lab.

       Description                       | Device name      | Plan4Act ID         | Remote ID           |
       -----------------------------------------------------------------------------------------------
       Puerta principal                  | doorHome         | MAIN_DOOR           | PUERTA_PRINCIPAL    |
       Puerta/Ventanal Salon             | doorSalon        | LIVINGROOM_WINDOW   | VENTANAL_SALON      |
       Puerta del bano                   | doorBath         | BATHROOM_DOOR       | PUERTA_ASEO_ABIERTA / PUERTA_ASEO_CERRADA |
       Puerta/Ventanal Cocina            | doorKitchen      | KITCHEN_WINDOW      | VENTANAL_COCINA     |  
       Persiana Dormitorio               | RollupBed        | BEDROOM_BLIND       | PERSIANA_HABITACION |
       Persiana Cocina                   | RollupKit        | KITCHEN_BLIND       | PERSIANA_COCINA     |
       Persiana Salon                    | RollupSal        | LIVINGROOM_BLIND    | PERSIANA_SALON      |
       Luz Cocina                        | KitchenLight     | KITCHEN_LIGHT       | LUZ_COCINA          |
       Luz Salon                         | SalaLight        | LIVINGROOM_LIGHT    | LUZ_SALON           |
       Luz Dormitorio                    | BedroomLight     | BEDROOM_LIGHT       | LUZ_HABITACION      |
       Luz Servicio                      | BathroomLight    | BATHROOM_LIGHT      | LUZ_ASEO_INODORO    |   
       Luz Sala Control                  | SCLight          | CONTROLROOM_LIGHT   | Only 3D simulator   !
       Luz Habitacion Realidad Virtual   | RVLight          | VR_ROOM_LIGHT       | Only 3D simulator   |
       Persiana Sala Control             | RollupSC         | CONTROLROOM_BLIND   | Only 3D simulator   |
       Puerta Sala Control               | doorSC           | CONTROLROOM_DOOR    | Only 3D simulator   |
       ---------------------------------------------------------------------------------------------------
       MediaPlayer                       | Media Player     | MEDIA_PLAYER        | Not in remote
       Test light device                 | Test Light       | HOUSE_LIGHT         | Not in remote
        */

   private static boolean isRemote = true;

   public static String MEDIA_PLAYER= "MEDIA_PLAYER";
   public static String HOUSE_LIGHT = "HOUSE_LIGHT";
   public static String MAIN_DOOR = "MAIN_DOOR";
   public static String LIVINGROOM_WINDOW = "LIVINGROOM_WINDOW";
   public static String BATHROOM_DOOR = "BATHROOM_DOOR";
   public static String KITCHEN_WINDOW = "KITCHEN_WINDOW";
   public static String BEDROOM_BLIND = "BEDROOM_BLIND";
   public static String KITCHEN_BLIND = "KITCHEN_BLIND";
   public static String LIVINGROOM_BLIND = "LIVINGROOM_BLIND";
   public static String KITCHEN_LIGHT = "KITCHEN_LIGHT";
   public static String LIVINGROOM_LIGHT = "LIVINGROOM_LIGHT";
   public static String BEDROOM_LIGHT = "BEDROOM_LIGHT";
   public static String BATHROOM_LIGHT = "BATHROOM_LIGHT";
	
   private static Hashtable<String, String> devices;
   
   public static Hashtable<String, String> names;

   
   private static void initDevices(){
	   if (devices==null){
		   devices = new Hashtable<String, String>();
			if(isRemote){
						 devices.putAll(RemoteDeviceConstants.devices);  
					   }
		   devices.put("MEDIA_PLAYER", "Media Player");
		  
	   }
   }
   
   private static void initNames(){
	   if (names==null){
		   names = new Hashtable<String, String>();
		   names.put("MEDIA_PLAYER", "Media Player");
		   names.put("HOUSE_LIGHT", "LUZ_CASA nam");
		   names.put("MAIN_DOOR", "PUERTA_PRINCIPAL nam");
		   names.put("LIVINGROOM_WINDOW", "PUERTA_PRINCIPAL nam");
		   names.put("BATHROOM_DOOR", "PUERTA_ASEO_ABIERTA nam");
		   names.put("KITCHEN_WINDOW", "VENTANAL_COCINA nam");
		   names.put("BEDROOM_BLIND", "PERSIANA_HABITACION nam");
		   names.put("KITCHEN_BLIND", "PERSIANA_COCINA nam");
		   names.put("LIVINGROOM_BLIND", "PERSIANA_SALON nam");
		   names.put("KITCHEN_LIGHT", "LUZ_COCINAnam");
		   names.put("LIVINGROOM_LIGHT", "LUZ_SALON");
		   names.put("BEDROOM_LIGHT", "LUZ_HABITACION");
		   names.put("BATHROOM_LIGHT", "LUZ_ASEO_INODORO"); 
	   }
   }
   
   public static String get(String key){
	 try{
	  initDevices();
	  String get= devices.get(key);
	  return get;	
	   }
	   catch(Exception e){
	   return "unknown";	   
	   }
   }
   
   public static String getNames(String key){
	   try{
	  initNames();	
	  String get= names.get(key.trim());
	  return get;	
	   }
	   catch(Exception e){ 
	   return "unknown";	   
	   }
   }
   

 
  

  
    
}
