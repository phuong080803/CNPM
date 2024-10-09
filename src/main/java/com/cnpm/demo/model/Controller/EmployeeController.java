package com.cnpm.demo.model.Controller;

import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @GetMapping("/api/get-current-employee")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> response = new HashMap<>();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();

            response.put("username",username);
        } else {
            response.put("error", "user not found");
        }
        return ResponseEntity.ok(response);
    }

    // API để thêm nhân viên mới vào cơ sở dữ liệu
    @PostMapping("/api/add-employee")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        try {
            // Lưu nhân viên vào cơ sở dữ liệu
            employeeRepository.save(employee);
            return ResponseEntity.ok("Employee added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding employee: " + e.getMessage());
        }
    }
}
//lấy thông tin nhân viên hiện tại từ SecurityContext
