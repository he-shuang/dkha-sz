package com.dkha.service;

/**
 * 网关下Modbus数据发生变更的时候需要发送消息通知
 */
public interface ScGateWayModBusDataChangeService {

     void sendGateWayModBusChange(String gwid, boolean needChange);

}
