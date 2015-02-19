package de.paulgap.usbphp;


public class PHPServer {
	
	private boolean running;
	private int port;
	private String webPath, phpPath, protocol = "HTTP/1.1", serverinformation = "usbPHP v.1.0";

	public PHPServer(int port, String phpPath, String webPath) {
		this.port = port;
		this.phpPath = phpPath;
		this.webPath = webPath;
	}
	
	public void start() {
		
		Thread webthread = new WebSocketThread(this);
		webthread.start();
		
		this.running = true;
	}
	
	public void stop() {
		this.running = false;
	}
	
	public void restart() {
		stop();
		start();
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public String getWebPath() {
		return this.webPath;
	}
	
	public String getPHPPath() {
		return this.phpPath;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public String getServerInformation() {
		return this.serverinformation;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
}
