package com.dinamoproductions.wowow.server.http.handlers;

import java.io.*;

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


		HttpResponse response=null;
		try {
			response=request.getResponse();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		response.statusCode=StatusCodes.SC_OK;
		String sres="";
		
		for(String f: file.list()){
			sres+=f+"\n";
		}
		
		InputStream ddis=getClass().getResourceAsStream("dir.html");
		
		sres=utils.ChannelTools.convertStreamToString(ddis);
		
		BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(sres.getBytes()));
		
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream=is;
		request.handled=true;
		
	}

}
