package es.upm.interfaces;



public interface Configuration {
	
	
	public void setLivingLabIPAddress(String ip);
	public String getLivingLabIPAddress();
	
	public void setLivingLabPort(int p);
	public int getLivingLabPort();
	
	public void setMediaPlayerIPAddress(String ip);
	public String getMediaPlayerIPAddress();
	
	public void setMediaPlayerPort(int p);
	public int setMediaPlayerPort();
	
	public void setExtendedParameter(Object type, Object name, Object value) throws Exception;
	public Object getExtendedParameter(Object type, Object name) throws Exception; 
	
	
}
