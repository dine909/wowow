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

public class FileSystemHttpHandler extends HttpHandler {
	AbstractFile file=null;
	Class<? extends AbstractFile> fType=null;
	
	public boolean allowDirectoryBrowsing=false
			;
	public FileSystemHttpHandler(HttpHeaderMatcher m, AbstractFile f) {
		super(m);
		fType = f.getClass();
		file=(AbstractFile)f;
		// TODO Auto-generated constructor stub
	}
	
	public void handle(HttpRequest request) throws IOException, URISyntaxException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		if(!this.httpHeaderMatcher.matchHeader(request)) return;

		String fullPath=request.getPathInfo().getPath();
		HttpResponse response=request.getResponse();

		String path=request.getPath().getRawPath();
		Constructor<? extends AbstractFile> constructor = fType.getConstructor(new Class [] {File.class,String.class});
		AbstractFile fileOrDir=constructor.newInstance(new Object[] {file,URLDecoder.decode(path)});
		
		boolean isDirectory = fileOrDir.isDirectory();

		if(!fullPath.endsWith("/")&&isDirectory&&allowDirectoryBrowsing){
			response.redirect(request, fullPath+"/");
			return;
		}

		if(isDirectory&&allowDirectoryBrowsing){
			handleDirectoryListing(request, fileOrDir, response);			
		}else if(!isDirectory){
			if(fileOrDir.exists()){
				response.inputStream=fileOrDir.openInputStream();		
				response.statusCode=StatusCodes.SC_OK;
				request.handled=true;
			}
		}
		
	}
	String html=null;
	String item=null;
	private void getHTML(){
		if(html!=null)return;
		InputStream ddis=getClass().getResourceAsStream("dir.html");	
		html=utils.ChannelTools.convertStreamToString(ddis);
		String[] htmls=html.split("%%");
		html=htmls[0];
		item=htmls[1];
	}
	private void handleDirectoryListing(HttpRequest request, AbstractFile fileOrDir, HttpResponse response) throws IOException, URISyntaxException {
		response.statusCode=StatusCodes.SC_OK;
		getHTML();
		String items="";
		
		String path = request.getPathInfo().getPath();
		for(String f: fileOrDir.list()){
			AbstractFile nf=fileOrDir.getFile(f);
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
		String htmlo=html.replace("%ITEMS%", items);
		htmlo=htmlo.replace("%PATH%", path);
		
		
		BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(htmlo.getBytes()));
		
		response.setHeader("Content-Type", "text/plain; charset=iso-8859-1");
		response.inputStream=is;
		request.handled=true;
	}

}
