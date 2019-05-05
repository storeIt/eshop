package com.ppetrov.eshop.service;

import com.ppetrov.eshop.domain.entity.Role;
import com.ppetrov.eshop.domain.model.service.RoleServiceModel;
import com.ppetrov.eshop.domain.model.service.UserServiceModel;
import com.ppetrov.eshop.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String ROLE_ROOT = "ROLE_ROOT";

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRolesInDb() {
       if (roleRepository.count() == 0){
            roleRepository.saveAndFlush(new Role(ROLE_USER));
            roleRepository.saveAndFlush(new Role(ROLE_MODERATOR));
            roleRepository.saveAndFlush(new Role(ROLE_ADMINISTRATOR));
            roleRepository.saveAndFlush(new Role(ROLE_ROOT));
       }
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return modelMapper.map(roleRepository.findByAuthority(authority), RoleServiceModel.class);
    }
}
