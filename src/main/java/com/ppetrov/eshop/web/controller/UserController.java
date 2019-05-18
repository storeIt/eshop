package com.ppetrov.eshop.web.controller;

import com.ppetrov.eshop.domain.model.binding.UserEditBindingModel;
import com.ppetrov.eshop.domain.model.binding.UserRegisterBindingModel;
import com.ppetrov.eshop.domain.model.service.UserServiceModel;
import com.ppetrov.eshop.domain.model.view.UserAllViewModel;
import com.ppetrov.eshop.domain.model.view.UserProfileViewModel;
import com.ppetrov.eshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(){
        return view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(UserRegisterBindingModel userRegisterBindingModel){
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            return redirect("/register");
        }
        userService.registerUser(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));
        return view("/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(){
        return view("login");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView){
        UserServiceModel userServiceModel = userService.findUserByUsername(principal.getName());
        UserProfileViewModel profileViewModel = modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("model", profileViewModel);
        return view("profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView){
        modelAndView.addObject(
                "model",
                modelMapper.map(userService.findUserByUsername(principal.getName()), UserProfileViewModel.class)
        );
        return view("edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute UserEditBindingModel model){
        if (!model.getPassword().equals(model.getConfirmPassword())){
            return view("edit-profile");
        }
        userService.editUserProfile(modelMapper.map(model, UserServiceModel.class), model.getOldPassword());
        return redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allUsers(ModelAndView modelAndView){
        List<UserAllViewModel> users = userService.findAllUsers()
                .stream()
                .map(u -> {
                    UserAllViewModel user = modelMapper.map(u, UserAllViewModel.class);
                    user.setAuthorities(u.getAuthorities()
                            .stream()
                            .map(a -> a.getAuthority())
                            .collect(Collectors.toSet()));

                    return user;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("users", users);
        return view("all-users", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id){
        userService.setUserRole(id, "user");
        return redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView setModerator(@PathVariable String id){
        userService.setUserRole(id, "moderator");
        return redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView setAdmin(@PathVariable String id){
        userService.setUserRole(id, "admin");
        return redirect("/users/all");
    }
}
