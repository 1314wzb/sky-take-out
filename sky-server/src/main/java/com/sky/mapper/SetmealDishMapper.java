package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ID来这张中间表看看有没有这道菜出现，如果有，就表示这道菜被某一个套餐所包含了！
     * @param ids
     * @return
     */
    List<SetmealDish> findByDishId(List<Long> ids);
}
