package com.cnpm.demo.model.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminEmployeeController {
@GetMapping("/admin/employee")
    public String employeePage() {
        return "employee";
    }
}
