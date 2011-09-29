package com.dinamoproductions.wowow.server;

import java.io.IOException;
import java.net.*;

public class SocketHandle extends Thread {
	public Socket baseSocket=null;
	public SocketHandle(Socket s) {
		baseSocket=s;
	}

	@Override
	public void run(){
		try {
			baseSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
