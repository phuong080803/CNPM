package com.cnpm.demo.model.Repository;

import com.cnpm.demo.model.Model.Attendence;
import com.cnpm.demo.model.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendence, Integer> {
    // Tìm kiếm điểm danh theo nhân viên và ngày cụ thể
    List<Attendence> findByEmployeeAndDate(Employee employee, LocalDate date);
}