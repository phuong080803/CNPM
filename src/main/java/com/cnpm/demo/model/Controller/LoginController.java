    package com.cnpm.demo.model.Controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.servlet.ModelAndView;

    import com.cnpm.demo.model.Repository.EmployeeRepository;

    @Controller
    public class LoginController {
        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private AuthenticationManager authenticationManager;

        // Phương thức GET để hiển thị trang login
        @GetMapping("/login")
        public ModelAndView showLoginPage() {
            return new ModelAndView("login"); // trả về trang login (login.html)
        }

        @GetMapping("/home_page")
        public ModelAndView showHomePage() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ModelAndView mav = new ModelAndView("home_page");
            mav.addObject("username", username);
            return mav;
        }
    }

