package com.cnpm.demo.model.Controller;

import com.cnpm.demo.model.Service.LeaveRequestService;
import com.cnpm.demo.model.Model.LeaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminLeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping("/leave-requests")
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestService.getAllLeaveRequests();
    }

    @PostMapping("/leave-requests/{id}/approve")
    public void approveLeaveRequest(@PathVariable("id") int idLeave) {
        leaveRequestService.updateLeaveRequestStatus(idLeave, "Approved");
    }

    @PostMapping("/leave-requests/{id}/reject")
    public void rejectLeaveRequest(@PathVariable("id") int idLeave) {
        leaveRequestService.updateLeaveRequestStatus(idLeave, "Rejected");
    }
}
