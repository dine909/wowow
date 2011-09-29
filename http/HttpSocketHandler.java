package com.dinamoproductions.wowow.server.http;

import java.net.*;
import java.util.LinkedList;

import com.dinamoproductions.wowow.server.*;

public class HttpSocketHandler extends SocketHandler  {
	public static LinkedList<DefaultHttpHandler> handlerList = new LinkedList<DefaultHttpHandler>();

	public HttpSocketHandler() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void addHandler(DefaultHttpHandler h){
		handlerList.add(h);
	}
	
	@Override
	public void handle(Socket s){
		HttpSocketHandle hh=new HttpSocketHandle(s,handlerList);
		hh.start();
	}

}
