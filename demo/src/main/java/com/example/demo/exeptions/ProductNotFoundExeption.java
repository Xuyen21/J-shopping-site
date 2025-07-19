package com.example.demo.exeptions;

public class ProductNotFoundExeption extends RuntimeException {
    public ProductNotFoundExeption(String message) {
        super(message);
    }
}
