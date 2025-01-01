package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类管理
 */
@Api(tags = "分类接口")
@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result add(@RequestBody CategoryDTO dto) {
        categoryService.add(dto);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO dto){

        PageResult pageResult = categoryService.page(dto);
        return Result.success(pageResult);
    }

    /**
     * 启用或者禁用分类状态
     */
    @ApiOperation("启用或者禁用分类状态")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status,Long id) {

        categoryService.updateStatus(status,id);
        return  Result.success();
    }

    /**
     * 修改分类
     */
    @ApiOperation("修改分类")
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO dto) {
        categoryService.updateCategory(dto);
        return Result.success();
    }


    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @ApiOperation("根据id删除分类")
    @DeleteMapping
    public Result delete(Long id) {
        Boolean result =  categoryService.delete(id);
        return Result.success(result);
    }

    /**
     * 根据类型查询分类列表
     * @param type
     * @return
     */
    @ApiOperation("根据类型查询分类列表")
    @GetMapping("list")
    public Result list(Integer type){

        List<Category> result = categoryService.list(type);
        return Result.success(result);
    }

}
