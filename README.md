# aispringcloud
spring cloud 基础集成



1, npm init -y 初始化项目
2, npm i webpack -D   全局安装webpack
3, npm  i webpack -cli   打包命令
4,





2019-08-31
单体引用的存在的问题
1，随着业务的房展，开发会变得越来越复杂。
2，修改，新增某个功能，需要对整个系统进行测试，重新部署。
3，一个模块出现问题，可能导致整个系统奔溃。
4，多个系统对数据进行管理，容易产生安全漏洞。
5，模块内容过于复杂，如果员工离职，可能需要很长时间才能完成工作的交接。


分布式， 集群
集群：一台服务器无法负荷高并发的数据访问量，那么久设置10台服务器一起来分担压力，10台不行就饿设置成100台服务器（物理层面的），很多人干同一件事情，分摊复负荷。
分布式：将一个复杂问题拆分成若干个简单的小问题，（软件设计层面的）


springcloud的核心组件
1，服务治理 eureka
2,服务通信   Ribbion
3，服务通信  feign
4,服务网关， zuul
5,服务容错  hystrix
6,服务配置  Config
7,服务监控 Actuaor
8，服务跟踪  zipkin


eureka
服务提供者，  服务消费者，注册中心
在分布式系统中，每个微服务将自己的信息存储到注册中心，叫做服务注册。
服务消费者从注册中心货物服务提供者的网络信息，通过该信息调用服务， 叫做服务发现。
spring cloud的服务治理使用的eureka来实现的，eureka是netfix开源的基于Rest的服务治理解决方案，

Sptring cloud eureka
eureka server 注册中心
eureka client 所有要进行注册附图通过这个注册到eureka。


1, 创建一个父工程 project   pom依赖组件的代码实现；
    <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.yihan.aispringcloud</groupId>
    <artifactId>aispringcloud</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>eurekaserver</module>
    </modules>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.1.6.RELEASE</version>
        </dependency>
        <!--jaxb-->
        <dependency>
            <groupId>com.sun.xml.bind</groupId>
            <artifactId>jaxb-impl</artifactId>
            <version>2.2.11</version>
        </dependency>
        <dependency>
            <groupId>com.sun.xml.bind</groupId>
            <artifactId>jaxb-core</artifactId>
            <version>2.2.11</version>
        </dependency>
<!--龙目岛自动生成get set  constructor-->
<dependency>
    	<groupId>org.projectlombok</groupId>
    	<artifactId>lombok</artifactId>
    	<version>1.18.6</version>
    	<scope>provided</scope>
    </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.SR2</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
	
Eureka server的实现代码

2,在父工程下面创建module. pom.xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
    </dependencies>	
  创建配置文件 application.yml	添加eureka相关的配置信息
server:
  port: 8761  当前的eureka的端口
eureka:
  client:
    register-with-eureka: false   是否将当前eureka当做客户端注册
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka     eureka的访问地址
	  
3，创建启动类


package com.yihan;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServerEurekaApplication {
    public static void main(String[] args) {

        SpringApplication.run(ServerEurekaApplication.class,args);
    }
}
	  

注解说明	  
@SpringBootApplication  :声明该类是一个spring boot的服务入口
@EnableEureka:声明该类是一个eureka server微服务， 提供服务注册和服务发现功能，即注册中心


Eureka Client代码实现
创建Module , pom.xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>
</dependencies>

创建配置文件 application.yml 添加Eureka Client的相关配置

server:
  port: 8763
spring:
  application:
    name: provider
eureka:
  client:
    service-url:
    defaultZone: http://127.0.0.1:8761/eureka/
  instance:
    prefer-ip-address: true
属性说明

  spring.application.name :当前服务注册到eureka Server上的名称
  eureka.client.service-url.defaultZone:注册中心的访问地址
  eureka.instance.prefer-ip-address:  是否将当前服务的ip注册到Eureka Server

创建启动类

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrividerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrividerApplication.class,args);
    }
}



@RestController
@RequestMapping("/userController")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;

    @GetMapping("/findAll")
    public List<User> findAll(){
        return  userServiceImp.findAll();
    }

    @RequestMapping("/getUserById/{id}")
    public User getUserById(@PathParam("id") long id){
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


}


RestTemplate的使用
RestTemplate:  是spring框架提供的基于Rest的服务组件，底层是对于HTTP请求级响应进行了分装，提供了许多访问Rest服务的方法，可以简化代码的开发；

如何使用RestTemplate
1,创建maven工程mdule, pom.xml
2，创建实体类

  package com.yihan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private  Long  useId;

    private String userName;

    private Integer age;

}



3，创建Controller

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


4,启动类，将RestTemplate 注入到spring容器中

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class,args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }
}


服务消费者

1创建一个maven工程  pom.xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.0.2.RELEASE</version>
</dependency>

2,创建配置文件 application.yml

server:
  port: 8030
spring:
  application:
    name: consumer
eureka:
  instance:
    prefer-ip-address: true
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

3,启动类


package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public RestTemplate getRestemplate() {
        return new RestTemplate();
    }
}


服务网关

  spring cloud集成了zuul组件， 实现了服务网关
   什么是zuul
zuul是netflix提供的一个开源的api网关服务器， 是客户端和网站后端所有请求的中间件，对外开放一个api,将所有的
请求导入统一的入口，屏蔽了服务器的具体实现逻辑，zuul可以实现反向代理的功能，在网关内部实现动态路由。身份认证， ip过滤，数据监控等。

创建一个maven工程  pom.xml


<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.0.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    <version>2.0.2.RELEASE</version>
</dependency>

配置文件 application.yml

server:
  port: 8040
spring:
  application:
    name: gateway
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
zuul:
  routes:
    provider: /p/**

属性说明：
zuul.routes.provier: 给服务提供者provider来设置映射

创建启动类；

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableAutoConfiguration
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class,args);
    }
}

注解说明
@EnableZuulProxy: 包含过了@EnableZuulServer，设置该类是网关的启动类

@EnableAutoConfiguration: 可以帮助spring boot应用将所有的符合条件的@Configuration配置加载到当前Spring Boot创建并使用IOC容器中
	  
zuul自带了负载均衡的功能。修改provider 通过端口号，测试负载均衡

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


Ribbon负载均衡

Ribbon: spring cloud的一个组件，负责负载均衡的， Ribbon是netfliex发布的负载均衡器。 Spring Cloud Ribbon是
基于NetFlix Ribbon实现的，是一个用户http请求进行控制的负载均衡客户端。
在注册中心的对Ribbon注册后，Ribbon就可以基于某种负载均衡算法，如轮询，随机，加权轮询，加权随机等自动获取服务消费者调用接口，开发者可以根据具体需求自定义Ribbon负载均衡算法。实际开发中，Spring Cloud Ribbon需要结合Spring Cloud Eurek来使用。
Eureka Server 提供所以可以调用的服务提供者列表，Ribbon基于特定的负载均衡算法从这些服务提供者中选择要调用的具体实例

创建一个module pom.xml


server:
  port: 8060
spring:
  application:
    name: ribbon
eureka:
  client:
    service-url:
      defaultZone: http:localhost:8761/eureka
  instance:
    prefer-ip-address: true


创建启动类

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate getResttemplate() {
        return new RestTemplate();
    }
}

@LoadBalanced :声明一个基于Ribbon的负载均衡。


Controller的创建

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


provider：微服务的应用名

Fegin的使用

fegin: 和Ribbon一样，Feign也是netFlix提供的，Fegin是一个声明是，模版化的web service客户端，它简化了开发者编写Web服务客户端的操作，开发者可以通过简单的接口和注解来调用HTTP api, Spring cloud feign, 它整合了Ribbon和Hystrix，具有可插拔，负责均衡，服务熔断等一系列的便捷功能。 
相比较于 Ribbon+Restemplate的方式，Feign大大简化了代码的开发，Feign支持多种注解，包括Feign注解，Jax-RS注解，Spring MVC注解等，Spring cloud 对Feign进行了优化，整合了Ribbon和Eureka,从而让feign的使用更加方便。

Ribbon和Feign区别
Ribbon是一个通用的HTTP客户端工具，Feign是基于Ribbon实现的。

Feign的特点
1，Feign是一个声明式的web service客户端。
2，支持Feign注解，Spring mvc注解，JAX-RS注解。
3，Feign是基于Ribbon实现的，使用起来更加简单。
4，Feign集成了HyStrix，具备服务熔断功能。

创建一个module pom.xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>
</dependencies>

创建配置文件 applicaton.yml

server:
  port: 8050
spring:
  application:
    name: feign
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true

注解解释： feign.hystrix.enabled 开启熔断机制。


创建启动类


package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class,args);
    }
}

创建声明式接口

package com.yihan.feign;

import com.yihan.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "provider")   //微服务的应用名称
public interface FeignProviderClient {

    @GetMapping("/userController/findAll")    //提供的服务的请求的 RequestMapping("/")的路径，请求路径
    public List<User> findAll();

    @RequestMapping("/userController/index")
    public String index();
}


Controller 直接注入接口；

package com.yihan.controller;

import com.yihan.entity.User;
import com.yihan.feign.FeignProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private FeignProviderClient feignProviderClient;

    @RequestMapping("/findAll")
    public List<User> findAll(){
        return feignProviderClient.findAll();
    }

    @RequestMapping("/index")
    public String index(){
        return feignProviderClient.index();
    }

}



服务熔断。 application.yml添加熔断机制

server:
  port: 8050
spring:
  application:
    name: feign
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true

注解解释： feign.hystrix.enabled 开启熔断机制。

在FeignProviderClient接口的实现类FeignError,定义熔断处理逻辑，通过@Component注解将 FeignError注入到ioc中


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

在FeignProviderClient定义处通过@FeignClient的fallback属性设置映射


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



Hystrix的容错机制

在不改变各个微服务调用关系的前提下，针对错误情况进行预先处理。

设计原则
1，服务隔离机制
2，服务降级机制
3，熔断机制
4，提供实时的监控和报警功能，
5，提供实时的配置修改功能。

Hystrix数据监控需要结合Sping boot Actuator 来使用，Actuator提供了对服务的健康监控，数据统计，可以通过hystrix.stream节点获取监控的请求数据，提供了可视化的监控界面。

创建module pom.xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
        <version>2.0.7.RELEASE</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        <version>2.0.2.RELEASE</version>
    </dependency>
</dependencies>

创建配置文件 application.yml

server:
  port: 8060
spring:
  application:
    name: hystrix
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: 'hystrix.stream'


创建启动类

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrixDashboard
public class HystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class,args);
    }
}


注解说明：
@EnableCircuitBreaker:声明启用数据监控

@EnableHystrixDashboard:  声明启用可视化数据监控



Controller


package com.yihan.Controller;

import com.yihan.entity.User;
import com.yihan.feign.FeignProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    @Autowired
    FeignProviderClient feignProviderClient;

    @RequestMapping("/findAll")
    public List<User> findAll(){
        return feignProviderClient.findAll();
    }

    @RequestMapping("/index")
    public String index(){
        return feignProviderClient.index();
    }
}


可视化界面的路径：http://localhost:8060/hystrix    
：可以看到可视化的监控界面，输入要监控的地址节点即可看到该节点的可视化数据监控。


actuator的路径：http://localhost:8060/actuator/hystrix.stream  启动成功后，在这个界面可以监控到请求数据

Spring Cloud配置中心

spring cloud config 通过服务端可以为多个组件提供配置方服务。 Spring Cloud Config可以配置到文件存储在本地，也可以将配置文件存储在远程的GIT仓库中。创建Config Server, 通过它可以管理配置所有的配置文件。

本地文件系统

创建maven工程，  pom.xml的

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
    <version>2.0.2.RELEASE</version>
</dependency>

创建application.yml  (注意：这个不可以通过 application-dev.yml文件 添加后 通过 spring.profiles.active: dev 来加入)

server:
  port: 8762
spring:
  application:
    name: nativeconfigserver
  profiles:
    active: native
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared

注解的说明： 
profiles.active: 配置文件的获取方式
cloud.config.server.native.search-locations:本地配置文件的路劲

在resouces路径下创建shared文件夹，并且在此路径下创建configclient-dev.yml.

server:
  port:8070
foo: foo version 1

创建启动类

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class NativeConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(NativeConfigApplication.class, args);
    }
}

注解说明：
@EableConfigServer 声明是一个配置中心。



创建一个客户端读取配置中心的文件

创建一个module  pom.xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
    <version>2.0.2.RELEASE</version>
</dependency>


创建 bootstrap.yml 配置读取本地配置中心的配置信息

spring:
  application:
    name: configclient
  profiles:
    active: dev
  cloud:
    config:
      uri: http://localhost:8762
      fail-fast: true

注解说明：
cloud.config.uri:本地config server的访问路径
cloud.config.fail-fase: 设置客户端优先判断config server获取是否正常。

通过spring.application.name.结合spring.profiles.active拼接目标配置文件名，confingclient-dev.yml,去
config server中查找该文件

创建启动类

package com.yihan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NativeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NativeApplication.class,args);
    }
}


spring cloud config 远程仓库 GIT

1,文件传到git仓库中去。






















	
	




