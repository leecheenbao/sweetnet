package com.sweetNet.until;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class HexUtility {
	public static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   }
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
    public static final String N = "c28e0ffa96fa48571dd31b3ea6ff7aa22e8b9ef28e6fc72fa0b731864685c922"  
            +  "ca27faa3a3e543991dca178820c224763e48a6c4fff7ecaa40065905c738fd18"  
            +  "80a93b74c2380bd0dcb79144e1e79fc621031a8316014961578b5f81a82b7071"  
            +  "dd65d440066d10655d91a1493f947e7e5f774b836c058754a17f131f755df783"  
            +  "582f0e21cd451dc50c8b652089671b1d9c71f2cf6755f38656a115ac3e644376"  
            +  "febf8f35f09e47bf8e4a2c8fb02a92bca9a026797186695e549e29656d768a9c"  
            +  "6df8fc3f37f0a7daf614028ada9c36c7f0c93c975b1e7642e39fdda050dae230"  
            +  "cc61d263c438abd033812a37767f35f84328b8666975c267ad8150eed367eab7";  
      
    public static final String E = "00010001"; // hex:00010001 decimal: 65537 base64: AQAB  
      
    public static final String D = "79cb3b5f91925f25024bbcfb8cc9d4b8a0d0d111616fc24f239a6b4b76ec9bcc"  
            +  "c6a71e75c0cd6e72f53e255b17bed1daa0051539b050417d1715a23746cf7b4a"  
            +  "12895eea2a07b205ef968f3f82f8608244fa4f678ea8018b09a5fb850c851d20"  
            +  "7b0c1b427583634741bb402fbdb8b533618a29e0bd07fcff53165d1f4d7724d1"  
            +  "a1e2b395b7f7ab1b79790848bb83d04db1325d643d97ace063378f9a850e41e0"  
            +  "cc2a696b4c6cad89a94009a2816451210140769a530e5d1117f802d6f3d3c446"  
            +  "d7c88a07dff9be278dabb6c97b8019101bcec8ad7329aa6f4d3b5b677df5634f"  
            +  "3ea828b18939337b367f5a8968471cb1f7dfec2f587f20e7c6141f8caf72d7a9";  

	/** 
	* 从hex string生成公钥  
	* @param stringN 
	* @param stringE 
	* @return 构造好的公钥 
	* @throws NoSuchAlgorithmException 
	* @throws InvalidKeySpecException 
	*/  
	public static PublicKey createPublicKey(String stringN, String stringE)   
	throws NoSuchAlgorithmException, InvalidKeySpecException   
	{  
	   try  
	   {  
	       BigInteger N = new BigInteger(stringN,16); // hex base  
	       BigInteger E = new BigInteger(stringE,16); // hex base  
	  
	  
	       RSAPublicKeySpec spec = new RSAPublicKeySpec(N, E);  
	       KeyFactory kf = KeyFactory.getInstance("RSA");  
	       return  kf.generatePublic(spec);  
	   }   
	   catch(Exception e)   
	   {  
	       e.printStackTrace();  
	   }  
	  
	  
	   return null;  
	}  
	  
	/** 
	* 从hex string 生成私钥 
	* @param stringN 
	* @param stringD 
	* @return 构造好的私钥 
	* @throws NoSuchAlgorithmException 
	* @throws InvalidKeySpecException 
	*/  
	public static PrivateKey createPrivateKey(String stringN, String stringD)   
	throws NoSuchAlgorithmException, InvalidKeySpecException   
	{  
	   try  
	   {  
	       BigInteger N = new BigInteger(stringN,16); // hex base  
	       BigInteger D = new BigInteger(stringD,16); // hex base  
	  
	  
	       RSAPrivateKeySpec spec = new RSAPrivateKeySpec(N, D);  
	       KeyFactory kf = KeyFactory.getInstance("RSA");  
	       return  kf.generatePrivate(spec);  
	   }   
	   catch(Exception e)   
	   {  
	       e.printStackTrace();  
	   }  
	  
	  
	   return null;  
	}  
	  
	  
	/** 
	* 用公钥加密信息 
	* @param message 
	* @param key 
	* @return 加密后的密文 
	*/  
	public static byte[] encrypt(String message)   
	{  
	   try  
	   {  
		   PublicKey pubKey = HexUtility.createPublicKey(HexUtility.N, HexUtility.E);
	       Cipher cipher = Cipher.getInstance("RSA");  
	       cipher.init(Cipher.ENCRYPT_MODE, pubKey);  
	       byte[] data = cipher.doFinal(message.getBytes());  
	       return data;  
	   }   
	   catch(Exception e)   
	   {  
	       e.printStackTrace();  
	   }  
	   return new byte[1];  
	}  
	  
	  
	/** 
	* 用私钥解密信息 
	* @param cipherText 
	* @param key 
	* @return 解密后的明文 
	*/  
	public static String decrypt(byte[] cipherText)   
	{  
	   try  
	   {  
		   PrivateKey priKey = HexUtility.createPrivateKey(HexUtility.N, HexUtility.D); 
	       Cipher cipher = Cipher.getInstance("RSA");  
	       cipher.init(Cipher.DECRYPT_MODE, priKey);  
	       byte[] data = cipher.doFinal(cipherText);  
	       return new String(data);  
	   }   
	   catch(Exception e)   
	   {  
	       e.printStackTrace();  
	   }  
	   return new String("");  
	}  
	

	
}
