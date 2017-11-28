package es.upm.p4act;

import java.util.ArrayList;

import es.upm.vlc.BrowseFile;
import es.upm.vlc.ServerResponse;
import es.upm.vlc.Status;
import es.upm.vlc.VlcCommand;
import es.upm.vlc.VlcCommand.OnServerListener;

public class Plan4ActVLCDecoder {
    
	private static String IP = "127.0.0.1";
	private static String PORT = "8080";
	private static String PWD = "lst7upm";
	private static boolean on_off_mode = false;
	
	public static VlcCommand command;
	
	public static boolean error = false;
	
	protected static void initVLC(){
		if(command==null){
			command = new VlcCommand(IP+":"+PORT,PWD);
	        command.setOnServerListener(new OnServerListener(){

				@Override
				public void onStatus(int error, Status status) {
					System.out.println("OnStatus:");
					
				}

				@Override
				public void onBrowse(int error, ArrayList<BrowseFile> browse) {
					System.out.println("onBrowse");
					
				}

				@Override
				public void onRawServerResponse(ServerResponse sr) throws Exception {
					System.out.println("onRawServerResponse: ");
					if(sr==null) {
						System.out.println("onRawServerResponse: throw new Exception(Error);");
						error = true;
						throw new java.io.IOException(" error");//checked exception  
					}
					
				}

	        });
		}
	}
	
	public static void fadeInMusicEffect() throws Exception{
		initVLC();
        command.mute();
        Thread.sleep(500);
        command.play();
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        System.out.println("command.volumeUp()");
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("command.volumeUp()");
        Thread.sleep(500);
        command.volumeUp();
        System.out.println("last command.volumeUp()");
        if (error) {
        	System.out.println("Connection error");
        	error = false;
        	throw new java.io.IOException("Connection error");
        }
		
	}
	
	public static void fadeOutMusicEffect() throws Exception{
		initVLC();
        command.volumeDown();
        System.out.println("command.volumeDown()");
        Thread.sleep(500);
        System.out.println("command.volumeDown()");
        command.volumeDown();
        System.out.println("command.volumeDown();");
        Thread.sleep(500);
        command.volumeDown();
        System.out.println("command.volumeDown();");
        Thread.sleep(500);
        command.volumeDown();
        System.out.println("command.volumeDown();");
        Thread.sleep(500);
        command.volumeDown();
        System.out.println("command.volumeDown();");
        Thread.sleep(500);
        command.volumeDown();
        System.out.println("command.volumeDown();");
        Thread.sleep(500);
        command.volumeDown();
        System.out.println("command.volumeDown();");
        Thread.sleep(500);
        command.volumeDown();
        System.out.println("last command.volumeDown();");
        Thread.sleep(500);
        command.stop();
        Thread.sleep(500);
        command.mute();
        if (error) {
        	System.out.println("Connection error");
        	error = false;
        	throw new java.io.IOException("Connection error");
        }
	}
	
	public static void musicPlay() throws Exception{
		initVLC();
        command.play();
        if (error) {
        	System.out.println("Connection error");
        	error = false;
        	throw new java.io.IOException("Connection error");
        }
	}
	
	public static void musicStop() throws Exception{
		initVLC();
        command.stop();
        if (error) {
        	System.out.println("Connection error");
        	error = false;
        	throw new java.io.IOException("Connection error");
        }
	}
	
}
