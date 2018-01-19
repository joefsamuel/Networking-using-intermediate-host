/**
 * 
 */
import java.io.IOException;
import java.net.*;
/**
 * @author joesamuel
 *
 */
public class IntermediateHost {

	 DatagramPacket sendPacket, receivePacket;
	 DatagramSocket sendSocket, receiveSocket;
	 
	 public IntermediateHost() {
		 
		try {
			sendSocket = new DatagramSocket(69);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			receiveSocket = new DatagramSocket(23);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void scanForever() {
		 byte buf[] = new byte[100];
		 
	      receivePacket = new DatagramPacket(buf, buf.length);
	      
	      System.out.println("Intermediate Host: Waiting for Packet.\n");

	      // Block until a datagram packet is received from receiveSocket.
	      try {        
	         System.out.println("Intermediate Host: Waiting..."); // so we know we're waiting
	         receiveSocket.receive(receivePacket);
	      } catch (IOException e) {
	         System.out.print("Intermediate Host: IO Exception: likely:");
	         System.out.println("Intermediate Host: Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }

	      // Process the received datagram.
	      System.out.println("Intermediate Host: Packet received:");
	      System.out.println("Intermediate Host: From host: " + receivePacket.getAddress());
	      System.out.println("Intermediate Host: Host port: " + receivePacket.getPort());
	      int len = receivePacket.getLength();
	      System.out.println("Intermediate Host: Length: " + len);
	      System.out.print("Intermediate Host: Containing: " );

	      // Form a String from the byte array.
	      String received = new String(buf,0,len);   
	      System.out.println(received + "\n");
	      
	      // Slow things down (wait 5 seconds)
	      try {
	          Thread.sleep(5000);
	      } catch (InterruptedException e ) {
	          e.printStackTrace();
	          System.exit(1);
	      }

	      sendPacket = new DatagramPacket(buf, receivePacket.getLength(),
                  receivePacket.getAddress(), receivePacket.getPort());

System.out.println( "Server: Sending packet:");
System.out.println("To host: " + sendPacket.getAddress());
System.out.println("Destination host port: " + sendPacket.getPort());
len = sendPacket.getLength();
System.out.println("Length: " + len);
System.out.print("Containing: ");
System.out.println(new String(sendPacket.getData(),0,len));
// or (as we should be sending back the same thing)
// System.out.println(received); 

// Send the datagram packet to the client via the send socket. 
try {
sendSocket.send(sendPacket);
} catch (IOException e) {
e.printStackTrace();
System.exit(1);
}

System.out.println("Server: packet sent");

// We're finished, so close the sockets.
sendSocket.close();
receiveSocket.close();
	}
	
	public static void main( String args[] )
	   {
		while(true) {
			 IntermediateHost ihost = new IntermediateHost();
			 ihost.scanForever();
		}
	     
	   }
}
