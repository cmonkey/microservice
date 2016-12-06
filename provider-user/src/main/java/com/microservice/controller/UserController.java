package com.microservice.controller;

import com.microservice.dao.UserRepository;
import com.microservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cmonkey on 12/6/16.
 */
@RestController
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private DiscoveryClient disconveryClient;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        User findOne = userRepository.findOne(id);

        logger.info("findById result = {}", findOne);
        return findOne;
    }

    @GetMapping("/all")
    public List<User> all(){
        List<User> list = userRepository.findAll();

        logger.info("all result = {}", list);
        return list;
    }

    @GetMapping("/instance-info")
    public ServiceInstance showInfo(){
        ServiceInstance serviceInstance = this.disconveryClient.getLocalServiceInstance();

        return serviceInstance;
    }
}
