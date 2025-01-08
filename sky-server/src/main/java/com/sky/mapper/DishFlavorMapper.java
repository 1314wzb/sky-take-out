package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 单个添加口味数据
     * @param dishFlavor
     */
//    void add(DishFlavor dishFlavor);

    /**
     * 批量添加口味数据
     * @param flavorList
     */
    void add(List<DishFlavor> flavorList);

    /**
     * 根据菜品id来删除口味数据
     * @param ids
     */
    void deleteByDishId(List<Long> ids);

    /**
     * 根据菜品的ID,查询口味数据
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> findById(Long id);
}
