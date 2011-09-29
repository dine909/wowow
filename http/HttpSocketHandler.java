package com.dinamoproductions.wowow.server.http;

import java.net.*;

import com.dinamoproductions.wowow.server.*;

public class HttpSocketHandler extends SocketHandler  {
	
	public HttpSocketHandler() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void handle(Socket s){
		HttpSocketHandle hh=new HttpSocketHandle(s);
		hh.start();
		
		
	}

}
