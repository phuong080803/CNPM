package com.cnpm.demo.model.Repository;

import com.cnpm.demo.model.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByUsername(String username);
}
//truy vấn thông tin từ bảng employee, để tìm Employee theo username.