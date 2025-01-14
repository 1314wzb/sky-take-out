package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.handler.MessageContext;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper  userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    private String url = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 用户登录
     *
     * 1. 使用httpclient发起请求，把code + oppId + secret 一并发送给微信的官方
     *
     * 2. 接收结果：
     *  2.1 判定返回的openid(String)是否有值
     *  2.2 如果openid没有值，表示登录失败
     *      抛出一个登录失败的异常！
     *  2.3 如果openid有值，表示登录成功
     *      2.3.1 根据openid去查询user表的数据
     *          2.3.1.1 如果能查询到user对象，就表示这个用户是老用户了，直接返回user数据
     *          2.3.1.2 如果没有查询到user对象，表示这个用户是新用户
     *              a. 把这个用户信息添加到user表里面去，需要获取主键返回
     *              b. 把这个user设置回去
     * @param dto
     * @return
     */
    @Override
    public UserLoginVO login(UserLoginDTO dto) {

        //1. 发起请求，把code + appId + secret 一并发送给微信的官方
        Map<String,String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", dto.getCode());
        map.put("grant_type","authorization_code");

        //获取微信官方返回的结果  {"session_key":"u7mRbQoT2YZvevmPayI4Cw==","openid":"o2Qsd5QmQyldDXYwxWHo_bp1F_wA"}
        String result = HttpClientUtil.doGet(url,map);

        //打印返回的结果
        System.out.println(result);

        //2. 判断openid，用来决定到底是登录成功了还是没有成功！
        JSONObject jsonObject = JSONObject.parseObject(result);

        //"openid":"o2Qsd5QmQyldDXYwxWHo_bp1F_wA"获取对应的值用json这个类来解析
        String openid = jsonObject.getString("openid");

        //如果为null，表明登录失败！
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //如果不为null,那么就来到这里，即表示登录成功，需要查询数据库，确定这个用户是新用户还是老用户
        User user = userMapper.findByOpenId(openid);
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.add(user);
        }

        //组装UserLoginVO
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);
        UserLoginVO vo = new UserLoginVO(user.getId(),openid,token);

        return vo;
    }
}
