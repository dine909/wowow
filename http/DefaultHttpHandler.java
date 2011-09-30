package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.Socket;

public class DefaultHttpHandler extends HttpHandler{
	
	public DefaultHttpHandler(HttpHeaderMatcher matcher) {
		super(matcher);
		
	}

	public void handle(HttpRequest request) {
		if(!this.httpHeaderMatcher.matchHeader(request)) return;
		HttpResponse response=null;
		try {
			response=request.getResponse();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		response.statusCode=StatusCodes.SC_OK;
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		String sres="hello\n";
		
		BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(
				sres.getBytes()));
		
		response.inputStream=is;
		request.handled=true;
		
	}

}
