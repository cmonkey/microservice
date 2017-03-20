package com.microservice.service;

import com.microservice.entity.PlatFormData;
import com.microservice.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by cmonkey on 12/6/16.
 */
@FeignClient(name = "microservice-provider-user")
public interface PlatFeignClient {

    @RequestMapping("/getPlatFormData/{isDb}/{startTime}/{endTime}")
    public List<PlatFormData> getPlatFormData(
            @RequestParam("isDb") boolean isDb,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime);
}
