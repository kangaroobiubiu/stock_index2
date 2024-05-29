package com.itheima.stock.service.impl;


import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


/*
股票服务实现
 */
@Service("stockService")
@Slf4j
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









   public void stockExport(HttpServletResponse response, Integer page, Integer pageSize){
        // 1.获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockInfoByPage(page,pageSize);
       List<StockUpdownDomain> rows = r.getData().getRows();

       // 2. 将数据导出到excel
       //设置响应excel文件格式类型
       response.setContentType("application/vnd.ms-excel");
       //2.设置响应数据的编码格式
       response.setCharacterEncoding("utf-8");
       //3.设置默认的文件名称
       // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系

       try {
           String fileName = URLEncoder.encode("stockRt", "UTF-8");
           //设置默认文件名称：兼容一些特殊浏览器
           response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
           //4.响应excel流
           EasyExcel
                   .write(response.getOutputStream(),StockUpdownDomain.class)
                   .sheet("股票涨幅信息")
                   .doWrite(rows);
       }catch (IOException e){
           log.error("当前页码:{},每页大小:{},当前时间:{},异常信息:{}",page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),e.getMessage());
           //   通知前端异常，然后重试
           response.setContentType("application/json");
           response.setCharacterEncoding("utf-8");
           R<Object> error= R.error(ResponseCode.ERROR);
           try {
               String jsonData = new ObjectMapper().writeValueAsString(error);
               response.getWriter().write(jsonData);
           }catch (IOException ioException){
               log.error("stockexport:响应错误信息失败,时间:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
           }
       }
   }


    @Override
    public R<Map<String, List>> getComparedStockTradeAmt() {
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime tEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());

        // mock data
        tEndDateTime = DateTime.parse("2022-01-03 14:40:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndDate = tEndDateTime.toDate();


        // 开盘时间
        Date tStartDate = DateTimeUtil.getOpenDate(tEndDateTime).toDate();


        // t-1 日
        DateTime preTEndDateTime = DateTimeUtil.getPreviousTradingDay(tEndDateTime);
        // mock data
        preTEndDateTime = DateTime.parse("2022-01-02 14:40:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));


        Date preTEndDate = preTEndDateTime.toDate();

        // 开盘时间
        Date tPreStartDate = DateTimeUtil.getOpenDate(preTEndDateTime).toDate();

        //  调用mapper查询
        List<Map> tData = stockRtInfoMapper.getSumAmtInfo(tStartDate,tEndDate,stockInfoConfig.getInner());
        List<Map> preTData = stockRtInfoMapper.getSumAmtInfo(tPreStartDate,preTEndDate,stockInfoConfig.getInner());
        // 组装数据
        HashMap<String,List> info = new HashMap<>();
        info.put("amtList",tData);
        info.put("yesAmtList",preTData);
        // 响应数据
        return R.ok(info);

    }






    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *   map结构示例：
     *      {
     *       "volList": [{"count": 3926392,"time": "202112310930"},......],
     *       "yesVolList":[{"count": 3926392,"time": "202112310930"},......]
     *      }
     * @return
     */
    @Override
    public R<Map> stockTradeVol4InnerMarket() {
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime lastDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4T = openDateTime.toDate();
        Date endTime4T=lastDateTime.toDate();
        //TODO  mock数据
        startTime4T=DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime4T=DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //1.2 获取T-1日的区间范围
        //获取lastDateTime的上一个股票有效交易日
        DateTime preLastDateTime = DateTimeUtil.getPreviousTradingDay(lastDateTime);
        DateTime preOpenDateTime = DateTimeUtil.getOpenDate(preLastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4PreT = preOpenDateTime.toDate();
        Date endTime4PreT=preLastDateTime.toDate();
        //TODO  mock数据
        startTime4PreT=DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime4PreT=DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2.获取上证和深证的配置的大盘id
        //2.1 获取大盘的id集合
        List<String> markedIds = stockInfoConfig.getInner();
        //3.分别查询T日和T-1日的交易量数据，得到两个集合
        //3.1 查询T日大盘交易统计数据
        List<Map> data4T=stockMarketIndexInfoMapper.getStockTradeVol(markedIds,startTime4T,endTime4T);
        if (CollectionUtils.isEmpty(data4T)) {
            data4T = new ArrayList<>();
        }
        //3.2 查询T-1日大盘交易统计数据
        List<Map> data4PreT=stockMarketIndexInfoMapper.getStockTradeVol(markedIds,startTime4PreT,endTime4PreT);
        if (CollectionUtils.isEmpty(data4PreT)) {
            data4PreT=new ArrayList<>();
        }
        //4.组装响应数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList",data4T);
        info.put("yesAmtList",data4PreT);
        //5.返回数据
        return R.ok(info);
    }






}
