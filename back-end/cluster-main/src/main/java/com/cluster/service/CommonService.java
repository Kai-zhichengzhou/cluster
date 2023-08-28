package com.cluster.service;


import org.springframework.web.multipart.MultipartFile;

public interface CommonService {
    /**
     * 上传文件至阿里云
     */
    void uploadOSS(MultipartFile file, String folder) throws Exception;

    void delete(String objectName);
}
