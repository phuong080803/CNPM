package com.cnpm.demo.model.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.demo.model.DTO.AttendenceDTO;
import com.cnpm.demo.model.Service.AttendenceService;

@RestController
public class AttendenceDataController {

    @Autowired
    private AttendenceService attendenceService;

    @GetMapping("/api/clockin-attendence-data")
    public ResponseEntity<List<AttendenceDTO>> getAttendenceData() {
        // Gọi service để lấy danh sách AttendenceDTO
        List<AttendenceDTO> attendenceData = attendenceService.getAllAttendence();
        return ResponseEntity.ok(attendenceData);
    }
}
