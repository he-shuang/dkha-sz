package com.dkha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 学生未归寝每日统计
 *
 * @author Mark
 * @since v1.0.0 2020-10-09
 */
@Data
@ApiModel(value = "学生未归寝每日统计")
public class ScStudentsOutHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "统计时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date date;

	@ApiModelProperty(value = "人数")
	private Integer num;

}
