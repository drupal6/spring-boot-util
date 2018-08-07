package com.spring.boot.web.springbootweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("hello1")
    public String hello1() {
        return "page/hello1";
    }

    @RequestMapping("hello2")
    public String hello2(Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hello");
        map.put("age", 18);
        model.addAttribute("info", map);
        return "page/hello2";
    }
}
