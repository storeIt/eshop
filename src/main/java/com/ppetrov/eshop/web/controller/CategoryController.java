package com.ppetrov.eshop.web.controller;

import com.ppetrov.eshop.domain.model.binding.CategoryAddBindingModel;
import com.ppetrov.eshop.domain.model.service.CategoryServiceModel;
import com.ppetrov.eshop.domain.model.view.CategoryViewModel;
import com.ppetrov.eshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.PortUnreachableException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {

     private final CategoryService categoryService;
     private final ModelMapper modelMapper;

     @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addCategory(){
        return view("category/add-category");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addCategoryConfirm(@ModelAttribute CategoryAddBindingModel model){
         categoryService.addCategory(modelMapper.map(model, CategoryServiceModel.class));
         return redirect("/categories/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allCategories(ModelAndView modelAndView){
        modelAndView.addObject("categories",
                categoryService.findAllCategories()
                    .stream()
                    .map(category -> modelMapper.map(category, CategoryViewModel.class))
                    .collect(Collectors.toList())
        );
         return view("category/all-categories", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editCategory(@PathVariable String id, ModelAndView modelAndView){
         modelAndView.addObject(
                 "model",
                 modelMapper.map(categoryService.findCategoryById(id), CategoryViewModel.class));
         return view("category/edit-category", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editCategoryConfirm(@PathVariable String id, @ModelAttribute CategoryAddBindingModel model){
        categoryService.editCategory(id, modelMapper.map(model, CategoryServiceModel.class));
        return redirect("/categories/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteCategory(@PathVariable String id, ModelAndView modelAndView){
         modelAndView.addObject("model",
                 modelMapper.map(categoryService.findCategoryById(id), CategoryViewModel.class));
         return view("category/delete-category", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteCategoryConfirm(@PathVariable String id ){
         categoryService.deleteCategory(id);
         return redirect("/categories/all");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<CategoryViewModel> fetchCategories(){
         return categoryService.findAllCategories()
                 .stream()
                 .map(category -> modelMapper.map(category, CategoryViewModel.class))
                 .collect(Collectors.toList());

    }

}
