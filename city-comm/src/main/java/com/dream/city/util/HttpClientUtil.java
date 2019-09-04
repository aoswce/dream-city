package com.dream.city.util;

import com.dream.city.server.WebSocketServer;
import com.dream.city.domain.Message;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

/**
 * @author Wvv
 */
public class HttpClientUtil {
    public static void post(Message msg) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = "http://localhost:8081/"+msg.getTarget()+"/"+msg.getMessageType();
        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);

        String jsonString = JSON.toJSONString(msg);

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

                Message message = JSON.parseObject(resp,Message.class);

                WebSocketServer.sendInfo(message);
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
