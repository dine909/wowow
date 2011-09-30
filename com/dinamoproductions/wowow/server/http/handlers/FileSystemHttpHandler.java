package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import com.dinamoproductions.wowow.server.utils;
import com.dinamoproductions.wowow.server.http.*;

public class FileSystemHttpHandler extends HttpHandler {
	File file=null;
	public boolean allowDirectoryBrowsing=false
			;
	public FileSystemHttpHandler(HttpHeaderMatcher m, File f) {
		super(m);
		file=f;
		// TODO Auto-generated constructor stub
	}
	
	public void handle(HttpRequest request) throws IOException, URISyntaxException {
		if(!this.httpHeaderMatcher.matchHeader(request)) return;

		String fullPath=request.getPathInfo().getPath();
		HttpResponse response=request.getResponse();

		String path=request.getPath().getRawPath();
		File fileOrDir=new File(file,URLDecoder.decode(path));
		boolean isDirectory = fileOrDir.isDirectory();

		if(!fullPath.endsWith("/")&&isDirectory&&allowDirectoryBrowsing){
			response.redirect(request, fullPath+"/");
			return;
		}

		if(isDirectory&&allowDirectoryBrowsing){
			handleDirectoryListing(request, fileOrDir, response);			
		}else if(!isDirectory){
			if(fileOrDir.exists()){
				FileInputStream is = new FileInputStream(fileOrDir.getPath());
				response.statusCode=StatusCodes.SC_OK;
				response.inputStream=is;
				request.handled=true;
			}
		}
		
	}

	private void handleDirectoryListing(HttpRequest request, File fileOrDir, HttpResponse response) throws IOException, URISyntaxException {
		response.statusCode=StatusCodes.SC_OK;
		InputStream ddis=getClass().getResourceAsStream("dir.html");
		
		String html=utils.ChannelTools.convertStreamToString(ddis);
		String[] htmls=html.split("%%");
		html=htmls[0];
		String item=htmls[1];
		String items="";
		
		String path = request.getPathInfo().getPath();
		File realPath = new File(file,URLDecoder.decode(request.getPath().getRawPath()));
		for(String f: realPath.list()){
			File nf=new File(realPath,f);
			String size="";
			Date mod=new Date(nf.lastModified());
			String fe=URLEncoder.encode(f);
			if(nf.isDirectory())
			{
				f+="/";
			}else{
				size=Long.toString(nf.length())+" bytes";
			}
			items+=item.replace("%PATHITEM%", path+fe)
					.replace("%ITEM%", f)
					.replace("%MOD%",mod.toString())
					.replace("%SIZE%",size);
		}
		html=html.replace("%ITEMS%", items);
		html=html.replace("%PATH%", path);
		
		
		BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(html.getBytes()));
		
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream=is;
		request.handled=true;
	}

}
