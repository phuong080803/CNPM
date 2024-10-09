package com.cnpm.demo.model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnpm.demo.model.Model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);
    boolean existsByUsername(String username);
}
//truy vấn thông tin từ bảng employee, để tìm Employee theo username.