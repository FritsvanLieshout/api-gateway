package com.kwetter.frits.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fallback")
public class FallbackController {

    @GetMapping()
    public String fallback(@RequestParam String endpoint) {
        return  endpoint + " service is not available";
    }
}
