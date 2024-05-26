package com.itheima.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/*
大盘数据
约定从数据库查询的数据如果来自多张表或者单表的部分字段，则封装到domain实体类下；
 */
@ApiModel("大盘数据")
public class InnerMarketDomain {

  @ApiModelProperty("大盘编码")
  private String code ;//大盘编码
  private String name ;//指数名称
  private BigDecimal openPoint ;//开盘点
  private BigDecimal curPoint;//当前点
  private BigDecimal preClosePoint;//前收盘点
  private Long tradeAmt;//交易量
  private BigDecimal tradeVol;//交易金额
  private BigDecimal upDown;//涨跌值
  private BigDecimal rose;//涨幅
  private BigDecimal amplitude;//振幅

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
  private BigDecimal curTime;//当前时间



}
