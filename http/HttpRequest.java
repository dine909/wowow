package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class HttpRequest {
	public Socket baseSocket;
	public boolean handled = false;
	private InputStream inputStream = null;
	private String pathInfo = null;
	HttpResponse httpResponse = null;
	private LinkedList<String> headers = new LinkedList<String>();

	public HttpRequest(Socket s) {
		baseSocket = s;
	}

	public HttpResponse getResponse() throws IOException {
		if (httpResponse == null) {
			return httpResponse = new HttpResponse(
					this.baseSocket.getOutputStream());
		}
		return httpResponse;
	}

	public LinkedList<String> getHeaders(HttpRequest request) {
		if (headers.size() > 0)
			return headers;

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					request.baseSocket.getInputStream()));

			// Receive data
			while (true) {
				String s = in.readLine().trim();

				if (s.equals("")) {
					break;
				}
				headers.add(s);
			}
		} catch (Exception e) {
			return null;
		}
		return headers;

	}
}
