package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import com.dinamoproductions.wowow.server.utils;
import com.dinamoproductions.wowow.server.http.*;
import com.dinamoproductions.wowow.filesystem.*;

public class FileSystemHttpHandler extends AbstractHttpHandler {
	AbstractFile file = null;

	public boolean allowDirectoryBrowsing = false;

	public FileSystemHttpHandler(HttpHeaderMatcher m, AbstractFile f) {
		super(m);
		file = (AbstractFile) f;
		// TODO Auto-generated constructor stub
	}

	public void handle(HttpRequest request) throws IOException,
			URISyntaxException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		if (!this.httpHeaderMatcher.matchHeader(request))
			return;

		String fullPath = request.getPathInfo().getPath();
		HttpResponse response = request.getResponse();

		String path = request.getPath().getRawPath();
		AbstractFile fileOrDir = file.getFile(URLDecoder.decode(path));

		boolean isDirectory = fileOrDir.isDirectory();

		if (!fullPath.endsWith("/") && isDirectory && allowDirectoryBrowsing) {
			response.redirect(request, fullPath + "/");
			return;
		}

		if (isDirectory && allowDirectoryBrowsing) {
			handleDirectoryListing(request, fileOrDir, response);
		} else if (!isDirectory) {
			handleContentDownload(request, response, fileOrDir);
		}

	}

	private void handleContentDownload(HttpRequest request,
			HttpResponse response, AbstractFile fileOrDir) throws IOException {
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

	String html = null;
	String item = null;

	private void getHTML() {
		if (html != null)
			return;
		InputStream ddis = getClass().getResourceAsStream("dir.html");
		html = utils.ChannelTools.convertStreamToString(ddis);
		String[] htmls = html.split("%%");
		html = htmls[0];
		item = htmls[1];
	}

	private void handleDirectoryListing(HttpRequest request,
			AbstractFile fileOrDir, HttpResponse response) throws IOException,
			URISyntaxException {
		response.statusCode = StatusCodes.SC_OK;
		getHTML();
		String items = "";

		String path = request.getPathInfo().getPath();
		for (String f : fileOrDir.list()) {
			AbstractFile nf = fileOrDir.getFile(f);
			String size = "";
			Date mod = new Date(nf.lastModified());
			String fe = URLEncoder.encode(f);
			if (nf.isDirectory()) {
				f += "/";
			} else {
				size = Long.toString(nf.length()) + " bytes";
			}
			items += item.replace("%PATHITEM%", path + fe).replace("%ITEM%", f)
					.replace("%MOD%", mod.toString()).replace("%SIZE%", size);
		}
		String htmlo = html.replace("%ITEMS%", items);
		htmlo = htmlo.replace("%PATH%", path);

		BufferedInputStream is = new BufferedInputStream(
				new ByteArrayInputStream(htmlo.getBytes()));

		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream = is;
		request.handled = true;
	}

}
