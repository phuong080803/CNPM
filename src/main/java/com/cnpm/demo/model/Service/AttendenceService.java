package com.cnpm.demo.model.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.demo.model.DTO.AttendenceDTO;
import com.cnpm.demo.model.Model.Attendence;
import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.AttendenceRepository;
import com.cnpm.demo.model.Repository.EmployeeRepository;

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

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        return attendenceRepository.findByEmployeeAndDate(employee, date);
    }

    public List<AttendenceDTO> getAllAttendence() {
        // Lấy tất cả các bản ghi từ bảng attendence
        List<Attendence> attendenceList = attendenceRepository.findAll();

        // Dùng stream để kết hợp dữ liệu từ bảng employee và attendence
        return attendenceList.stream()
                .map(attendence -> {
                    Employee employee = employeeRepository.findById(attendence.getEmployee().getIdEmployee())
                            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + attendence.getEmployee().getIdEmployee()));

                    // Tạo AttendenceDTO chứa thông tin cần thiết
                    return new AttendenceDTO(
                            (int) employee.getIdEmployee(),
                            employee.getName(),
                            attendence.getDate(),
                            attendence.getTimeIn(),
                            attendence.getTimeOut()
                    );
                })
                .collect(Collectors.toList());
    }
}
