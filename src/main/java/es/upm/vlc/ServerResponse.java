package es.upm.vlc;

public class ServerResponse
{
	private String response;
	private int error_code;
	
	
	public ServerResponse(String resp, int code)
	{
		response = resp;
		error_code = code;
	}
	
	public String getResponse() { return response; }
	public int getError() { return error_code; }
}
