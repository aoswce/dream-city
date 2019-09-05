package com.dream.city.controller;

import com.dream.city.service.CityMessageService;
import com.dream.city.service.CityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wvv
 */
@RestController
@RequestMapping("/consumer")
public class FeignController {
    @Autowired
    CityUserService cityUserService;

    @Autowired
    CityMessageService messageService;

    @RequestMapping("/getcode")
    public Object getCode(){
        return messageService.getCode();
    }

    @RequestMapping("/get/user/{id}")
    public Object getUser(@PathVariable("id")String id){
        return cityUserService.users(id);
    }

    @RequestMapping
    public Object userCode(){
        Map<String,Object> map= new HashMap<>();
        String index = cityUserService.userIndex("index-user");
        Object code = messageService.getCode();
        map.put("index",index);
        map.put("code",code);
        return map;
    }
}
