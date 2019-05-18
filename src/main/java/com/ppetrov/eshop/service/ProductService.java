package com.ppetrov.eshop.service;

import com.ppetrov.eshop.domain.model.service.ProductServiceModel;

import java.util.List;

public interface ProductService {

    ProductServiceModel addProduct(ProductServiceModel productServiceModel);

    List<ProductServiceModel> findAllProducts();

}
