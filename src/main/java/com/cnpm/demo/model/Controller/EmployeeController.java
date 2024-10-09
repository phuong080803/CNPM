package com.cnpm.demo.model.Controller;

import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.EmployeeRepository;
import com.cnpm.demo.model.Service.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
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

    @GetMapping("/api/check-username")
    public ResponseEntity<Map<String,Boolean>> checkUsername(String username) {
        boolean exists = employeeRepository.existsByUsername(username);
        Map<String,Boolean> response = new HashMap<>();
        response.put("exists", exists);
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

    @DeleteMapping("/api/delete-employee/{id_employee}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id_employee) {
        try {
            if (employeeRepository.existsById(id_employee)) {
                employeeRepository.deleteById(id_employee);
                return ResponseEntity.ok("Employee deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Employee not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting employee: " + e.getMessage());
        }
    }
    @PutMapping("/api/update-employee/{id_employee}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable Long id_employee, @RequestBody Employee employeeDetails) {
        Map<String, Object> response = new HashMap<>();
        boolean updateSuccess = employeeService.updateEmployee(id_employee, employeeDetails);
        if (updateSuccess) {
            response.put("success", true);
            response.put("message", "Employee updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Employee not found");
            return ResponseEntity.status(404).body(response);
        }
    }
}