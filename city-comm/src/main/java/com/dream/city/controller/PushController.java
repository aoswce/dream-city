package com.dream.city.controller;

import com.dream.city.server.WebSocketServer;
import com.dream.city.domain.ApiReturnObject;
import com.dream.city.domain.Message;
import com.dream.city.util.ApiUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author Wvv
 */
@RestController
@RequestMapping("/push")
public class PushController {
    /**
     * 页面请求
     */

    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav = new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }

    /**
     * 推送数据接口
     *
     * @param fid
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("/socket/push/{fid}")
    public ApiReturnObject pushToWeb(@PathVariable String fid, String message) {
        if (message == null) {
            message = "这是测试信息...";
        }
        try {
            //ApiSendObject msg = ApiUtil.pack(message);
            Message msg = new Message();
            msg.setMsgContent(message);
            WebSocketServer.sendInfo(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiUtil.error(fid + "#" + e.getMessage());
        }
        return ApiUtil.success(fid);
    }
}
