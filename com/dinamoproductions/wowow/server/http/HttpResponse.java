package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.util.*;

public class HttpResponse {
	public InputStream inputStream=null ;
	private HttpHeaders headers=new HttpHeaders(); 
	public String statusCode=StatusCodes.SC_NOT_FOUND;
	public String mimetype;

	public HttpResponse() {
		
	}
	
	public void setHeader(String key,String value){
		headers.put(key, value);
	}
	public String getHeader(String key){
		return (String) headers.get(key);
	}
	
	public String getHeaders(){
		String ret="";
		Enumeration e = headers.keys();
		while(e.hasMoreElements()){
			String k=(String) e.nextElement();
			String v=(String) headers.get(k);
			ret+=k+" "+v+"\n";
		}
		return ret;
				
	}

	public void redirect(HttpRequest httpRequest, String location) throws IOException{
		statusCode=StatusCodes.SC_FOUND;
		setHeader("Location:", location);
		httpRequest.handled=true;
	}
}
