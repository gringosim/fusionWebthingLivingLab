package es.upm.vlc;

import java.net.HttpURLConnection;

import javax.swing.SwingWorker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



public class RemoteRequest extends SwingWorker<ServerResponse, Void>
{

	private Authentication auth;
	private OnServerResultListener osrl;
	private String[] urls;
	
	public interface OnServerResultListener
	{
		public void onServerResponse(ServerResponse sr) throws Exception;
	}

	public RemoteRequest(Authentication auth, OnServerResultListener listener)
	{
		this.auth = auth;
		osrl = listener;
	}
	
	public void setUrls(String... url){
		this.urls = url;
	}
	
	@Override
	protected ServerResponse doInBackground() throws Exception 
	{
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;System.out.println("URL: "+this.urls[0]);
		HttpGet httpget = new HttpGet(this.urls[0]);
		httpget.addHeader(auth.getAuth());
		try { response = client.execute(httpget); }
		catch (Exception e)
		{
			ServerResponse resp = new ServerResponse(null, HttpURLConnection.HTTP_NOT_FOUND);
			onPostExecute(resp);
			System.out.println("Connection error X");
			osrl.onServerResponse(null);
			throw new Exception("Connection Error");
		}
		
		if (osrl!=null)
		{
			if (response!=null)
			{
				HttpEntity responseEntity = response.getEntity();
				StatusLine statusLine = response.getStatusLine();
				int status = statusLine.getStatusCode();
				if (responseEntity!=null) 
				{
					try
					{
						String result = EntityUtils.toString(responseEntity);
						ServerResponse resp = new ServerResponse(result, status);
						onPostExecute(resp);
						return resp;
					}
					catch (Exception e)
					{
						ServerResponse resp = new ServerResponse(null,  HttpURLConnection.HTTP_UNAUTHORIZED);
						onPostExecute(resp);
						throw e;
					}
				}
			}
		}
	
		return null;
	}
	

	protected void onPostExecute(ServerResponse sr) throws Exception
	{
		//System.out.println("onServerResponse: "+sr.getResponse()+" "+sr.getError());
		osrl.onServerResponse(sr);
	}
    
	@Override
	protected void done() {
	    System.out.println("done");
	  }
	
	
}
