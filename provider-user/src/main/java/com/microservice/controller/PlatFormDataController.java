package com.microservice.controller;
import com.microservice.entity.PlatFormData;
import com.microservice.service.PlatFormService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cmonkey on 3/20/17.
 */
@RestController
public class PlatFormDataController {

    private final static Logger logger = LoggerFactory.getLogger(PlatFormDataController.class);

    @Resource
    PlatFormService platFormService;

    @GetMapping("/getPlatFormData/{isDb}/{startTime}/{endTime}")
    public List<PlatFormData> getPlatFormData(
            @RequestParam(value = "isDb") boolean isDb,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime){
        //DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTime startDt = fmt.parseDateTime(startTime);
        DateTime endDt = fmt.parseDateTime(endTime);

        return platFormService.getPlatFormData(isDb,startDt,endDt);
    }
}