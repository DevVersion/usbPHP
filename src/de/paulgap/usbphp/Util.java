package de.paulgap.usbphp;

import java.io.File;

public class Util {

	public static Request stringToRequest(String string) {
		return Request.valueOf(string.toUpperCase());
	}

	public static String autoRequestUrl(String string) {
		if (string.endsWith("/")) {
			if (new File(string + "index.html").exists()) return string + "index.html"; else return string + "index.php";
		}
		return string;
	}

	public static String getContentType(String clientRequestUrl) {
		String[] split = clientRequestUrl.replaceAll("\\\\", "/").split("/");
		String filename = split[split.length - 1];
		if (filename.contains(".")) {
			String[] fsplit = filename.split("\\.");
			String type = fsplit[fsplit.length - 1];
			return type;
		}
		return "";
	}
	
	public static String stringToType(String string) {
		switch (string) {
			case "php":
			case "html": {
				return "text/html";
			}
			case "xml": {
				return "text/xml";
			}
			case "png": {
				return "image/png";
			}
			case "jpg":
			case "jpeg": {
				return "image/jpeg";
			}
			case "gif": {
				return "image/gif";
			}
			default: {
				return "text/html";
			}
		}
	}
	
}
