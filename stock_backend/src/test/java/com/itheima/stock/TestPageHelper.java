package com.itheima.stock;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: wxl-kangaroobiubiu
 * @Date: 2024/5/28 13:33
 * @Description:
 */

@SpringBootTest
public class TestPageHelper {

    @Autowired
    private SysUserMapper sysUserMapper;

    /*
    测试分页
     */
    @Test
    public  void test01(){

        Integer page=2; // 当前页
        Integer pageSize=2; // 每页大小
        PageHelper.startPage(page,pageSize);
        List<SysUser> all = sysUserMapper.findAll();
        // 将查询的page对象封装到PageInfo下可以获取分页的各种数据
        PageInfo<SysUser> pageInfo = new PageInfo<>(all);
        // 获取分页详情数据
        int pageNum = pageInfo.getPageNum();// 当前页
        int pages = pageInfo.getPages(); // 总页数
        int pageSize1= pageInfo.getPageSize();//每页大小
        int size=pageInfo.getSize();//当前页的记录数
        Long total = pageInfo.getTotal();// 总记录数
        List<SysUser> list = pageInfo.getList();//获取当前页内容

        System.out.println(all);
    }

}
