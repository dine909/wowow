package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URISyntaxException;

import com.dinamoproductions.wowow.server.http.HttpHeaderMatcher;
import com.dinamoproductions.wowow.server.http.HttpRequest;

public class HttpHandler {
	public HttpHeaderMatcher httpHeaderMatcher=null;

	public HttpHandler(HttpHeaderMatcher m) {
		httpHeaderMatcher=m;
	}

	public void handle(HttpRequest request) throws IOException, URISyntaxException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
	}


	
}
