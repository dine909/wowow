package com.dinamoproductions.wowow.server.http;

import java.net.*;
import java.util.LinkedList;

import com.dinamoproductions.wowow.server.*;
import com.dinamoproductions.wowow.server.http.handlers.*;

public class HttpSocketHandler extends AbstractSocketHandler  {
	public static LinkedList<AbstractHttpHandler> handlerList = new LinkedList<AbstractHttpHandler>();

	public HttpSocketHandler() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void addHandler(AbstractHttpHandler h){
		handlerList.add(h);
	}
	
	@Override
	public void handle(Socket s){
		HttpSocketHandle hh=new HttpSocketHandle(s,handlerList);
		hh.start();
	}

}
