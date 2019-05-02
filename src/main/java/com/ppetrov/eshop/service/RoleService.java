package com.ppetrov.eshop.service;

import com.ppetrov.eshop.domain.model.service.RoleServiceModel;
import com.ppetrov.eshop.domain.model.service.UserServiceModel;

import java.util.Set;

public interface RoleService {

    void seedRolesInDb();

//    void assignUserRoles(UserServiceModel userServiceModel, long numberOfUsers);

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}
