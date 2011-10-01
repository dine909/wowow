package com.dinamoproductions.wowow.filesystem;

import java.io.*;
import java.net.*;

public class FileSystemFile extends AbstractFile {

	public InputStream openInputStream() throws FileNotFoundException{
		return new FileInputStream(this.getAbsolutePath());
	}

	public FileSystemFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public FileSystemFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	public FileSystemFile(String parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

	public FileSystemFile(File parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AbstractFile getFile(String path) {
		// TODO Auto-generated method stub
		return new FileSystemFile(this,path);
	}

}
