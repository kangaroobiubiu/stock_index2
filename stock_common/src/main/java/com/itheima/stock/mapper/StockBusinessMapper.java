package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.StockBusiness;

import java.util.List;

/**
* @author 46035
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2022-09-19 15:48:56
* @Entity com.itheima.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);



    /**
     * 获取所有股票的code
     * @return
     */
    List<String> getStockIds();

}
