
import java.io.IOException;
import java.net.*;
/**
 * @author joesamuel
 *
 */
public class IntermediateHost {

	private DatagramPacket sendPacket, receivePacket, serverPacket, clientPacket;
	private DatagramSocket sendSocket, receiveSocket;

	public IntermediateHost() {
		//Setting up Send socket
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Intermediate Host: Failed to establish send socket.");
			e.printStackTrace();
			System.exit(1);
		}
		//Setting up Receive socket
		try {
			receiveSocket = new DatagramSocket(5023);
		} catch (SocketException e) {
			System.out.println("Intermediate Host: Failed to establish receive socket.");
			e.printStackTrace();
			System.exit(1);
		}
	}


	public void runHost() {
		//Receiving a packet for retransmission
		byte buf[] = new byte[100];
		receivePacket = new DatagramPacket(buf, buf.length);
		System.out.println("Intermediate Host: Waiting for Packet.\n");
		try {        
			System.out.println("Intermediate Host: Waiting to receive packet."); 
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.println("Intermediate Host: Failed to receive packet.");
			e.printStackTrace();
			System.exit(1);
		}

		// Unpacking the received packet
		System.out.println("Intermediate Host: Unpacking packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		int len = receivePacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Host: Containing: " );
		String received = new String(buf,0,len);   
		System.out.println("Containing: " + received + "\n");

		//Setting up for retransmission of received packet
		sendPacket = new DatagramPacket(buf, receivePacket.getLength(),
				receivePacket.getAddress(), 5069);

		System.out.println("Intermediate Host: Sending packet that was received.");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		len = sendPacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing: ");
		System.out.println(new String(sendPacket.getData(),0,len));

		// Sending the new packet to Server
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("Intermediate Host: Failed to send packet to Server.");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Intermediate Host: Packet successfully sent to server.");
		
		//SERVER PACKET RESOPNSE 
		//Receiving a packet for retransmission
				byte newBuf[] = new byte[100];
				serverPacket = new DatagramPacket(newBuf, newBuf.length);
				try {        
					System.out.println("Intermediate Host: Waiting to receive packet from server."); 
					receiveSocket.receive(serverPacket);
				} catch (IOException e) {
					System.out.println("Intermediate Host: Failed to receive packet from server.");
					e.printStackTrace();
					System.exit(1);
				}

				// Unpacking the received packet
				System.out.println("Intermediate Host: Unpacking packet received from server:");
				System.out.println("From host: " + receivePacket.getAddress());
				System.out.println("Host port: " + receivePacket.getPort());
				len = serverPacket.getLength();
				System.out.println("Length: " + len);
				System.out.print("Host: Containing: " );
				String serverString = new String(buf,0,len);   
				System.out.println("Containing: " + serverString + "\n");

				// Slow things down (wait 5 seconds)
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e ) {
					e.printStackTrace();
					System.exit(1);
				}

				//Setting up for retransmission of received packet
				clientPacket = new DatagramPacket(buf, serverPacket.getLength(),
						serverPacket.getAddress(), 5023);

				System.out.println("Intermediate Host: Sending packet that was received to client.");
				System.out.println("To host: " + clientPacket.getAddress());
				System.out.println("Destination host port: " + clientPacket.getPort());
				len = clientPacket.getLength();
				System.out.println("Length: " + len);
				System.out.print("Containing: ");
				System.out.println(new String(clientPacket.getData(),0,len));

				// Sending the new packet to Server
				try {
					sendSocket.send(clientPacket);
				} catch (IOException e) {
					System.out.println("Intermediate Host: Failed to send packet to client.");
					e.printStackTrace();
					System.exit(1);
				}

				System.out.println("Intermediate Host: Packet successfully sent to client.");

				// Closing sockets
				sendSocket.close();
				receiveSocket.close();
		
		
	}

	public static void main( String args[] )
	{
		IntermediateHost host = new IntermediateHost();
		while(true) {
			host.runHost();
		}

	}
}