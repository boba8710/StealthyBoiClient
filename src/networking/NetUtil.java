package networking;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetUtil {
	public static List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
        	String partString = string.substring(i, Math.min(len, i + partitionSize));
        	while(partString.length() != partitionSize){
        		partString=partString+"0";
        	}
            parts.add(partString);
        }
        return parts;
    }
	public static ArrayList<String> getPartsRandB64Pad(String string, int partitionSize) {
		Random r = new Random();
		char[] alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/+".toCharArray();
        ArrayList<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
        	String partString = string.substring(i, Math.min(len, i + partitionSize));
        	boolean paddingUsed = false;
        	while(partString.length() != partitionSize){
        		if(!paddingUsed){
        			partString+='*';
        			paddingUsed = true;
        			continue;
        		}
        		partString=partString+alpha[r.nextInt(alpha.length)];
        	}
        	if(paddingUsed){
        		partString+="%%";
        	}
            parts.add(partString);
        }
        return parts;
    }
}
