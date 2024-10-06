package com.cnpm.demo.model.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/salary_page")
    public ModelAndView salaryPage() {
        return new ModelAndView("salary_page"); // Trả về view home_page.html
    }
    @GetMapping("/notification_page")
    public ModelAndView notificationPage() {
        return new ModelAndView("notification_page"); // Trả về view home_page.html
    }
    @GetMapping("/absence_page")
    public ModelAndView absencePage() {
        return new ModelAndView("absence_page"); // Trả về view home_page.html
    }
    @GetMapping("/support_page")    
    public ModelAndView supportPage() {
        return new ModelAndView("support_page"); // Trả về view home_page.html
    }
    @GetMapping("/profile_page")
    public ModelAndView profilePage() {
        return new ModelAndView("profile_page"); // Trả về view home_page.html
    }    
    @GetMapping("/addface_page")    
    public ModelAndView addfacePage() {
        return new ModelAndView("addface_page"); // Trả về view home_page.html
    }
    @GetMapping("/edit_profile")
    public ModelAndView editProfile() {
        return new ModelAndView("edit_profile"); // Trả về view home_page.html
    }
    @GetMapping("/change_pass_page")
    public ModelAndView changePassPage() {
        return new ModelAndView("change_pass_page"); // Trả về view home_page.html
    }
}
