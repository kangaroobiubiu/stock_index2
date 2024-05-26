package com.itheima.stock.controller;


import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockservice;

    /*
    获取国内大盘最新数据
     */
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){

    }


}
