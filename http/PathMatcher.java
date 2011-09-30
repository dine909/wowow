package com.dinamoproductions.wowow.server.http;

import java.util.regex.*;

public class PathMatcher extends HttpHeaderMatcher {
	String path=null;
	
	public PathMatcher(String p) {
		path=p;
	}
	
	@Override
	public boolean matchHeader(HttpRequest request){
		String h=request.getHeader("get");
		Pattern p = Pattern.compile(path);
		Matcher m = p.matcher(h);
		boolean b = m.matches();
		return b;
	}
	
}
