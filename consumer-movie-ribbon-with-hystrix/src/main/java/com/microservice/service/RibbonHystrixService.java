package com.microservice.service;

import com.microservice.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by cmonkey on 12/6/16.
 */
@Service
public class RibbonHystrixService {

    private final static Logger logger = LoggerFactory.getLogger(RibbonHystrixService.class);


    @Autowired
    private RestTemplate restTemplate;

    // microservice-provider-user 是服务在eureka上注册的服务名
    private final static String url = "http://microservice-provider-user/";

    @HystrixCommand(fallbackMethod = "fallback")
    public User findById(Long id){
        return this.restTemplate.getForObject(url+id, User.class);
    }

    public User fallback(Long id){
        logger.info("fallback param id = {}", id);

        User user = new User();
        user.setId(-1L);
        user.setUsername("default username");
        user.setAge(0);

        return user;
    }

}
