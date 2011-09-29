package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.Socket;

public class HttpHandler {
	private HttpHeaderMatcher httpHeaderMatcher=null;

	public HttpHandler(HttpHeaderMatcher m) {
		httpHeaderMatcher=m;
	}

	public void handle(HttpRequest request) {
		
	}

	public boolean checkPath(){
		
		return false;
	}
	
}
