package com.dinamoproductions.wowow.server.http;

import java.util.regex.*;

public class PathMatcher extends HttpHeaderMatcher {
	String path=null;
	
	public PathMatcher(String path) {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean matchHeader(String header){
		Pattern p = Pattern.compile("GET ("+path+") ");
		Matcher m = p.matcher(header);
		boolean b = m.matches();
		return b;
	}
	
}
