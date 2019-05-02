package com.ppetrov.eshop.service;

import com.ppetrov.eshop.domain.entity.Category;
import com.ppetrov.eshop.domain.model.service.CategoryServiceModel;
import com.ppetrov.eshop.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {
        Category category = modelMapper.map(categoryServiceModel, Category.class);
        return modelMapper.map(categoryRepository.saveAndFlush(category), CategoryServiceModel.class);
    }
}
