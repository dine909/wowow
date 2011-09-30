package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.regex.*;

public class HttpRequest {
	public Socket baseSocket;
	public boolean handled = false;
	private InputStream inputStream = null;
	private String pathInfo = null;
	HttpResponse httpResponse = null;
	private HttpHeaders headers = new HttpHeaders();

	public HttpRequest(Socket s) {
		baseSocket = s;
	}

	public HttpResponse getResponse() throws IOException {
		if (httpResponse == null) {
			return httpResponse = new HttpResponse();
		}
		return httpResponse;
	}

	public HttpHeaders getHeaders(String header) throws IOException {
		if (header != null)
			header = header.toLowerCase();
		if (headers.size() > 0)
			return headers;

		BufferedReader in = null;

		in = new BufferedReader(new InputStreamReader(
				baseSocket.getInputStream()));
		while (!in.ready())
			;
		while (true) {
			String s = null;
			s = in.readLine().trim();

			if (s.equals("")) {
				break;
			}
			Pattern pattern = Pattern.compile("(.*?) (.*)");
			Matcher matcher = pattern.matcher(s);
			boolean matchFound = matcher.find();

			if (matchFound) {
				// Get all groups for this match
				String key = matcher.group(1).toLowerCase();
				headers.put(key, matcher.group(2));
				if (key.equals(header)) {
					break;
				}
			}
			// headers.put(key, value)
		}

		return headers;

	}

	public String getHeader(String header) throws IOException {
		String h = null;
		h = (String) this.getHeaders(header).get(header);
		return h;
	}
}
