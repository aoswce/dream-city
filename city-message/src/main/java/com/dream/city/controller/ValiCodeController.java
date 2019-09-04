package com.dream.city.controller;

import com.dream.city.domain.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

/**
 * @author Wvv
 */
@RestController
@RequestMapping("/message")
public class ValiCodeController {
    @RequestMapping("/getValiCode")
    public Message getValiCode(@RequestBody Message msg ) {
        String code = String.valueOf(new Random().nextInt(999999));
        msg.setTarget(msg.getSource());
        msg.setSource(msg.getTarget());
        msg.setMsgContent(code);
        return msg;
    }

}
