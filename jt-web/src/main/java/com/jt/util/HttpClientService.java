package com.jt.util;

import com.baomidou.mybatisplus.extension.api.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class HttpClientService {
    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private RequestConfig requestConfig;
    public String doGet(String url, Map<String,String> params,String charset){
        //判断用户是否传递参数
        if(StringUtils.isEmpty(charset)){
            charset="UTF-8";
        }
        //2判断用户是否传参
        if(params!=null){

            url+="?";
            for (Map.Entry<String,String> entry :  params.entrySet()){
                String name=entry.getKey();
                String value=entry.getValue();
                url+= name+"="+value+"&";
            }
           url= url.substring(0,url.length()-1);
        }
        HttpGet httpGet=new HttpGet(url);
        httpGet.setConfig(requestConfig);

        String result=null;
        //发起http请求
        try {
           HttpResponse response= httpClient.execute(httpGet);
           if(response.getStatusLine().getStatusCode()==200){
               HttpEntity entity=response.getEntity();

                result=EntityUtils.toString(entity,charset);
           }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }
    public String doGet(String url){
        return doGet(url,null,null);
    }
    public String doGet(String url,Map<String,String> params){
        return doGet(url,params,null);
    }

}
