package com.cnpm.demo.model.Service;

import com.cnpm.demo.model.Model.Employee;
import com.cnpm.demo.model.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm người dùng từ cơ sở dữ liệu
        Employee employee = employeeRepository.findByUsername(username);

        if (employee != null) {
            String role = employee.getRole();
            // Sử dụng lớp Employee của Spring Security (dùng tên đầy đủ)
            return org.springframework.security.core.userdetails.User.withUsername(employee.getUsername())
                    .password(employee.getPassword())
                    .authorities(role)
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
