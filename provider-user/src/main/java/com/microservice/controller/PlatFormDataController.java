package com.microservice.controller;
import com.microservice.entity.PlatFormData;
import com.microservice.service.PlatFormDataService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cmonkey on 3/20/17.
 */
@RestController
public class PlatFormDataController {

    private final static Logger logger = LoggerFactory.getLogger(PlatFormDataController.class);
    private final static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Resource
    PlatFormDataService platFormDataService;

    @GetMapping("/plat_data/{startTime}/{endTime}/{offset}/{count}")
    public List<PlatFormData> getPlatFormData(
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime,
            @RequestParam(value = "offset") int offset,
            @RequestParam(value = "count") int count
            ){
        //DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        DateTime startDt = fmt.parseDateTime(startTime);
        DateTime endDt = fmt.parseDateTime(endTime);

        return platFormDataService.getPlatFormData(startDt,endDt,offset, count);
    }

    @PutMapping("/plat_data/refresh")
    public boolean refreshPlatformData(){
        return platFormDataService.refreshPlatformData();
    }
}
