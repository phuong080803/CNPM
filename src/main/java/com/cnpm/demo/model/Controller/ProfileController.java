package com.cnpm.demo.model.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.demo.model.Model.UserProfile;
import com.cnpm.demo.model.Service.UserProfileService;

@RestController
public class ProfileController {

    @Autowired
    private UserProfileService UserProfileService;

    @GetMapping("/api/profile")
    public UserProfile getLoggedInEmployee() {
        // Lấy thông tin người dùng đang đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tìm kiếm thông tin nhân viên theo username
        return UserProfileService.getProfileByUsername(username);
    }
}
