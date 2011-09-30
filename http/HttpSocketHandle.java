package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import com.dinamoproductions.wowow.server.SocketHandle;
import com.dinamoproductions.wowow.server.utils;
import com.dinamoproductions.wowow.server.http.handlers.*;

public class HttpSocketHandle extends SocketHandle {
	LinkedList<HttpHandler> handlerList;

	public HttpSocketHandle(Socket _s, LinkedList<HttpHandler> h) {
		super(_s);
		handlerList = h;
	}

	@Override
	public void run() {
		HttpRequest request = new HttpRequest(this.baseSocket);
		HttpResponse response = null;
		for (HttpHandler h : handlerList) {
			try {
				h.handle(request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (request.handled)
				break;
		}
		try {
			response = request.getResponse();

			OutputStream ros = null;
			try {
				ros = request.baseSocket.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			response.setHeader("Server:", "com.dinamoproductions.wowow");
			String h = response.getHeaders();
			BufferedOutputStream bos = new BufferedOutputStream(ros);
			PrintWriter pw = new PrintWriter(bos, true);
			pw.println("HTTP/1.1 " + response.statusCode);
			pw.println(h);
		
			pw.flush();
			bos.flush();

			if (response.inputStream != null) {
				try {
					utils.ChannelTools.fastStreamCopy(response.inputStream, bos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			bos.flush();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
