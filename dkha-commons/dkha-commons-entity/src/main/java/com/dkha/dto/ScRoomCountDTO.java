package com.dkha.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;

import java.io.Serializable;
import java.util.*;


/**
 * 学生档案信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "学生档案信息")
public class ScRoomCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "1 没有出入记录的 2 只有进的记录 3. 只有出的记录")
	private String typr;

	@ApiModelProperty(value = "数量")
	private String sumcount;

	@ApiModelProperty(value = "周")
	private String weeks;

	@ApiModelProperty(value = "月")
	private String months;

    @ApiModelProperty(value = "学生档案")
    private String scStdid;

    @ApiModelProperty(value = "学生姓名")
    private String scStuname;


	@ApiModelProperty(value = "没有出入记录的")
	private String type1;
	@ApiModelProperty(value = "只有进的记录")
	private String type2;
	@ApiModelProperty(value = "只有出的记录")
	private String type3;
	public String getTypr() {
		return typr;
	}

	public void setTypr(String typr) {
		this.typr = "1".equals(typr) ? "未进未出" : "2".equals(typr) ? "只进未出" : "只出未进";
	}



}
