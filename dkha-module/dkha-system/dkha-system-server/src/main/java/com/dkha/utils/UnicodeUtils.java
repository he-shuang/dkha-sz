package com.dkha.utils;

import org.apache.http.util.TextUtils;

public class UnicodeUtils {

    /**
     * 解码
     * @param unicode
     * @return
     */
    public static String unicode2String(String unicode) {
        if (unicode == null || "".equals(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }

    /**
     * CN转Unicode
     * @param string
     * @return
     */
    public static String string2Unicode(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            int chr1 = (char) string.charAt(i);
            char c = string.charAt(i);
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
                unicode.append("\\u" + Integer.toHexString(chr1));
            }else{
                unicode.append(c);
            }
        }
        return unicode.toString();
    }
}
