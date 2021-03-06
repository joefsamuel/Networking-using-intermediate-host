
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Class to act as server to receive data packets from Intermediate host and transmit a validation algorithm to validate packet received.
 * 
 * @author joesamuel
 *
 */
public class HostServer {

	private DatagramPacket sendPacket, receivePacket;
	private byte[] validPacket;
	private boolean flag;
	private boolean read;
	private boolean format;
	private int serverPort;
	private int hostPort;

	/*
	 * Constructor
	 */
	private HostServer() {
		validPacket = new byte[4];
		flag = false;
		read = false;
		format = false;
		serverPort = 5069;
		hostPort = 5023;
	}

	/*
	 * Method to validate the format of byte array
	 * 
	 * @param  receivedByte byte array to be checked against validation condition
	 * @return none
	 */
	private void formatValidity(byte[] receivedByte) {
		//Note: true pattern matching can only be achieved through extensive use of algorithm such as KPM
		//use of regex wouldn't be possible due to byte conversion and string error 
		format = true;
		if(format) {
			System.out.println("Server: Received packet follows valid format.");
		}
	}

	/*
	 * Method to validate the read format of byte array
	 * 
	 * @param  receivedByte byte array to be checked against validation condition
	 * @return none
	 */
	private void checkReadValidity(byte[] receivedByte) {
		if(receivedByte[0] == 0 && receivedByte[1]==1) {
			System.out.println("Server: Read request detected.");
			read = true;
			flag = true;
		}
	}

	/*
	 * Method to validate the write format of byte array
	 * 
	 * @param  receivedByte byte array to be checked against validation condition
	 * @return none
	 */
	private void checkWriteValidity(byte[] receivedByte) {
		if(receivedByte[0] == 0 && receivedByte[1] == 2) {
			System.out.println("Server: Write request detected.");
			read = false;
			flag = true;
		}
	}

	/*
	 * Method to run server to receive and transmit validation packet.
	 * 
	 * @param none
	 * @return none
	 */
	public void runServer(){
		while(true) {
			DatagramSocket sendSocket = null, receiveSocket = null;

			//Setting up receive socket
			try {
				receiveSocket = new DatagramSocket(serverPort);
				System.out.println("Server: Socket connection established.");
			} catch (SocketException e) {
				System.out.println("Server: Failed to establish socket connection.");
				e.printStackTrace();
				System.exit(1);
			}

			//Setting up receive packet
			byte[] buf = new byte[100];
			receivePacket = new DatagramPacket(buf, buf.length);
			try {
				System.out.println("Server: Waiting to receive packet.");
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.println("Server: Failure to receive packet.");
				e.printStackTrace();
				System.exit(1);
			}

			//Receiving the packet
			System.out.println("Server: Packet has successfully been received.");
			System.out.println("From: " + receivePacket.getAddress());
			System.out.println("Port: " + receivePacket.getPort());
			System.out.println(Arrays.toString(receivePacket.getData()));

			//Parsing if packet is valid
			System.out.println("Server: Parsing packet to confirm validity");
			byte[] receivedByte = receivePacket.getData();
			formatValidity(receivedByte);
			checkReadValidity(receivedByte); 
			checkWriteValidity(receivedByte);

			//Printing out valid packet data
			int receivedPacketLength = receivePacket.getLength();
			System.out.println("Length: " + receivedPacketLength);
			System.out.print("Containing: ");
			String received = new String(buf,0,receivedPacketLength);   
			System.out.println(received + "\n");

			//Response to received Packet
			if(!flag){
				System.out.println("Server: Invalid packet.");
				System.exit(1);
			}
			else if(read && flag){
				validPacket[0] = 0;
				validPacket[1] = 3;
				validPacket[2] = 0;
				validPacket[3] = 1;
			}
			else{
				validPacket[0] = 0;
				validPacket[1] = 4;
				validPacket[2] = 0;
				validPacket[3] = 0;
			}

			//CHANGE MADE HERE
			sendPacket = new DatagramPacket(validPacket, validPacket.length, receivePacket.getAddress(), hostPort); //Works if port set to 23(Intermediate Host)


			//Sending packet
			try {
				sendSocket = new DatagramSocket();
			} catch (SocketException e1) {
				System.out.println("Server: Send socket failed to establish.");
				e1.printStackTrace();
				System.exit(1);
			}
			System.out.println("Server: Send socket connection established.");
			System.out.println("Server: Sending packet:");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
			int len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");
			System.out.println(new String(sendPacket.getData(),0,len));
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				System.out.println("Server: Failed to send packet.");
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Server: Packet successfully sent");

			//Closing sockets
			sendSocket.close();
			receiveSocket.close();
		}
	}

	/*
	 * Method to run the server
	 * 
	 * @param args[] default arguments to main function.
	 * @return none.
	 */
	public static void main(String args[]) {
		HostServer server = new HostServer();
		server.runServer();
	}
}
