package com.dream.city.server;

import com.dream.city.domain.ApiSendObject;
import com.dream.city.domain.Message;
import com.dream.city.util.HttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @author Wvv
 */

@ServerEndpoint("/dream/city/")
@Component
public class WebSocketServer {

    static Log log= LogFactory.getLog(WebSocketServer.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     *  concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */

    private Session session;

    /**
     * 接收sid
     */

    private String sid="";

    private String clientId="";

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        sid = session.getId();
        log.info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();


        this.sid=sid;
        try {
            //初始化连接的客户端，返回clientId
            String clientId = String.valueOf(new Random().nextInt(99999))+"-"+String.valueOf(sid);
            this.clientId = clientId;
            log.info("有新窗口开始监听:"+clientId+",当前在线人数为" + getOnlineCount());

            //String data = "{\"cmd\":\"init\",\"clientId\":\""+clientId+"\",\"msg\":\"连接成功\"}";
            //sendMessage(data);
            Message message=new Message();
            message.setMessageType("init");
            message.setTarget(clientId);
            message.setCreatetime(new Date().toString());
            message.setInfoSourceIP("server");
            message.setInfoSourceIP("client");
            message.setMsgContent(clientId);
            message.setOtherContent("连接成功");
            String msg = JSON.toJSON(message).toString();
            sendMessage(msg);
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+sid+"的信息:"+message);
        //根据sid 到服务上找对应的数据，=》校验 =》 推送数据到客户端
        try {
            Message msg =  JSONObject.parseObject(message,Message.class);
            //请求restful接口，将数据发送给客户端
            HttpClientUtil.post((Message) msg);
        }catch (Exception e){
            System.out.println("消息格式不正确！");
            e.printStackTrace();
        }


        //群发消息
        /*for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 异步发送对象数据
     *
     * @param object
     * @throws IOException
     */
    public void sendAsyncObject(Object  object) throws IOException{
        this.session.getAsyncRemote().sendObject(object);
    }

    public void sendAsyncMessage(String message) throws IOException{
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 基本方式
     * @param object
     * @throws IOException
     * @throws EncodeException
     */
    public void sendBasicObject(Object object) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(object);
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(Message message) throws IOException {
        log.info("推送消息到窗口"+message.getTarget()+"，推送内容:"+message);
        String clientId = message.getTarget();
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(clientId==null) {
                    item.sendAsyncObject(message);
                }else if(item.clientId.equals(clientId)){
                    item.sendAsyncObject(message);
                    item.sendMessage(JSONObject.toJSONString(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}