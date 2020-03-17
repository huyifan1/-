package com.uboxol.cloud.pandorasBox.utils;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zg
 * Date: 13-12-31
 * Time: 下午2:43
 * To change this template use File | Settings | File Templates.
 */
public class SignatureUtils {

	public final static String charset = "UTF-8";

	/**
	 * 获取签名
	 *  注意：签名时将字符串转化成字节流时指定的字符集charset一致。
	 *  MD5签名计算公式：
	 *      sign = Md5(原字符串&key=商户密钥). toUpperCase
	 *
	 * @param params
	 * @return
	 */
	public static String sign(Map params, String priviteKey){
		return Coder.encryptSHA1(getContent(params, priviteKey), Charset.forName(charset));
	}

	/**
	 * 功能：将安全校验码和参数排序
	 * 无论是请求还是应答，无论是用get、post还是xml，签名原始串按以下方式组装成字符串：
	 *  1、除sign字段外，所有参数按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接而成，空值不传递，不参与签名组串。
	 *  2、所有参数是指通信过程中实际出现的所有非空参数，即使是接口中无描述的字段，也需要参与签名组串。如退款接口中无test字段，如果商户请求时或财付通应答时，test有值，test字段也得参与参与签名组串
	 *  3、签名原始串中，字段名和字段值都采用原始值，不进行URL Encode。
	 *  4、财付通返回的应答或通知消息可能会由于升级增加参数，请验证应答签名时注意允许这种情况。
	 *
	 * @param params      参数集合
	 * @param privateKey  安全校验码
	 */
	public static String getContent(Map params, String privateKey){
		List keys = new ArrayList(params.keySet());
		Collections.sort(keys);//对参数key进行排序
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			if (value == null || "".equals(value.trim()) || key.equalsIgnoreCase("sign")) {
				continue;
			}
			sb.append(key).append("=").append(value);
		}
		String urlKey = sb.append("_").append(privateKey).toString();
		return urlKey;
	}


	public static void main(String[] args){
		Map params = new HashMap();
		params.put("innerCode", "0000000");
		params.put("password", "116591");
		params.put("phone", "18611619885");
		System.out.println(getContent(params, "adfadfdafdasfa"));
	}
}
