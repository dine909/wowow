package com.dinamoproductions.wowow.server.http.handlers;

import java.io.IOException;

import com.dinamoproductions.wowow.filesystem.AbstractFile;
import com.dinamoproductions.wowow.server.http.HttpCacheControl;
import com.dinamoproductions.wowow.server.http.HttpDate;
import com.dinamoproductions.wowow.server.http.HttpRequest;
import com.dinamoproductions.wowow.server.http.HttpResponse;
import com.dinamoproductions.wowow.server.http.StatusCodes;

public class FileSystemHttpContentHandler {

	public void handle(HttpRequest request, HttpResponse response, AbstractFile fileOrDir, FileSystemHttpHandler fileSystemHttpHandler) throws IOException {
		int start = 0;
		int end = 0;
		if (fileOrDir.exists()) {
			String sContentRange = request.getHeader("range:");
			response.statusCode = StatusCodes.SC_OK;
	
			long len = fileOrDir.length();
			long partialLen = len;
			response.setHeader("Accept-Ranges:", "bytes");
			response.setHeader("Last-Modified:",
					HttpDate.rfc1123Format.format(fileOrDir.lastModified()));
	
			HttpCacheControl httpCacheControl = request.getHttpCacheControl();
	
			if (!httpCacheControl.ifNotModified) {
				response.inputStream = fileOrDir.openInputStream();
				if (sContentRange != null) {
					if (sContentRange.startsWith("bytes=")) {
						String sRange = sContentRange.substring(6);
						start = 0;
						String[] split = sRange.split("-");
						int available = response.inputStream.available();
	
						start = 0;
						end = available - 1;
	
						if (sRange.startsWith("-")) {
							end = Integer.parseInt(split[1]);
						} else if (sRange.endsWith("-")) {
							start = Integer.parseInt(split[0]);
						} else if (sRange.contains("-")) {
							start = Integer.parseInt(split[0]);
							end = Integer.parseInt(split[1]);
						}
						if (start > 0) {
							response.inputStream.skip(start);
						}
	
						partialLen = (end - start) + 1;
						if (partialLen != len) {
							response.statusCode = StatusCodes.SC_PARTIAL_CONTENT;
	
						}
						response.setHeader("Content-Range:", "bytes " + start
								+ "-" + (end) + "/" + len);
					}
				}
				response.setHeader("Content-Length:",
						Long.toString(partialLen, 10));
			}
			request.handled = true;
	
		}
	}

}
