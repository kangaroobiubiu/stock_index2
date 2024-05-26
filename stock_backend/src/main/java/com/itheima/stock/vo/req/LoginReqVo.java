package com.itheima.stock.vo.req;

import lombok.Data;

/**
 * @author : itheima
 * @date : 2022/9/19 17:21
 * @description : 登录时请求参数封装vo-value object view-object
 */
@Data
public class LoginReqVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    // 验证码

    private String code;

    /**
     * 保存redis随机码的key，也就是sessionId
     */
    private String sessionId;


//    private String rkey;
}
