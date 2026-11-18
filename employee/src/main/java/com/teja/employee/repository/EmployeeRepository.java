package com.teja.employee.repository;

import com.teja.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository   extends JpaRepository<Employee, Long> {

}
