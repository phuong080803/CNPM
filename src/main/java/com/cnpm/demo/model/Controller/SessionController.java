package com.cnpm.demo.model.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class SessionController {

    @GetMapping("/session/info")
    public String getSessionInfo(HttpSession session, Model model) {
        // Lấy ID session
        model.addAttribute("sessionId", session.getId());

        // Chuyển đổi thời gian tạo từ kiểu long sang LocalDateTime
        LocalDateTime creationTime = Instant.ofEpochMilli(session.getCreationTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        model.addAttribute("creationTime", creationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Chuyển đổi thời gian truy cập cuối cùng
        LocalDateTime lastAccessedTime = Instant.ofEpochMilli(session.getLastAccessedTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        model.addAttribute("lastAccessedTime", lastAccessedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Thời gian không hoạt động tối đa
        model.addAttribute("maxInactiveInterval", session.getMaxInactiveInterval());

        return "session_info";
    }
}
