package com.cluster.service.Impl;

import com.cluster.config.OSSConfig;
import com.cluster.pojo.ApiResponse;
import com.cluster.service.CommonService;
import com.cluster.utils.OSSBootUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    @Value("${oss.filehost}")
    private String fileHost;

    @Autowired
    private OSSConfig ossConfig;

    /**
     * 上传文件至阿里云 oss
     *
     * @param file
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public void uploadOSS(MultipartFile file, String folder) throws Exception {

        // 格式化时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        // 高依赖版本 oss 上传工具
        String ossFileUrlBoot = null;
        /**
         * ossConfig 配置类
         * file 文件
         * "jpc/"+format 上传文件地址 加时间戳
         */
        String fileDir = fileHost + "/" + folder + "/" + folder;
        System.out.println(fileDir);
        ossFileUrlBoot = OSSBootUtil.upload(ossConfig, file, fileDir);
        System.out.println(ossFileUrlBoot);
        Map<String, Object> resultMap = new HashMap<>(16);
//        resultMap.put("ossFileUrlSingle", ossFileUrlSingle);
        resultMap.put("ossFileUrlBoot", ossFileUrlBoot);

    }

    @Override
    public void delete(String objectName) {
        OSSBootUtil.delete(objectName, ossConfig);

    }
}