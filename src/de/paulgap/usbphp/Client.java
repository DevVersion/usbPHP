package de.paulgap.usbphp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Client {
	
	private PHPServer phpserver;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private Scanner sIn;
	private PrintWriter pwOut;
	
	private String clientProtocol;
	private Request clientRequest;
	private String clientRequestUrl;
	
	private String serverContentType;
	private String serverFileType;
	private Response serverResponse;
	
	public Client(PHPServer phpserver, Socket socket) {
		this.phpserver = phpserver;
		this.socket = socket;
		
		try {
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
			this.sIn = new Scanner(in);
			this.pwOut = new PrintWriter(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean loadHeader() {
		if (sIn.hasNextLine()) {
			// LOAD DATA
			String[] splited = sIn.nextLine().split(" ");
			if (splited.length != 3 || splited[0].isEmpty() || splited[1].isEmpty() || splited[2].isEmpty()) return false;
			
			Request rq = Util.stringToRequest(splited[0]);
			String path = Util.autoRequestUrl(phpserver.getWebPath() + splited[1]);
			File file = new File(path);
			
			// SET VARS
			if (file.exists()) serverResponse = Response.OK; else serverResponse = Response.NOTFOUND;
			this.clientRequest = rq;
			this.clientProtocol = splited[2];
			this.clientRequestUrl = file.getPath();
			this.serverFileType = Util.getContentType(this.clientRequestUrl);
			this.serverContentType = Util.stringToType(this.serverFileType);
			return true;
		}
		return false;
	}
	
	public Response getServerResponseType() {
		return serverResponse;
	}
	
	public Request getRequestType() {
		return clientRequest;
	}
	
	public String getRequestUrl() {
		return this.clientRequestUrl;
	}
	
	public String getProtocol() {
		return this.clientProtocol;
	}
	
	public void sendProtocol() {
		pwOut.print(phpserver.getProtocol());
		pwOut.flush();
	}
	
	public void sendResponseType() {
		pwOut.print(" " + getServerResponseType().getHeader() + "\n");
		pwOut.flush();
	}
	
	public void sendServerInformation() {
		pwOut.println("Server: " + phpserver.getServerInformation());
		pwOut.flush();
	}
	
	public void sendDate() {
		pwOut.println("Date: " + new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US).format(new Date()));
		pwOut.flush();
	}
	
	public void sendVary() {
		pwOut.println("Vary: Accept-Encoding");
		pwOut.flush();
	}
	
	public void sendConnectionClose() {
		pwOut.println("Connection: close");
		pwOut.flush();
	}
	
	public void sendContentType() {
		pwOut.println("Content-Type: " + serverContentType);
		pwOut.flush();
	}
	
	public void sendContent() {
		pwOut.println();
		pwOut.flush();
		switch (getServerResponseType()) {
			case OK: {
				try {
					File file = new File(clientRequestUrl);
					InputStream is; 
					if (serverFileType.equals("php")) {
						is = Runtime.getRuntime().exec(phpserver.getPHPPath() + " " + clientRequestUrl, null, new File(phpserver.getWebPath()).getParentFile()).getInputStream();
					} else {
						is = new FileInputStream(file);
					}
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}
					out.flush();
					is.close();
				} catch (Exception e) {
					pwOut.println("Error 204 - Error loading Content");
				}
				break;
			}
			default: {
				pwOut.println("Error 404 - File not found");
				pwOut.flush();
				break;
			}
		}
		
	}
	
	public void close() throws IOException {
		this.sIn.close();
		this.pwOut.close();
		this.in.close();
		this.out.close();
		this.socket.close();
	}
	
	public InetAddress getAdress() {
		return this.socket.getInetAddress();
	}
	
	public Socket getSocket() {
		return this.socket;
	}

}
