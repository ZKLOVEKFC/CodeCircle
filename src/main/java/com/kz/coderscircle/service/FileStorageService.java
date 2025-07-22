package com.kz.coderscircle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file-storage.path}")
    private String storagePath;

    /**
     * 将上传的文件保存到本地磁盘
     file 用户上传的文件
     */
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // 获取文件扩展名
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 生成唯一的UUID文件名
        String fileName = UUID.randomUUID().toString() + extension;

        // 创建目标文件对象
        File destFile = new File(storagePath + fileName);

        // 确保父目录存在
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        // 将上传的文件内容传输到目标文件
        file.transferTo(destFile);

        // 返回新的文件名
        return fileName;
    }
}