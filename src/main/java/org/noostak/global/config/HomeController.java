package org.noostak.global.config;


import org.springframework.web.bind.annotation.GetMapping;


public class HomeController {

    @GetMapping("/actuator/health")
    public String home() {
        return "Hello, World!";
    }
}
