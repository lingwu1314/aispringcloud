package com.yihan.feign;

import com.yihan.entity.User;
import com.yihan.feign.imp.FeignError;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "provider",fallback = FeignError.class)
public interface FeignProviderClient {

    @GetMapping("/userController/findAll")
    public List<User> findAll();

    @RequestMapping("/userController/index")
    public String index();
}
