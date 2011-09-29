package com.dinamoproductions.wowow.server.http;

import java.io.*;

public class HttpResponse {
	private OutputStream outputStream=null;
	public int responseCode=404;
	
	public HttpResponse(OutputStream os) {
		outputStream=os;
	}
	
}
