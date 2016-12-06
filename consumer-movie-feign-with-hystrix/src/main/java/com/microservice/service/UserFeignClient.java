package com.microservice.service;

import com.microservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cmonkey on 12/6/16.
 */
@FeignClient(name = "microservice-provider-user", fallback = UserFeignClient.HystrixClientFallback.class)
public interface UserFeignClient {

    @RequestMapping("/{id}")
    public User findByIdFeign(@RequestParam("id") Long id);

    @Component
    static class HystrixClientFallback implements UserFeignClient{

        private final static Logger logger = LoggerFactory.getLogger(HystrixClientFallback.class);

        @Override
        public User findByIdFeign(Long id){
            logger.info("findByIdFeign fallback param id = {}", id);

            User user = new User();

            user.setId(-1L);
            user.setUsername("default username");
            user.setAge(28);

            return user;
        }

    }
}
