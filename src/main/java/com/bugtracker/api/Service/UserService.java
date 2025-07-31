package com.bugtracker.api.Service;


import com.bugtracker.api.Model.Role;
import com.bugtracker.api.Model.User;
import com.bugtracker.api.Repository.RoleRepository;
import com.bugtracker.api.Repository.UserRepository;
//import com.bugtracker.api.Security.UserDetailsImpl;

import com.bugtracker.api.Security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String email, String rawPassword, String roleName) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Username or email already exists");
        }
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnabled(true);
        return userRepository.save(user);

    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Return a custom UserDetails implementation or adapt User to UserDetails here
        return new UserDetailsImpl(user); // You’ll need to define UserDetailsImpl
    }

}
