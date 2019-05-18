package com.ppetrov.eshop.web.controller;

import com.ppetrov.eshop.domain.model.binding.ProductAddBindingModel;
import com.ppetrov.eshop.domain.model.service.ProductServiceModel;
import com.ppetrov.eshop.domain.model.view.ProductAllViewModel;
import com.ppetrov.eshop.service.CategoryService;
import com.ppetrov.eshop.service.CloudinaryService;
import com.ppetrov.eshop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProduct(ModelAndView modelAndView){
        modelAndView.addObject("categories", categoryService.findAllCategories());
        return view("product/add-product", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProductConfirm(@ModelAttribute ProductAddBindingModel model) throws IOException {
        ProductServiceModel productServiceModel = modelMapper.map(model, ProductServiceModel.class);
        productServiceModel.setCategories(
                categoryService.findAllCategories()
                .stream()
                .filter(categoryServiceModel -> model.getCategories().contains(categoryServiceModel.getId()))
                .collect(Collectors.toList())
        );
        productServiceModel.setImageUrl(
                cloudinaryService.uploadImage(model.getImage())
        );
        productService.addProduct(productServiceModel);
        return redirect("/products/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allProducts(ModelAndView modelAndView){
        modelAndView.addObject("products", productService.findAllProducts()
                                .stream()
                                .map(productServiceModel -> modelMapper.map(productServiceModel, ProductAllViewModel.class))
                                .collect(Collectors.toList()));
        return view("product/all-products", modelAndView);
    }


}
