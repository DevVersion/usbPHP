package de.paulgap.usbphp;

public enum Response {
	OK("200 OK"), NOTFOUND("404 Not Found"), MOVED("301 Moved Permanently");
	String header;
	Response(String header) {
		this.header = header;
	}
	public String getHeader() {
		return this.header;
	}
}
