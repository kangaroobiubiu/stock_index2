package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 46035
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2022-09-19 15:48:56
* @Entity com.itheima.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /*
    查询指定时间点下的股票数据集合
     */
    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);


    List<StockUpdownDomain> getStockInfoByTime2(@Param("curDate") Date curDate);

    /*
    指定日期范围内的涨跌停
     */
    List<Map> getStockUpdownCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flag") int flag);

    List<Map> getSumAmtInfo(@Param("openDate")Date openDate,@Param("endDate")Date endDate,@Param("marketCodes")List<String> marketCodes);
    // 这里报红不用理会 day4-03
}
