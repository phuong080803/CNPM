package com.cnpm.demo.model.Service;

import com.cnpm.demo.model.Model.LeaveRequest;
import com.cnpm.demo.model.Repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public void saveLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public void updateLeaveRequestStatus(int idLeave, String status) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(idLeave).orElse(null);
        if (leaveRequest != null) {
            leaveRequest.setStatus(status);
            leaveRequestRepository.save(leaveRequest);
        }
    }
}

