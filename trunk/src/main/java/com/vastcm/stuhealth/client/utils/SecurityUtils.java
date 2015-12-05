/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.utils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.rsa.RSAKeyFactory;

/**
 *
 * @author House
 */
public class SecurityUtils {
    
      public static PublicKey getPublicKey(String modulus,String publicExponent) throws Exception {  
            BigInteger m = new BigInteger(new BASE64Decoder().decodeBuffer(modulus));  
            BigInteger e = new BigInteger(new BASE64Decoder().decodeBuffer(publicExponent));  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m,e);  
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            PublicKey publicKey = keyFactory.generatePublic(keySpec);  
            return publicKey;  
      }  

      public static PrivateKey getPrivateKey(String modulus,String privateExponent) throws Exception {  
            BigInteger m = new BigInteger(modulus);  
            BigInteger e = new BigInteger(privateExponent);  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m,e);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
            return privateKey;  
      }  
  
    
    public static boolean verifySignature(byte[] data, String sign) throws Exception {
        BASE64Decoder b64Decoder = new BASE64Decoder();
        String modules = "vicoqlh6+GkXGrMtfkG4EumYz7pkdy2T4lXTTHaE+Yw8XwnclX/y06LO1ka3f9OmYdqKny+OEFOCzsZtdk9vUMWkwA7yS/nlvOo6PgDSfJYReJThaXyBOevVV1Lmuci29MVe4JZlgfJ4xYUr14tEn0mkON3Jp1zojv/GxR/na2k=";
        String exponent = "AQAB";
        PublicKey publicKey = SecurityUtils.getPublicKey(modules, exponent);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey); 
        signature.update("4401060100352157957X".getBytes("utf-8"));
        return signature.verify(b64Decoder.decodeBuffer("J4OuCVc5GLIflUbnfv60G3FendbLoFtA1N2HsDngn/UWUUxPegoy1UFpIJn4e7Qb44imu7QXyO/3jKeu3v6zZWOhexSeXgYldiQnQRmKXq8AdRWfN9mnZqAoMbVaSsUR1kkZ8UJMy4dCuUGJAiPtK1I6xyBMpKJ5AWJ+vM+Tj5k="));
        
//        byte[] dataByteArray = "4401060100352157957X".getBytes();
//        byte[] signByteArray = b64Decoder.decodeBuffer("J4OuCVc5GLIflUbnfv60G3FendbLoFtA1N2HsDngn/UWUUxPegoy1UFpIJn4e7Qb44imu7QXyO/3jKeu3v6zZWOhexSeXgYldiQnQRmKXq8AdRWfN9mnZqAoMbVaSsUR1kkZ8UJMy4dCuUGJAiPtK1I6xyBMpKJ5AWJ+vM+Tj5k=");
//        String privateKeyStr = "<RSAKeyValue><Modulus>vicoqlh6+GkXGrMtfkG4EumYz7pkdy2T4lXTTHaE+Yw8XwnclX/y06LO1ka3f9OmYdqKny+OEFOCzsZtdk9vUMWkwA7yS/nlvOo6PgDSfJYReJThaXyBOevVV1Lmuci29MVe4JZlgfJ4xYUr14tEn0mkON3Jp1zojv/GxR/na2k=</Modulus><Exponent>AQAB</Exponent><P>5kogjZoERZZSHrHe0CpZ0ojtoTaG/+FOjYJUJZoH2LLxo2T3udrjqT5pDNR3POc86CvIyi4WQPWnoFqifzSpFQ==</P><Q>02Hkt7vCDFPBfnKjWvyUwoXMFmyERcnneDWGviWYhbir2uwgqjjWlRonc4HXzWqQ/hHYd4fJbq2MQXY/S2smBQ==</Q><DP>ij5JmHeyxMtedMq2RSSHU1mbvUFsfUMV0uE//mqe9zlE0Z+VDt8V981dZrRM+CSgbYeLO8O63Dgb7qk6Dz7fmQ==</DP><DQ>Cp40OvZ3fN8ILS3c/skJP+wwoD+7NTY7JlKp/gstaiool8qcqXBJdxvlN2wiRvM/bUPAN+DeQWz+SpuB+mvbcQ==</DQ><InverseQ>G0wi5JtbssNYtv7aq0FtuqUSHifXSB7GZ0O3Ub6PsA8tFd/bOSJOqm9fzmoeKChQCxRmVAZRCH309OxZQISq3A==</InverseQ><D>mSdPEIy9VrDGBCJGxQdHrCvrgXw+CONz1fuMeWyIQ7pYsa9utBbezOi3sUw55cHho7Tyq64QvUOV+tdFOOJX45Hf3wodgGVBRCYrGCPLzk8wF+x4nEeMbo18OEoKcuTzE7PN/O3Sgoeo3SsslYpOiKmH6xqxr8T2NKJX9qcsS1E=</D></RSAKeyValue>";
//        PrivateKey priKey = RsaHelper.decodePrivateKeyFromXml(privateKeyStr);
//        byte[] signDataByteArray = RsaHelper.signData(dataByteArray, priKey);
//        System.out.println(new BASE64Encoder().encode(signDataByteArray));
//        String publicKeyStr = "<RSAKeyValue><Modulus>vicoqlh6+GkXGrMtfkG4EumYz7pkdy2T4lXTTHaE+Yw8XwnclX/y06LO1ka3f9OmYdqKny+OEFOCzsZtdk9vUMWkwA7yS/nlvOo6PgDSfJYReJThaXyBOevVV1Lmuci29MVe4JZlgfJ4xYUr14tEn0mkON3Jp1zojv/GxR/na2k=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
//        PublicKey publicKey = RsaHelper.decodePublicKeyFromXml(publicKeyStr);
//        return RsaHelper.verifySign(dataByteArray, signByteArray, publicKey);
        
    }
    
    public static String cryptPassword(String password) {
        return SecurityUtils.md5(password + "PermitOrgan2205");
    }
     
    public static String md5(String string)
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
        'E', 'F' };
        try {
            byte[] bytes = string.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            byte[] updateBytes = messageDigest.digest();
            int len = updateBytes.length;
            char myChar[] = new char[len * 2];
            int k = 0;
            for (int i = 0; i < len; i++) {
                byte byte0 = updateBytes[i];
                myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
                myChar[k++] = hexDigits[byte0 & 0x0f];
            }
        return new String(myChar);
        } catch (Exception e) {
        return null;
        }
    }
    
    public static void main(String[] args) {
		System.out.println(cryptPassword("888888"));
	}
}
