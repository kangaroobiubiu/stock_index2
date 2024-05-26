package com.itheima.stock.config;

import com.itheima.stock.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : itheima
 * @date : 2022/9/19 17:35
 * @description : 定义公共的配置类
 */
@Configuration
public class CommonConfig {
    /**
     * 定义密码加密匹配器bean
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
        id 生成器
     */
    @Bean
    public IdWorker idWorker() {
        /*
        参数1：机器id
        参数2：机房id
        机房和机器编号一般由运维人员进行唯一规划   day2-03
         */
        return new IdWorker(1l,2l);
    }


}
