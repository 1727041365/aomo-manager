package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 新闻
 *
 * @author frk frk@126.com
 * @since 1.0.0 2025-08-08
 */

@Data
@TableName("news")
public class NewsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 缩略图URL
	 */
	private String thumbnail;
	/**
	 * 内容标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 首页标题
	 */
	private String homeTitle;
	/**
	 * 首页内容
	 */
	private String homeDeatil;
	/**
	 * 内容分类
	 */
	private String category;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 状态
	 */
	private String status;
	/**
	 * 轮播
	 */
	private String carousel;

	/**
	 * 发布时间
	 */
	private Date publishDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
}