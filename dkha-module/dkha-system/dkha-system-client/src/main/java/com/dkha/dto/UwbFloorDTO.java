package com.dkha.dto;

import lombok.Data;

import java.util.List;

/**
 * Copyright(C) 2013-2020 电科惠安公司 Inc.ALL Rights Reserved.
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-08-29 17:50
 */
@Data
public class UwbFloorDTO {


        public  String label;
        public  String value;
        public List<UwbFloorDTO> children;

}
