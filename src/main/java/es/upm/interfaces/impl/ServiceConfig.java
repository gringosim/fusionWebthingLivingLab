package es.upm.interfaces.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import es.upm.interfaces.Configuration;

public class ServiceConfig implements Configuration {

	String ll_ip="";
	String mp_ip="";
	int ll_p=80;
	int mp_p=8080;
	
	HashMap<List<Object>, Object> extended_params;
	
	@Override
	public void setLivingLabIPAddress(String ip) {
		this.ll_ip=ip;
		
	}

	@Override
	public String getLivingLabIPAddress() {
		return ll_ip;
	}

	@Override
	public void setLivingLabPort(int p) {
		this.ll_p=p;
		
	}

	@Override
	public int getLivingLabPort() {
		return ll_p;
	}

	@Override
	public void setMediaPlayerIPAddress(String ip) {
		this.mp_ip=ip;
		
	}

	@Override
	public String getMediaPlayerIPAddress() {
		return this.mp_ip;
	}

	@Override
	public void setMediaPlayerPort(int p) {
		 mp_p=p;
		
	}

	@Override
	public int setMediaPlayerPort() {
		return mp_p;
	}

	@Override
	public void setExtendedParameter(Object type, Object name, Object value) throws Exception {
		extended_params.put(
			    // unmodifiable so key cannot change hash code
			    Collections.unmodifiableList(Arrays.asList(type, name)),
			    value
			);

		
	}

	@Override
	public Object getExtendedParameter(Object type, Object name) {
		return extended_params.get(Arrays.asList(type,name));
	}
	
	

}
