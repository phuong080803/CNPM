package com.cnpm.demo.model.Repository;

import com.cnpm.demo.model.Model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
}
