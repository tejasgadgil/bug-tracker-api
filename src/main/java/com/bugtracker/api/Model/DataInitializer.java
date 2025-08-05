package com.bugtracker.api.Model;

import com.bugtracker.api.Model.Role;
import com.bugtracker.api.Repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        if (!roleRepository.findByName("ADMIN").isPresent()) {
            roleRepository.save(new Role("ADMIN"));
        }
        if (!roleRepository.findByName("DEVELOPER").isPresent()) {
            roleRepository.save(new Role("DEVELOPER"));
        }
    }
}

