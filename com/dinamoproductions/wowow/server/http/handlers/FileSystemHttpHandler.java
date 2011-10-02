package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import com.dinamoproductions.wowow.server.*;
import com.dinamoproductions.wowow.server.http.*;
import com.dinamoproductions.wowow.filesystem.*;

public class FileSystemHttpHandler extends AbstractHttpHandler {
	AbstractFile file = null;
    FileSystemHttpContentHandler fileSystemHttpContentHandler=null;
    FileSystemHttpDirectoryListingHandler fileSystemHttpDirectoryListingHandler=null;
	
	public boolean allowDirectoryBrowsing = false;

	public FileSystemHttpHandler(HttpHeaderMatcher m, AbstractFile f) {
		super(m);
		file = (AbstractFile) f;
		// TODO Auto-generated constructor stub
	}
	protected FileSystemHttpContentHandler getFileSystemHttpContentHandler(){
		if(fileSystemHttpContentHandler!=null) return fileSystemHttpContentHandler;
		return fileSystemHttpContentHandler=new FileSystemHttpContentHandler();
	}
	public void setContentHandler(FileSystemHttpContentHandler h){
		fileSystemHttpContentHandler = h;
	}
	protected FileSystemHttpDirectoryListingHandler getFileSystemHttpDirectoryListingHandler(){
		if(fileSystemHttpDirectoryListingHandler!=null) return fileSystemHttpDirectoryListingHandler;
		return fileSystemHttpDirectoryListingHandler=new FileSystemHttpDirectoryListingHandler();
	}
	public void setContentHandler(FileSystemHttpDirectoryListingHandler h){
		fileSystemHttpDirectoryListingHandler = h;
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
			getFileSystemHttpDirectoryListingHandler().handle(request, fileOrDir, response,this);
		} else if (!isDirectory) {
			getFileSystemHttpContentHandler().handle(request, response, fileOrDir,this);
		}

	}

	String html = null;
	String item = null;

	public void getHTML() {
		if (html != null)
			return;
		InputStream ddis = getClass().getResourceAsStream("dir.html");
		html = Utils.ChannelTools.convertStreamToString(ddis);
		String[] htmls = html.split("%%");
		html = htmls[0];
		item = htmls[1];
	}

}
