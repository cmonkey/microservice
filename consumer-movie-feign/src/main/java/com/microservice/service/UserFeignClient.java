package com.microservice.service;

import com.microservice.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cmonkey on 12/6/16.
 */
@FeignClient(name = "microservice-provider-user")
public interface UserFeignClient {

    @RequestMapping("/{id}")
    public User findByIdFeign(@RequestParam("id") Long id);
}
