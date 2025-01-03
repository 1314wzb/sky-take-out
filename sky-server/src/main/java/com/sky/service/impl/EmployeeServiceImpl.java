package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /*@Autowired
    private HttpServletRequest request;*/

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        /**
         * 进行md5加密啊
         */
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void add(EmployeeDTO employeeDTO) {
        System.out.println("Service的线程="+Thread.currentThread().getName());

        //1.手动创建出来一个Employee对象
        Employee employee = new Employee();

        //2.把dto里面的数据搬运到employee对象身上
        //从源对象dto身上拷贝属性的值到目标对象employee身上，只会拷贝同名属性
        BeanUtils.copyProperties(employeeDTO,employee);

        //3.看看employee对象还缺少什么属性没有值，缺少什么，我们就补充什么
        employee.setStatus(1);
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());


        //创建的用户和跟新的用户
        //2. 要想获取登录的ID值，必须要获取到token令牌
        //2. 要想获取到token令牌，必须从请求头里面获取
        //3. 要想从请求头里面获取数据，必须借助request对象!
        //4. 解决：  1. 注入进来request对象
       /* String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT("itcast",token);
        Long empId = Long.parseLong(claims.get(JwtClaimsConstant.EMP_ID).toString());*/

        //方法二：利用ThreadLocal这个线程获取
        //Long empId = BaseContext.getCurrentId();
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        //md5加密
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);

        //4.调用dao完成工作
        employeeMapper.add(employee);
    }

    /**
     * 员工分页
     * @param dto
     * @return
     */
    @Override
    public PageResult page(EmployeePageQueryDTO dto) {
        //1. 使用分页插件，设置查询第几页，每页多少条
        PageHelper.startPage(dto.getPage(),dto.getPageSize());

        //2. 调用mapper
        Page<Employee> p = employeeMapper.page(dto);

        //3. 封装成PageResult对象并返回
        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 更新员工状态
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {

        //1. 组装对象
        //普通的方法设置
        /*Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/

        //高级的方法
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        //2. 调用mapper
        employeeMapper.update(employee);
    }

    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    @Override
    public Employee findById(Long id) {

        Employee employee = employeeMapper.findById(id);

        //代表没有这个人或者ID写错了
        if(employee == null){
            return null;
        }
        employee.setPassword("****");
        return employee;
    }


    /**
     * 更新员工信息
     * @param dto
     */
    @Override
    public void update(EmployeeDTO dto) {

        //1. 构建一个Employee对象
        Employee employee = new Employee();

        //2. 搬运数据
        BeanUtils.copyProperties(dto,employee);

        //3. 看看还缺少上面，就补充什么
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }
}
