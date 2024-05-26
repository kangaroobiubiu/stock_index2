package com.itheima.stock.controller;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author : itheima
 * @date : 2022/9/19 16:18
 * @description : 定义访问用户的服务接口
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @GetMapping("user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String userName){
        return userService.getUserByUserName(userName);
    }

    /**
     * 用户登录功能
     * @param reqVo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo reqVo){

        return userService.login(reqVo);

    }

    /*
   生成图片验证码
   controller 接收参数，相应数据
   复杂的逻辑交给 service
    */
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode() {
        return userService.getCaptchaCode() ;
    }




}
