package com.dinamoproductions.wowow.server.http.handlers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;

import com.dinamoproductions.wowow.filesystem.AbstractFile;
import com.dinamoproductions.wowow.server.Utils;
import com.dinamoproductions.wowow.server.http.HttpRequest;
import com.dinamoproductions.wowow.server.http.HttpResponse;
import com.dinamoproductions.wowow.server.http.StatusCodes;

public class FileSystemHttpDirectoryListingHandler {

	String html = null;
	String item = null;
	private  void getHTML() {
		if (html != null)
			return;
		InputStream ddis = getClass().getResourceAsStream("dir.html");
		html = Utils.ChannelTools.convertStreamToString(ddis);
		String[] htmls = html.split("%%");
		html = htmls[0];
		item = htmls[1];
	}
	
	public void handle(HttpRequest request, AbstractFile fileOrDir, HttpResponse response) throws IOException,
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
