package com.cluster.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    String storeFile(MultipartFile file, String uploadDir);


    Path loadFile(String filename, String uploadDir);
}
