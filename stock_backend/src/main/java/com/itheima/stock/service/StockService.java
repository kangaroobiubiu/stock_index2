package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
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


}
