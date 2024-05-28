package com.itheima.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;

import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
股票服务实现
 */
@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {

        //1.获取最新交易时间点 精确到分钟  毫秒设置=0
        //DateTime curDateTime  = DateTimeUtil.getLastDate4Stock(DateTime.now());

        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();

        // mock data 假数据 等后续完成采集工程，再将代码删除
        curDate = DateTime.parse("2022-07-07 14:03:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        // Date curData = curDateTime.toDate();

        //2.大盘编码集合
        List<String> mCodes = stockInfoConfig.getInner();
        //3.调用mapper 查询数据
        List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curDate,mCodes);

        System.out.println("--------------------------\n");
        System.out.println(data);

       //4.封装并且响应
        return R.ok(data);


    }





    /**
     *需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {

        //获取股票最新交易时间点
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock数据,后续删除
        lastDate=DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.调用mapper接口获取数据
        List<StockBlockDomain> infos = stockBlockRtInfoMapper.sectorAllLimit(lastDate);
        //2.组装数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }

    /*
    day3
    分页查询最新股票数据
     */
    @Override
    public  R<PageResult<StockUpdownDomain>>  getStockInfoByPage(Integer page, Integer pageSize) {

        //     1.获取股票最新交易时间（精确到分钟，毫秒设置=0）
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //     mock data   假数据模拟，后序删除
        curDate = DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //     2.设置pagehelp分页参数
        PageHelper.startPage(page, pageSize);
        //     3.调用mapper查询
        List<StockUpdownDomain> pageData = stockRtInfoMapper.getStockInfoByTime(curDate);
        //     4.组装pageresult对象
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageData);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);

        //     5.响应数据
        return R.ok(pageResult);

    }




    public  R<List<StockUpdownDomain>>  getIncreaseStocks() {

        //     1.获取股票最新交易时间（精确到分钟，毫秒设置=0）
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //     mock data   假数据模拟，后序删除
        curDate = DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();


        //     3.调用mapper查询
        List<StockUpdownDomain> pageData = stockRtInfoMapper.getStockInfoByTime2(curDate);


        //     4.响应数据
        return R.ok(pageData);

    }

    public R<Map<String,List>> getStockUpDownCount(){
        //1.获取最新的交易时间范围 openTime  curTime
        // 获取最新股票交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());

        // 假数据
        curDateTime= DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate= curDateTime.toDate();
        //2 获取最新交易时间对应的开盘时间
        Date startDate = DateTimeUtil.getOpenDate(curDateTime).toDate();

        //3.查询涨停数据
        //约定mapper中flag入参： 1-》涨停数据 0：跌停
        List<Map> upList=stockRtInfoMapper.getStockUpdownCount(startDate,endDate,1);
        //3.查询跌停
        List<Map> downList=stockRtInfoMapper.getStockUpdownCount(startDate,endDate,0);
        //4.组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("upList",upList);
        info.put("downList",downList);
        //5.返回结果
        return R.ok(info);


    }






}
