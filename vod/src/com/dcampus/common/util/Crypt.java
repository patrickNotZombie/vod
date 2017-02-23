package com.dcampus.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;




public class Crypt {

	private static final String Seed = "COCONUT";
	
	private static final byte[] EncryptFlag = "20ENCRYPTbyWEBLIBsys15Yp".getBytes();
	
	public static final int EncryptLen = EncryptFlag.length*2;
	
	public static final int EncryptKeyLen = EncryptFlag.length;
	
	public static final byte[] KEY = new byte[]{1,0,1,1,1,0,1,0};
	
	private static Log log = Log.getLog(Crypt.class);
	
	private static final int DEFAULT_BUFFER_SIZE = 1024*1024;//4M
	
	private static final int EOF = -1;
	
	/**
	 * 加密
	 * @param cleartext
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String cleartext)
			throws Exception {
		return encrypt(Seed, cleartext);
	}
	/**
	 * 解密
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encrypted)
			throws Exception {
		return decrypt(Seed, encrypted);
	}
	
	/**
	 * 加密
	 * @param seed
	 * @param cleartext
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String seed, String cleartext)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}
	/**
	 * 解密
	 * @param seed
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String seed, String encrypted)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	//java 合并两个byte数组
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
	
	public static byte[] byteRemove(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_2.length-byte_1.length];
		System.arraycopy(byte_2, byte_1.length, byte_3, 0, byte_2.length-byte_1.length);
		return byte_3;
	}
		
	public static void fileEncrypt(File in, File out) throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(in);
			if (out.exists()) {
				out.delete();
			}
			outputStream = new FileOutputStream(out, true);
			fileEncrypt(inputStream, outputStream);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					log.error(e, e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					log.error(e, e);
				}
			}
		}
	}
	
	public static void fileEncrypt(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        byte[] keys = new byte[EncryptKeyLen];
		byte[] seed = seed(keys);
		output.write(seed, 0, seed.length);
		byte[] k = byteMerger(keys, KEY);
        while (EOF != (n = input.read(buffer))) {
        	en(buffer, k);
            output.write(buffer, 0, n);
        }
	}
	
	public static void fileDecrypt(File in, File out) throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(in);
			if (out.exists()) {
				out.delete();
			}
			outputStream = new FileOutputStream(out, true);
			fileDecrypt(inputStream, outputStream);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					log.error(e, e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					log.error(e, e);
				}
			}
		}
	}
	public static byte[] encryptKey(File file) throws IOException {
		boolean hasEn = false;
        byte b[] = new byte[EncryptLen];
        int n = 0;
        byte[] result = null;
        BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			if (EOF != (n = input.read(b))) {
				result = getEncrypKey(b);
		    }
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					log.error(e, e);
				}
			}
			
		}
		if (result != null) {
			return byteMerger(result, KEY);
		}
		return result;
	}
	public static boolean hasEncrypt(InputStream input) throws IOException {
		boolean hasEn = false;
        byte b[] = new byte[EncryptLen];
        int n = 0;
        if (EOF != (n = input.read(b))) {
        	hasEn = hasEncrypted(b);
        }
        return hasEn;
	}
	public static void fileDecrypt(InputStream input, OutputStream output) throws IOException {
		byte buffer[] = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		boolean hasEn = false;
		byte b[] = new byte[EncryptLen];
		byte[] keys = null;
        if (EOF != (n = input.read(b))) {
        	byte[] pres = getEncrypKey(b);
        	if (pres != null) {
        		hasEn = true;
        		keys = byteMerger(pres, KEY);
        	}
        	if (!hasEn){
        		output.write(b, 0, n);
        	}
        }
        while (EOF != (n = input.read(buffer))) {
        	if(hasEn) {
    			en(buffer,keys);
    		}
    		output.write(buffer, 0, n);
        }
    
	}
	public static long realLength(File data) {
		return data.length() - EncryptLen;
	}
	
	public static int realLength(byte[] data) {
		return data.length - EncryptLen;
	}
	
	public static void en(byte[] data, byte[] keys){
		en(data, keys, 0);
	}
	
	public static void en(byte[] data, byte[] keys,int byteStart){
		int keyIndex = byteStart ;
		for(int x = 0 ; x < data.length ; x++) {
			data[x] = (byte)(data[x] ^ keys[keyIndex]);
			if (++keyIndex == keys.length){
				keyIndex = 0;
			}
		}
	}
	public static void en(byte[] data){
		byte[] k = new byte[EncryptKeyLen];
		seed(k);
		byte[] keys = byteMerger(k, KEY);
		int keyIndex = 0 ;
		for(int x = 0 ; x < data.length ; x++) {
			data[x] = (byte)(data[x] ^ keys[keyIndex]);
			if (++keyIndex == keys.length){
				keyIndex = 0;
			}
		}
	}
	
	public static boolean hasEncrypted(byte[] data) {
		byte[] b = getEncrypKey(data);
		return b != null;
	}
	
	private static byte[] getEncrypKey(byte[] data) {
		int e = EncryptLen;
		if (data.length < e) {
			return null;
		}
		int index = 0;
		int kk = 0;
		byte[] pres = new byte[EncryptKeyLen];
		for(int x = 0 ; x < e ; x++) {
			if (x%2 == 1) {
				if (data[x] != EncryptFlag[index++]) {
					return null;
				}
			} else {
				pres[kk++] = data[x];
			}
		}
		return pres;
	}
	
	private static byte[] seed(byte[] keys) {
		int e = EncryptLen;
		int enIndex = 0;
		int index = 0;
		byte[] results = new byte[e];
		for(int x = 0 ; x < e ; x++) {
			if (x%2 == 1) {
				results[x] = EncryptFlag[enIndex++];
			} else {
				int ran = (int)(Math.random()*10)%2;
				results[x] = (byte)ran;//随机
				keys[index++] = results[x];
			}
		}
		return results;
	}
	
	
	public static void main(String[] args) {
		System.out.println(EncryptKeyLen+KEY.length+"--"+KEY.length+"-"+EncryptKeyLen);
		try {
			//String[] files = new String[]{"1.psd","1.jpg","a.rar","1.txt","3.docx"};
			String[] files = new String[]{"1.txt","3.docx"};
			for (String name : files) {
				long bb = System.currentTimeMillis();
				File f = new File("D:\\-data-\\"+name);
				File n = new File("D:\\-data-\\en"+name);
				
				File n2 = new File("D:\\-data-\\ok"+name);
				File n3 = new File("D:\\-data-\\ok3"+name);
				Crypt.fileEncrypt(f, n);
				long bb2 = System.currentTimeMillis();
				System.out.println(bb2 - bb);
				
				System.out.println(Crypt.hasEncrypt(new FileInputStream(n)));
				
				Crypt.fileDecrypt(n, n2);
				Crypt.fileDecrypt(f, n3);
				System.out.println(System.currentTimeMillis() - bb2);
				System.out.println(System.currentTimeMillis() - bb);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			String[] files = new String[]{"ss.docx"};
			for (String name : files) {
				long bb = System.currentTimeMillis();
				File f = new File("D:\\-data-\\"+name);
				File n = new File("D:\\-data-\\--ok"+name);
				Crypt.fileDecrypt(f, n);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}