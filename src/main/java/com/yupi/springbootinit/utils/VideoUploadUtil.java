package com.yupi.springbootinit.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class VideoUploadUtil {

    // 视频保存路径（实际项目建议配置在 application.properties 中）
    private static final String VIDEO_SAVE_PATH = "./school/video/";

    // 视频访问URL前缀（用于拼接最终访问路径）
    private static final String VIDEO_ACCESS_PREFIX = "https://manager.impactflowth.org/aomoVideo/";

    /**
     * 保存上传的视频文件并压缩
     * @param videoFile 前端上传的视频文件（MultipartFile 类型）
     * @return 视频在服务器的访问路径，若失败则返回 null
     */
    public static String saveVideoFile(MultipartFile videoFile) throws IOException {
        // 参数校验：文件为空或无文件名时直接返回
        if (videoFile == null || videoFile.isEmpty()) {
            return null;
        }

        try {
            // 检查文件大小是否超过100MB（你原来的限制）
            if (videoFile.getSize() > 100 * 1024 * 1024L) {
                // 先保存原始文件
                String originalFilename = videoFile.getOriginalFilename();
                if (originalFilename == null) {
                    return null;
                }
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String tempFileName = UUID.randomUUID().toString() + "_original" + suffix;
                String tempPath = VIDEO_SAVE_PATH + tempFileName;

                // 创建保存目录（如果不存在）
                File dir = new File(VIDEO_SAVE_PATH);
                if (!dir.exists()) {
                    boolean mkdirsResult = dir.mkdirs();
                    if (!mkdirsResult) {
                        throw new RuntimeException("创建视频保存目录失败：" + VIDEO_SAVE_PATH);
                    }
                }

                // 保存原始文件
                File originalFile = new File(tempPath);
                videoFile.transferTo(originalFile);

                // 压缩文件
                String compressedFileName = UUID.randomUUID().toString() + suffix;
                String compressedPath = VIDEO_SAVE_PATH + compressedFileName;

                if (compressVideo(tempPath, compressedPath)) {
                    // 检查压缩后的文件是否存在且大小合理
                    File compressedFile = new File(compressedPath);
                    if (compressedFile.exists() && compressedFile.length() > 0) {
                        // 删除原始文件
                        originalFile.delete();
                        // 返回压缩后的文件路径
                        return VIDEO_ACCESS_PREFIX + compressedFileName;
                    } else {
                        // 压缩后文件有问题，删除并保留原始文件
                        compressedFile.delete();
                        return VIDEO_ACCESS_PREFIX + tempFileName;
                    }
                } else {
                    // 压缩失败，保留原始文件
                    return VIDEO_ACCESS_PREFIX + tempFileName;
                }
            } else {
                // 文件大小在限制内，直接保存
                String originalFilename = videoFile.getOriginalFilename();
                if (originalFilename == null) {
                    return null;
                }
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
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
            }
        } catch (IOException e) {
            // 实际项目中建议用日志框架（如 Slf4j）记录异常
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 压缩视频文件
     * @param sourceFilePath 源视频文件路径
     * @param targetFilePath 目标视频文件路径
     * @return 压缩是否成功
     */
    public static boolean compressVideo(String sourceFilePath, String targetFilePath) {
        try {
            // 优化的FFmpeg命令，确保更好的兼容性和播放效果
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg",
                    "-i", sourceFilePath,           // 输入文件
                    "-vcodec", "libx264",           // 使用H.264编码（广泛兼容）
                    "-crf", "28",                   // 压缩质量，18-28范围，数值越大压缩率越高
                    "-preset", "medium",            // 编码速度/压缩比权衡（medium更平衡）
                    "-acodec", "aac",               // 音频编码（广泛兼容）
                    "-b:a", "128k",                 // 音频比特率
                    "-ar", "44100",                 // 音频采样率
                    "-pix_fmt", "yuv420p",          // 像素格式（关键！确保兼容性）
                    "-movflags", "+faststart",      // 优化网络播放
                    "-profile:v", "baseline",       // 视频配置（提高兼容性）
                    "-level", "3.0",                // 编码级别
                    targetFilePath
            );
            // 设置工作目录和环境变量
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
