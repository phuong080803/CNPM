package com.cnpm.demo.model.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpm.demo.model.Model.LeaveRequest;
import com.cnpm.demo.model.Service.LeaveRequestService;

@Controller
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("/submit-leave")
    @ResponseBody // Trả về phản hồi trực tiếp mà không chuyển hướng
    public ResponseEntity<String> submitLeaveRequest(@RequestParam("startDate") String startDate,
                                                     @RequestParam("endDate") String endDate,
                                                     @RequestParam("reason") String reason) {
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        int idEmployee = getIdFromUserDetails(userDetails); // Tùy thuộc vào cấu trúc UserDetails của bạn

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setIdEmployee(idEmployee);
        leaveRequest.setStartDate(LocalDate.parse(startDate));
        leaveRequest.setEndDate(LocalDate.parse(endDate));
        leaveRequest.setReason(reason);
        leaveRequest.setRequestDate(LocalDate.now());
        leaveRequest.setStatus("Pending");

        leaveRequestService.saveLeaveRequest(leaveRequest);

        // Trả về thông báo thành công
        return ResponseEntity.ok("Leave request submitted successfully"); // Trả về thông báo thành công
    }

    // Hàm để lấy idEmployee từ UserDetails (tùy chỉnh theo ứng dụng của bạn)
    private int getIdFromUserDetails(UserDetails userDetails) {
        // Giả sử UserDetails có chứa thông tin idEmployee, bạn có thể sửa lại để lấy idEmployee
        // Thí dụ nếu userDetails chứa email/username, bạn có thể tìm employee từ database.
        return 30; // Trả về giá trị tạm thời, bạn cần sửa lại cho phù hợp
    }
}
