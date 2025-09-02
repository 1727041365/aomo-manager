package com.yupi.springbootinit.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;


@Data
public class NewsMoreVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 标题
     */
    private String homeTitle;
    /**
     * 详情
     */
    private String homeDeatil;
}
