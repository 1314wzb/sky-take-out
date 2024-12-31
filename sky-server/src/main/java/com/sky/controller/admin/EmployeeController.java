package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Api(tags = "员工接口")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "员工登录接口")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "员工退出登录接口")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @ApiOperation("新增员工")
    @PostMapping
    public Result add(@RequestBody EmployeeDTO employeeDTO){
        System.out.println("Controller的线程="+Thread.currentThread().getName());
        employeeService.add(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页
     * @param dto
     * @return
     */
    @ApiOperation("员工分页")
    @GetMapping("/page")
    public Result page(EmployeePageQueryDTO dto){

        PageResult pageResult = employeeService.page(dto);

        return Result.success(pageResult);

    }

    /**
     * 启用或者禁用员工状态
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用或者禁用员工")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status,Long id){

        employeeService.updateStatus(status,id);

        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @ApiOperation("根据id查询员工")
    @GetMapping("{id}")
    public Result findById(@PathVariable Long id){

        Employee employee = employeeService.findById(id);

        return Result.success(employee);
    }

    /**
     * 更新员工信息
     * @param dto
     * @return
     */
    @ApiOperation("更新员工信息")
    @PutMapping
    public Result update(@RequestBody EmployeeDTO dto){

        employeeService.update(dto);
        return Result.success();
    }
}
