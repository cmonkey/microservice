package com.microservice.controller;

import com.microservice.entity.User;
import com.microservice.service.RibbonHystrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cmonkey on 12/6/16.
 */
@RestController
public class RibbonController {

    @Autowired
    private RibbonHystrixService ribbonHystrixService;

    @GetMapping("/ribbon/{id}")
    public User findById(@PathVariable Long id){
        return ribbonHystrixService.findById(id);
    }

}
