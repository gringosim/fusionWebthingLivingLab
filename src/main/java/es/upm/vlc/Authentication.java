package es.upm.vlc;

import org.apache.http.Header;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.HTTP;

public class Authentication
{
	private Header auth;
	
	public Authentication (String passw)
	{
		if (passw==null) passw = "lst7upm";
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("", passw);
		auth = BasicScheme.authenticate(creds, HTTP.UTF_8, false);
	}
	
	public Header getAuth()
	{
		return auth;	
	}
	
}
