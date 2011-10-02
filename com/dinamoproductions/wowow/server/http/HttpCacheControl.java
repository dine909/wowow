package com.dinamoproductions.wowow.server.http;

public class HttpCacheControl {
	String cacheControl=null;
	boolean mustNotCache=false;
	boolean preferCache=false;
	boolean onlyCache=false;
	
	public HttpCacheControl(HttpRequest request){
		cacheControl=request.getHeader("cache-control:");
		if(cacheControl==null)return;
		if(cacheControl.contains("max-age=0")||cacheControl.contains("no-cache")){
			mustNotCache=true;
			preferCache=false;
		}
		if(cacheControl.contains("only-if-cached")){
			preferCache=true;
			mustNotCache=false;
			onlyCache=true;
		}
		onlyCache=onlyCache;
	}
}
