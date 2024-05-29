package com.itheima.stock.controller;


import com.itheima.stock.pojo.domain.*;
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

import javax.servlet.http.HttpServletResponse;
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
    @ApiOperation(value = "板块指数")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }


    /*
    分页查询最新股票交易数据
     */
    @ApiOperation(value = "分页查询 涨幅榜 ")
    @GetMapping("/stock/all")
    public  R<PageResult<StockUpdownDomain>> getStockInfoByPage(@RequestParam(value="page",defaultValue = "1",required = false) Integer page,
                                                               @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize){


        return stockService.getStockInfoByPage(page,pageSize);


    }


    /*
       涨幅榜功能实现(作业内容）  涨幅榜前4
       day3 -6 视频中未说明
     */
    @ApiOperation(value = "首页涨幅榜")
    @GetMapping("/stock/increase")
    public  R<List<StockUpdownDomain>> increaseStock(){

        return stockService.getIncreaseStocks();


    }

    /*
    涨跌停数统计
     */
    @ApiOperation(value = "涨跌停股票数目 统计")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpDownCount(){


        return stockService.getStockUpDownCount();

    }


    /**
     * 将指定页的股票数据导出到excel表下
     * @param response
     * @param page  当前页
     * @param pageSize 每页大小
     */
    @ApiOperation(value = "数据导出excel")
    @GetMapping("/stock/export")
    public void stockExport(@RequestParam(value="page",defaultValue = "1",required = false) Integer page,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize,
                            HttpServletResponse response){
        stockService.stockExport(response,page,pageSize);
    }


    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *  返回的json文件 那里  有点问题  "amtList":
     * @return
     */
    @ApiOperation(value = "成交量对比")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getComparedStockTradeAmt(){
        return stockService.getComparedStockTradeAmt();
    }


    /**
     * md文件版本
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    // @GetMapping("/stock/tradeAmt")
    // public R<Map> stockTradeVol4InnerMarket(){
    //     return stockService.stockTradeVol4InnerMarket();
    // }
    //



    /**
     * md 文件版本
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    @ApiOperation(value = "涨跌幅度统计")
    @GetMapping("/stock/updown")
    public R<Map> getStockUpDown(){
        return stockService.stockUpDownScopeCount();
    }



    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code){
        return stockService.stockScreenTimeSharing(code);
    }

    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @RequestMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getDayKLinData(@RequestParam("code") String stockCode){
        return stockService.stockCreenDkLine(stockCode);
    }










}
