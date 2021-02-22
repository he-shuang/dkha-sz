package com.dkha.utils;

/**
 * 5门禁抓拍图片文件名规则
 * 人脸地址前缀：http://192.168.8.55/static/snapshot/
 * 人脸日期目录：将f_open_time时间戳转日期目录2020/09/17
 * 文件名对应上脚本：人脸ID对应f_person_id，抓拍时间对应f_open_time，人脸类型固定08，状态对应f_state
 * 文件名：1B0B000029DA625F0801.jpg
 * 1B0B0000        29DA625F       08              01
 * 人脸ID          抓拍时间        类型（人脸）    状态（非法/合法）
 *
 * 计算方法：人脸ID和抓拍时间，每两字符反序：
 * 人脸ID   = 16进制转10进制(00000B1B)
 * 抓拍时间 = 16进制转10进制(5F62DA29)
 */
public class BaseConversion {

    public static void main(String[] args) {
        String n = intToHex(3482,8);
        System.out.println("转换十六进制每两字符反序：" + ruleChange(n) + "  转换十六进制：" + n + "  f_person_id：3482");
        String n2 = intToHex(1601090854,8);
        System.out.println("转换十六进制每两字符反序：" + ruleChange(n2)  + "  转换十六进制：" + n2 + "  f_open_time：1601090854");
    }

    private static String intToHex(int n,int size) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        a = add_zore(a,size);
        return a;
    }

    public static String add_zore(String str, int size){
        if (str.length()<size){
            str= "0"+str;
            str=add_zore(str,size);
            return str;
        }else {
            return str;
        }
    }

    // 每两字符反序
    public static String ruleChange(String str){
        StringBuilder curNew = new StringBuilder(str);
        StringBuilder curReturn = new StringBuilder();
        char [] chs = curNew.reverse().toString().toCharArray();
        for (int i = 0,sizei = chs.length;i < sizei;i++){
            if(i % 2 == 1){
                curReturn.append(chs[i]);
                curReturn.append(chs[i - 1]);
            }
        }
        return curReturn.toString();
    }

}
