package com.dinamoproductions.wowow.server.http;

import java.io.IOException;
import java.util.Date;

public class HttpCacheControl {
	String cacheControl=null;
	String sImsDate=null;
	boolean mustNotCache=false;
	public boolean preferCache=false;
	boolean onlyCache=false;
	public boolean ifNotModified=false;
	
	public HttpCacheControl(HttpRequest request) throws IOException{
		cacheControl=request.getHeader("cache-control:");
		sImsDate=request.getHeader("if-modified-since:");
		
		if(cacheControl!=null){
			if(cacheControl.contains("max-age=0")||cacheControl.contains("no-cache")){
				mustNotCache=true;
				preferCache=false;
			}
			if(cacheControl.contains("only-if-cached")){
				preferCache=true;
				mustNotCache=false;
				onlyCache=true;
			}
		}
		if(sImsDate!=null){
			HttpResponse response=request.getResponse();
			
			if(sImsDate.equals(response.getHeader("Last-Modified:"))){
				response.statusCode=StatusCodes.SC_NOT_MODIFIED;
				ifNotModified=true;
			}
		}
		onlyCache=onlyCache;
	}
}
