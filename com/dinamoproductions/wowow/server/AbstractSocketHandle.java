package com.dinamoproductions.wowow.server;

import java.io.IOException;
import java.net.*;

public abstract class AbstractSocketHandle extends Thread {
	public Socket baseSocket = null;

	public AbstractSocketHandle(Socket s) {
		baseSocket = s;
	}

	@Override
	public void run() {
		try {
			baseSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
