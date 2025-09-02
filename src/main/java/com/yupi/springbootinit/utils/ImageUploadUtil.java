package com.yupi.springbootinit.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 图片上传相关工具方法
 */
public class ImageUploadUtil {

    // 图片保存路径（实际项目建议配置在 application.properties 中）
    private static final String IMAGE_SAVE_PATH = "C:/school/img/";
    // 图片访问URL前缀（用于拼接最终访问路径）
    private static final String IMAGE_ACCESS_PREFIX = "http://localhost:8108/images/";

    /**
     * 保存上传的单张图片
     * @param imageFile 前端上传的图片文件（MultipartFile 类型）
     * @return 图片在服务器的访问路径，若失败则返回 null
     */
    public static String saveSingleImage(MultipartFile imageFile) {
        // 参数校验：文件为空或无文件名时直接返回
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }
        try {
            // 获取原始文件名（用于提取后缀）
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename == null) {
                return null;
            }
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成唯一文件名（避免重复）
            String fileName = UUID.randomUUID().toString() + suffix;
            String fullPath = IMAGE_SAVE_PATH + fileName;
            // 创建保存目录（如果不存在）
            File dir = new File(IMAGE_SAVE_PATH);
            if (!dir.exists()) {
                boolean mkdirsResult = dir.mkdirs();
                if (!mkdirsResult) {
                    throw new RuntimeException("创建图片保存目录失败：" + IMAGE_SAVE_PATH);
                }
            }
            // 将 MultipartFile 写入目标文件
            imageFile.transferTo(new File(fullPath));
            // 拼接并返回可访问的图片URL
            return IMAGE_ACCESS_PREFIX + fileName;
        } catch (IOException e) {
            // 实际项目中建议用日志框架（如 Slf4j）记录异常
            e.printStackTrace();
            return null;
        }
    }
}