package es.upm;

import es.upm.p4act.LivingLabHandler;

import es.upm.p4act.Plan4ActRequest;


public class DeviceManager {



	public  static String handleRequest(spark.Request request, spark.Response response)  {
      	response.header("Content-Type", "application/json");
        response.header("Access-Control-Allow-Origin","*");
        response.header("Access-Control-Allow-Methods","GET,PUT,POST,DELETE,PATCH,OPTIONS");
        response.header("Access-Control-Allow-Headers","Authorization, authorization");


        String version = request.queryParams("version");

        String cmd = request.queryParams("cmd");
        String device_id = request.queryParams("device_id");
        String device_name = request.queryParams("device_name");
        String value = request.queryParams("value");
        String sequence = request.queryParams("sequence_number");
        
        Plan4ActRequest r = new Plan4ActRequest();
        r.version = version;
            System.out.println(r.version);//pruebas
        r.cmd = cmd;
            System.out.println(r.cmd );
        r.device_id = device_id;//pruebas
            System.out.println(r.device_id);
        r.device_name = device_name;//pruebas
            System.out.println(r.device_name);
        r.value = value;
            System.out.println(r.value);//pruebas
        r.sequence_number = sequence;
            System.out.println(r.sequence_number);//pruebas
        return (new LivingLabHandler()).handleDevice(r);
      }
	
}
