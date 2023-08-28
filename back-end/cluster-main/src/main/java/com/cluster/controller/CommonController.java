package com.cluster.controller;

import com.cluster.pojo.ApiResponse;
import com.cluster.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 公共模块控制中心
 * @Author: junqiang.lu
 * @Date: 2018/12/24
 */
@RestController
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private CommonService commonService;

    /**
     * 上传文件至阿里云 oss
     *
     * @param file
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/oss", method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse uploadOSS(@RequestParam(value = "file") MultipartFile file) throws Exception {
        System.out.println(file.getInputStream());
        commonService.uploadOSS(file, "avatar");

        HttpHeaders headers = new HttpHeaders();

        return ApiResponse.success("上传成功");
    }

    @RequestMapping("/delete/oss")
    public ApiResponse deltetOss(String objectName){
        System.out.println(objectName+"-------------------------------");
        commonService.delete(objectName);
        return ApiResponse.success("删除成功");
    }

}