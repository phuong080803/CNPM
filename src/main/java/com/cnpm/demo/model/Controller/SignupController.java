package com.cnpm.demo.model.Controller;

import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SignupController {

    @Autowired
    private EmployeeRepository employeeRepository;


    @GetMapping("/create_account")
    public ModelAndView signupPage() {
        return new ModelAndView("create_account"); // Trả về trang đăng ký
    }

    @PostMapping("/signup")
    public ModelAndView signup(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        // Kiểm tra username đã tồn tại chưa
        Employee existingUser = employeeRepository.findByUsername(username);
        if (existingUser != null) {
            // Username đã tồn tại
            ModelAndView mav = new ModelAndView("create_account");
            mav.addObject("error", "Username already exists!");
            return mav;
        }

        // Tạo người dùng mới nếu username chưa tồn tại
        Employee newEmployee = new Employee();
        newEmployee.setUsername(username);
        newEmployee.setPassword(password);
        employeeRepository.save(newEmployee);

        return new ModelAndView("redirect:/home_page");
    }
}
