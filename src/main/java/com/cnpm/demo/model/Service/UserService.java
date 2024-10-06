package com.cnpm.demo.model.Service;

import com.cnpm.demo.model.Model.User;
import com.cnpm.demo.model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public String getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user.getUsername();
    }
}
