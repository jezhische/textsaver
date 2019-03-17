package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.AppUser;

public interface AppUserService {

    AppUser findByUsername(String username);

    AppUser save(AppUser user);
}
