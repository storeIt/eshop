package com.ppetrov.eshop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController extends BaseController{

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
