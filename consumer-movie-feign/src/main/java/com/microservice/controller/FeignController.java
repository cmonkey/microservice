package com.microservice.controller;

import com.microservice.entity.User;
import com.microservice.service.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cmonkey on 12/6/16.
 */
@RestController
public class FeignController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/feign/{id}")
    public User findIdByFeign(@PathVariable Long id){
        return userFeignClient.findByIdFeign(id);
    }
}
