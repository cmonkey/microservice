package com.microservice.controller;

import com.microservice.entity.PlatFormData;
import com.microservice.entity.User;
import com.microservice.service.PlatFeignClient;
import com.microservice.service.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/feign/plat_data/{startTime}/{endTime}/{offset}/{count}")
    public List<PlatFormData> getPlatfromData(@PathVariable String startTime,
                                              @PathVariable String endTime,
                                              @PathVariable int offset,
                                              @PathVariable int count
                                              ){

        return platFeignClient.getPlatFormData(startTime, endTime, offset, count);
    }

    @PutMapping("/feign/plat_data/refresh")
    public boolean loadPlatFormData(){
        return platFeignClient.refreshPlatData();
    }
}
