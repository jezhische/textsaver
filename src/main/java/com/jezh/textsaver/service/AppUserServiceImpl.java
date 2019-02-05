package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.repository.AppUserRepository;
import com.jezh.textsaver.service.roleProperties.ExistingRoles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;


@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepository appUserRepository;
    private RoleService roleService;
    private BCryptPasswordEncoder encoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.appUserRepository = appUserRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public AppUser findByName(String name) {
        return appUserRepository.findByName(name);
    }

    @Override
    public AppUser save(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(roleService.findByRole(ExistingRoles.ROLE_USER))));
        return appUserRepository.saveAndFlush(user);
    }
}
