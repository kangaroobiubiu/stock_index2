package com.itheima.stock.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({StockInfoConfig.class})  // 在公共配置类中(开启)加载实体VO对象：
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




    /**
     * 构建缓存bean
     * @return
     */
    @Bean
    public Cache<String,Object> caffeineCache(){
        Cache<String, Object> cache = Caffeine
                .newBuilder()
                .maximumSize(200)//设置缓存数量上限
//                .expireAfterAccess(1, TimeUnit.SECONDS)//访问1秒后删除
//                .expireAfterWrite(1,TimeUnit.SECONDS)//写入1秒后删除
                .initialCapacity(100)// 初始的缓存空间大小
                .recordStats()//开启统计
                .build();
        return cache;
    }


}
