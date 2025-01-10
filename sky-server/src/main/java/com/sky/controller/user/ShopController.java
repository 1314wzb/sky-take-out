package com.sky.controller.user;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.handler.MessageContext;

@Api(tags = "店铺接口")
@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {


    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取店铺状态
     * @return
     */
    @ApiOperation("获取店铺状态")
    @GetMapping("/status")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(MessageConstant.SHOP_STATUS);
        return Result.success(status);
    }
}
