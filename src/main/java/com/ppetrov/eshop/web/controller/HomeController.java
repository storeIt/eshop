package com.ppetrov.eshop.web.controller;

import ch.qos.logback.core.layout.EchoLayout;
import com.ppetrov.eshop.domain.entity.User;
import com.ppetrov.eshop.repository.UserRepository;
import com.ppetrov.eshop.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@Controller
public class HomeController extends BaseController{
    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ModelAndView index(){
        return view("index");
    }


    @GetMapping("/home")
    public ModelAndView home(Principal principal, ModelAndView modelAndView){
        modelAndView.addObject("username", principal.getName());
        return view("user-home", modelAndView);
    }



}
