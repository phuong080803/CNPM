package com.cnpm.demo.model.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/admin/admin-home_page")
    public String homePage() {
        return "admin-home_page";
    }


    @GetMapping("/admin/employee")
    public String employeePage() {
        return "employee";
    }

    @GetMapping("/admin/admin-add_employee")
    public String addEmployeePage() {
        return "admin-add_employee";
    }

    @GetMapping("/admin/admin-edit_employee")
    public String editEmployeePage() {
        return "admin-edit_employee";
    }


    @GetMapping("/admin/admin-clockin_page")
    public String clockinPage() {
        return "admin-clockin_page";
    }


    @GetMapping("/admin/admin-salary_page")
    public String salaryPage() {
        return "admin-salary_page";
    }


    @GetMapping("/admin/admin-absence_page")
    public String absencePage() {
        return "admin-absence_page";
    }

    @GetMapping("/admin/admin-support_page")
    public String supportPage() {
        return "admin-support_page";
    }
}
