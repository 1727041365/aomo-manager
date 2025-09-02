package com.yupi.springbootinit.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsDetailVo implements Serializable {
    /**
     * 内容标题
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 发布时间
     */
    private Date publishDate;
    /**
     * 内容
     */
    private String content;
}
