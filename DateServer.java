import java.net.*;
import java.io.*;

public class DateServer extends Thread {
	Socket client;

	public DateServer(Socket c) {
		client = c;
	}
	
	public void run() {
		try {
			PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
			/* write the Date to the socket */
			pout.println(new java.util.Date().toString());
			/* close the socket and resume */
			/* listening for connections */
			client.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}

	}

	public static void main(String[] args) {
		try {
			ServerSocket sock = new ServerSocket(6013);
			DateServer ds = new DateServer(sock.accept());
			ds.start();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}