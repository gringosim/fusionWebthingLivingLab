package es.upm.ll;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.SwingWorker;

import es.upm.vlc.ServerResponse;
import javax.swing.SwingWorker;
/**
 * Created by Eugenio on 02/06/2017.
           String url = "http://192.168.2.3:4200/device?id=LUZ_CASA_ONOFF&value=1";
   Usage: (new LLConnector(this)).sendRequestToLL(url);


 */

public class LlBridge {
    /*
 Utilities functions Living Lab
 This class define the devices of the lab.

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
    Test light device                 | Test Light       | HOUSE_LIGHT
     */
	
	public static boolean connectToRemote = true;
	
    public static final Hashtable<String, String> devices;
    
    static {
    	Hashtable<String, String> aMap = new Hashtable<String, String>();
    	aMap.put("MEDIA_PLAYER", "Media Player");
    	aMap.put("HOUSE_LIGHT", "Test Light device");
        devices = aMap;
    }
    
    

    public static String BLIND = "PERSIANA_SALON";
    public static String WINDOW = "VENTANAL_SALON";
    //public static String HOUSE_LIGHT = "LUZ_CASA_ONOFF";
    public static String STREET_LIGHT = "LUZ_CALLE_ONOFF";
    public static String GARDEN_LIGHT = "LUZ_PORCHE_ONOFF";
    public static String SCEXT_LIGHT = "LUZ_SCEXT_ONOFF";
    public static String ENTRY_LIGHT = "LUZ_ENTRADA_ONOFF";
    public static String MAIN_DOOR = "PUERTA_PRINCIPAL";
    public static String MAIN_BLIND = "PERSIANAS_CASA";
    public static String ENTRY_ROOM_LIGHT = "LUZ_HABITACION_ENTRADA_ONOFF";
    public static String HEADBOARD_ROOM_LIGHT = "LUZ_HABITACION_CABECERA_ONOFF";
    public static String ENTRY_LIVINGROOM_LIGHT = "LUZ_SALON_ENTRADA_ONOFF";

    public static String MEDIA_PLAYER = "MEDIA_PLAYER";
    public static String HOUSE_LIGHT = "HOUSE_LIGHT";

    public static String OPEN="1";
    public static String CLOSE="0";
    public static String ON="1";
    public static String OFF="0";
   

    public LlBridge() {
      
    }

    public String sendSyncRequestToLL(String state, String dev){
        // params comes from the execute() call: params[0] is the url.
        //device [VENTANAL_SALON, PERSIANA_SALON] state [Open 1,Close 0]
 
        if (true) {
            try {
                return downloadUrl(encodeURL(state, dev));
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        } else {
            return "Unable to retrieve web page. No connection.";
        }

    }

    public String sendSyncRedirectToLL(String uri){
        // params comes from the execute() call: params[0] is the url.
        //device [VENTANAL_SALON, PERSIANA_SALON] state [Open 1,Close 0]
        if (true) {
            try {
                return downloadUrl(uri);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        } else {
            return "Unable to retrieve web page. No connection.";
        }

    }


    public String encodeURL(String state, String dev){

        String url = "http://138.4.10.224/remote.htm?id="+dev+"&value="+state;
        if (!connectToRemote){
        	//change url request to uAAL service
        }
        System.out.println(this.getClass().getSimpleName()+" Connecting to: "+url);
        return url;
    }

    public void sendRequestToLL(String state,String dev) {
        //device [VENTANAL_SALON, PERSIANA_SALON] state [Open 1,Close 0]
        if (true) {
           //new SendGetRequestTask().execute(encodeURL(state, dev));
        } else {
            //log network error
        }

    }


   private abstract class SendGetRequestTask extends SwingWorker<String, Void> {
        
    	private String[] urls_tocall;
    	
    	SendGetRequestTask(String... urls){
    		this.urls_tocall = urls;
    	}
    	
    	
    	public abstract void callback(String response);
    	
    	@Override
    	protected String doInBackground() {

            // params comes from the execute() call: params[0] is the url.
            try {
            	String response = downloadUrl(this.urls_tocall[0]);
            	this.callback(response);
            	return response;
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        
	
    }



    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
           System.out.println("The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            //String contentAsString = readIt(is, len);
            String contentAsString = readUTF8Text(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Reads all the InputStream and converts it to a String.
    public String readUTF8Text(InputStream stream)  throws IOException, UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

}
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

