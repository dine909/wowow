package com.dinamoproductions.wowow.server.http;

public class HttpHeaderMatcher {
	
	public boolean matchHeader(String header){
		return true;
	}
	public boolean match(HttpRequest request) {
		// TODO Auto-generated method stub
		return matchHeader(null);
	}
}
