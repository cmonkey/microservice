package com.microservice.service;

import com.microservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by cmonkey on 12/6/16.
 */
@Service
public class RibbonService {

    @Autowired
    private RestTemplate restTemplate;

    // microservice-provider-user 是服务在eureka上注册的服务名
    private final static String url = "http://microservice-provider-user/";

    public User findById(Long id){
        return this.restTemplate.getForObject(url+id, User.class);
    }

}
