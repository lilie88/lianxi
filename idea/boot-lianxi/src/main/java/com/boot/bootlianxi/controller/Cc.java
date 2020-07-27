package com.boot.bootlianxi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public Object ll(){

        return "nihao";
    }
    @RequestMapping("/he")
    public Object ll(Model model){
        model.addAttribute("name","张明");
        return "hh";
    }
}
