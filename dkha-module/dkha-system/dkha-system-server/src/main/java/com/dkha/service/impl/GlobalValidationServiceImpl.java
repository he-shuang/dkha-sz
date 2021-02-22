package com.dkha.service.impl;

import com.dkha.dao.GlobalValidationDao;
import com.dkha.service.GlobalValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-09-09 9:57
 */

@Service
public class GlobalValidationServiceImpl  implements GlobalValidationService {


    @Autowired
    private GlobalValidationDao globalValidationDao;

    @Override
    public Boolean checkUwb(Long id,String uwb) {
        int count = globalValidationDao.checkUwb(id, uwb);
        if(count > 0){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkRfid(Long id, String rfid) {
        int count = globalValidationDao.checkRfid(id,rfid);
        if(count > 0){
            return false;
        }
        return true;
    }
}
