package com.dream.city.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author  Wvv
 */
@Data
public class Message<T> implements Serializable{

    private static final long serialVersionUID = -6451812593150428369L;

    /**
     * 信息来源
      */
    private String source;
    // 消息类型
    private String messageType;
    // 消息内容
    private T msgContent;
    // 发送目的地
    private String target;
    // 信息来源ip
    private String infoSourceIP;
    // 消息保存时间
    private String createtime;
    // 其他信息
    private String otherContent;


    /*public Message(String sourse, String messageType, String msgContent,
                       String target, String infoSourceIP, String createtime,
                       String otherContent) {
        super();
        this.sourse = sourse;
        this.messageType = messageType;
        this.msgContent = msgContent;
        this.target = target;
        this.infoSourceIP = infoSourceIP;
        this.createtime = createtime;
        this.otherContent = otherContent;
    }*/

    /*@Override
    public String toString() {
        return "Message [sourse=" + sourse + ", messageType=" + messageType
                + ", msgContent=" + msgContent + ", target=" + target
                + ", infoSourceIP=" + infoSourceIP + ", createtime="
                + createtime + ", otherContent=" + otherContent + "]";
    }*/
}
