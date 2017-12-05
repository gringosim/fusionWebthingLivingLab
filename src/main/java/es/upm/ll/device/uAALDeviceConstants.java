package es.upm.ll.device;

import java.util.Hashtable;
/*
 * This class matches the constants that identify the devices inside the service of the Plan4Act with the identifiers used into the universAAL service 
 */
public class uAALDeviceConstants {

	
public static final Hashtable<String, String> devices;
private static final String LOCATION_PREFIX = "http://livinglab.lst.tfo.upm.es/Location#";
private static final String DEVICE_PREFIX = "http://livinglab.lst.tfo.upm.es/Device#";
  
//Service constants
public static final String METHOD_LOCATION="/locations";
public static final String METHOD_DEVICE_TYPE="/deviceTypes";
public static final String METHOD_FILTER_DEV="/devices";
public static final String METHOD_SUBSCRIBE="/subscribe";
public static final String METHOD_SET_DEVICE_VALUE="/set";
public static final String PARAMETER_RESOURCE = "resource";
public static final String PARAMETER_DEVICE_URL = "device";
public static final String PARAMETER_DEVICE_VALUE = "value";
public static final String PARAMETER_LOCATION = "location";
public static final String PARAMETER_DEVICE_TYPE = "device_type";
public static final String INFO = "info";

    static {
    	//key Plan4Act constant, values uAAL device constant
    	Hashtable<String, String> aMap = new Hashtable<String, String>();
    	aMap.put("HOUSE_LIGHT", "");
    	aMap.put("MAIN_DOOR", "http://livinglab.lst.tfo.upm.es/Device%23magneticContact-Recall-Entrance");
    	aMap.put("LIVINGROOM_WINDOW", "PUERTA_PRINCIPAL");
    	aMap.put("BATHROOM_DOOR", "http://livinglab.lst.tfo.upm.es/Device%23magneticContact-BathRoom/Door");
    	aMap.put("KITCHEN_WINDOW", "VENTANAL_COCINA");
    	aMap.put("BEDROOM_BLIND", "PERSIANA_HABITACION");
    	aMap.put("KITCHEN_BLIND", "PERSIANA_COCINA");
    	aMap.put("LIVINGROOM_BLIND", "PERSIANA_SALON");
    	aMap.put("KITCHEN_LIGHT", "LUZ_COCINA");
    	aMap.put("LIVINGROOM_LIGHT", "LUZ_SALON");
    	aMap.put("BEDROOM_LIGHT", "LUZ_HABITACION");
    	aMap.put("BATHROOM_LIGHT", "LUZ_ASEO_INODORO");
        devices = aMap;
    }
    
    public static boolean is_uAALDevice(String key){
    	return devices.containsKey(key);
    }
    
    /*
     
    Utilities functions Living Lab
    This class define the devices of the lab.

       Description                       | Device name      | Plan4Act ID         | Remote ID           | uAAL Device
       ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
       Puerta principal                  | doorHome         | MAIN_DOOR           | PUERTA_PRINCIPAL    | http://livinglab.lst.tfo.upm.es/Device%23magneticContact-Recall-Entrance\
       Puerta/Ventanal Salon             | doorSalon        | LIVINGROOM_WINDOW   | VENTANAL_SALON      |
       Puerta del bano                   | doorBath         | BATHROOM_DOOR       | PUERTA_ASEO_ABIERTA  http://livinglab.lst.tfo.upm.es/Device%23magneticContact-BathRoom/Door
       																			  / PUERTA_ASEO_CERRADA  
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
       Test light device                 | Test Light       | HOUSE_LIGHT         | LUZ_CASA            |
        
      */
    



    public static String OPEN="1";
    public static String CLOSE="0";
    public static String ON="1";
    public static String OFF="0";
    /*
    URL:
    http://138.4.10.224/remote.htm?id= LUZ_CASA_ONOFF&value=1

    *LUZ_CASA_ONOFF
    LUZ_CASA
    ESTADO_LUZ_CASA_ONOFF
    LUZ_CASA_ONOFF_REENVIO
    *LUZ_CALLE_ONOFF
    *LUZ_PORCHE_ONOFF
    *LUZ_SCEXT_ONOFF
    *LUZ_ENTRADA_ONOFF
    LUZ_ENTRADA
    ESTADO_LUZ_ENTRADA_ONOFF
    ESTADO_PUERTA_PRINCIPAL
    HVAC_ONOFF
    HVAC_TEMPERATURA
    HVAC_MODOINVIERNO
    HVAC_MODOVERANO
    *PUERTA_PRINCIPAL
    *PERSIANAS_CASA
    *LUZ_HABITACION_ENTRADA_ONOFF
    LUZ_HABITACION_ENTRADA
    ESTADO_LUZ_HABITACION_ENTRADA_ONOFF
    *LUZ_HABITACION_CABECERA_ONOFF
    LUZ_HABITACION_CABECERA
    ESTADO_LUZ_HABITACION_CABECERA_ONOFF
    *LUZ_SALON_ENTRADA_ONOFF
    LUZ_SALON_ENTRADA
    LUZ_SALON_COCINA_ONOFF
    LUZ_SALON_COCINA
    LUZ_SALON_COCINA_ESTADO
    LUZ_SALON_HABITACION_ONOFF
    LUZ_SALON_HABITACION
    LUZ_SALON_ASEO_ONOFF
    LUZ_SALON_ASEO
    LUZ_SALATV_ASEO_ONOFF
    LUZ_SALATV_ASEO
    LUZ_SALATV_HABITACION_ONOFF
    LUZ_SALATV_HABITACION
    LUZ_COCINA_FREGADERO_ONOFF
    LUZ_COCINA_FREGADERO
    LUZ_COCINA_FRIGORIFICO_ONOFF
    LUZ_COCINA_FRIGORIFICO
    LUZ_COCINA_HORNO_ONOFF
    LUZ_COCINA_HORNO
    LUZ_COCINA_VITRO_ONOFF
    LUZ_COCINA_VITRO
    LUZ_ASEO_DUCHA_ONOFF
    LUZ_ASEO_DUCHA
    ESTADO_LUZ_ASEO_DUCHA_ONOFF
    LUZ_ASEO_INODORO_ONOFF
    LUZ_ASEO_INODORO
    ESTADO_LUZ_ASEO_WC_ONOFF
    PERSIANA_HABITACION
    PERSIANA_SALON_ENTRADA
    PERSIANA_SALON_AUTOMATIZADA
    PERSIANA_COCINA_SALON
    PERSIANA_COCINA_AUTOMATIZADA
    PERSIANA_COCINA
    PERSIANA_SALON
    ESTADO_VENTANAL_SALON
    ESTADO_VENTANAL_SALON_CM_REENVIO
    ESTADO_VENTANAL_COCINA
    ESTADO_HVAC_MODOVERANO
    ESTADO_HVAC_MODOINVIERNO
    ESTADO_HVAC_TEMPERATURA
    PUERTA_ASEO_ABIERTA
    PUERTA_ASEO_CERRADA
    CASCADA_ESTANQUE_ONOFF
    FUENTE_ESTANQUE_ONOFF
    AIREADOR_ESTANQUE_ONOFF
    VENTANAL_SALON
    VENTANAL_COCINA
    LUZ_HABITACION_ONOFF
    LUZ_HABITACION
    ESTADO_LUZ_HABITACION_ONOFF
    LUZ_SALON_ONOFF
    LUZ_SALON
    ESTADO_LUZ_SALON_ONOFF
    LUZ_SALON_ENTRADA_COCINA_ONOFF
    LUZ_SALON_ENTRADA_COCINA
    LUZ_SALON_HABITACION_ASEO_ONOFF
    LUZ_SALON_HABITACION_ASEO
    LUZ_SALATV_ONOFF
    LUZ_SALATV
    ESTADO_LUZ_SALATV_ONOFF
    LUZ_COCINA_ONOFF
    LUZ_COCINA
    LUZ_COCINA_HORNO_VITRO_ONOFF
    LUZ_COCINA_HORNO_VITRO
    ESTADO_LUZ_COCINA_ONOFF
    LUZ_COCINA_FRIGO_FREGADERO_ONOFF
    LUZ_COCINA_FRIGO_FREGADERO
    LUZ_ASEO_ONOFF
    LUZ_ASEO
    ESTADO_LUZ_ASEO_ONOFF
    VENTANAL_COCINA_SALON
     */
	
}
