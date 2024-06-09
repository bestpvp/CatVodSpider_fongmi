package com.github.catvod.utils;

import android.util.Base64;

public class Tag {

    /**
     * 将字符串进行 Base64 加密
     *
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    public static String encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    /**
     * 将 Base64 字符串进行解密
     *
     * @param str 要解密的字符串
     * @return 解密后的字符串
     */
    public static String decode(String str) {
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    public static String notifyMsg() {
        // 插兜时光机: 免费不收费
        String str = "5o+S5YWc5pe25YWJ5py6OiDlhY3otLnkuI3mlLbotLk=";
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    public static String prefixMsg() {
        // ⇪码上时光机™️
        String str = "4oeq56CB5LiK5pe25YWJ5py64oSi77iP";
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    public static String categoryMsg() {
        // 插兜的干货仓库
        String str = "5o+S5YWc55qE5bmy6LSn5LuT5bqT";
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    public static String jxErrMsg() {
        // 时光机广告解析报错: 可能jxToken过期
        String str = "5pe25YWJ5py65bm/5ZGK6Kej5p6Q5oql6ZSZOiDlj6/og73mmK9qeFRva2Vu6L+H5pyf";
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    public static String jxBlankMsg() {
        // 时光机广告解析报错: 未配置jxToken
        String str = "5pe25YWJ5py65bm/5ZGK6Kej5p6Q5oql6ZSZOiDmnKrphY3nva5qeFRva2Vu";
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

    public static String jxEnableMsg() {
        // 该线路支持时光机广告过滤
        String str = "6K+l57q/6Lev5pSv5oyB5pe25YWJ5py65bm/5ZGK6L+H5ruk";
        return new String(Base64.decode(str, Base64.DEFAULT));
    }

}
