package main;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.lang.Thread;

import networking.MultiserviceStealthUDPSocket;
import networking.StealthyUDPClientSocket;

public class MainClient {
	public static void main(String[] args) {
		try {
			MultiserviceStealthUDPSocket theHopper = new MultiserviceStealthUDPSocket("192.168.80.135", 1337);
			theHopper.sendHoppingStealthMessage("This data was sent using protocol hopping stealth!", 500);
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
