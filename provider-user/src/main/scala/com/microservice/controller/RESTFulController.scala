package com.microservice.controller

import java.util.concurrent.TimeUnit

import com.microservice.entity.User
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RequestParam, RestController}

import scala.beans.BeanProperty

@RestController
@RequestMapping(value = Array("/restful"))
class RESTFulController {

  @GetMapping(value = Array("/api/{id}"))
  def getId(): User = {

    TimeUnit.SECONDS.sleep(2);

    val user = new User()
    user.setUsername("1111")
    user.setId(10L)
    user.setAge(30)

    user
  }
}

case class Message (){
  @BeanProperty
  var id :Int = _

  @BeanProperty
  var name :String = _
}