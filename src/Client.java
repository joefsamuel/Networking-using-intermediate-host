/**
 * 
 */

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author joesamuel
 *
 */
public class Client {
	private DatagramSocket socket;
	private List<DatagramPacket> packets;
	private DatagramPacket Packet, recievePacket;
	private byte[] buf;
	
	/**
	 * @param p1
	 */
	public Client() {
		packets = new ArrayList<>();
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendPacket() {
	String testString = "Hello World!";
	System.out.println("Client: Sending a packet containing: \n" + testString);
	byte buf[] = testString.getBytes();
	try {
		 Packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 23);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		System.out.println("Client: Sending packet");
		System.out.println("Host: " + Packet.getAddress());
		System.out.println("Destination host port: " + Packet.getPort());
		int len = Packet.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: ");
	      System.out.println(new String(Packet.getData(),0,len)); 
	      
	      try {
			socket.send(Packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      System.out.println("Houston... Packet has been sent.");
	      
	}
	
	public void recievePackets() {
		byte data[] = new byte[100];
		recievePacket = new DatagramPacket(data, data.length);
		try {
			socket.receive(recievePacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client: Packet received:");
	      System.out.println("From host: " + recievePacket.getAddress());
	      System.out.println("Host port: " + recievePacket.getPort());
	      int len = recievePacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: ");
	      
	   // Form a String from the byte array.
	      String received = new String(data,0,len);   
	      System.out.println(received);

	      // We're finished, so close the socket.
	      socket.close();
	}
	
	public static void main(String args[]) {
		Client c = new Client();
		c.sendPacket();
	}
	
}
