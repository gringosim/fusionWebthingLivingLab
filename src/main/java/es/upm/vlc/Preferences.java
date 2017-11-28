package es.upm.vlc;

import es.upm.vlc.intf.PrefInterface;


public class Preferences implements PrefInterface
{
	
	
	
	private final static String
		KEY = "lst.smartcards",
		SERVER = "server",
		PASSWORD = "password",
		USERID = "userid";
	
	VlcServer server = null;

	public Preferences()
	{
	}
	
	public VlcServer getServer()
	{
		
		if(server == null) server = new VlcServer("", "");
		return server;
	}
	
	public void cleanServer()
	{
		server = null;
	}
	
	public void setServer(String server, String password)
	{

	}
	
	public String getUserID()
	{
		return "user";
	}
	
	public void setUser(String user)
	{

	}
}
