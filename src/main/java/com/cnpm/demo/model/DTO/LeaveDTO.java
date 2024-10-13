package com.cnpm.demo.model.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaveDTO {
    
    @JsonProperty("id_leave")
    private Long id_leave;
    @JsonProperty("id_employee")
    private Long id_employee;
    @JsonProperty("start_date")
    private LocalDate start_date;
    @JsonProperty("end_date")
    private LocalDate end_date;
    @JsonProperty("status")
    private String status;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("request_date")
    private LocalDate request_date;


    // Getters and Setters
    public Long getId_leave() {
        return id_leave;
    }

    public void setId_leave(Long id_leave) {
        this.id_leave = id_leave;
    }

    public Long getId_employee() {
        return id_employee;
    }

    public void setId_employee(Long id_employee) {
        this.id_employee = id_employee;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDate request_date) {
        this.request_date = request_date;

    }
    public LeaveDTO(Long id_leave, Long id_employee, LocalDate start_date, LocalDate end_date, String status, String reason, LocalDate request_date) {
        this.id_leave = id_leave;
        this.id_employee = id_employee;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.reason = reason;
        this.request_date = request_date;
    }
}
