package com.dkha.dto;/**
 * TODO
 *
 * @version 1.0
 * @author Administrator
 * @date 2020/8/28 20:10
 */

import lombok.Data;

/**
 *
 * @Author yangjun
 * @Date2020/8/28 20:10
 */
@Data
public class DeviceMassge {

    private String deid;

    private String devicename;

    private String type;
    private Integer status;

    private String operation;

}