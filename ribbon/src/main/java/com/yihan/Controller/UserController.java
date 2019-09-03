package com.yihan.Controller;

import com.yihan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return restTemplate.getForObject("http://provider/userController/findAll", List.class);
    }

    @RequestMapping("/index")
    public String index() {
        return restTemplate.getForObject("http://provider/userController/index", String.class);
    }
}
