package com.example.demo.service.product;

import com.example.demo.dtos.AddProductDTO;
import com.example.demo.dtos.ImageDto;
import com.example.demo.dtos.ProductDto;
import com.example.demo.dtos.ProductUpdateDTO;
import com.example.demo.exeptions.ProductNotFoundExeption;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ImageRepo;
import com.example.demo.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        return productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExeption("product not found oh oh"));
    }

    @Override
    public ProductDto responseGetProductById(Long id) {
        Product product = getProductById(id);
        return modelMapper.map(product, ProductDto.class);
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
    public ProductDto updateProduct(ProductUpdateDTO productUpdateDTO, Long productId) {
        Product product = productRepo.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, productUpdateDTO))
//                .map(productRepo::save)
                .orElseThrow(() -> new ProductNotFoundExeption("Product not found to update"));

        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        productRepo.save(product);
        return productDto;
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

    // map product with ProductDto
    @Override
    public List<ProductDto> getConvertedProducts() {
        List<Product> products = productRepo.findAll();

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
    public List<ProductDto> getProductsByBrand(String brand) {
        List<Product> products = productRepo.findByBrand(brand);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByCategoryAndBrand(String someCategory, String brand) {

        List<Product> products = productRepo.findByCategoryNameAndBrand(someCategory, brand);
        return products.stream().
                map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByName(String name) {

        List<Product> products = productRepo.findByName(name);
        if (products.isEmpty()) {
            throw new ProductNotFoundExeption("no product with name %s".formatted(name));
        }
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByBrandAndName(String brand, String name) {

        List<Product> products = productRepo.findByBrandAndName(brand, name);
        return products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {

        return productRepo.countByBrandAndName(brand, name);
    }

}
