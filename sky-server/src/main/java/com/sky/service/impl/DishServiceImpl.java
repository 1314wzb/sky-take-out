package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     *  1. 菜品数据包含菜品的基本数据 和 口味数据
     *  2. 菜品数据是保存在菜品表(dish)，口味数据是保存在口味表(dish_flavor)
     *  3. 所以新增一道菜，就需要往两张表里面添加数据【和以前不一样了！】
     *      3.1 需要注入进来dish表，对应DishMapper
     *      3.2 需要注入进来dish_flavor对应DishFlavorMapper
     *  4. 先调用谁？ 后调用谁？
     *      先调用菜品表(dish)，然后在调用口味表(dish_flavor)，
     *      这样口味表里面就有菜品表的id了，保证是那个菜的口味了
     * @param dishDTO
     */
    @Override
    public void add(DishDTO dishDTO) {

        //1. 先调用菜品的dao，完成菜品基础数据的新增工作
        Dish dish = new Dish();//创建dish对象
        BeanUtils.copyProperties(dishDTO,dish);//将dishDTO对象属性复制到dish对象中
        dishMapper.add(dish);

        //2. 在调用口味的dao，完成口味的新增数据工作
        //2.1 从dishDTO里面获取出来口味数据
        List<DishFlavor> flavorList = dishDTO.getFlavors();

        //2.2 判定，只有有口味数据的时候，才往里面添加数据
        if(flavorList != null && flavorList.size() > 0){

            //2.3 需要遍历集合，给每一个口味数据都设置他们的菜品ID，
            //这样就知道这个口味数据是哪一道菜的数据了
            flavorList.forEach(dishFlavor -> {
                //需要添加菜品的时候，让mybatis返回菜品的主键ID
                dishFlavor.setDishId(dish.getId());
            });


            //2.4 调用dao添加口味数据
            dishFlavorMapper.add(flavorList);
        }
    }

    /**
     * 菜品分页
     * 方法一：mapper中直接用dishVO接收返回值
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        //1. 使用分页插件，设置查询第几页，每页多少条
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        //2. 调用mapper
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);


        //3. 封装成PageResult对象并返回
        return new PageResult(page.getTotal(),page.getResult());



    }
    /**
     * 方法二：在service中创建dishVO，mapper中传的是Page<Dish>
     */
    public PageResult page1(DishPageQueryDTO dishPageQueryDTO) {

        //1. 使用分页插件，设置查询第几页，每页多少条
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        //2. 调用mapper
        Page<Dish> dishPage = dishMapper.page1(dishPageQueryDTO);
        /**
         * for的形式一：用forEach 遍历
         */
        /*// 创建一个空的 List 用于存放 DishVO 对象
        List<DishVO> dishVOList = new ArrayList<>();

        // 使用 forEach 遍历 dishPage 中的每个 Dish 对象
        dishPage.forEach(dish -> {
            // 创建一个新的 DishVO 对象
            DishVO dishVO = new DishVO();

            // 将 Dish 对象的属性复制到 DishVO 中
            BeanUtils.copyProperties(dish, dishVO);

            // 调用 dishMapper.page2() 方法获取类别名，并设置到 dishVO 中
            String name = dishMapper.page2(dish.getId());
            dishVO.setCategoryName(name);

            // 将转换后的 dishVO 添加到 dishVOList 中
            dishVOList.add(dishVO);
        });*/

        /**
         * for形式二：用流的方式直接for循环完毕赋值给前面的List
         */
        // 将 dishPage 流中的每个 Dish 对象转换为 DishVO 对象，并收集成一个新的 List<DishVO>
        List<DishVO> dishVOList = dishPage.stream()  // 对 dishPage 进行流式处理
                .map(dish -> {  // 使用 map 方法，将每个 Dish 对象映射为 DishVO 对象
                    DishVO dishVO = new DishVO();  // 创建一个新的 DishVO 对象，用于存储转换后的数据

                    // 将 Dish 对象中的属性值复制到 DishVO 对象中
                    // 注意：BeanUtils.copyProperties 会根据字段名自动复制值，但需要字段类型匹配
                    BeanUtils.copyProperties(dish, dishVO);

                    // 获取 Dish 对象的类别名，这里假设 dishMapper.page2(dish.getId()) 是获取类别名的方法
                    String name = dishMapper.page2(dish.getId());

                    // 设置 DishVO 对象的类别名称
                    dishVO.setCategoryName(name);

                    // 返回转换后的 DishVO 对象
                    return dishVO;
                })
                .collect(Collectors.toList());  // 将映射后的 DishVO 对象收集到 List 中并返回

        //3. 封装成PageResult对象并返回
        return new PageResult(dishPage.getTotal(),dishVOList);

    }


    /**
     * 批量删除菜品
     *  1. 可以删除一个菜品，也可以删除多个菜品
     *  2. 起售中的菜品不能删除
     *  3. 如果有套餐包含这道菜，那么也不能删除
     *  4. 初次之外都可以以删除，但是除啦要删除菜品的数据，也要删除口味的数据
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {

        //1. 先看这里的菜品有没有属于起售的菜，如果有，禁止删除！
        ids.forEach(id ->{
            Dish dish = dishMapper.findById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //TODO:: 如果进入了这里，那么表明有起售的菜，禁止删除！
//                throw new DeletionNotAllowedException("有菜品属于起售状态，禁止删除!");
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });

        //2. 再看这些菜有没有被套餐所包含着，如果有，禁止删除！
        List<SetmealDish> setmealDishList = setmealDishMapper.findByDishId(ids);
        if(setmealDishList != null && setmealDishList.size() > 0){
            //TODO:: 如果进入了这里，那么表明这道菜品、其中的某一道菜品有被套餐包含着!
//            throw new DeletionNotAllowedException("有菜品被套餐关联着，禁止删除!");
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3. 来到这里，即表示可以删除菜品
        //3.1 删除菜品
        dishMapper.delete(ids);
        //3.2 删除口味数据
        dishFlavorMapper.deleteByDishId(ids);
    }

    /**
     * 根据菜品ID查询菜品
     *  1. 菜品数据既要包含菜品也要包含口味
     *  2. 选择在service层使用VO来包装它门两个数据
     *  3. 这就要求一会要去查询两张表得到数据回来之后，再这里进行打包向上返回！
     *      3.1 查询菜品表，得到菜品数据
     *      3.2 查询口味表得到口味数据
     * @param id
     * @return
     */
    @Override
    public DishVO findById(Long id) {

        //1. 根据菜品ID，查询菜品数据
        Dish dish = dishMapper.findById(id);

        //2. 根据菜品的ID,查询口味数据
        List<DishFlavor> flavorList = dishFlavorMapper.findById(id);

        //3. 把这两个数据打包成一个DishVO
        DishVO dishVO = new DishVO();

        //3.1 拷贝dish数据到dishVO上面来
        BeanUtils.copyProperties(dish, dishVO);

        //3.2 设置进来口味的数据
        dishVO.setFlavors(flavorList);

        return dishVO;
    }


    /**
     * 更新菜品
     *  1. 更新菜品数据，有可能机要更新菜品表(dish)，还需要更新口味表(dish_flavor)
     *  2. 先往菜品表更新数据
     *  3. 更新口味表，不是按照一般的跟新数据来做!
     *      3.1 因为要判定的东西太多了!口味是否是多了，是否是少了，是否是追加新的口味
     *      3.2 可以先把原来的口味数据全部删除掉，然后把页面传递过来的口味数据添加到数据表里面即可!
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {

        //1. 更新菜品数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //2. 先删除掉原来的口味数据
        dishFlavorMapper.deleteByDishId(Arrays.asList(dishDTO.getId()));

        //3. 添加口味数据:: 如果页面传递过来的口味数据是没有的，那么就不用添加!
        if(dishDTO.getFlavors() != null && dishDTO.getFlavors().size() > 0){

            //获取口味数据
            List<DishFlavor> dishFlavors = dishDTO.getFlavors();

            //设置每一个口味属于那一道菜
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });

            //批量添加
            dishFlavorMapper.add(dishFlavors);
        }
    }

    /**
     * 菜品的启用或者禁用
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {

        /*//1. 先创建一个dish类
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);*/

        //高级的方法
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();

        //调用dishMapper的update方法进行状态的更新
        dishMapper.update(dish);
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {

        // 1. 查询菜品数据：通过传入的 dish 对象，查询所有符合条件的菜品列表
        List<Dish> dishList = dishMapper.list(dish);

        // 2. 创建一个 List<DishVO> 用于存放每个菜品的详细信息及口味数据
        List<DishVO> dishVOList = new ArrayList<>();

        // 3. 遍历查询到的菜品列表，将每个菜品的名称和口味数据拼接到一起
        for (Dish dish1 : dishList) {
            // 3.1 创建一个新的 DishVO 对象，DishVO 用于展示菜品信息及口味数据
            DishVO dishVO = new DishVO();

            // 3.2 使用 BeanUtils.copyProperties 方法将 dish1 中的属性拷贝到 dishVO 中
            // 这样 dishVO 就包含了菜品的基本信息，如名称、价格等
            BeanUtils.copyProperties(dish1, dishVO);

            // 3.3 根据菜品的 ID 获取该菜品对应的口味数据
            List<DishFlavor> dishFlavorList = dishFlavorMapper.findById(dish1.getId());

            // 3.4 把查询到的口味数据设置到 dishVO 对象的 flavors 属性中
            dishVO.setFlavors(dishFlavorList);

            // 3.5 将拼接好的 dishVO 对象添加到 dishVOList 中
            dishVOList.add(dishVO);
        }

        // 4. 返回包含所有菜品及其口味信息的 dishVOList
        return dishVOList;
    }
}
