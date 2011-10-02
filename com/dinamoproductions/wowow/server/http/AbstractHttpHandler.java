package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URISyntaxException;


public abstract class AbstractHttpHandler {
	public HttpHeaderMatcher httpHeaderMatcher=null;

	public AbstractHttpHandler(HttpHeaderMatcher m) {
		httpHeaderMatcher=m;
	}

	public void handle(HttpRequest request) throws IOException, URISyntaxException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
	}


	
}
