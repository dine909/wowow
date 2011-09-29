package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.Socket;

public class DefaultHttpHandler extends HttpHandler{
	
	public DefaultHttpHandler(HttpHeaderMatcher matcher) {
		super(matcher);
		
	}

	
	public void handle(HttpRequest request) {
		if(!this.checkPath()) return;
		
		OutputStream os = null;
		try {
			os=request.baseSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter pr=new PrintWriter(os);
		pr.print("hello\n");
		pr.flush();
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.handled=true;
		
	}

}
