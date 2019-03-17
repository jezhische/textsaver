package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.repository.AppUserRepository;
import com.jezh.textsaver.service.roleProperties.ExistingRoles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Arrays;
import java.util.HashSet;


@Service
@Transactional
public class
AppUserServiceImpl implements AppUserService {

    private AppUserRepository appUserRepository;
    private RoleService roleService;
    private BCryptPasswordEncoder encoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.appUserRepository = appUserRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser save(AppUser user) {
        if (appUserRepository.findByUsername(user.getUsername()) != null) {
            throw new EntityExistsException("User with such name existed");
        } else user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(roleService.findByRole(ExistingRoles.USER))));
        user.setEnabled(true);
        return appUserRepository.saveAndFlush(user);
    }
}
