package com.icee.service;

import com.icee.dto.EmployeeLoginDTO;
import com.icee.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
