package com.yupi.springbootinit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 图片上传相关工具方法
 */
@Slf4j
public class ImageUploadUtil {

    // 图片保存路径（实际项目建议配置在 application.properties 中）
    private static final String IMAGE_SAVE_PATH = "./school/img/";
    // 视频保存路径（实际项目建议配置在 application.properties 中）
    private static final String VIDEO_SAVE_PATH = "./school/video/";
    // 图片访问URL前缀（用于拼接最终访问路径）
    private static final String IMAGE_ACCESS_PREFIX = "https://manager.impactflowth.org/aomoImages/";
    // 视频访问URL前缀（用于拼接最终访问路径）
    private static final String VIDEO_ACCESS_PREFIX = "https://manager.impactflowth.org/aomoVideo/";
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

    /**
     * 解析富文本中的图片并保存，替换为服务器上的图片路径
     */
    public static String parseAndSaveImages(String richText) throws Exception {
        // 使用Jsoup解析HTML
        Document doc = Jsoup.parse(richText);
        Elements imgElements = doc.select("img"); // 选择所有img标签

        for (Element img : imgElements) {
            String imgSrc = img.attr("src");
            if (imgSrc == null || imgSrc.isEmpty()) {
                continue;
            }

            // 处理Base64格式的图片（通常以data:image/开头）
            if (imgSrc.startsWith("data:image/")) {
                String imagePath = saveBase64Image(imgSrc);
                // 替换img标签的src为服务器上的图片URL
                img.attr("src", IMAGE_ACCESS_PREFIX + imagePath);
            }
            // 处理网络URL图片（可选，根据实际需求决定是否下载）
            else if (imgSrc.startsWith("http://") || imgSrc.startsWith("https://")) {
                String imagePath = downloadImageFromUrl(imgSrc);
                img.attr("src", IMAGE_ACCESS_PREFIX + imagePath);
            }
            // 其他情况（如已存在的本地路径，可根据需要处理）
        }
        // 返回处理后的富文本内容
        return doc.html();
    }

    /**
     * 保存Base64编码的图片到服务器
     */
    public static String saveBase64Image(String base64Str) throws Exception {
        // 提取Base64数据部分（去除前缀）
        String[] base64Parts = base64Str.split(",");
        if (base64Parts.length < 2) {
            throw new Exception("无效的Base64图片格式");
        }

        // 提取图片格式（如png、jpg等）
        String format = base64Parts[0].split(";")[0].split("/")[1];

        // 生成唯一文件名，避免重复
        String fileName = UUID.randomUUID().toString() + "." + format;
        String fullPath = IMAGE_SAVE_PATH + fileName;

        // 创建保存目录（如果不存在）
        File dir = new File(IMAGE_SAVE_PATH);
        log.info("目录路径: {}, 是否存在: {}, 绝对路径: {}", IMAGE_SAVE_PATH, dir.exists(), dir.getAbsolutePath());
        if (!dir.exists()) {
            log.error("无法创建目录: {}", dir.getAbsolutePath());
            dir.mkdirs();
        }
        // 解码并保存图片
        byte[] imageBytes = Base64.getDecoder().decode(base64Parts[1]);
        try (OutputStream out = new FileOutputStream(fullPath)) {
            out.write(imageBytes);
        }

        return fileName;
    }

    /**
     * 从网络URL下载图片并保存到服务器
     */
    public static String downloadImageFromUrl(String imageUrl) throws Exception {
        // 这里使用Java原生URL连接，也可以用HttpClient等库
        java.net.URL url = new java.net.URL(imageUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        // 获取图片格式
        String contentType = conn.getContentType();
        String format = contentType.split("/")[1];
        if (format.equals("jpeg")) format = "jpg"; // 统一格式

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "." + format;
        String fullPath = IMAGE_SAVE_PATH + fileName;

        // 读取网络流并保存到文件
        try (java.io.InputStream in = conn.getInputStream();
             OutputStream out = new FileOutputStream(fullPath)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } finally {
            conn.disconnect();
        }

        return fileName;
    }

    /**
     * 保存上传的视频文件
     * @param videoFile 前端上传的视频文件（MultipartFile 类型）
     * @return 视频在服务器的访问路径，若失败则返回 null
     */
    public static String saveVideoFile(MultipartFile videoFile) {
        // 参数校验：文件为空或无文件名时直接返回
        if (videoFile == null || videoFile.isEmpty()) {
            return null;
        }
        try {
            // 获取原始文件名（用于提取后缀）
            String originalFilename = videoFile.getOriginalFilename();
            if (originalFilename == null) {
                return null;
            }
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成唯一文件名（避免重复）
            String fileName = UUID.randomUUID().toString() + suffix;
            String fullPath = VIDEO_SAVE_PATH + fileName;
            // 创建保存目录（如果不存在）
            File dir = new File(VIDEO_SAVE_PATH);
            if (!dir.exists()) {
                boolean mkdirsResult = dir.mkdirs();
                if (!mkdirsResult) {
                    throw new RuntimeException("创建视频保存目录失败：" + VIDEO_SAVE_PATH);
                }
            }
            // 将 MultipartFile 写入目标文件
            videoFile.transferTo(new File(fullPath));
            // 拼接并返回可访问的视频URL
            return VIDEO_ACCESS_PREFIX + fileName;
        } catch (IOException e) {
            // 实际项目中建议用日志框架（如 Slf4j）记录异常
            e.printStackTrace();
            return null;
        }
    }


}