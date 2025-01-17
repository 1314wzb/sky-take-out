package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void add(DishDTO dishDTO);


    /**
     * 菜品分页
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void delete(List<Long> ids);


    /**
     * 根据菜品ID查询菜品
     * @param id
     * @return
     */
    DishVO findById(Long id);

    /**
     * 更新菜品
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 菜品的启用或者禁用
     * @param status
     * @param id
     */
    void updateStatus(Integer status,Long id);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
