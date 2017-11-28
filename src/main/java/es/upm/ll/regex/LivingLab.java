package es.upm.ll.regex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.SwingWorker;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class LivingLab
{
	
	public static final int
		OFF 	= 0,
		ON 		= 1,
		RAISE 	= 0,
		LOWER 	= 1,
		OPEN 	= 1,
		CLOSE 	= 0;


	public static final String
		url = "http://138.4.10.224/remote.htm?",
		id = "id=",
		value = "&value=",
		hello = "helloRemote",
		
		
		frontDoor = "PUERTA_PRINCIPAL",
		
		// Group of lights
		lightsAll = "LUZ_CASA_ONOFF",
		lightsRoom = "LUZ_HABITACION_ONOFF",
		lightsLivingRoom = "LUZ_SALON_ONOFF",
		lightsTVRoom = "LUZ_SALATV_ONOFF",
		lightsKitchen = "LUZ_COCINA_ONOFF",
		lightsSinkFridge = "LUZ_COCINA_FRIGO_FREGADERO_ONOFF",
		lightsKitchenOvenCooktop = "LUZ_COCINA_HORNO_VITRO_ONOFF",
		lightsToilet = "LUZ_ASEO_ONOFF",
		
		// Individual lights
		lightsHall = "LUZ_ENTRADA_ONOFF",
		lightsHallRoom = "LUZ_HABITACION_ENTRADA_ONOFF",
		lightsHeadboardRoom = "LUZ_HABITACION_CABECERA_ONOFF",	
		lightsHallLivingRoom = "LUZ_SALON_ENTRADA_ONOFF",
		lightsKitchenLivingRoom = "LUZ_SALON_COCINA_ONOFF",
		lightsToiletLivingRoom = "LUZ_SALON_ASEO_ONOFF",
		lightsRoomLivingRoom = "LUZ_SALON_HABITACION_ONOFF",
		lightsToiletTvRoom = "LUZ_SALATV_ASEO_ONOFF",
		lightsRoomTvRoom = "LUZ_SALATV_HABITACION_ONOFF",
		lightsSinkKitchen = "LUZ_COCINA_FREGADERO_ONOFF",
		lightsFridgeKitchen = "LUZ_COCINA_FRIGORIFICO_ONOFF",
		lightsCooktopKitchen = "LUZ_COCINA_VITRO_ONOFF",
		lightsOvenKitchen = "LUZ_COCINA_HORNO_ONOFF",
		lightsShowerToilet = "LUZ_ASEO_DUCHA_ONOFF",
		lightsWCToilet = "LUZ_ASEO_INODORO_ONOFF",
		ligthsPorch = "LUZ_PORCHE_ONOFF",
		lightsSCExt = "LUZ_SCEXT_ONOFF",
		lightsOutside = "LUZ_CALLE_ONOFF",
		
		// Group of blinds
		blindKitchen = "PERSIANA_COCINA",
		blindLivingRoom = "PERSIANA_SALON",
		
		// Individual blinds
		blindRoom = "PERSIANA_HABITACION",
		blindRoomLivingRoom = "PERSIANA_SALON_ENTRADA",
		blindAutomaticLivingRoom = "PERSIANA_SALON_AUTOMATIZADA",
		blindKitchenLivingRoom = "PERSIANA_COCINA_SALON",
		blindAutomaticKitchen = "PERSIANA_COCINA_AUTOMATIZADA";
		
		
	public static void openFrontDoor() { sendOrder(LivingLab.frontDoor, OPEN); }
	public static void closeFrontDoor() { sendOrder(LivingLab.frontDoor, CLOSE); }	
		
	public static void allLightsOn() { sendOrder(LivingLab.lightsAll, ON); }
	public static void allLightsOff() { sendOrder(LivingLab.lightsAll, OFF); }
	
	public static void roomLightsOn() { sendOrder(LivingLab.lightsRoom, ON); }
	public static void roomLightsOff() { sendOrder(LivingLab.lightsRoom, OFF); }
	
	public static void livingRoomLightsOn() { sendOrder(LivingLab.lightsLivingRoom, ON); }
	public static void livingRoomOff() { sendOrder(LivingLab.lightsLivingRoom, OFF); }
	
	public static void tvRoomOn() { sendOrder(LivingLab.lightsTVRoom, ON); }
	public static void tvRoomOff() { sendOrder(LivingLab.lightsTVRoom, OFF); }

	public static void kitchenOn() { sendOrder(LivingLab.lightsKitchen, ON); }
	public static void kitchenOff() { sendOrder(LivingLab.lightsKitchen, OFF); }
	
	public static void sinkFridgeLightsOn() { sendOrder(LivingLab.lightsSinkFridge, ON); }
	public static void sinkFridgeLightsOff() { sendOrder(LivingLab.lightsSinkFridge, OFF); }
	
	public static void kitchenOvenCooktopLightsOn() { sendOrder(LivingLab.lightsKitchenOvenCooktop, ON); }
	public static void kitchenOvenCooktopLightsOff() { sendOrder(LivingLab.lightsKitchenOvenCooktop, OFF); }
	
	public static void toiletLightsOn() { sendOrder(LivingLab.lightsToilet, ON); }
	public static void toiletLightsOff() { sendOrder(LivingLab.lightsToilet, OFF); }
	
	public static void hallLightsOn() { sendOrder(LivingLab.lightsHall, ON); }
	public static void hallLightsOff() { sendOrder(LivingLab.lightsHall, OFF); }
	
	public static void hallRoomLightsOn() { sendOrder(LivingLab.lightsHallRoom, ON); }
	public static void hallRoomLightsOff() { sendOrder(LivingLab.lightsHallRoom, OFF); }
	
	public static void headboardRoomLightsOn() { sendOrder(LivingLab.lightsHeadboardRoom, ON); }
	public static void headboardRoomLightsOff() { sendOrder(LivingLab.lightsHeadboardRoom, OFF); }
	
	public static void hallLivingRoomLightsOn() { sendOrder(LivingLab.lightsHallLivingRoom, ON); }
	public static void hallLivingRoomLightsOff() { sendOrder(LivingLab.lightsHallLivingRoom, OFF); }

	public static void kitchenLivingRoomLightsOn() { sendOrder(LivingLab.lightsKitchenLivingRoom, ON); }
	public static void kitchenLivingRoomLightsOff() { sendOrder(LivingLab.lightsKitchenLivingRoom, OFF); }

	public static void toiletLivingRoomLightsOn() { sendOrder(LivingLab.lightsToiletLivingRoom, ON); }
	public static void toiletLivingRoomLightsOff() { sendOrder(LivingLab.lightsToiletLivingRoom, OFF); }

	public static void roomLivingRoomLightsOn() { sendOrder(LivingLab.lightsRoomLivingRoom, ON); }
	public static void roomLivingRoomLightsOff() { sendOrder(LivingLab.lightsRoomLivingRoom, OFF); }

	public static void toiletTvRoomLightsOn() { sendOrder(LivingLab.lightsToiletTvRoom, ON); }
	public static void toiletTvRoomLightsOff() { sendOrder(LivingLab.lightsToiletTvRoom, OFF); }

	public static void roomTvRoomLightsOn() { sendOrder(LivingLab.lightsRoomTvRoom, ON); }
	public static void roomTvRoomLightsOff() { sendOrder(LivingLab.lightsRoomTvRoom, OFF); }

	public static void sinkKitchenLightsOn() { sendOrder(LivingLab.lightsSinkKitchen, ON); }
	public static void sinkKitchenLightsOff() { sendOrder(LivingLab.lightsSinkKitchen, OFF); }

	public static void fridgeKitchenLightsOn() { sendOrder(LivingLab.lightsFridgeKitchen, ON); }
	public static void fridgeKitchenLightsOff() { sendOrder(LivingLab.lightsFridgeKitchen, OFF); }

	public static void cooktopKitchenLightsOn() { sendOrder(LivingLab.lightsCooktopKitchen, ON); }
	public static void cooktopKitchenLightsOff() { sendOrder(LivingLab.lightsCooktopKitchen, OFF); }

	public static void ovenKitchenLightsOn() { sendOrder(LivingLab.lightsOvenKitchen, ON); }
	public static void ovenKitchenLightsOff() { sendOrder(LivingLab.lightsOvenKitchen, OFF); }

	public static void showerToiletLightsOn() { sendOrder(LivingLab.lightsShowerToilet, ON); }
	public static void showerToiletLightsOff() { sendOrder(LivingLab.lightsShowerToilet, OFF); }

	public static void wcToiletLightsOn() { sendOrder(LivingLab.lightsWCToilet, ON); }
	public static void wcToiletLightsOff() { sendOrder(LivingLab.lightsWCToilet, OFF); }

	public static void porchLightsOn() { sendOrder(LivingLab.ligthsPorch, ON); }
	public static void porchLightsOff() { sendOrder(LivingLab.ligthsPorch, OFF); }

	public static void scExtLightsOn() { sendOrder(LivingLab.lightsSCExt, ON); }
	public static void scExtLightsOff() { sendOrder(LivingLab.lightsSCExt, OFF); }

	public static void outsideLightsOn() { sendOrder(LivingLab.lightsOutside, ON); }
	public static void outsideLightsOff() { sendOrder(LivingLab.lightsOutside, OFF); }
	
	public static void kitchenBlindRaise() { sendOrder(LivingLab.blindKitchen, RAISE); }
	public static void kitchenBlindLower() { sendOrder(LivingLab.blindKitchen, LOWER); }
	
	public static void livingRoomBlindRaise() { sendOrder(LivingLab.blindLivingRoom, RAISE); }
	public static void livingRoomBlindLower() { sendOrder(LivingLab.blindLivingRoom, LOWER); }
	
	public static void RoomBlindRaise() { sendOrder(LivingLab.blindRoom, RAISE); }
	public static void RoomBlindLower() { sendOrder(LivingLab.blindRoom, LOWER); }
	
	public static void roomLivingRoomBlindRaise() { sendOrder(LivingLab.blindRoomLivingRoom, RAISE); }
	public static void roomLivingRoomBlindLower() { sendOrder(LivingLab.blindRoomLivingRoom, LOWER); }
	
	public static void livingRoomAutomaticBlindRaise() { sendOrder(LivingLab.blindAutomaticLivingRoom, RAISE); }
	public static void livingRoomAutomaticBlindLower() { sendOrder(LivingLab.blindAutomaticLivingRoom, LOWER); }
	
	public static void kitchenLivingRoomBlindRaise() { sendOrder(LivingLab.blindKitchenLivingRoom, RAISE); }
	public static void kitchenLivingRoomBlindLower() { sendOrder(LivingLab.blindKitchenLivingRoom, LOWER); }
	
	public static void automaticKitchenBlindRaise() { sendOrder(LivingLab.blindAutomaticKitchen, RAISE); }
	public static void automaticKitchenBlindLower() { sendOrder(LivingLab.blindAutomaticKitchen, LOWER); }
	
	
	public static void raiseAllBlinds()
	{
		SwingWorker<Void, Void> at = new SwingWorker<Void, Void>()
    	{
	        @Override
	        protected Void doInBackground() throws Exception 
	        {
	        	try
	        	{
	        		HttpClient client = new DefaultHttpClient();
	                HttpGet command = new HttpGet(url+id+blindKitchen+value+RAISE);
	                client.execute(command);
	                command = new HttpGet(url+id+blindLivingRoom+value+RAISE);
	                client.execute(command);
	                command = new HttpGet(url+id+blindRoom+value+RAISE);
	                client.execute(command);
	        	}		  
	        	catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}
				return null;
	        }
    	};
    	at.execute();
	}
	
	public static void lowerAllBlinds()
	{
		SwingWorker<Void, Void> at = new SwingWorker<Void, Void>()
    	{
	        @Override
	        protected Void doInBackground() throws Exception 
	        {
	        	try
	        	{
	        		HttpClient client = new DefaultHttpClient();
	                HttpGet command = new HttpGet(url+id+blindKitchen+value+LOWER);
	                client.execute(command);
	                command = new HttpGet(url+id+blindLivingRoom+value+LOWER);
	                client.execute(command);
	                command = new HttpGet(url+id+blindRoom+value+LOWER);
	                client.execute(command);
	        	}		  
	        	catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}
				return null;
	        }
    	};
    	at.execute();
	}
	
    public static void sendOrder2(String subject, int param)
    {
    	try
    	{
    		HttpClient client = new DefaultHttpClient();
            HttpGet command = new HttpGet(url+id+subject+value+param);
            client.execute(command);
    	}		  
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public static void sendOrder(final String subject, final int param)
    {
    	SwingWorker<Void, Void> at = new SwingWorker<Void, Void>()
    	{
	        @Override
	        protected Void doInBackground() throws Exception 
	        {
	        	try
	        	{
	        		HttpClient client = new DefaultHttpClient();
	                HttpGet command = new HttpGet(url+id+subject+value+param);
	                client.execute(command);
	        	}		  
	        	catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}
				return null;
	        }
    	};
    	at.execute();
    }
    
    public static boolean isServerOnline (){
    	
    	boolean isOnline = false;
    	String inputLine;
    	
    	try 
    	{
    		URL helloUrl = new URL(url+hello);
    		BufferedReader in = new BufferedReader(new InputStreamReader(helloUrl.openStream()));

    		while ((inputLine = in.readLine()) != null && !isOnline)
    		    if(inputLine.contains("OK;0"))
    		    	isOnline = true;
    		 	
    		in.close();
    		
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		} 
    	
    	return isOnline;
    }
    
    public static int getDeviceStatus (String device)
    {
    
	    String deviceStatus = "ESTADO_" + device;
	    int status = -1;
	    String inputLine = null;
	    try 
    	{
    		URL statusUrl = new URL(url+id+deviceStatus);
    		BufferedReader in = new BufferedReader(new InputStreamReader(statusUrl.openStream()));

    		while ((inputLine = in.readLine()) != null)
    		{
    		    if (inputLine.contains("OK;0"))
    		    	status = 0;
    		    else if (inputLine.contains("OK;1"))
    		    	status = 1;
		    }
    		 	
    		in.close();
    		
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		} 
		
		return status; 
	    
	}
    	
}


