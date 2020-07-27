package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName cc
 * @Description TODO
 * @Author LiBinWei
 * @Date 2020/6/20 21:06
 * @Version 1.0
 **/
//@RestController
@Controller
@RequestMapping("/user")
public class Cc {

//    @Autowired
//    StringRedisTemplate redisTemplate;

    @GetMapping("/hello")
    @ResponseBody
    public Object ll(){

        return "ni";
    }
    @RequestMapping("/he")
    public Object ll(Model model){
        model.addAttribute("name","张明");
        return "hh";
    }
}
