package main;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.lang.Thread;

import networking.MultiserviceStealthUDPSocket;
import networking.StealthyUDPClientSocket;

public class MainClient {
	public static void main(String[] args) {
		try {
			MultiserviceStealthUDPSocket theHopper = new MultiserviceStealthUDPSocket(args[0]);
			Scanner s = new Scanner(System.in);
			String message = s.nextLine();
			theHopper.sendHoppingStealthMessage(message, 500);
			s.close();
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
