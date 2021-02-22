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
public class ScStatisticsMonthExcel implements Serializable {
	private static final long serialVersionUID = 1L;

/*	@Excel(name = "周数")
	private String week;*/
	/**
	 * 年月
	 */@Excel(name = "月数")
	private String months;
	/**
	 * 没有进没有出 次数
	 */
	@Excel(name = "未进未出数量")
	private int type1;
	/**
	 * 只有进 次数
	 */
	@Excel(name = "只有进数量")
	private int type2;
	/**
	 * 只有出 次数
	 */
	@Excel(name = "只有出数量")
	private int type3;
}
