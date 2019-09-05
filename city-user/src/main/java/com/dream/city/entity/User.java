package com.dream.city.entity;

import lombok.Data;

/**
 * @author Wvv
 */
@Data
public class User {

    private Integer uId;
    private String uName;
    private String uPass;
    private String nick;
    private String invite;

    public User(Integer uId, String uName, String uPass, String nick, String invite){
        this.uId =uId;
        this.uName = uName;
        this.uPass = uPass;
        this.invite = invite;
        this.nick = nick;
    }
}
