package com.ppetrov.eshop.web.controller;

import com.ppetrov.eshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryController extends BaseController {

     private final CategoryService categoryService;
     private final ModelMapper modelMapper;

     @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }


}
