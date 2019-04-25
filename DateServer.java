import java.net.*;
import java.io.*;

public class DateServer extends Thread{
	Socket client;
	public DateServer(Socket client) {
		// TODO Auto-generated constructor stub
		this.client =client;
	}
	public void run()
	{
//		System.out.println("Thread #:" + Thread.currentThread().getId()+
//				" Date:"+new java.util.Date().toString());
		try {
			PrintWriter pout = new
			PrintWriter(client.getOutputStream(), true);
			/* write the Date to the socket */
			pout.println(new java.util.Date().toString());
			/* close the socket and resume */
			/* listening for connections */
			client.close();
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
	public static void main(String[] args) throws InterruptedException {
		
		try {
			ServerSocket sock = new ServerSocket(6013);
			/* now listen for connections */
			while (true) {
			Socket client = sock.accept();
			DateServer d = new DateServer (client);
			d.start();
			}
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}