package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class CommandChannel implements Runnable{
	String ip;
	Socket s;
	PrintWriter out;
	BufferedReader in;
	public CommandChannel(String ip) throws IOException {
		this.ip = ip;
		s=new Socket(ip,80);
		out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	@Override
	public void run() {
		while(true) {
			try {
				String input = in.readLine();
				if(input.startsWith("cmd "));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
