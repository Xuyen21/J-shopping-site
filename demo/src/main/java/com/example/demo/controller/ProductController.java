package com.example.demo.controller;

import com.example.demo.dtos.AddProductDTO;
import com.example.demo.dtos.ProductDto;
import com.example.demo.dtos.ProductUpdateDTO;
import com.example.demo.model.Product;
import com.example.demo.response.APIResponse;
import com.example.demo.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllProducts() {
        try {
            List<ProductDto> convertedProducts = productService.getConvertedProducts();
            return ResponseEntity.ok(new APIResponse("products retrieved", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(" no product found", null));
        }
    }

    @GetMapping("product/{productId}/product")
    public ResponseEntity<APIResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new APIResponse("product retrieved", product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addProduct(@RequestBody AddProductDTO addProductDTO) {
        try {
            Product theProduct = productService.addProduct(addProductDTO);
            return ResponseEntity.ok(new APIResponse("product added", theProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PutMapping("product/{productId}/product")
    public ResponseEntity<APIResponse> updateProduct(@RequestBody ProductUpdateDTO productUpdateDTO, @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(productUpdateDTO, productId);
            return ResponseEntity.ok(new APIResponse("product updated", theProduct));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("product/{productId}/product")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new APIResponse("product deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<APIResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("no product found", null));
            }
            return ResponseEntity.ok(new APIResponse("get prodct by brand and name success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<APIResponse> getProductByCategoryAndBrand(@RequestParam String brandName, @RequestParam String category) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("no product found", null));
            }
            return ResponseEntity.ok(new APIResponse("get prodct by brand and category success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by-brand")
    public ResponseEntity<APIResponse> getProductByBrand(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("no product found", null));
            }
            return ResponseEntity.ok(new APIResponse("get product by brand success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by-category")
    public ResponseEntity<APIResponse> getProductByCategory(@RequestParam String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("no product found", null));
            }
            return ResponseEntity.ok(new APIResponse("get product by brand success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count/products/by/brand&name")
    public ResponseEntity<APIResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long countProduct = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new APIResponse("product count", countProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

}
