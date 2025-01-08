package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜品接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result add(@RequestBody DishDTO dishDTO){
        System.out.println("新增菜品："+dishDTO.toString());
        dishService.add(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品分页")
    @GetMapping("/page")
    public Result page(DishPageQueryDTO dishPageQueryDTO){

        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询菜品")
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){

        DishVO dishVO = dishService.findById(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("更新菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){

        dishService.update(dishDTO);
        return Result.success();
    }


    /**
     * 菜品的启用或者禁用
     * @param status
     * @return
     */
    @ApiOperation("菜品的启用或者禁用")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status,Long id){

        dishService.updateStatus(status,id);
        return Result.success();
    }

}
