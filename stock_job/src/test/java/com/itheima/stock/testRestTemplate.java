package com.itheima.stock;

import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockTimerTaskService;
import com.itheima.stock.service.impl.StockTimerTaskServiceImpl;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @Author: wxl-kangaroobiubiu
 * @Date: 2024/5/30 11:16
 * @Description:
 */
@SpringBootTest
public class testRestTemplate {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Test
    public void testMarket(){
        stockTimerTaskService.getInnerMarketInfo();
    }


    @Test
    public void testMarket2(){

        stockTimerTaskService.getStockRtIndex();  // 个股数据
    }

}
