package com.itheima.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.itheima.stock.constant.StockConstant;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : itheima
 * @date : 2022/9/19 16:23
 * @description :
 */
@Service("userService")
@Slf4j // day2新增
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // idworkder,redistemplate day2新增
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserMapper.getUserByUserName(userName);
    }

    /**
     * 用户登录功能
     *
     * @param reqVo
     * @return
     */
  /*  @Override
    public R<LoginRespVo> login(LoginReqVo reqVo) {
        //判断输入参数的合法性
        if (reqVo==null || StringUtils.isBlank(reqVo.getUsername()) || StringUtils.isBlank(reqVo.getPassword())|| StringUtils.isBlank(reqVo.getCode())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //根据用户名查询用户信息
        SysUser dbUser = sysUserMapper.getUserByUserName(reqVo.getUsername());
        //判断用户是否存在
        if(dbUser == null){
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }

        //3. 调用密码匹配器 匹配输入的明文密码和密文密码   左边参数原始密码 右边加密密码
        if(!passwordEncoder.matches(reqVo.getPassword(),dbUser.getPassword())){
            return  R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }


        //4. 响应
        LoginRespVo respVo = new LoginRespVo();
//        respVo.setId(dbUser.getId());
//        respVo.setNickName(dbUser.getNickName());
//        respVo.setPhone(dbUser.getPhone());
//        respVo.setUsername(dbUser.getUsername());
        //我们发现LoginRespVo与SysUser  对象 属性名和类型一致
        // 必须保证属性名和类型一致才能 使用  BeanUtils.copyProperties
        BeanUtils.copyProperties(dbUser,respVo);
        return R.ok(respVo);
    }*/

    // day2-06版本 login
    @Override
    public R<LoginRespVo> login(LoginReqVo reqVo) {
        //判断输入参数的合法性
        if (reqVo == null || StringUtils.isBlank(reqVo.getUsername()) || StringUtils.isBlank(reqVo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR);
        }

        // 判断输入的验证码是否存在
        if (StringUtils.isBlank(reqVo.getCode()) || StringUtils.isBlank(reqVo.getSessionId())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        // 判断输入的验证码和redis中保存的验证码是否相同（比较时，忽略大小写）
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + reqVo.getSessionId());

        if (StringUtils.isBlank((redisCode))) {
            // 验证码过期
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }

        if (!redisCode.equalsIgnoreCase((reqVo.getCode()))) {
            // 验证码错误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }

        //根据用户名查询用户信息
        SysUser dbUser = sysUserMapper.getUserByUserName(reqVo.getUsername());
        //判断用户是否存在
        if (dbUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }

        //3. 调用密码匹配器 匹配输入的明文密码和密文密码   左边参数原始密码 右边加密密码
        if (!passwordEncoder.matches(reqVo.getPassword(), dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }


        //4. 响应
        LoginRespVo respVo = new LoginRespVo();

        BeanUtils.copyProperties(dbUser, respVo);
        return R.ok(respVo);
    }


        @Override
        public R<Map> getCaptchaCode () {
            // 1. 生成图片验证码
            //宽、高、验证码长度、干扰线数量
            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
            // 设置背景颜色
            captcha.setBackground(Color.LIGHT_GRAY);
            //自定义生成的校验码规则
            //自定义校验码生成方式
//        captcha.setGenerator(new CodeGenerator() {
//            @Override
//            public String generate() {
//                return RandomStringUtils.randomNumeric(4);
//            }
//            @Override
//            public boolean verify(String code, String userInputCode) {
//                return code.equalsIgnoreCase(userInputCode);
//            }
//        });

            // 获取验证码
            String checkCode = captcha.getCode();
            //获取经过base64编码处理过的图片数据
            String imageData = captcha.getImageBase64();
            //2.生成sessionid  转化成Sring，避免精度丢失
            String sessionId = String.valueOf(idWorker.nextId());
            log.info("当前生成的图片校验码：{}，会话id：{}", checkCode, sessionId);
            //3.将sessionid作为key，校验码作为value，保存在redis中
        /*
        使用redis模拟session行为，通过过期时间设置
        加ck 前缀 好统计
         */
            redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX + sessionId, checkCode, 5, TimeUnit.MINUTES);
            //4.组装数据
            Map<String, String> data = new HashMap();
            data.put("imageData", imageData);
            data.put("sessionId", sessionId);
            log.info("data={}", data);
            //5.响应数据
            return R.ok(data);
        }



}
