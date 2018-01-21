
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * @author joesamuel
 *
 */
public class Client {

	private DatagramPacket sendPacket, receivePacket;

	/**
	 * @param p1
	 */
	public Client() {

	}

	private byte[] invalidRequest(String fileName, String mode) {
		System.out.println("Client: Sending a packet containing: \n" + fileName);

		byte[] a = new byte[2];
		a[0] = 1;
		a[1] = 4;
		byte[] b = fileName.getBytes();
		byte[] b5 = new byte[1];
		b5[0] = 6;
		byte[] c = mode.getBytes();
		byte[] d = new byte[1];
		d[0] = 7;
		byte[] finalBuf = new byte[a.length + b.length + b5.length + c.length + d.length];
		System.arraycopy(a, 0, finalBuf, 0, a.length);
		System.arraycopy(b, 0, finalBuf, a.length, b.length);
		System.arraycopy(b5, 0, finalBuf, a.length + b.length, b5.length);
		System.arraycopy(c, 0, finalBuf, a.length + b.length + b5.length, c.length);
		System.arraycopy(d, 0, finalBuf, a.length + b.length + b5.length + c.length, d.length);
		return finalBuf;
	}

	private byte[] readRequest(String fileName, String mode){
		System.out.println("Client: Sending a packet containing: \n" + fileName);

		byte[] a = new byte[2];
		a[0] = 0;
		a[1] = 1;
		byte[] b = fileName.getBytes();
		byte[] b5 = new byte[1];
		b5[0] = 0;
		byte[] c = mode.getBytes();
		byte[] d = new byte[1];
		d[0] = 0;
		byte[] finalBuf = new byte[a.length + b.length + b5.length + c.length + d.length];
		System.arraycopy(a, 0, finalBuf, 0, a.length);
		System.arraycopy(b, 0, finalBuf, a.length, b.length);
		System.arraycopy(b5, 0, finalBuf, a.length + b.length, b5.length);
		System.arraycopy(c, 0, finalBuf, a.length + b.length + b5.length, c.length);
		System.arraycopy(d, 0, finalBuf, a.length + b.length + b5.length + c.length, d.length);
		return finalBuf;
	}

	private byte[] writeRequest(String fileName, String mode){
		System.out.println("Client: Sending a packet containing: \n" + fileName);		
		byte[] a = new byte[2];
		a[0] = 0;
		a[1] = 2;
		byte[] b = fileName.getBytes();
		byte[] b5 = new byte[1];
		b5[0] = 0;
		byte[] c = mode.getBytes();
		byte[] d = new byte[1];
		d[0] = 0;
		byte[] finalBuf = new byte[a.length + b.length + b5.length + c.length + d.length];
		System.arraycopy(a, 0, finalBuf, 0, a.length);
		System.arraycopy(b, 0, finalBuf, a.length, b.length);
		System.arraycopy(b5, 0, finalBuf, a.length + b.length, b5.length);
		System.arraycopy(c, 0, finalBuf, a.length + b.length + b5.length, c.length);
		System.arraycopy(d, 0, finalBuf, a.length + b.length + b5.length + c.length, d.length);
		return finalBuf;
	}

	public void runClient(int i) {
		//Socket creation
		DatagramSocket sendReceiveSocket = null;
		try {
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Client: Failed to establish socket connection.");
			e.printStackTrace();
			System.exit(1);
		}

		//Setting up the packet
		byte[] buf;
		if(i%2 == 0 && i<11) {
			buf = readRequest("test.txt", "ocTEt");
		}
		else if(i == 11){
			buf = invalidRequest("invalid.txt", "ocTEt");
		}
		else {
			buf = writeRequest("test.txt", "netascii");
		}

		System.out.println(new String(buf,0,buf.length)); 
		try {
			sendPacket = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 23);
		} catch (UnknownHostException e1) {
			System.out.println("Client: Failed to create data packet.");
			e1.printStackTrace();
			System.exit(1);
		}
		//Sending the packet
		System.out.println("Client: Sending out the packet to: ");
		System.out.println("Host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		int len = sendPacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing: ");
		System.out.println(Arrays.toString(sendPacket.getData()));
		System.out.println(new String(sendPacket.getData(),0,len)); 
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("Client: Unable to send packet.");
			e.printStackTrace();
		}
		System.out.println("Client: Houston... Packet has been sent successfully.");

		//Receiving Packets
		//Setting up receive packet to accept data
		byte receiveBuf[] = new byte[100];
		receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
		try {
			System.out.println("Client: Waiting for packets to be received.");
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.println("Client: Unable to receive packets.");
			e.printStackTrace();
		}

		//Unpacking the received packet
		System.out.println("Client: Packet successfully received");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		int receivedPacketLength = receivePacket.getLength();
		System.out.println("Length: " + receivedPacketLength);
		System.out.print("Containing: ");
		String received = new String(receiveBuf,0,receivedPacketLength);   
		System.out.println(received);

		// Closing socket
		sendReceiveSocket.close();
	}

	public static void main(String args[]) {
		Client c = new Client();
		for(int i = 1; i <= 11; i++) {	
			c.runClient(i);
		}
	}

}