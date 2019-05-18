package com.ppetrov.eshop.service;

import com.ppetrov.eshop.domain.entity.User;
import com.ppetrov.eshop.domain.model.service.UserServiceModel;
import com.ppetrov.eshop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private static final String USER_NOT_FOUND = "Username not found!";
    private static final String WRONG_PASSWORD = "Wrong password!";
    private static final String WRONG_ID = "Wrong ID!";

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        roleService.seedRolesInDb();
        if (userRepository.count() == 0){
            userServiceModel.setAuthorities(roleService.findAllRoles());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(roleService.findByAuthority(RoleServiceImpl.ROLE_USER));
        }
        User user = modelMapper.map(userServiceModel, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword) {
        User user = userRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
        user.setPassword(
                !"".equals(userServiceModel.getPassword()) ?
                        bCryptPasswordEncoder.encode(userServiceModel.getPassword()) :
                        user.getPassword()
        );
        user.setEmail(userServiceModel.getEmail());
        return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void setUserRole(String id, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WRONG_ID));
        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();
        switch (role){
            case "user":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                break;
            case "moderator":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }
        userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));
    }




}
