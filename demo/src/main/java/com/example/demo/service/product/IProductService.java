package com.example.demo.service.product;


import com.example.demo.dtos.AddProductDTO;
import com.example.demo.dtos.ProductDto;
import com.example.demo.dtos.ProductUpdateDTO;
import com.example.demo.model.Product;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductDTO product);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(ProductUpdateDTO product, Long productId);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String categpry, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts();

}
