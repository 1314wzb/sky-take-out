package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 用户登录
     * @param dto
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO dto) {

        //1. 调用service
        UserLoginVO userLoginVO = userService.login(dto);

        //2. 包装数据返回
        return Result.success(userLoginVO);
    }
}
