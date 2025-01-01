package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {


    /**
     * 新增员工
     */
    void add(CategoryDTO dto);

    /**
     * 分页查询
     */
    PageResult page(CategoryPageQueryDTO dto);

    /**
     * 启用或者禁用分类状态
     * @param statues
     * @param id
     */
    void updateStatus(Integer statues,Long id);


    /**
     * 修改分类
     */
    void updateCategory(CategoryDTO dto);

    /**
     * 根据id删除分类
     * @param id
     */
    Boolean delete(Long id);

    /**
     * 根据类型查询分类列表
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
