package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "店铺接口")
@RequestMapping("/admin/shop")
@RestController
public class ShopController {




    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺状态
     * @param status
     * @return
     */
    @ApiOperation("设置店铺状态")
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){

        //存储到redis里面去
        redisTemplate.opsForValue().set(MessageConstant.SHOP_STATUS,status);
        return Result.success();

    }


    /**
     * 获取店铺状态
     * @return
     */
    @ApiOperation("获取店铺状态")
    @GetMapping("status")
    public Result getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(MessageConstant.SHOP_STATUS);
        return Result.success(status == null ? 0 : status);
    }
}
