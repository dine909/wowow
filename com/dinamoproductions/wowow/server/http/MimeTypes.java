package com.dinamoproductions.wowow.server.http;

import java.util.*;

public class MimeTypes {
	public static final Map<Integer, String> extentions = createMap();

	private static Map<Integer, String> createMap() {
		Map<Integer, String> result = new HashMap<Integer, String>();
		result.put(1, "one");
		result.put(2, "two");
		return Collections.unmodifiableMap(result);
	}
}
