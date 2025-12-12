package com.icee;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class HttpClientTest {

    @Test
    public void testGet() throws Exception{
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpGet对象
        HttpGet httpGet = new HttpGet("http://localhost:8081/user/shop/status");
        //执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        int code = response.getStatusLine().getStatusCode();
        System.out.println(code);
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println(body);

        response.close();
        httpClient.close();
    }

    @Test
    public void testPost() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8081/admin/employee/login");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");
        StringEntity entity=new StringEntity(jsonObject.toString());
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        System.out.println(code);
        HttpEntity responseEntity = response.getEntity();
        String body = EntityUtils.toString(responseEntity);
        System.out.println(body);
        response.close();
        httpClient.close();
    }
}
