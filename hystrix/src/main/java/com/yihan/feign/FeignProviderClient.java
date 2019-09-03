package com.yihan.feign;

import com.yihan.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "provider")
public interface FeignProviderClient {

    @GetMapping("/userController/findAll")
    public List<User> findAll();

    @RequestMapping("/userController/index")
    public String index();
}
