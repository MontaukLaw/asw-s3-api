package com.wulala.awss3api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String rootTest() {
        return "Go fuck yourself!";
    }


}
