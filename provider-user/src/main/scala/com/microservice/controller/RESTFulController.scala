package com.microservice.controller

import java.time.format.DateTimeFormatter
import java.util.List
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

import com.microservice.entity.{PlatFormData, User}
import com.microservice.service.PlatFormDataService
import lombok.extern.slf4j.Slf4j
import org.joda.time.format.DateTimeFormat
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty

@RestController
@RequestMapping(value = Array("/restful"))
@Slf4j
class RESTFulController @Resource()(val platFormDataService: PlatFormDataService ){

  @GetMapping(value = Array("/api/{id}"))
  def getId(): User = {

    TimeUnit.SECONDS.sleep(2);

    val user = new User()
    user.setUsername("1111")
    user.setId(10L)
    user.setAge(30)

    user
  }

  @PostMapping(value = Array("/getPlatFormData"))
  def getPortalData(@RequestParam(value = "startTime") startTime: String,
                    @RequestParam(value = "endTime") endTime: String,
                    @RequestParam(value = "offset") offset: Int,
                    @RequestParam(value = "count") count: Int
                   ): List[PlatFormData] = {

//    log.info("getPlatFormData params {}, {}, {}, {}", startTime, endTime, offset, count)
    val fmt = DateTimeFormat.forPattern("yyyy-MM-hh HH:mm:ss")

    val startDt = fmt.parseDateTime(startTime)
    val endDt = fmt.parseDateTime(endTime)

    val resultList = platFormDataService.getPlatFormData(startDt, endDt, offset, count)

    resultList
  }
}

case class Message (){
  @BeanProperty
  var id :Int = _

  @BeanProperty
  var name :String = _
}