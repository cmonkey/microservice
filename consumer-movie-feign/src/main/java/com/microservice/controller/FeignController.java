package com.microservice.controller;

import com.microservice.entity.PlatFormData;
import com.microservice.entity.User;
import com.microservice.service.PlatFeignClient;
import com.microservice.service.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cmonkey on 12/6/16.
 */
@RestController
public class FeignController {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PlatFeignClient platFeignClient;

    @GetMapping("/feign/{id}")
    public User findIdByFeign(@PathVariable Long id){
        return userFeignClient.findByIdFeign(id);
    }

    @GetMapping("/feign/{isDb}/{startTime}/{endTime}")
    public List<PlatFormData> getPlatfromData(@PathVariable boolean isDb, @PathVariable String startTime, @PathVariable String endTime){

        return platFeignClient.getPlatFormData(isDb,startTime, endTime);
    }
}
