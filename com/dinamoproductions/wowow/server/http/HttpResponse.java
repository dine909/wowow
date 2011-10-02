package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.util.*;

import com.dinamoproductions.wowow.server.Utils;
import com.dinamoproductions.wowow.server.Utils.ChannelTools;

public class HttpResponse {
	public InputStream inputStream = null;
	private HttpHeaders headers = new HttpHeaders();
	public String statusCode = StatusCodes.SC_NOT_FOUND;
	public String mimetype;
	protected String cacheControlMaxAge = null;

	public HttpResponse() {
		setCacheMaxAge(0);

	}

	public void setCacheMaxAge(int age) {
		cacheControlMaxAge = "max-age=" + age;
		if (age == 0) {
			cacheControlMaxAge += " must-revalidate";
		}
	}

	public void setHeader(String key, String value) {
		headers.put(key, value);
	}

	public String getHeader(String key) {
		return (String) headers.get(key);
	}

	public String getHeaders() {
		String ret = "";
		Enumeration e = headers.keys();
		while (e.hasMoreElements()) {
			String k = (String) e.nextElement();
			String v = (String) headers.get(k);
			ret += k + " " + v + "\n";
		}
		return ret;

	}

	public void redirect(HttpRequest httpRequest, String location)
			throws IOException {
		statusCode = StatusCodes.SC_FOUND;
		setHeader("Location:", location);
		httpRequest.handled = true;
	}

	protected void sendResponse(OutputStream ros) throws IOException {

		setHeader("Server:", "com.dinamoproductions.wowow");
		setHeader("Connection:", "close");
		setHeader("Cache-Control:", cacheControlMaxAge);
		setHeader("Date:", HttpDate.rfc1123Format.format(new Date()));

		if (inputStream != null && getHeader("Content-Length:") == null)
			setHeader("Content-Length:",
					Integer.toString(inputStream.available(), 10));

		String h = getHeaders();
		BufferedOutputStream bos = new BufferedOutputStream(ros);
		PrintWriter pw = new PrintWriter(bos, true);
		pw.println("HTTP/1.1 " + statusCode);
		pw.println(h);

		pw.flush();
		bos.flush();

		if (inputStream != null) {
			try {
				ChannelTools.streamCopy(inputStream, bos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		bos.flush();
		bos.close();
	}

	public void sendErrorResponse(HttpRequest request, Exception e) {
		String sres = statusCode;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		PrintStream out1 = new PrintStream(bout);
		e.printStackTrace(out1);
		sres = bout.toString();

		BufferedInputStream is = new BufferedInputStream(
				new ByteArrayInputStream(sres.getBytes()));

		statusCode = StatusCodes.SC_INTERNAL_SERVER_ERROR;
		setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		inputStream = is;
		request.handled = true;
	}
}
