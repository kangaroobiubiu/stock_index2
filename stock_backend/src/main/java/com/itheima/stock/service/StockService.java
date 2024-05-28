package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;

import java.util.List;

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

}
