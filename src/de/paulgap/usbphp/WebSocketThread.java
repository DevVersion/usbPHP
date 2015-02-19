package de.paulgap.usbphp;

import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketThread extends Thread {
	
	private PHPServer phpserver;

	public WebSocketThread(PHPServer phpserver) {
		this.phpserver = phpserver;
	}

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(phpserver.getPort());
			while (phpserver.isRunning()) {
				Socket socket = server.accept();

				Client cl = new Client(phpserver, socket);
				
				if (cl.loadHeader()) {
					cl.sendProtocol();
					cl.sendResponseType();
					cl.sendServerInformation();
					cl.sendDate();
					cl.sendVary();
					cl.sendConnectionClose();
					cl.sendContentType();
					cl.sendContent();
				}
				cl.close();
			}
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
