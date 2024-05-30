package com.itheima.stock.service.impl;

import com.google.common.collect.Lists;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import com.itheima.stock.pojo.entity.StockRtInfo;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockTimerTaskService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.utils.ParseType;
import com.itheima.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("stockTimerTaskService")
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;



    //注入格式解析bean
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;



    @Autowired
    private RabbitTemplate rabbitTemplate;


    /*
     优化：请求头统一配置
      初始化StockTimerTaskServiceImpl 这个bean时，初始化这个属性---涉及bean的生命周期
     */
    private  HttpEntity<Object>  entity;



   /* @Override
    public void getInnerMarketInfo() {
        // 1.定义采集的url接口
        String url=stockInfoConfig.getMarketUrl() + String.join(",",stockInfoConfig.getInner());
        // 2.调用restTemplate采集数据
        // 2.1 组装请求头
        HttpHeaders headers = new HttpHeaders();   // 注意选择spring的
        // 必须填写，否则数据采集不到
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        // 2.2 组装请求对象
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // 发起请求
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
        int statusCode = responseEntity.getStatusCodeValue();
        if(statusCode!=200){
            // 请求失败

            log.error("当前时间点:{}，数据采集失败,http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCode);
            return;
        }
        // js格式数据
        String jsData = responseEntity.getBody();
        log.info("当前时间点:{}，数据采集内容:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),jsData);
        }



*/


    /*
     md 文件版本的 获取数据成功
     采集成功。
     */
    @Override
    public void getInnerMarketInfo() {
        //1.定义采集的url接口
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
        //2.调用restTemplate采集数据


        //2.1 组装请求头
        /*
        HttpHeaders headers = new HttpHeaders();
        //请求头 必须填写，否则数据采集不到
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        //2.2 组装请求对象
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        */



        //2.3 resetTemplate发起请求
        String resString = restTemplate.postForObject(url, entity, String.class);
        log.info("当前时间点:{}，数据采集内容:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), resString);
        // log.info("当前采集的数据：{}",resString);
        //3.数据解析（重要）
//        var hq_str_sh000001="上证指数,3267.8103,3283.4261,3236.6951,3290.2561,3236.4791,0,0,402626660,398081845473,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2022-04-07,15:01:09,00,";
//        var hq_str_sz399001="深证成指,12101.371,12172.911,11972.023,12205.097,11971.334,0.000,0.000,47857870369,524892592190.995,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2022-04-07,15:00:03,00";
        String reg = "var hq_str_(.+)=\"(.+)\";";
        //编译表达式,获取编译对象
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(resString);
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        //判断是否有匹配的数值
        while (matcher.find()) {
            //获取大盘的code
            String marketCode = matcher.group(1);
            //获取其它信息，字符串以逗号间隔
            String otherInfo = matcher.group(2);
            //以逗号切割字符串，形成数组
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName = splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //组装entity对象  需要在 StockMarketIndexInfo  配置   不然无法build
            // @NoArgsConstructor
            // @AllArgsConstructor
            // @Builder
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            //收集封装的对象，方便批量插入
            list.add(info);
        }
        log.info("正则匹配后，采集的大盘数据：{}",list);
    //     调用mapper 批量入库
        int cnt = stockMarketIndexInfoMapper.insertBatch(list);
        if(cnt>0){
            //大盘数据采集完成后,通知backend工程刷新缓存
            //通知后台终端刷新本地缓存，发送日期对象，接收方通过接收的日期与当前日期比对，能判断出数据延迟的时长，用于运维通知处理。
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());

            log.info("当前时间:{},插入数据:{},成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }else {
            log.error("当前时间:{},插入数据:{},失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }


    }









    /**
     * 批量获取股票分时数据详情信息
     * http://hq.sinajs.cn/list=sz000002,sh600015
     */
    @Override
    public void getStockRtIndex() {
        //批量获取股票ID集合
        List<String> stockIds = stockBusinessMapper.getStockIds();
        //计算出符合sina命名规范的股票id数据
        stockIds = stockIds.stream().map(id -> {
            return id.startsWith("6") ? "sh" + id : "sz" + id;
        }).collect(Collectors.toList());



        //设置公共请求头对象
        //设置请求头数据
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        */



        //一次性查询过多，我们将需要查询的数据先进行分片处理，每次最多查询20条股票数据
        Lists.partition(stockIds,15).forEach(list->{
            // 这里是串行的  会有延迟   需要多线程优化



            //拼接股票url地址
            String stockUrl=stockInfoConfig.getMarketUrl()+String.join(",",list);
            //获取响应数据  原始数据
            String result = restTemplate.postForObject(stockUrl,entity,String.class);
            // 调用工具类 解析后的数据
            List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
            log.info("采集个股数据:{},数据量:{}",infos,infos.size());   // 到这里测试成功


            //TODO 批量插入 day5-16
            int cnt = stockRtInfoMapper.insertBatch(infos);

            if(cnt>0){
                log.info("当前时间:{},插入个股数据:{},成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
            }else {
                log.error("当前时间:{},插入个股数据:{},失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
            }


        });





    }




    /*
    bean生命周期初始化回调方法
     */
    @PostConstruct
    public void initData(){

        //设置公共请求头对象
        //设置请求头数据
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        entity = new HttpEntity<>(headers);

    }


}