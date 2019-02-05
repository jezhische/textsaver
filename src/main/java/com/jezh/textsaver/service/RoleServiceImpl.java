package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Role;
import com.jezh.textsaver.repository.RoleRepository;
import com.jezh.textsaver.service.roleProperties.ExistingRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role findByRole(ExistingRoles role) {
        return repository.findByRole(role.toString());
    }
}
