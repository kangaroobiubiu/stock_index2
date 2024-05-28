package com.itheima.stock.controller;


import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/quot")
@Api(value = "api/quot",tags = {"定义股票相关接口控制器"})
public class StockController {

    @Autowired
    private StockService stockService;

    /*
    获取国内大盘最新数据
     */


    @ApiOperation(value = "获取国内大盘最新数据",notes="获取国内大盘最新数据",httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return stockService.getInnerMarketInfo();
    }


    /**
     *需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * 板块指数功能实现【作业】
     * @return
     */
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }


    /*
    分页查询最新股票交易数据
     */
    @GetMapping("/stock/all")
    public  R<PageResult<StockUpdownDomain>> getStockInfoByPage(@RequestParam(value="page",defaultValue = "1",required = false) Integer page,
                                                               @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize){


        return stockService.getStockInfoByPage(page,pageSize);


    }


    /*
       涨幅榜功能实现(作业内容）  涨幅榜前4
       day3 -6 视频中未说明
     */

    @GetMapping("/stock/increase")
    public  R<List<StockUpdownDomain>> increaseStock(){

        return stockService.getIncreaseStocks();


    }

    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpDownCount(){


        return stockService.getStockUpDownCount();

    }








}
