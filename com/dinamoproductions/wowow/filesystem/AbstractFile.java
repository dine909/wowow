package com.dinamoproductions.wowow.filesystem;

import java.io.*;
import java.net.URI;

public class AbstractFile extends File {

	
	public InputStream openInputStream() throws FileNotFoundException{	
		return null;
	}
	
	public AbstractFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public AbstractFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	public AbstractFile(String parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

	public AbstractFile(File parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

}
