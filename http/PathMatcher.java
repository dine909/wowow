package com.dinamoproductions.wowow.server.http;

import java.io.IOException;
import java.util.regex.*;

public class PathMatcher extends HttpHeaderMatcher {
	String path=null;
	private Pattern pMatcher=null;
	
	public PathMatcher(String p) {
		path=p;
	}
	private Pattern getMatcherPattern(HttpRequest request) throws IOException{
		if(pMatcher!=null) return pMatcher;
		String regex="";
		if(path.endsWith("/*")){
			regex=path.substring(0,path.length()-1);
			regex+=".*";
			regex+="|"+path.substring(0,path.length()-2);
		}
		return pMatcher=Pattern.compile(regex);

	}
	@Override
	public boolean matchHeader(HttpRequest request) throws IOException{		
		return getMatcherPattern(request).matcher(request.getFullPath()).matches();
	}
	
}
