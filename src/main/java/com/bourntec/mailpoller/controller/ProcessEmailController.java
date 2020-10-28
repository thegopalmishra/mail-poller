package com.bourntec.mailpoller.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gopal
 *
 */
@RestController
@RequestMapping("/process")
public class ProcessEmailController {


    @GetMapping("/ping")
    public String publish() {
        return "{\"message:\" : \" Server is up and running. \"}";
    }
}