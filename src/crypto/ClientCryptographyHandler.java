package crypto;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ClientCryptographyHandler {
	SecretKey aesKey;
	byte[] nonce;
	
	KeyPair rsaKeyPair;
	Cipher encryptCipher;
	Cipher decryptCipher;
	
	
	public ClientCryptographyHandler() {
		byte[] nonce = new byte[16];
		System.out.println("Starting crypto handler...");
		try {
			encryptCipher = Cipher.getInstance("AES/CTR/NoPadding");
			decryptCipher = Cipher.getInstance("AES/CTR/NoPadding");
			System.out.println("Ciphers created");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Starting RSA generation...");
		generateRSAKeys();
		System.out.println("Key generation completed!");
	}
	void generateRSAKeys(){
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			rsaKeyPair = kpg.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public KeyPair getRSAKeyPair(){
		return this.rsaKeyPair;
	}
	public byte[] getRSAPubKey(){
		PublicKey pubKey = this.rsaKeyPair.getPublic();
		byte[] encodedPubKey = pubKey.getEncoded();
		return encodedPubKey;
	}
	public void setAESCTRDetails(SecretKey recvKey, byte[] recvNonce){
		this.aesKey = recvKey;
		this.nonce = recvNonce;
		IvParameterSpec ivSpec = new IvParameterSpec(nonce);
		try {
			encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
			decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Cipher getEncryptionCipher(){
		return encryptCipher;
	}
	public Cipher getDecryptionCipher(){
		return decryptCipher;
	}
	
}