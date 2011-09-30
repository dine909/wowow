package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;
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
	
	public void handle(HttpRequest request) throws IOException {
		if(!this.httpHeaderMatcher.matchHeader(request)) return;

		String fullPath=request.getFullPath();

		HttpResponse response=null;
		response=request.getResponse();
		
		response.statusCode=StatusCodes.SC_OK;
		InputStream ddis=getClass().getResourceAsStream("dir.html");
		
		String html=utils.ChannelTools.convertStreamToString(ddis);
		String[] htmls=html.split("%%");
		html=htmls[0];
		String item=htmls[1];
		String items="";
		
		for(String f: file.list()){
			File nf=new File(file,f);
			String size="";
			Date mod=new Date(nf.lastModified());
			if(nf.isDirectory())
			{
				f+="/";
			}else{
				size=Long.toString(file.length())+" bytes";
			}
			items+=item.replace("%PATHITEM%", fullPath+f)
					.replace("%ITEM%", f)
					.replace("%MOD%",mod.toString())
					.replace("%SIZE%",size);
		}
		html=html.replace("%ITEMS%", items);
		html=html.replace("%PATH%", fullPath);
		
		
		BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(html.getBytes()));
		
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream=is;
		request.handled=true;
		
	}

}
