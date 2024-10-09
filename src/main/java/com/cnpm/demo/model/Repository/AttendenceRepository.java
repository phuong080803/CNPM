package com.cnpm.demo.model.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cnpm.demo.model.Model.Attendence;
import com.cnpm.demo.model.Model.Employee;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendence, Integer> {
    // Tìm kiếm điểm danh theo nhân viên và ngày cụ thể
    List<Attendence> findByEmployeeAndDate(Employee employee, LocalDate date);
    @Query("SELECT a FROM Attendence a JOIN FETCH a.employee")
    List<Attendence> findAllWithEmployee();
}