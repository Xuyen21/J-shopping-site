package com.example.demo.service.product;

import com.example.demo.dtos.AddProductDTO;
import com.example.demo.dtos.ImageDto;
import com.example.demo.dtos.ProductDto;
import com.example.demo.dtos.ProductUpdateDTO;
import com.example.demo.exeptions.ProductNotFoundExeption;
import com.example.demo.model.Category;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ImageRepo;
import com.example.demo.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final ImageRepo imageRepo;

    private Product createProduct(AddProductDTO request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product addProduct(AddProductDTO addProductDTO) {
        // if category exists -> set it as the category of the product
        // if not exist -> save to Category then set new product

        Category category = Optional.ofNullable(categoryRepo.findByName(addProductDTO.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(addProductDTO.getCategory().getName());
                    return categoryRepo.save(newCategory);
                });

        addProductDTO.setCategory(category);
        return productRepo.save(createProduct(addProductDTO, category));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new ProductNotFoundExeption("product not found oh oh"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepo.findById(id)
                .ifPresentOrElse(productRepo::delete,
                        () -> {
                            throw new ProductNotFoundExeption("product not found");
                        });
    }

    @Override
    public Product updateProduct(ProductUpdateDTO productUpdateDTO, Long productId) {
        return productRepo.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, productUpdateDTO))
                .map(productRepo::save)
                .orElseThrow(() -> new ProductNotFoundExeption("Product not found to update"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateDTO productUpdateDTO) {
        existingProduct.setName(productUpdateDTO.getName());
        existingProduct.setBrand(productUpdateDTO.getBrand());
        existingProduct.setPrice(productUpdateDTO.getPrice());
        existingProduct.setInventory(productUpdateDTO.getInventory());
        existingProduct.setDescription(productUpdateDTO.getDescription());
        Category someCategory = categoryRepo.findByName(productUpdateDTO.getCategory().getName());
        existingProduct.setCategory(someCategory);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepo.findAll();
    }

    // map product with ProductDto
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
                .map(product -> {
                    ProductDto productDto = modelMapper.map(product, ProductDto.class);
                    productDto.setImages(product.getImages().stream()
                            .map(image -> modelMapper.map(image, ImageDto.class))
                            .toList());
                    return productDto;
                })
                .toList();

    }

    @Override
    public List<Product> getProductsByCategory(String someCategory) {

        return productRepo.findByCategoryName(someCategory);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {

        return productRepo.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String someCategory, String brand) {

        return productRepo.findByCategoryNameAndBrand(someCategory, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {

        return productRepo.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {

        return productRepo.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {

        return productRepo.countByBrandAndName(brand, name);
    }


}
