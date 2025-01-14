package com.sky.service;


import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {


    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    List<SetmealVO> getDishItemById(Long id);


}
