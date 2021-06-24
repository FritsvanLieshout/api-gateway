package com.kwetter.frits.apigateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fallback")
public class FallbackController {

    @RequestMapping("/tweet")
    public String fallbackTweetService() {
        return "tweet-service is not available";
    }
}
