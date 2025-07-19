package com.example.demo.exeptions;

public class ImageNotFound extends RuntimeException{
    public ImageNotFound(String message) {
        super(message);
    }
}
