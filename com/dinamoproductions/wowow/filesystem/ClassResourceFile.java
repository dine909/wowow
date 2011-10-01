package com.dinamoproductions.wowow.filesystem;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class ClassResourceFile extends AbstractFile {
	public Class<? extends Object> resourceClass=null;
	
	public InputStream openInputStream() throws FileNotFoundException{
		String path = removeSlash();
		return resourceClass.getResourceAsStream(path);
	}

	private String removeSlash() {
		String path = this.getPath();
		if(path.startsWith("/")){
			path=path.substring(1);
			
		}
		return path;
	}
	
	public ClassResourceFile(String pathname) throws IOException {
		super(pathname);
		throw new IOException("Cannot create ClassResourceFile with no clazz");
		// TODO Auto-generated constructor stub
	}
	public ClassResourceFile(Class<? extends Object> clazz, String pathname) throws IOException {
		super(pathname);
		resourceClass=clazz;
		// TODO Auto-generated constructor stub
	}

	public ClassResourceFile(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	public ClassResourceFile(String parent, String child) {
		super(parent, child);
		// TODO Auto-generated constructor stub
	}

	public ClassResourceFile(File parent, String child) {
		super(parent, child);
		resourceClass=((ClassResourceFile)parent).resourceClass;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return super.exists();
	}

	@Override
	public boolean isDirectory() {
		// TODO Auto-generated method stub
		String path = removeSlash();
		URL dirURL = resourceClass.getClassLoader().getResource(path);
	      if (dirURL != null && dirURL.getProtocol().equals("file")) {
	        /* A file path: easy enough */
	        try {
				return new File(dirURL.toURI()).isDirectory();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      } 
	      return false;
	}

	@Override
	public boolean isFile() {
		// TODO Auto-generated method stub
		return super.isFile();
	}

	@Override
	public String[] list() {
		String path = removeSlash();
		URL dirURL = resourceClass.getClassLoader().getResource(path);
	      if (dirURL != null && dirURL.getProtocol().equals("file")) {
	        /* A file path: easy enough */
	        try {
				return new File(dirURL.toURI()).list();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      } 

	      if (dirURL == null) {
	        /* 
	         * In case of a jar file, we can't actually find a directory.
	         * Have to assume the same jar as clazz.
	         */
	        String me = resourceClass.getName().replace(".", "/")+".class";
	        dirURL = resourceClass.getClassLoader().getResource(me);
	      }
	      
	      if (dirURL.getProtocol().equals("jar")) {
	        /* A JAR path */
	        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
	        JarFile jar = null;
			try {
				jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	        Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
	        while(entries.hasMoreElements()) {
	          String name = entries.nextElement().getName();
	          if (name.startsWith(path)) { //filter according to the path
	            String entry = name.substring(path.length());
	            int checkSubdir = entry.indexOf("/");
	            if (checkSubdir >= 0) {
	              // if it is a subdirectory, we just return the directory name
	              entry = entry.substring(0, checkSubdir);
	            }
	            result.add(entry);
	          }
	        }
	        return result.toArray(new String[result.size()]);
	      } 
	        
	      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}

}
