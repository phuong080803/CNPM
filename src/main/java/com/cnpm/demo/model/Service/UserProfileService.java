package com.cnpm.demo.model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.demo.model.Model.UserProfile;
import com.cnpm.demo.model.Repository.UserProfileRepository;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile getProfileByUsername(String username) {
        return userProfileRepository.findByUsername(username);
    }
}
