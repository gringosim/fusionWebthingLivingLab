package es.upm.vlc;

public class VlcServer 
{
	private String ip, password;
	
	public VlcServer(String ip, String password)
	{   
		ip="localhost";
		password = "lst7upm";
		this.ip = ip;
		this.password = password;
	}
	
	public String getIP() { return ip; }
	
	public String getPassword() { return password; }
}
