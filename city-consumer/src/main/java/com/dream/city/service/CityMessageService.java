package com.dream.city.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wvv
 */
@FeignClient(value = "city-message")
public interface CityMessageService {

    @RequestMapping("/message/get/code")
    public Object getCode();
}
