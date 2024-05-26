package com.itheima.stock.service.impl;

import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.resp.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/*
股票服务实现
 */

public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {

        //1.获取最新交易时间点 精确到分钟  毫秒设置=0
//        DateTime curDateTime  = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curData = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();

        // mock data 等后续完成采集工程，再将代码删除
        curData = DateTime.parse("2022-12-19 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

//        Date curData = curDateTime.toDate();
        //2.大盘编码集合
        List<String> mCodes = stockInfoConfig.getInner();
        //3.调用mapper 查询数据
       List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curData);
        //4.封装并且响应
        return R.ok(data);


        return null;
    }
}