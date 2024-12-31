package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     */
    @Insert("insert into employee values(null,#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Employee employee);

    /**
     * 员工分页
     * @param dto
     * @return
     */
    Page<Employee> page(EmployeePageQueryDTO dto);

    /**
     * 更新员工(根据id来更新，使用动态的SQL更新)
     * @param employee
     */
    void update(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee findById(Long id);
}
