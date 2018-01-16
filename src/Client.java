/**
 * 
 */

import java.net.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author joesamuel
 *
 */
public class Client {

	List<DatagramPacket> packets;
	byte[] buf;
	
	/**
	 * @param p1
	 */
	public Client() {
		packets = new ArrayList<>();
	}
	
	public void packetGenerator(String fileName, String mode) {
		//Generating 11 test packets
		for(int i = 0; i < 11; i++) {
			if(i%2 == 0) {
				//For Read: 01[filename]0[mode]0
				
				buf = [0,1,fileName.getBytes(),0,mode.getBytes(),0];
			}
			else {
				//For Write: 010[filename]0[mode]0
				buf = [0,1,1,fileName.getBytes(),0,mode.getBytes(),0];
			}
			
			//Setting up packets
			packet = new DatagramPacket(, buf.length);
		}
	}
	
	
	
}
