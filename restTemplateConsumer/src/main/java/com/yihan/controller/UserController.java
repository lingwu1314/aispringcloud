package com.yihan.controller;

import com.yihan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/userController")
public class UserController {

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping("/getAllUser")
    public List<User> getAllUser() {
        String url = "http://localhost:8010/userController/findAll";

        return restTemplate.getForEntity(url, List.class).getBody();

    }

    @RequestMapping("/getAllUser2")
    public List<User> getAllUser2() {
        String url = "http://localhost:8010/userController/findAll";
        return restTemplate.getForObject(url, List.class);
    }

    @GetMapping("/findById/{id}")
    public User findById(@PathVariable("id") Long id) {
        String url = "http://localhost:8010/userController/getUserById/{id}";
        return restTemplate.getForEntity(url, User.class, id).getBody();
    }


    @GetMapping("/findById2/{id}")
    public User findbyId2(@PathVariable("id") Long id) {
        String url = "http://localhost:8010/userController/getUserById/{id}";
        return restTemplate.getForObject(url, User.class);
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        String url = "http://localhost:8010/userController/deleteUser/{id}";
        restTemplate.delete(url);
    }
    @PutMapping("/saveUpdUser")
    public void saveUpdUser(@RequestBody User user){
        String url = "http://localhost:8010/userController/saveUpdUser";
        restTemplate.put(url,user);
    }
}
