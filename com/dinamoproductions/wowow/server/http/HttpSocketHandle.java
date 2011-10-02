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
		URI uNormalized =null;
		try {			
			for (HttpHandler h : handlerList) {
				h.handle(request);
				if (request.handled)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				response = request.getResponse();
				String sres = response.statusCode;

				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				PrintStream out1 = new PrintStream(bout);
				e.printStackTrace(out1);
				sres = bout.toString();

				BufferedInputStream is = new BufferedInputStream(
						new ByteArrayInputStream(sres.getBytes()));

				response.statusCode = StatusCodes.SC_INTERNAL_SERVER_ERROR;
				response.setHeader("Content-Type",
						"text/plain; charset=iso-8859-1");
				response.inputStream = is;
				request.handled = true;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
			
			if (response.inputStream != null&&response.getHeader("Content-Length:")==null)
				response.setHeader("Content-Length:", Integer.toString(response.inputStream.available(),10));
			
			String h = response.getHeaders();
			BufferedOutputStream bos = new BufferedOutputStream(ros);
			PrintWriter pw = new PrintWriter(bos, true);
			pw.println("HTTP/1.1 " + response.statusCode);
			pw.println(h);

			pw.flush();
			bos.flush();

			if (response.inputStream != null) {
				try {
					utils.ChannelTools
							.fastStreamCopy(response.inputStream, bos);
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
