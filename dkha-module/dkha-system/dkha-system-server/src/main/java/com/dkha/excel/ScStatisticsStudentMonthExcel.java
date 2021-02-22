package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-27
 */
@Data
public class ScStatisticsStudentMonthExcel implements Serializable {
	private static final long serialVersionUID = 1L;
/*	@Excel(name = "周数")
	private String weeks;*/
	/**
	 * 年月
	 */@Excel(name = "月数")
	private String months;
	/**
	 * 没有进没有出 次数
	 */
	@Excel(name = "未进未出人数")
	private int typestudent1;
	/**
	 * 只有进 人数
	 */
	@Excel(name = "只有进人数")
	private int typestudent2;
	/**
	 * 只有出 人数
	 */
	@Excel(name = "只有出人数")
	private int typestudent3;
}
