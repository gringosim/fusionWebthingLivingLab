package es.upm.vlc.intf;

import es.upm.vlc.VlcServer;

public interface PrefInterface 
{

	public VlcServer getServer();
	
	public void cleanServer();
	
	public void setServer(String server, String password);
	
	public String getUserID();
	
	public void setUser(String user);

}
