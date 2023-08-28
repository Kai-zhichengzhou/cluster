package com.cluster.utils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.ObjectMetadata;
import com.cluster.config.OSSConfig;
import com.cluster.pojo.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class OSSBootUtil {



    private OSSBootUtil(){}

    /**
     * 工具客户端
     */
    private volatile static OSSClient ossClient = null;

    /**
     * 上传文件至阿里云OSS
     * 文件上传成功，返回文件完整的访问路径
     * 失败则返回null
     */

    public static String upload(OSSConfig ossConfig, MultipartFile file, String fileDir)
    {
        initOSS(ossConfig);
        StringBuilder fileUrl = new StringBuilder();
        try{
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0,18) + suffix;
            if(!fileDir.endsWith("/"))
            {
                fileDir = fileDir.concat("/");
            }
            fileUrl = fileUrl.append(fileDir + fileName);
            System.out.println(fileUrl+"-----------------");
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            ossClient.putObject(ossConfig.getBucketName(), fileUrl.toString(), file.getInputStream(),objectMetadata);

        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
        fileUrl = fileUrl.insert(0, ossConfig.getUrl());
        System.out.println(fileUrl);
        return fileUrl.toString();
    }

    /**
     * 初始化 oss 客户端
     */
    public static OSSClient initOSS(OSSConfig ossConfig)
    {
        if(ossClient == null)
        {
            synchronized (OSSBootUtil.class)
            {
                if(ossClient == null)
                {
                    ossClient = new OSSClient(ossConfig.getEndPoint(),
                            new DefaultCredentialProvider(ossConfig.getAccessKeyId(),
                                    ossConfig.getAccessKeySecret()), new ClientConfiguration());

                }
            }
        }
        return ossClient;
    }

    /**
     * 根据前台传过来的文件地址 删除文件
     * @author jiangpengcheng
     * @param objectName
     * @param ossConfig
     * @return
     */
    public static ApiResponse delete(String objectName, OSSConfig ossConfig) {
        initOSS(ossConfig);
        //将完整路径替换成 文件地址 因为yml里的url有了地址链接https: //oos-all.oss-cn-shenzhen.aliyuncs.com/
        // 如果再加上地址 那么又拼接了 所以删除不了 要先把地址替换为 jpc/2020-07-16/1594857669731-51d057b0-9778-4aed.png
        String fileName = objectName.replace("https://clusterbucket01.oss-cn-hongkong.aliyuncs.com", "");
        System.out.println(fileName+"******************************");
        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(ossConfig.getBucketName(), fileName);

        return ApiResponse.success("删除成功",fileName);
    }
}
