package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/*
股票服务接口
 */
public interface StockService {

    public R<List<InnerMarketDomain>> getInnerMarketInfo();  //大盘数据


    /**
     * 需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    R<List<StockBlockDomain>> sectorAllLimit();

    /*
    分页查询最新的股票交易数据

     */
    R<PageResult<StockUpdownDomain>>  getStockInfoByPage(Integer page,Integer pageSize);


    /*
    股票涨幅榜   作业
     */
    R<List<StockUpdownDomain>>  getIncreaseStocks();



    R<Map<String,List>> getStockUpDownCount();


    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    /*
       t t-1 日 成交量对比功能   视频版本   但是前端页面上 该部分加载不出来
     */
    R<Map<String,List>> getComparedStockTradeAmt();



    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）   md文件版本
     * @return
     */
    R<Map> stockTradeVol4InnerMarket();



    /**
     * md 文件版本
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    R<Map> stockUpDownScopeCount();



    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code);

    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    R<List<Stock4EvrDayDomain>> stockCreenDkLine(String code);



}
