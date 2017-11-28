package es.upm.p4act;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Plan4ActMessageParser {
/*
 * Remote protocol
 *
URL:
http://138.4.10.224/remote.htm?id=LUZ_CASA_ONOFF&value=1

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

/*
 Plan4Act Ref
 
 class Device:
    """

    This class define the devices of the lab. Living lab 3D Simulator codes

    Description                       | Device name      | Device id           | 
    ---------------------------------------------------------------------------
    Puerta principal                  | doorHome         | MAIN_DOOR   
    Puerta/Ventanal Salon             | doorSalon        | LIVINGROOM_WINDOW
    Puerta del bano                   | doorBath         | BATHROOM_DOOR
    Puerta/Ventanal Cocina            | doorKitchen      | KITCHEN_WINDOW
    Persiana Dormitorio               | RollupBed        | BEDROOM_BLIND
    Persiana Cocina                   | RollupKit        | KITCHEN_BLIND
    Persiana Salon                    | RollupSal        | LIVINGROOM_BLIND
    Luz Cocina                        | KitchenLight     | KITCHEN_LIGHT
    Luz Salon                         | SalaLight        | LIVINGROOM_LIGHT
    Luz Dormitorio                    | BedroomLight     | BEDROOM_LIGHT
    Luz Servicio                      | BathroomLight    | BATHROOM_LIGHT
    Luz Sala Control                  | SCLight          | CONTROLROOM_LIGHT
    Luz Habitacion Realidad Virtual   | RVLight          | VR_ROOM_LIGHT
    Persiana Sala Control             | RollupSC         | CONTROLROOM_BLIND
    Puerta Sala Control               | doorSC           | CONTROLROOM_DOOR
    ---------------------------------------------------------------------------
    MediaPlayer                       | Media Player     | MEDIA_PLAYER
    
    */
	
	
	
    /*
    """
    LIVINGROOM_WINDOW = "LIVINGROOM_WINDOW"
    BATHROOM_DOOR = "BATHROOM_DOOR"
    KITCHEN_WINDOW = "KITCHEN_WINDOW"
    BEDROOM_BLIND = "BEDROOM_BLIND"
    KITCHEN_BLIND = "KITCHEN_BLIND"
    LIVINGROOM_BLIND = "LIVINGROOM_BLIND"
    KITCHEN_LIGHT = "KITCHEN_LIGHT"
    LIVINGROOM_LIGHT = "LIVINGROOM_LIGHT"
    BEDROOM_LIGHT = "BEDROOM_LIGHT"
    BATHROOM_LIGHT = "BATHROOM_LIGHT"
    CONTROLROOM_LIGHT = "CONTROLROOM_LIGHT"
    VR_ROOM_LIGHT = "VR_ROOM_LIGHT"
    CONTROLROOM_BLIND = "CONTROLROOM_BLIND"
    CONTROLROOM_DOOR = "CONTROLROOM_DOOR"
    #New devices
    MAIN_DOOR = "MAIN_DOOR"
    MAIN_LIGHT = "MAIN_LIGHT"
    SMART_BOX = "SMART_BOX"
    MAIN_BLIND = "MAIN_BLIND"
    MEDIA_PLAYER = "MEDIA_PLAYER" 

class Value:
    """

    This class defines the possible states of the devices.
    On and Open such as Close and Off have the same value.
    you can call Device.MAIN_DOOR, Value.OPEN or  Device.MAIN_DOOR, Value.ON.
    """
    ON = "1"
    OFF = "0"
    OPEN = "1"
    CLOSE = "0"

Request schema:

{
"version":"string/enum[mercury, venus, earth, etc..]",     
"cmd" : "string/enum[getstatus, setstatus]",
"device_id":"string", 
"device_name":"string/enum[door1, door2, blind, light1, etc...]",
"value":"string/optional [true/false] required when cmd=setstatus", 
"sequence_number":"string/optional" 
}

Response schema:

{
"version":"string/enum[mercury, venus, earth, etc..]",       
"cmd" : "string/enum[getstatus, setstatus]",
"code" : "integer/enu[200,400,...]",
"description": "string",
"device_id":"string", 
"device_name":"string/enum[door1, door2, blind, light1, etc...]",
"value":"string/optional [true/false] required when code=200",
"sequence_number":"string/optional"  
}

 
 */

	
	public static String fromP4AResponseToJson(Plan4ActResponse response){
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(response);
		System.out.println(json);
		return json;
	}
	
	public static String fromP4ARequestToJson(Plan4ActRequest request){
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(request);
		System.out.println(json);
		return json;
	}
	
	
	public static Plan4ActResponse fromJsonToP4AResponse(String response){
		Gson gson = new Gson();
		Plan4ActResponse p4a_res= gson.fromJson(response, Plan4ActResponse.class);
		return p4a_res;
	}
	
	public static Plan4ActRequest fromJsonToP4ARequest(String request){
		Gson gson = new Gson();
		Plan4ActRequest p4a_req= gson.fromJson(request, Plan4ActRequest.class);
		return p4a_req;
	}
	
	
	

}
