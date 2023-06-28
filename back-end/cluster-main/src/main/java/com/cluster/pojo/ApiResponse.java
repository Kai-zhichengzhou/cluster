package com.cluster.pojo;


import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定义一个通用的结果对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private int code;
    private String message;
    private Object object;


    /**
     * 成功返回的结果
     * @param message
     * @return
     */
    public static ApiResponse success(String message)
    {
        //返回一个状态码为200的成功响应
        return new ApiResponse(200, message, null);
    }
    public static ApiResponse success(String message,Object object)
    {
        //返回带有对象的成功响应
        return new ApiResponse(200, message,object);

    }
    /**
     * 失败返回的结果
     */
    public static ApiResponse error(String message)
    {
        return new ApiResponse(500, message, null);
    }
    public static ApiResponse error(String message, Object object)
    {
        return new ApiResponse(500, message, object);
    }



}
