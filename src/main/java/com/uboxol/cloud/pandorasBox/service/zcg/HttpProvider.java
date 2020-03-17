package com.uboxol.cloud.pandorasBox.service.zcg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.uboxol.cloud.pandorasBox.utils.SignatureUtils;

/**
 * Created by fenzhong on 15/5/28.
 */
public class HttpProvider {
    public HttpClient httpClient;
    public HttpProvider() {
        httpClient = new DefaultHttpClient();
    }

    /**
     * 拼装 POST 参数，并生成 sign
     */
    public UrlEncodedFormEntity fillParam(String appSecret, String appId, String v, String t, Map<String, String> map) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for(Map.Entry<String, String> entry: map.entrySet()){
            if(entry.getValue()!=null)
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        map.put("app_id", appId);
        map.put("v", v);
        map.put("t", t);
        nvps.add(new BasicNameValuePair("sign", SignatureUtils.sign(map,appSecret)));
        return new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8"));
    }

    /**
     * 拼装 POST 参数，并生成错误的 sign
     */
    public UrlEncodedFormEntity fillParamWithWrongSign(String appSecret, String appId, String v, String t, Map<String, String> map) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for(Map.Entry<String, String> entry: map.entrySet()){
            if(entry.getValue()!=null)
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        map.put("app_id", appId);
        map.put("v", v);
        map.put("t", t);
        String sign = SignatureUtils.getContent(map, appSecret+"0");
        nvps.add(new BasicNameValuePair("sign", sign));
        return new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8"));
    }

    /**
     * 打印并消费 response
     */
    public String consumeResponse(org.apache.http.HttpEntity entity) throws IOException {
//        byte[] buffer = new byte[entity.getContent().available()];
//        entity.getContent().read(buffer);
//        String responseContent = new String(buffer, "UTF-8");
//        System.out.println(responseContent);
//        EntityUtils.consume(entity);
//        return responseContent;
    	
    	String temp = EntityUtils.toString(entity);
        System.out.println(temp );
        return  temp;
    	
    }
}
