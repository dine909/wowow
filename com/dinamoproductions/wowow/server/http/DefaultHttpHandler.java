package com.dinamoproductions.wowow.server.http;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class DefaultHttpHandler extends AbstractHttpHandler {

	public DefaultHttpHandler(HttpHeaderMatcher matcher) {
		super(matcher);

	}

	public void handle(HttpRequest request) throws IOException,
			URISyntaxException {
		if (!this.httpHeaderMatcher.matchHeader(request))
			return;
		HttpResponse response = null;
		response = request.getResponse();
		String sres = response.statusCode;
		BufferedInputStream is = new BufferedInputStream(
				new ByteArrayInputStream(sres.getBytes()));

		// response.statusCode=StatusCodes.SC_OK;
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream = is;
		request.handled = true;

	}

}
