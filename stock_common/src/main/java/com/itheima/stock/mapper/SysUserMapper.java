package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
* @author 46035
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2022-09-19 15:48:56
* @Entity com.itheima.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    SysUser getUserByUserName(@Param("userName") String userName);
}
