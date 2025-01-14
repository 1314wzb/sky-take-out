package com.sky.controller.user;


import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "套餐接口")
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealController;

    /**
     * 根据分类id查询套餐
     * @param id
     * @return
     */
    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    public Result list(Long id){

        // 1. 创建一个新的 Setmeal 对象
        Setmeal setmeal = new Setmeal();

        // 2. 设置 Setmeal 对象的 categoryId 属性，用来指定该套餐属于哪个类别
        setmeal.setCategoryId(id);

        // 3. 设置 Setmeal 对象的状态为 ENABLE，表示该套餐是可用的
        setmeal.setStatus(StatusConstant.ENABLE);


        // 4. 调用 setmealController 的 list 方法，传入 setmeal 对象获取套餐列表
        // 这里的 list 方法应该是根据传入的 setmeal 对象查询符合条件的套餐
        List<Setmeal> list = setmealController.list(setmeal);

        // 5. 返回成功的结果，并将查询到的套餐列表作为返回值
        // Result.success() 方法通常是封装成功的返回结果，这里将套餐列表 list 作为成功的响应数据返回
        return Result.success(list);
    }

    @ApiOperation("根据套餐id查询菜品")
    @GetMapping("/dish/{id}")
    public Result dishList(@PathVariable("id") Long id){

        List<SetmealVO> setmealVOList = setmealController.getDishItemById(id);
        return Result.success(setmealVOList);

    }
}
