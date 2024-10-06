package com.cnpm.demo.model.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmployeeController {
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
}
//lấy thông tin nhân viên hiện tại từ SecurityContext
