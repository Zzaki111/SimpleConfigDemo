package com.deepwise.cloud.util;

import java.math.BigDecimal;

/**
 * @Author: Zzaki
 * @Description: 值处理工具类
 * @Date: Created on 14:17 2018/5/28
 * @Company: DeepWise
 */
public class ValueUtil {
    public static final char CHAR_BACKSLASH = '\\';
    public static final char CHAR_SLASH = '/';
    public static final char CHAR_Z = 'Z';
    public static final char CHAR_A = 'A';
    public static final String STR_SLASH = "/";
    public static final String STR_TRUE = Boolean.TRUE.toString();
    public static final String STR_FALSE = Boolean.FALSE.toString();
    public static final String STR_ZERO = "0";
    public static final String STR_ONE = "1";
    public static final String STR_SEMICOLON = ";";
    public static final String STR_COMMA = ",";
    public static final int INT_ONE = 1;
    public static final int INT_TWO = 2;
    public static final String EMPTY = "";
    public static final Byte B = new Byte((byte) 0);
    public static final Short S = new Short((short) 0);
    public static final Integer I = new Integer(0);
    public static final Long L = new Long(0L);
    public static final Float F = new Float(0f);
    public static final Double D = new Double(0d);
    public static final BigDecimal BD = new BigDecimal(0);

    ValueUtil(){
    }

    /**
     * @Author :Zzaki
     * @Description:
     * @Date: 2018/5/28
     * @Params: [value, defaultValue]
     * @Return: T
     * @Company: DeepWise
     */
    public static<T> T getValue(T value,T defaultValue){
        if (value == null){
            return defaultValue;
        }
        if (value instanceof String){
            return EMPTY.equals(value) ? defaultValue : value;
        }
        if (value instanceof Number){
            return value;
        }
        if (value instanceof Byte){
            return B.equals(value) ? defaultValue : value;
        }
        if (value instanceof Short){
            return S.equals(value) ? defaultValue : value;
        }
        if (value instanceof Integer){
            return I.equals(value) ? defaultValue : value;
        }
        if (value instanceof Long){
            return L.equals(value) ? defaultValue : value;
        }
        if (value instanceof Float){
            return F.equals(value) ? defaultValue : value;
        }
        if (value instanceof Double){
            return D.equals(value) ? defaultValue : value;
        }
        if (value instanceof BigDecimal){
            return BD.equals(value) ? defaultValue : value;
        }
        return BD.equals(new BigDecimal(value.toString())) ? defaultValue : value;
    }

    /**
     * @Author :Zzaki
     * @Description:
     * @Date: 2018/5/28
     * @Params: [s, type]
     * @Return: java.lang.Object
     * @Company: DeepWise
     */
    public static final Object convert(String s, Class<?> type){
        if (type == String.class){
            return s;
        }
        if (type == Integer.class || type == int.class){
            return Integer.valueOf(s);
        }
        if (type == Long.class || type == long.class){
            return Long.valueOf(s);
        }
        if (type == Double.class || type == double.class){
            return Double.valueOf(s);
        }
        if (type == Float.class || type == float.class){
            return Float.valueOf(s);
        }
        if (type == Boolean.class || type == boolean.class) {
            String value = s.toLowerCase();
            if (STR_ONE.equals(value) || STR_TRUE.equals(value)) {
                return Boolean.TRUE;
            } else if (STR_ZERO.equals(value) || STR_FALSE.equals(value)) {
                return Boolean.FALSE;
            } else {
                throw new RuntimeException("Can not parse to boolean type of value: " + s);
            }
        }
        if (type == java.math.BigDecimal.class){
            return new java.math.BigDecimal(s);
        }
        if (type == java.math.BigInteger.class){
            return new java.math.BigInteger(s);
        }
        if (type == byte[].class){
            return s.getBytes();
        }
        throw new RuntimeException(type.getName()
                + " can not be converted, please use other type in your config class!");
    }

}
