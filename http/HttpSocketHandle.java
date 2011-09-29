package com.dinamoproductions.wowow.server.http;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

import com.dinamoproductions.wowow.server.SocketHandle;

public class HttpSocketHandle extends SocketHandle {
	LinkedList<DefaultHttpHandler> handlerList;
	
	public HttpSocketHandle(Socket _s, LinkedList<DefaultHttpHandler> h){
		super(_s);
		handlerList = h;
	}
	
	@Override
	public void run(){
		HttpRequest request=new HttpRequest(this.baseSocket);
		for(DefaultHttpHandler h: handlerList){
			h.handle(request);
			if(request.handled) break;
		}
		try {
			this.baseSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
