package com.uboxol.cloud.pandorasBox;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import com.uboxol.cloud.pandorasBox.service.zcg.HttpProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fenzhong on 15/5/4.
 */
public class PlatAPIControllerTest extends HttpProvider {

    //private String site = "http://192.168.8.79:8080/boxcloud/";
    private String site = "http://localhost:10080/";
    //private String site = "http://boxcloud.uboxol.com/";
    private String app_id = "myapp";
    private String app_secret = "1234567890";
    private String v = "1.0";
    private String t = "" + System.currentTimeMillis() / 1000;


    @Test
    public void testOpenBox() throws IOException {
        site = "http://boxcloud.uboxol.com/";
        app_id = "ubox_20150820";
        app_secret = "PzUqPozMduUc6RohPxVdPmtXNfzzL6";
        String url = site + "api/open_box?";

        Map postmap = new HashMap();
        /**
         * 测试打开柜子, 成功
         */
        postmap.clear();
        postmap.put("inner_code", "99900946");
        postmap.put("unitCode", "A01");
        postmap.put("unitType", "1");
        HttpPost httpPost = new HttpPost(url + "app_id=" + app_id + "&v=" + v + "&t=" + t);
        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
        HttpResponse response = httpClient.execute(httpPost);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        consumeResponse(response.getEntity());
        
    }

    @Test
    public void testListCubes() throws IOException {
        String url = site + "api/list_cubes?";

        Map postmap = new HashMap();
        postmap.put("inner_code", "0021188");
        app_id = "ufan4000ce38";
        app_secret = "";
        v = "1.0.0";
        t = "1466481726";

        HttpPost httpPost = new HttpPost(url + "app_id=" + app_id + "&v=" + v + "&t=" + t);
        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
        HttpResponse response = httpClient.execute(httpPost);
        consumeResponse(response.getEntity());
    }
}
