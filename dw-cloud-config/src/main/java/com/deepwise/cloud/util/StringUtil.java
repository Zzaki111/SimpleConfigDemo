package com.deepwise.cloud.util;

/**
 * @Author: Zzaki
 * @Description: 字符串工具类
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public class StringUtil {
    public static final String[] EMPTY_ARRAY = new String[0];

    private StringUtil() {
    }

    /**
     * @Author :Zzaki
     * @Description: 字符串首字母小写
     * @Date: 2018/5/28
     * @Params: [str]
     * @Return: java.lang.String
     * @Company: DeepWise
     */
    public static String firstCharToLowerCase(String str){
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z'){
            char[] strArr = str.toCharArray();
            strArr[0] += ('a' - 'A');
            return new String(strArr);
        }
        return str;
    }

    /**
     * @Author :Zzaki
     * @Description: 判断字符串是否为空
     * @Date: 2018/5/28
     * @Params: [value]
     * @Return: boolean
     * @Company: DeepWise
     */
    public static boolean isBlank(String value){

        if (value == null || ValueUtil.EMPTY.equals(value.trim())){
            return true;
        }
        return false;
    }

    /**
     * @Author :Zzaki
     * @Description: 比较两个字符串是否相等
     * @Date: 2018/5/28
     * @Params: [s1, s2]
     * @Return: boolean
     * @Company: DeepWise
     */
    public static boolean equals(String s1,String s2){
        if (s1 == null && s2 == null)
            return true;
        if (s1 == null)
            return false;
        return s1.equals(s2);
    }

}
