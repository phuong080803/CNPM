package com.cnpm.demo.model.Service;

import com.cnpm.demo.model.Model.Attendence;
import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.AttendenceRepository;
import com.cnpm.demo.model.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AttendenceService {

    @Autowired
    private AttendenceRepository attendenceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Attendence> getAttendenceByUsernameAndDate(String username, String dateStr) {
        // Tìm employee theo username
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new RuntimeException("Employee not found with username: " + username);
        }

        // Chuyển đổi từ String sang LocalDate
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

        // Truy vấn điểm danh theo Employee và date
        return attendenceRepository.findByEmployeeAndDate(employee, date);
    }
}
