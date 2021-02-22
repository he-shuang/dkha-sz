

package com.dkha.commons.tools.constant;

import java.util.regex.Pattern;

/**
 * 常量
 *
 * @since 1.0.0
 */
public interface Constant {
    /**
     * 成功
     */
    int SUCCESS = 1;

    //正则，字符是否数字
    Pattern PATTERNNUM = Pattern.compile("^[-\\+]?[\\d]*$");
    //ip正则
    Pattern PATTERNIP = Pattern.compile("^((2[0-4][0-9])|(25[0-5])|(1[0-9]{0,2})|([1-9][0-9])|([1-9]))\\.(((2[0-4][0-9])|(25[0-5])|(1[0-9]{0,2})|([1-9][0-9])|([0-9]))\\.){2}((2[0-4][0-9])|(25[0-5])|(1[0-9]{0,2})|([1-9][0-9])|([1-9]))$");


    /**
     * 失败
     */
    int FAIL = 0;

    /**
     * 空数据过滤
     */
    String nullString = "空";
    /**
     * 重复编号
     */
    String repeatString = "重复";

    /**
     * 己过期
     */
    String expiredString = "己过期";
    /**
     * 己存在
     */
    String existString = "己存在";
    /**
     * 不存在
     */
    String nExistString = "不存在";
    /**
     * 检测端编号不是数字
     */
    String nNum = "检测端编号不是数字:";
    /**
     * 总线不是数字
     */
    String lineNum = "总线不是数字:";
    /**
     * 通讯地址编码不是数字:
     */
    String gbdAddr = "通讯地址编码不是数字:";
    /**
     * IP格式不正确
     */
    String errorIp = "IP格式不正确:";
    /**
     * 其他错误
     */
    String otherError = "其他错误:";
    /**
     * OK
     */
    String OK = "OK";
    /**
     * 用户标识
     */
    String USER_KEY = "userId";
    /**
     * 菜单根节点标识
     */
    Long MENU_ROOT = 0L;
    /**
     * 部门根节点标识
     */
    Long DEPT_ROOT = 0L;
    /**
     * 数据字典根节点标识
     */
    Long DICT_ROOT = 0L;
    /**
     * 升序
     */
    String ASC = "asc";
    /**
     * 降序
     */
    String DESC = "desc";
    /**
     * 删除字段名
     */
    String DEL_FLAG = "del_flag";
    /**
     * 创建时间字段名
     */
    String CREATE_DATE = "create_date";

    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sqlFilter";
    /**
     * 当前页码
     */
    String PAGE = "page";
    /**
     * 每页显示记录数
     */
    String LIMIT = "limit";
    /**
     * 排序字段
     */
    String ORDER_FIELD = "orderField";
    /**
     * 排序方式
     */
    String ORDER = "order";
    /**
     * token header
     */
    String TOKEN_HEADER = "token";

    /**
     * 总线字典类型
     */
    String dataType = "bus";
}
