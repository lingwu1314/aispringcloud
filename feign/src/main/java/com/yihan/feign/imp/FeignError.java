package com.yihan.feign.imp;

import com.yihan.entity.User;
import com.yihan.feign.FeignProviderClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeignError implements FeignProviderClient {
    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public String index() {
        return "服务器在维护中";
    }
}
