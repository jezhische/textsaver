package com.jezh.textsaver.service;

import com.jezh.textsaver.entity.Role;
import com.jezh.textsaver.service.roleProperties.ExistingRoles;

public interface RoleService {

    Role findByRole(ExistingRoles role);

}
