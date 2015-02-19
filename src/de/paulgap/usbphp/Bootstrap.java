package de.paulgap.usbphp;

public class Bootstrap {
	
	public static void main(String[] args) {
		final PHPServer ps = new PHPServer(80 ,"C:\\XAMPP\\php\\php.exe", "www");
		ps.start();
	}

}
