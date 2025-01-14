package com.sky.controller.user;


import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "用户菜品接口")
@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {

    @Autowired
    private DishService dishService;


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result list(Long categoryId){
        //1. 创建一个菜品类
        Dish dish = new Dish();

        //2. 设置要查询的ID
        dish.setCategoryId(categoryId);

        //3. 设置查询起售中的菜品
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<DishVO> listWithFlavor = dishService.listWithFlavor(dish);
        return Result.success(listWithFlavor);
    }

}
