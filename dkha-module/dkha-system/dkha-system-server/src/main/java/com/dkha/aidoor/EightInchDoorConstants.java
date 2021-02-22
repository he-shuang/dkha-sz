package com.dkha.aidoor;

/**
 * 8 英寸 门禁系统的常量定义
 */
public class EightInchDoorConstants {
    /**
     * 成功状态码
     */
    public static int CODE_RESPONSE_SUCCESS = 200;

    /**
     * 设备端ip和端口  每台设备不一样
     */
   // public static String SERVER_ADDRESS = "http://192.168.1.53:3639";

    /**
     * 设备端对外开放API
     */
    public static final String URI_PERSON_MANAGE_ADD = "/person/add";
    public static final String URI_PERSON_MANAGE_ADD_MULTIPLE = "/person/addMultiple";
    public static final String URI_PERSON_MANAGE_DELETE = "/person/delete";
    public static final String URI_PERSON_MANAGE_PERSON_LIST = "/person/list";
    public static final String URI_PERSON_MANAGE_DOOR_AUTHORITY = "/person/doorAuthority";
    public static final String URI_PERSON_MANAGE_DOOR_AUTHORITY_V2 = "/person/v2/doorAuthority";
    public static final String URI_EQUIPMENT_GET_FINGER_PRINT_INFO = "/equipment/getFingerprintInfo";
    public static final String URI_EQUIPMENT_CONNECT = "/equipment/connect";
    public static final String URI_EQUIPMENT_DISCONNECT = "/equipment/disconnect";
    public static final String URI_EQUIPMENT_SETTING = "/equipment/setting";
    public static final String URI_EQUIPMENT_GET_SETTING = "/equipment/getSetting";
    public static final String URI_EQUIPMENT_SET_LOGO = "/equipment/setLogo";
    public static final String URI_EQUIPMENT_CLEAN_DATA = "/equipment/cleanData";
    public static final String URI_EQUIPMENT_OPEN_DOOR = "/equipment/openDoor";
    public static final String URI_EQUIPMENT_REBOOT = "/equipment/reboot";
    public static final String URI_EQUIPMENT_GET_LOGO = "/equipment/getLogo";
    public static final String URI_PACKAGE_AUTHORITY = "/package/authority";
    public static final String URI_PACKAGE_TRANSFER = "/package/transfer";
    public static final String URI_EQUIPMENT_SETDEVICETIME = "/equipment/systemTime";
}
