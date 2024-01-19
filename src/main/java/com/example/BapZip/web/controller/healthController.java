package com.example.BapZip.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthController {
    @GetMapping("/health")
    public String checkHealth ()
    {
        return "I'm healthy!";
    }

}
