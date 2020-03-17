package com.uboxol.cloud.pandorasBox.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author huyuguo
 * @description 基础加密组件
 */
public abstract class Coder {   
    public static final String KEY_SHA = "SHA";   
    public static final String KEY_MD5 = "MD5";

	private static char[] hexDigits = { '0', '1', '2', '3', '4',
                             '5', '6', '7', '8', '9',
                             'a', 'b', 'c', 'd', 'e', 'f' };
  
    /**
     * @description MD5加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);   
        md5.update(data);   
  
        return md5.digest();
    }   
  
    /**
     * @description SHA加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA1(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);   
        sha.update(data);   
  
        return sha.digest();
    }
    public static String encryptSHA1(String data, Charset charset){
        try {
            byte[] btInput = data.getBytes(charset);
            MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
            sha.update(btInput);
            byte[] bytes = sha.digest();
            return new String(encodeHex(bytes));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * MD5加密
     * @param data
     * @return
     */
	public static String encryptMD5(String data, Charset charset){
		 try {
            byte[] btInput = data.getBytes(charset);
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(KEY_MD5);
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] bytes = mdInst.digest();
            //把密文转换成十六进制的字符串形式

            return new String(encodeHex(bytes));
        } catch (NoSuchAlgorithmException e) {
			return "";
		 }
	}

    /**
     * MD5加密
     * @param data
     * @return
     */
    public static String encryptMD5(String data){
        try {
            byte[] btInput = data.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(KEY_MD5);
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] bytes = mdInst.digest();
            //把密文转换成十六进制的字符串形式

            return new String(encodeHex(bytes));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * SHA1加密
     * @param data
     * @return
     */
    public static String encryptSHA1(String data) {
        try {
            byte[] btInput = data.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest sha1 = MessageDigest.getInstance(KEY_SHA);
            //使用指定的字节更新摘要
            sha1.update(btInput);
            //获得密文
            byte[] bytes = sha1.digest();
            //把密文转换成十六进制的字符串形式

            return new String(encodeHex(bytes));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 将字节数组编码成16进制字符数组
     *
     * @param data
     * @return
     */
    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = hexDigits[(0xf0 & data[i]) >>> 4];
            out[j++] = hexDigits[0x0f & data[i]];
        }
        return out;
    }
}  
