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
		System.out.println(System.getProperty("os.name"));
		while(true){
			try {
				Thread.sleep(1000);
				MultiserviceStealthUDPSocket msus = new MultiserviceStealthUDPSocket(args[0]);
				CommandChannel cc = new CommandChannel(args[0],msus);
				cc.run();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
