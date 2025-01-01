package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.beancontext.BeanContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

     /*@Autowired
    private HttpServletRequest request;*/


    /**
     * 新增员工
     * @param dto
     */
    @Override
    public void add(CategoryDTO dto) {
        //1. 创建一个Category对象
        Category category = new Category();

        //2. 搬运数据
        BeanUtils.copyProperties(dto,category);

        //3. 看看缺什么补什么
        category.setStatus(1);
        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());

        //创建的用户和更新的用户
        //2. 要想获取登录的ID值，必须要获取到token令牌
        //2. 要想获取到token令牌，必须从请求头里面获取
        //3. 要想从请求头里面获取数据，必须借助request对象!
        //4. 解决：  1. 注入进来request对象
       /* String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT("itcast",token);
        Long empId = Long.parseLong(claims.get(JwtClaimsConstant.EMP_ID).toString());*/

        //方法二：利用ThreadLocal这个线程获取
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.add(category);
    }

    /**
     * 分页查询
     * @param dto
     */
    @Override
    public PageResult page(CategoryPageQueryDTO dto) {

        //1. 使用分页插件，设置查询第几页，每页多少条
        PageHelper.startPage(dto.getPage(),dto.getPageSize());

        //2. 调用mapper
        Page<Category> p = categoryMapper.page(dto);

        //3. 封装成PageResult对象并返回
        return new PageResult(p.getTotal(),p.getResult());
    }


    /**
     * 启用或者禁用分类状态
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {

        //1. 组装对象
        //普通的方法设置
        /*Category category = new Category();
        category.setStatus(status);
        category.setId(id);*/

        //高级的方法
        Category category = Category.builder()
                            .status(status)
                            .id(id)
                            .build();
        categoryMapper.update(category);

    }

    /**
     * 修改分类
     */
    @Override
    public void updateCategory(CategoryDTO dto) {

        //1. 构建一个Category对象
        Category category = new Category();

        //2. 搬运数据
        BeanUtils.copyProperties(dto,category);

        //3. 看看还缺少上面，就补充什么
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public Boolean delete(Long id) {


        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        //1. 构建一个Category对象
        Category category = new Category();

        //2. 设置id
        category.setId(id);

        //3. 先查询一下id是否存在
        List<Category> result = categoryMapper.select(category);

        //4. 如果不存在，返回false
        if(result == null || result.isEmpty()){
            return false;
        }
        //5. 如果存在，删除
        categoryMapper.delete(id);

        //6. 返回true
        return true;
    }

    /**
     * 根据类型查询分类列表
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        //1. 构建一个Category对象
        Category category = new Category();

        //2. 设置id
        category.setType(type);

        //3. 查询
        List<Category> result = categoryMapper.select(category);

        //4. 返回结果
        return result;
    }
}
