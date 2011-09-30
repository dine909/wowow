package com.dinamoproductions.wowow.server.http;

import java.io.IOException;
import java.util.regex.*;

public class PathMatcher extends HttpHeaderMatcher {
	String path=null;
	private Pattern pMatcher=null;
	public String matched=null;
	
	public PathMatcher(String p) {
		path=p;
	}
	private Pattern getMatcherPattern(HttpRequest request) throws IOException{
		if(pMatcher!=null) return pMatcher;
		String regex="";
		if(path.endsWith("/*")){
			String tamePath = path.substring(0,path.length()-2);
			regex="("+tamePath;
			regex+=")/.*";
			regex+="|"+tamePath;
		}else{
			regex="("+path+")";			
		}
		return pMatcher=Pattern.compile(regex);

	}
	@Override
	public boolean matchHeader(HttpRequest request) throws IOException{		
		String fullPath = request.getFullPath();
		Matcher matcher=getMatcherPattern(request).matcher(fullPath);
		boolean matches=matcher.matches();
		if (matches) {
			matched = matcher.group(matcher.groupCount()).toLowerCase();
			request.setPath(fullPath.substring(matched.length()));
		}
		return matches;
	}
	
}
