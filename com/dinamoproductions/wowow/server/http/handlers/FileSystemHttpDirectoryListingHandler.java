package com.dinamoproductions.wowow.server.http.handlers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;

import com.dinamoproductions.wowow.filesystem.AbstractFile;
import com.dinamoproductions.wowow.server.http.HttpRequest;
import com.dinamoproductions.wowow.server.http.HttpResponse;
import com.dinamoproductions.wowow.server.http.StatusCodes;

public class FileSystemHttpDirectoryListingHandler {

	public void handle(HttpRequest request, AbstractFile fileOrDir, HttpResponse response, FileSystemHttpHandler fileSystemHttpHandler) throws IOException,
			URISyntaxException {
		response.statusCode = StatusCodes.SC_OK;
		fileSystemHttpHandler.getHTML();
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
			items += fileSystemHttpHandler.item.replace("%PATHITEM%", path + fe).replace("%ITEM%", f)
					.replace("%MOD%", mod.toString()).replace("%SIZE%", size);
		}
		String htmlo = fileSystemHttpHandler.html.replace("%ITEMS%", items);
		htmlo = htmlo.replace("%PATH%", path);
	
		BufferedInputStream is = new BufferedInputStream(
				new ByteArrayInputStream(htmlo.getBytes()));
	
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream = is;
		request.handled = true;
	}

}
