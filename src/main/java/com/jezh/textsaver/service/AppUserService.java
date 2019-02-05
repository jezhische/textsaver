package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.AppUser;

public interface AppUserService {

    AppUser findByName(String name);

    AppUser save(AppUser user);
}
