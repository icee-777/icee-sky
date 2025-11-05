package com.icee.service;

import com.icee.dto.EmployeeDTO;
import com.icee.dto.EmployeeLoginDTO;
import com.icee.dto.EmployeePageQueryDTO;
import com.icee.entity.Employee;
import com.icee.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    void status(Integer status, Long id);
}
