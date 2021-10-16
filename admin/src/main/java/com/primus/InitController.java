package com.primus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/application")
@RestController
public class InitController {

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    private ResponseEntity<String> checkLogin()
    {
        ResponseEntity entity =  new ResponseEntity<String>("Hello world", HttpStatus.OK);
        return  entity;
    }
}
