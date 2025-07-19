package com.example.demo.service.category;


import com.example.demo.exeptions.AlreadyExists;
import com.example.demo.exeptions.CategoryNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepo categoryRepo;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElseThrow(() -> new CategoryNotFoundException("categorynotfound"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepo.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.ofNullable(category)
                .filter(c -> !categoryRepo.existsByName(c.getName()))
                .map(categoryRepo::save)
                .orElseThrow(() ->
                        new AlreadyExists("category already exists")
                );
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category oldCategory = getCategoryById(id);
        oldCategory.setName(category.getName());
        return categoryRepo.save(oldCategory);
//        return Optional.ofNullable(getCategoryById(id))
//                .map(oldCategory -> {
//                    oldCategory.setName(category.getName());
//                    return categoryRepo.save(oldCategory);
//                }).orElseThrow(() -> new CategoryNotFoundException("category not found for updating"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepo.findById(id).ifPresentOrElse(categoryRepo::delete, () -> {
            throw new CategoryNotFoundException("for delete, category is not found");
        });

    }
}
