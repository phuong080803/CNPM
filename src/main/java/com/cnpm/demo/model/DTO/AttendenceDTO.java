package com.cnpm.demo.model.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttendenceDTO {
    @JsonProperty("id_employee")
    private int idEmployee;
    @JsonProperty("name")
    private String name;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("time_in")
    private LocalTime timeIn;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    @JsonProperty("time_out")
    private LocalTime timeOut;

    public AttendenceDTO(int idEmployee, String name, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        this.idEmployee = idEmployee;
        this.name = name;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

}
