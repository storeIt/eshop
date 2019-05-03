package com.ppetrov.eshop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShoppingController extends BaseController {

    @GetMapping("/e-cart")
    public ModelAndView shop(){
        return view("e-cart");
    }
}
