package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;


//@Data
//@AllArgsConstructor
//public class APIResponse<T> {
//    private String message;
//    private T data;
//}
@Data
@AllArgsConstructor
public class APIResponse {
    private String message;
    private Object data;
}
