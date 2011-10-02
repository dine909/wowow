package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import com.dinamoproductions.wowow.server.AbstractSocketHandle;
import com.dinamoproductions.wowow.server.http.handlers.*;

public class HttpSocketHandle extends AbstractSocketHandle {
	LinkedList<AbstractHttpHandler> handlerList;

	public HttpSocketHandle(Socket _s, LinkedList<AbstractHttpHandler> h) {
		super(_s);
		handlerList = h;
	}

	@Override
	public void run() {
		HttpRequest request = new HttpRequest(this.baseSocket);
		HttpResponse response = null;
		URI uNormalized =null;
		try {			
			for (AbstractHttpHandler h : handlerList) {
				h.handle(request);
				if (request.handled)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendErrorResponse(request, e);
		}
		try {
			response.sendResponse(request.baseSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
