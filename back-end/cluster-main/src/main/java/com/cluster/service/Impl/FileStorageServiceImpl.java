package com.cluster.service.Impl;

import com.cluster.exception.StorageException;
import com.cluster.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path rootLocation; // 定义存储文件的根路径

    @Autowired
    public FileStorageServiceImpl(@Value("${file.storage-location}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
    }

    /**
     * 存储文件到系统
     * @param file 文件
     * @return 文件名
     */
    @Override
    public String storeFile(MultipartFile file) {
        try {
            // 检查文件是否为空
            System.out.println(file);
            if (file.isEmpty()) {
                throw new StorageException("无法存储空文件 " + file.getOriginalFilename());
            }
            // 创建一个新的文件名，该文件名是基于UUID的，且保留了原始文件的扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String newFileName = StringUtils.cleanPath(UUID.randomUUID().toString()  + extension);

            try (InputStream inputStream = file.getInputStream()) {
                System.out.println("Current working directory: " + System.getProperty("user.dir"));

                System.out.println("it runs here");
                Files.copy(inputStream, this.rootLocation.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            // 返回新的文件名
            return newFileName;
        } catch (IOException e) {
            // 如果发生任何异常，包装并抛出StorageException
            e.printStackTrace();
            throw new StorageException("无法存储文件 " + file.getOriginalFilename());
        }
    }

    /**
     * 加载文件
     * @param filename 文件名
     * @return 路径
     */
    @Override
    public Path loadFile(String filename) {
        // 根据文件名解析文件路径
        return rootLocation.resolve(filename);
    }
}

