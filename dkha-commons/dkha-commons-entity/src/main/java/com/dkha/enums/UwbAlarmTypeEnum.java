package com.dkha.enums;

/**
 * UWB报警类型
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/30 11:49
 */
public enum UwbAlarmTypeEnum {
//            | 1    | 越界     |
//            | ---- | -------- |
//            | 2    | 超速     |
//            | 3    | 低电量   |
//            | 4    | 入界     |
//            | 6    | 超高     |
//            | 7    | 逗留     |
//            | 10   | 心率异常 |
//            | 11   | 体温异常 |
//            | 12   | 超时休眠 |
//            | 15   | 保密区域 |
//            | 104  | 拆卸     |
//            | 105  | 求救     |
//            | 106  | 低电量   |
//            | 107  | 超时休眠 |
//            | 108  | 手环摘除 |
//            | 109  | 心率异常 |
//            | 121  | 访客楼层 |
//            | 203  | 入界事件 |


    CROSS_BORDER(1),    //越界
    EXCEED_SPEED(2),    //超速
    LOW_POWER(3),       //低电量 手动设置阈值表示低电量
    ENTRY_BORDER(4),    //入界
    SUPER_HIGH(6),      //超高
    STAY_HERE(7),       //逗留
    ABNORMALITIES_HEART_RATE(10),   //心率异常
    ABNORMAL_BODY_TEMPERATURE(11),   //体温异常
    TIMEOUT_SLEEP(12),   //超时休眠
    IMPORT_DEVICE(16),   //重点设备越界
    SECURITY_AREA(15),   //保密区域
    DISASSEMBLE(104),    //拆卸
    CRY_FOR_HELP(105),   //求救
    LOW_POWER2(106),     //低电量2 设备主动上报的低电量阈值不可修改
    TIMEOUT_SLEEP2(107),   //超时休眠2
    REMOVAL_BRACELET(108), //手环摘除
    ABNORMALITIES_HEART_RATE2(109),  //心率异常
    VISITOR_FLOOR(121),     //访客楼层
    ENTRY_DORMITORY_BORDER(201),     //宿舍进入事件
    OUT_DORMITORY_BORDER(202),     //宿舍离开事件
    ENTRY_LIGHT_BORDER(203),     //入界事件控灯
    PM_HIGH(401),          //PM2.5偏高
    PM_LOW(402),           //PM2.5低
    TEMPERATURE_HIGH(403), //温度偏高
    TEMPERATURE_LOW(404),  //温度偏低
    HUMIDITY_HIGH(405),    //湿度偏高
    HUMIDITY_LOW(406),     //湿度偏高
    ELECTRIC_HIGH(407),    //电流偏高
    ELECTRIC_LOW(408);     //电流偏高

    private Integer value;

    UwbAlarmTypeEnum(int value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }


}
