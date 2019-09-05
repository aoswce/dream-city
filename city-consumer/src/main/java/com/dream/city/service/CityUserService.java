package com.dream.city.service;

import com.dream.city.service.impl.FallBackCityUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wvv
 */
@FeignClient(value = "city-user", fallback = FallBackCityUser.class)
public interface CityUserService {
    //city-user服务中方法的映射路径

    @RequestMapping("/user/index/{str}")
    String userIndex(@PathVariable("str")String str);

    @RequestMapping("/user/get/{id}")
    String users(@PathVariable("id")String id);
}
