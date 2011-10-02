package com.dinamoproductions.wowow.server.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.*;

public class PathMatcher extends HttpHeaderMatcher {
	String path = null;
	private Pattern pMatcher = null;
	public String matched = null;

	public PathMatcher(String p) {
		path = p;
	}

	private Pattern getMatcherPattern(HttpRequest request) throws IOException {
		if (pMatcher != null)
			return pMatcher;
		String regex = "";
		if (path.endsWith("/*")) {
			String tamePath = path.substring(0, path.length() - 2);
			regex = "(" + tamePath;
			regex += ")/.*";
			regex += "|" + tamePath;
		} else {
			regex = "(" + path + ")";
		}
		return pMatcher = Pattern.compile(regex);

	}

	@Override
	public boolean matchHeader(HttpRequest request) throws IOException,
			URISyntaxException {
		URI pathInfo = request.getPathInfo();
		String URIPath = pathInfo.getRawPath();
		Matcher matcher = getMatcherPattern(request).matcher(URIPath);
		boolean matches = matcher.matches();
		if (matches) {
			String mResult = matcher.group(1);
			if (mResult == null)
				mResult = matcher.group(0);
			int matchEnd = mResult.length();
			matched = URIPath.substring(0, matchEnd);
			request.setPath(new URI(URIPath.substring(matchEnd)));
		}
		return matches;
	}

}
