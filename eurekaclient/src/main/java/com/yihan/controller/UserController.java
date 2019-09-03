package com.yihan.controller;

import com.yihan.entity.User;
import com.yihan.service.impl.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/userController")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;

    @Value("${server.port}")
    private String port;

    @GetMapping("/findAll")
    public List<User> findAll(){
        return  userServiceImp.findAll();
    }

    @RequestMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") long id){
        return userServiceImp.findUserById(id);
    }

    @RequestMapping("/saveUpdUser")
    public void saveUpdUser(@RequestBody User user){
        userServiceImp.saveOrUpdateUser(user);
    }

    @RequestMapping("/deleteUser")
    public void deleteUser(@RequestBody User user){
        userServiceImp.deleteUserById(user);
    }

   @RequestMapping("/index")
    public String index(){
        return "端口号是"+this.port;
   }
}
