package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {


    /**
     * 新增员工
     */
    @Insert("insert into category values(null,#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Category category);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    Page<Category> page(CategoryPageQueryDTO dto);



    /**
     * 分类接口统一的修改方法
     * @param category
     */
    void update(Category category);


    /**
     *  查询分类信息
     */
    List<Category> select(Category category);

    /**
     * 根据id删除分类
     * @param id
     */
    void delete(Long id);
}
