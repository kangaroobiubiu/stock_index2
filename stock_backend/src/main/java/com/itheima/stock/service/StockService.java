package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.vo.resp.R;

import java.util.List;

/*
股票服务接口
 */
public interface StockService {
    public R<List<InnerMarketDomain>> getInnerMarketInfo();

}
