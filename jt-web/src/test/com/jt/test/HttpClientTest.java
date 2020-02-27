package com.jt.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.junit.Test;


public class HttpClientTest {
    @Test
    public  void   doGet()throws Exception{
        HttpClient httpClient=HttpClients.createDefault();
        String url = "http://www.baidu.com";
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        if(200 == response.getStatusLine().getStatusCode()) {
            System.out.println("请求正确!!!!!!");
            HttpEntity entity = response.getEntity();
            //将entity中携带的信息转化为字符串
            String result = EntityUtils.toString(entity,"utf-8");
            System.out.println(result);
        }else {
            System.out.println("请求异常!!!!!");
        }

    }
}
