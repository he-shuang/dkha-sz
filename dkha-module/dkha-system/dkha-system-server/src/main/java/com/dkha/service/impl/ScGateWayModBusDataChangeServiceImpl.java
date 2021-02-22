package com.dkha.service.impl;

import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.dto.ScGatewaydcDTO;
import com.dkha.service.ScGateWayModBusDataChangeService;
import com.dkha.service.ScGatewaydcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网关下设备绑定信息变更的时候需要进行修改
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/9/2 0002 11:36
 */
@Service
public class ScGateWayModBusDataChangeServiceImpl implements ScGateWayModBusDataChangeService {


    /** redis工具类*/
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    ScGatewaydcService scGatewaydcService;

    @Override
    public void sendGateWayModBusChange(String gwid, boolean needChange) {

        ScGatewaydcDTO scGatewaydcDTO= scGatewaydcService.get(gwid);
        if(scGatewaydcDTO!=null){
            redisUtils.set("GateWay."+scGatewaydcDTO.getGwIpgateway(),needChange);//设置网关变更事件，事件默认时间为24小时
        }

    }
}
