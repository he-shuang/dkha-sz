package com.dkha.service;

/**
 * Copyright(C) 2013-2020 电科惠安公司 Inc.ALL Rights Reserved.
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-09-09 9:57
 */
public interface GlobalValidationService {
    Boolean checkUwb(Long id,String uwb);

    Boolean checkRfid(Long id, String rfid);
}
