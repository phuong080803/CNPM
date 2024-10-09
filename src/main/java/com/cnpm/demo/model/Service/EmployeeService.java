package com.cnpm.demo.model.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.demo.model.DTO.EmployeeDTO;
import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        // Lấy tất cả các nhân viên từ DB
        return employees.stream()
                .filter(emp -> !"admin".equalsIgnoreCase(emp.getRole()))
                .map(emp -> new EmployeeDTO((int) emp.getIdEmployee(), emp.getName()))  // Chuyển đổi sang EmployeeDTO
                .collect(Collectors.toList());
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long idEmployee) {
        return employeeRepository.findById(idEmployee).orElse(null);
    }
    public boolean existsByUsername(String username) {
        return employeeRepository.existsByUsername(username);
    }
    public boolean updateEmployee(Long idEmployee, Employee newEmployeeDetails) {
        Employee existingEmployee = employeeRepository.findById(idEmployee).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setName(newEmployeeDetails.getName());
            existingEmployee.setGender(newEmployeeDetails.getGender());
            existingEmployee.setEmail(newEmployeeDetails.getEmail());
            existingEmployee.setPhone(newEmployeeDetails.getPhone());
            existingEmployee.setAddress(newEmployeeDetails.getAddress());
            employeeRepository.save(existingEmployee);
            return true;
        }
        return false;
    }
}