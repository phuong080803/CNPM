package com.cnpm.demo.model.Controller;

import com.cnpm.demo.model.DTO.EmployeeDTO;
import com.cnpm.demo.model.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeInfoController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/api/get-employee-info")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        // Trả về danh sách nhân viên dưới dạng JSON
        return ResponseEntity.ok(employees);
    }
}
