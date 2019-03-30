package main;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.IOException;
import java.lang.Thread;

import networking.CommandChannel;
import networking.MultiserviceStealthUDPSocket;
import networking.StealthyUDPClientSocket;

public class MainClient {
	public static void main(String[] args) {
		try {
			MultiserviceStealthUDPSocket theHopper = new MultiserviceStealthUDPSocket(args[0]);
			CommandChannel cc = new CommandChannel(args[0]);
			Thread t = new Thread(cc);
			t.start();
			String hoppingMessage = "This is a test hopping message";
			theHopper.sendHoppingStealthMessage(hoppingMessage, 500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
