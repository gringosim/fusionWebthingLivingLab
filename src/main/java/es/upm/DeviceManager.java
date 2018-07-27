package es.upm;

import es.upm.p4act.LivingLabHandler;

import es.upm.p4act.Plan4ActConstants;
import es.upm.p4act.Plan4ActRequest;
import java.net.*;
import java.io.*;
import java.lang.String;
import javax.servlet.http.*;
import java.lang.StringBuffer;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class DeviceManager {

        public static String actualURL;

        public static String requestUri;
/*
*matchURL will extract request's URI in order to match it with
* target URL listed on Plan4ActConstants "relations" hashtable.
*
 */
     public static String matchURL (spark.Request request){
         Plan4ActConstants.initRelations();
         requestUri = request.pathInfo();
         System.out.println("Este es el URI de request: "+ requestUri);
         actualURL=Plan4ActConstants.relations.get(requestUri);
         System.out.println("Este es el URL real: "+ actualURL);

         return actualURL;
     }
     /*
     *queryToMap will extract query parameters in order to insert them into "handleRequest"
     * as Plan4ActRequest parameters
      */
    public static  Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
            String [] firstSplit= query.split("\\?");
            query= firstSplit[1];
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
                result.put(entry[0], entry[1]);
        }
        return result;
    }

	public  static String handleRequest(spark.Request request, spark.Response response)  {
      	response.header("Content-Type", "application/json");
        response.header("Access-Control-Allow-Origin","*");
        response.header("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH,OPTIONS");
        response.header("Access-Control-Allow-Headers","Authorization, authorization");

        matchURL(request);
        // version, cmd, device_name and sequence parameters should ALSO be called from queryToMap.
        String version =request.queryParams("version");
        String cmd = request.queryParams("cmd");
        Map<String,String> params = queryToMap(actualURL);
        String device_id =params.get("device_id");
        System.out.println("Device_id "+device_id);
        String device_name = request.queryParams("device_name");
        String value = params.get("value");
        System.out.println("Value "+value);
        String sequence = request.queryParams("sequence_number");


        
        Plan4ActRequest r = new Plan4ActRequest();
         r.version = version;
         r.cmd = cmd;
         r.device_id = device_id;
         r.device_name = device_name;
         r.value = value;
         r.sequence_number = sequence;

        return (new LivingLabHandler()).handleDevice(r);
      }
	
}
