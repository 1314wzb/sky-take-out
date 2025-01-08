package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     * 新增一条记录之后，可以让mybatis把这条记录的ID值返回回来，
     * 但是不会再方法返回值返回，而是需要告诉他，执行成功之后，
     * 把ID值放到参数里面的某个属性身上去，使用数据库自增的ID值
     * @param dish
     */
    @Options(keyProperty = "id",useGeneratedKeys = true)
    @AutoFill
    @Insert("insert into dish values (null,#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Dish dish);


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);


    Page<Dish> page1(DishPageQueryDTO dishPageQueryDTO);

    @Select("select name from category where id = #{id}")
    String page2(Long id);

    /**
     * 根据菜品id来查询菜品数据
     * @param dishId
     * @return
     */
    @Select("select * from dish where id = #{dishId}")
    Dish findById(Long dishId);

    /**
     * 根据 菜品的ID，批量删除菜品
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 更新菜品数据
     * @param dish
     */
    @AutoFill
/*    @Update("update dish set name = #{name},category_id = #{categoryId},price = #{price}," +
            "image = #{image},description = #{description},status = #{status}," +
            "update_time = #{updateTime},update_user = #{updateUser} where id = #{id}")*/
    void update(Dish dish);

}
