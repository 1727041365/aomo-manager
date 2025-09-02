package com.yupi.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * 新闻
 *
 * @author frk frk@126.com
 * @since 1.0.0 2025-08-08
 */

@Data
@TableName("news")
public class NewsCarouselVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	* id
	*/
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;
	/**
	* 内容标题
	*/
	private String homeTitle;
	/**
	 * 内容
	 */
	private String homeDeatil;
	/**
	 * 内容分类
	 */
	private String category;
	/**
	 * 轮播排序
	 */
	private String carousel;
}