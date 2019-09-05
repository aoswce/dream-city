package com.dream.city.service.impl;

import com.alibaba.fastjson.JSON;
import com.dream.city.config.GateWayConfig;
import com.dream.city.domain.Message;
import com.dream.city.server.Prop;
import com.dream.city.server.WebSocketServer;
import com.dream.city.service.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Wvv
 */
@Service
public class HttpClientServiceImpl implements HttpClientService {
    @Autowired
    private GateWayConfig gateWayConfig;

    @Autowired
    private Prop myProps;

    @Value(value = "${spring.application.name}")
    private String appName;

    @Value(value = "${gate.zuul.url}")
    private String getWayUrl;


    @Override
    public void post(Message message) {
        if (null == getWayUrl){
            System.out.println("simpleProp: " + myProps.getSimpleProp());
            System.out.println("arrayProps: " + (myProps.getArrayProps()));
            System.out.println("listProp1: " + (myProps.getListProp1()));
            System.out.println("listProp2: " + (myProps.getListProp2()));
            System.out.println("mapProps: " + (myProps.getMapProps()));
        }
        System.out.println(appName);
        String url1 = gateWayConfig.getUrl();
        System.out.println(url1);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String gateWayUrl = "http://" + getWayUrl + "/";

        String gateWayPath = message.getTarget();
        String serviceModel = message.getTarget();
        String serviceOpt = message.getData().getType();

        String url = gateWayUrl + gateWayPath + "/" + serviceModel + "/" + serviceOpt;
        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);

        String jsonString = JSON.toJSONString(message);

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        //响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println(responseEntity.getContent());
                String resp = EntityUtils.toString(responseEntity);
                System.out.println("响应内容为:" + resp);

                Message msg = JSON.parseObject(resp, Message.class);

                WebSocketServer.sendInfo(msg);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
