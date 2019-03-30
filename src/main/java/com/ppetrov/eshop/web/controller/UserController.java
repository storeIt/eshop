package com.ppetrov.eshop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends BaseController {

//
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public UserController(ModelMapper modelMapper){
//        this.modelMapper = modelMapper;
//    }


    public UserController() {
    }

    @GetMapping("/login")
    public ModelAndView login(){
        return view("login");
    }

    @GetMapping("/register")
    public ModelAndView register(){
        return view(("register"));
    }
}
