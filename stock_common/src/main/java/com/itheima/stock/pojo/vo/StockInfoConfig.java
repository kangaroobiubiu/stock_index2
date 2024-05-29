package com.itheima.stock.pojo.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

// Data注解 自动配置 get set方法
@Data
@ConfigurationProperties(prefix = "stock")

public class StockInfoConfig {

    // day2 14

    //A股大盘ID集合
    private List<String> inner;
    //外盘ID集合
    private List<String> outer;

    //股票区间
    private List<String> upDownRange;

}
