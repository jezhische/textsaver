package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByName(String name);

    Optional<AppUser> findById(Long id);
}
