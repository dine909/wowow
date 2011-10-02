package com.dinamoproductions.wowow.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.*;

import com.dinamoproductions.wowow.filesystem.*;
import com.dinamoproductions.wowow.server.http.*;
import com.dinamoproductions.wowow.server.http.handlers.*;

public class Server extends Thread {
	public static LinkedList<Socket> clientList = new LinkedList<Socket>();
	boolean running=false;
	ServerSocket listener=null;
	SocketHandler shSocketHandler;
	
	public Server(int port) throws IOException {
		super();
		
		InetAddress ipadr = InetAddress.getByName("0.0.0.0");
		listener = new ServerSocket(port,0,ipadr);
		running=true;
	}
	@Override
	public void run() {
		while( running ) {
			Socket client = null;
			try {
				client = listener.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			shSocketHandler.handle(client);
			clientList.add(client);
		}
	}
	public void setSocketHandler(SocketHandler _cS){
		shSocketHandler=_cS;
	}
	public void stopServer() {
		running = false;
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized static void remove(Socket s) {
        clientList.remove(s);      
    }
	public static void main(String [ ] args) throws IOException, InterruptedException
	{
		int port=8888;
		String dir="/";
		if(args!=null){
			if(args.length>0)
			{
				dir=args[0];
			}
			if(args.length>1)
			{
				port=Integer.parseInt(args[1], 10);
			}
		}
		Server s= new Server(port);
		HttpSocketHandler httpSocketHandler=new HttpSocketHandler();
		
		//serve files
		FileSystemHttpHandler fileSystemHandler = new FileSystemHttpHandler(
				(HttpHeaderMatcher) new PathMatcher("/files/*"),new FileSystemFile(dir));
		fileSystemHandler.allowDirectoryBrowsing=true;
		httpSocketHandler.addHandler(fileSystemHandler);

		//serve resources
		AbstractFile classResourceFile = new ClassResourceFile(ClassResourceFile.class,"").getFile();
		FileSystemHttpHandler fileSystemHandler1 = new FileSystemHttpHandler(
				(HttpHeaderMatcher) new PathMatcher("/res/*"),classResourceFile);
		fileSystemHandler1.allowDirectoryBrowsing=true;
		httpSocketHandler.addHandler(fileSystemHandler1);
		
		//serve errors
		httpSocketHandler.addHandler(new DefaultHttpHandler((HttpHeaderMatcher) new PathMatcher("/*")));
		
		s.setSocketHandler(httpSocketHandler);
		s.start();
		s.join();
	}
}
