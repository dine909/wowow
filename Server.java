package com.dinamoproductions.wowow.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.*;


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
}
