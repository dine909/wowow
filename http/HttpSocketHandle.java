package com.dinamoproductions.wowow.server.http;

import java.io.IOException;
import java.net.*;

import com.dinamoproductions.wowow.server.SocketHandle;

public class HttpSocketHandle extends SocketHandle {

	public HttpSocketHandle(Socket _s){
		super(_s);
		
	}
	
	@Override
	public void run(){
		try {
			this.baseSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
