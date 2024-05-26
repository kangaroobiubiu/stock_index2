package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.SysRole;

/**
* @author 46035
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2022-09-19 15:48:56
* @Entity com.itheima.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

}
