package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;
import java.net.Socket;

import com.dinamoproductions.wowow.server.http.HttpHeaderMatcher;
import com.dinamoproductions.wowow.server.http.HttpRequest;

public class HttpHandler {
	public HttpHeaderMatcher httpHeaderMatcher=null;

	public HttpHandler(HttpHeaderMatcher m) {
		httpHeaderMatcher=m;
	}

	public void handle(HttpRequest request) {
		
	}


	
}
