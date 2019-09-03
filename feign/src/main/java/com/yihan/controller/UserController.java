package com.yihan.controller;

import com.yihan.entity.User;
import com.yihan.feign.FeignProviderClient;
import com.yihan.feign.imp.FeignError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private FeignProviderClient feignProviderClient;

    @RequestMapping(value = "/findAll")
    public List<User> findAll(){
        return feignProviderClient.findAll();
    }

    @RequestMapping("/index")
    public String index(){
        return feignProviderClient.index();
    }

}
