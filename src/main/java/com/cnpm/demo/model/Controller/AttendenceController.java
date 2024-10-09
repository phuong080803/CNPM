package com.cnpm.demo.model.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.demo.model.Model.Attendence;
import com.cnpm.demo.model.Service.AttendenceService;

@RestController
@RequestMapping("/api")
public class AttendenceController {
    @Autowired
    private AttendenceService attendenceService;

    @GetMapping("/get-attendence-data") //trả về danh sách điểm danh dựa trên username và date
    public List<Attendence> getAttendenceData(@RequestParam String username, @RequestParam String date) {
        return attendenceService.getAttendenceByUsernameAndDate(username, date);
    }



}
//sử dụng AttendenceService để lấy dữ liệu điểm danh dựa trên username và date.
// xử lý các yêu cầu HTTP liên quan đến dữ liệu điểm danh (attendance).