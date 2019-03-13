package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepository appUserRepository;
//    private BCryptPasswordEncoder encoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser save(AppUser user) {
//        user.setPassword(encoder.encode(user.getPassword()));
        return appUserRepository.saveAndFlush(user);
    }
}
