package com.itheima.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author by itheima
 * @Date 2022/1/1
 * @Description 定义访问http服务的配置类
 * 说明：RestTemplate是一个java Http的客户端，可以模拟浏览器的访问行为，获取接口数据；
 */
@Configuration
public class HttpClientConfig {
    /**
     * 定义restTemplate bean
     * @return
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}