package es.upm.ll.regex;

import java.util.Locale;
import java.util.regex.*; // Para poder utilizar expresiones regulares

import com.google.gson.JsonObject;


public class TextRecognizer {
	

	public String getParsedMessage(String input){
		String text = "";
		text = "La acción es: "+ReconocimientoAccion(input)  +"\nEl elemento seleccionado es: "+ReconocimientoElemento(input) + "\nEl lugar indicado es: "+ReconocimientoLugar(input);
		return text;
		
	}
	
	public String getJsonMessage(String input){
		JsonObject json=new JsonObject();
		String a = ReconocimientoAccion(input);
		String el = ReconocimientoElemento(input);
		String lug = ReconocimientoLugar(input);
		String text = "";
		text = "La acción es: "+ a +", el elemento seleccionado es: "+ el + ", el lugar indicado es: "+lug;
		json.addProperty("action", a);
		json.addProperty("element", el);
		json.addProperty("place", lug);
		json.addProperty("message", text);
		return json.toString();
		
	}
	
	// Para el reconocimiento de la accion que se quiere realizar
	private String ReconocimientoAccion(String input) {
	 		    String accion = null;
	 	 		// Ahora tendr�a que poner los patrones para ir comprobando con lo que dice el usuario
	 		 
		 		 Pattern[] acciones ={
		 				 Pattern.compile("([E|e]ncend[a-z]*)|([E|e]nciend[a-e]*)"),
		 				 Pattern.compile("apag[a-z]*"), Pattern.compile("abr[a-z]*"),
		 				 Pattern.compile("(cerr[a-z]*)|(cierr[a-z]*)"), Pattern.compile("baj[a-z]*"),
		 				 Pattern.compile("sub[a-z]*")	 
		 		 };
	 	 		
	 	 		
	 	 	
	 	 		Matcher m0 = acciones[0].matcher(input);
	 	 		Matcher m1 = acciones[1].matcher(input);
	 	 		Matcher m2 = acciones[2].matcher(input);
	 	 		Matcher m3 = acciones[3].matcher(input);
	 	 		Matcher m4 = acciones[4].matcher(input);
	 	 		Matcher m5 = acciones[5].matcher(input);
	 	 		
	 	 	if(accion == null || accion == "null"){
	 	 		if(m0.find()) {
	 	 			accion ="Encender";
	 	 		}else if (m1.find()){
	 	 			accion = "Apagar";
	 	 		}else if (m2.find()){
	 	 			accion = "Abrir";
	 	 		}else if (m3.find()){
	 	 			accion = "Cerrar";
	 	 		}else if (m4.find()){
	 	 			accion = "Bajar";
	 	 		}else if (m5.find()){
	 	 			accion = "Subir";
	 	 		}else return null;
	 	 		}
	 	 	   return accion; 
	 	 
	 	 }
	 
    // Para el reconocimiento del elemento sobre el qeu se realiza la accion.
	private String ReconocimientoElemento(String input){
	 		String elemento = null;
	 	 	// Una solucion rapida es poner un patron para cada uno de los elementos, aunque tengo que
	 	 	// optimizarlo de alguna forma.Quiz�s creando un arrayList de String y con yn for ir compilandolos
	 	 	// y luego comprobando, de forma que como van en orden puedo saber cual es cual.
	 	 	
	 	 	Pattern persiana = Pattern.compile("(persiana)|(persinas)");
	 	 	Pattern puerta = Pattern.compile("puerta");
	 	 	Pattern ventanal = Pattern.compile("ventanal");
	 	 	Pattern luz = Pattern.compile("(luz)|(luces)");
	 	 	
	 	 	Matcher mper = persiana.matcher(input);
	 	 	Matcher mpuer = puerta.matcher(input);
	 	 	Matcher mven = ventanal.matcher(input);
	 	 	Matcher mlu = luz.matcher(input);
	 	 
	 	 if (elemento == null || elemento =="null"){
	 	 	if(mper.find()){
	 	 		elemento = "persiana";
	 	 	}else if (mpuer.find()){
	 	 		elemento = "puerta";
	 	 	}else if (mven.find()){
	 	 		elemento = "ventanal";
	 	 	}else if (mlu.find()){
	 	 		elemento ="luz";
	 	 	}else return null;
	 	 }
	 	 return elemento;
	 	 }
	 	 
	// Para la seleccion del lugar
    private String ReconocimientoLugar(String input){
	 	 	
	 		String lugar = null;
	 	 	Pattern salon = Pattern.compile("salón");
	 	 	Pattern dormitorio = Pattern.compile("(dormitorio)|(habitación)");
	 	 	Pattern cocina = Pattern.compile("cocina");
	 	 	Pattern bano = Pattern.compile ("baño");
	 	 	Pattern entrada = Pattern.compile("(entrada)|(principal)");
	 	 	
	 	 	Matcher msa = salon.matcher(input);
	 	 	Matcher mdo = dormitorio.matcher(input);
	 	 	Matcher mco = cocina.matcher(input);
	 	 	Matcher mba = bano.matcher(input);
	 	 	Matcher min = entrada.matcher(input);
	 	 	
	 	 	if(lugar == "null" || lugar == null){
	 	 	if(msa.find()){
	 	 		lugar = "salon";
	 	 	
	 	 	}else if (mdo.find()){
	 			lugar = "dormitorio";
	 	 	}else if (mco.find()){
	 	 		lugar = "cocina";
	 	 	}else if (mba.find()){
	 	 		lugar = "baño";
	 	 	}else if (min.find()){
	 	 		lugar = "entrada";
	 	 	}else return null;
	 	 	
	 	  }
	 	  return lugar;	
	 	 }
	 	 


   // ESTE ES NECESARIO PARA EJECUTAR EN EL LAB LAS ACCIONES
   public void EjecutarLab (String input){
	 		 
	 		String accion = ReconocimientoAccion(input);
	 		String elemento = ReconocimientoElemento(input);
	 		String lugar = ReconocimientoLugar(input);
	 		
	 		 // Para las luces
	 		 if (accion == "Encender"&& elemento == "luz"){
	 			 if (lugar == "salon"){
	 				LivingLab.livingRoomLightsOn();
	 			 }else if (lugar == "dormitorio"){
	 				LivingLab.roomLightsOn();
	 			 }else if (lugar == "cocina");
	 			 	LivingLab.kitchenOn();
	 		     }else if (lugar =="baño"){
	 	 	    	 LivingLab.toiletLightsOn();
	 			 	
	 		 } if (accion == "Apagar"&& elemento == "luz"){
	 			 if (lugar == "salon"){
	 				LivingLab.livingRoomOff();
	 			 }else if (lugar == "dormitorio"){
	 				LivingLab.roomLightsOff();
	 			 }else if (lugar == "cocina");
	 			 	LivingLab.kitchenOff();
	 		     }else if (lugar =="baño"){
	 		    	 LivingLab.toiletLightsOn();
	 		     }
	 		 
	 		 // Para la puerta de entrada y el ventanal
	 		 if (accion == "Abrir"&& elemento =="puerta"){
	 			 if (lugar =="entrada"){
	 				 LivingLab.openFrontDoor();
	 			 }
	 		}
	 		 
	 		 if(accion =="Abrir"&& elemento =="ventanal"){
	 			 if(lugar =="salon"){
	 				 // No esta en la libreria
	 			 }else if(lugar =="cocina"){
	 				 //No esta en la libreria
	 			 }
	 		 }
	 		 
	 		 if (accion == "Cerrar"&& elemento =="puerta"){
	 			 if (lugar =="entrada"){
	 				 LivingLab.closeFrontDoor();
	 			 }
	 	 }
	 		 if(accion =="Cerrar"&& elemento =="ventanal"){
	 			 if(lugar =="salon"){
	 				 // No esta en la libreria
	 			 }else if(lugar =="cocina"){
	 				 //No esta en la libreria
	 			 }
	 		 }
	 		 
	 		 if(accion =="Subir"&& elemento =="persina"){
	 			 if (lugar =="salon"){
	 				LivingLab.livingRoomBlindRaise();
	 			 }
	 			 
	 		 }else if (lugar=="cocina"){
	 			LivingLab.automaticKitchenBlindRaise();
	 			 
	 		 }else if (lugar =="dormitorio"){
	 			LivingLab.RoomBlindRaise();
	 			 
	 		 }else if (lugar =="casa"){
	 			 LivingLab.raiseAllBlinds();
	 			 
	 		 }
	 		 
	 		 if(accion =="Bajar"&& elemento =="persina"){
	 			 if (lugar =="salon"){
	 				LivingLab.livingRoomBlindLower();
	 			 }
	 			 
	 		 }else if (lugar=="cocina"){
	 			LivingLab.automaticKitchenBlindLower();
	 			 
	 		 }else if (lugar =="dormitorio"){
	 			LivingLab.RoomBlindLower();
	 			 
	 		 }else if (lugar =="casa"){
	 			 LivingLab.lowerAllBlinds();
	 			 
	 		 }
	    
	 	 }

}
