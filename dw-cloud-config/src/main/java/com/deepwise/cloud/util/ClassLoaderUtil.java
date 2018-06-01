package com.deepwise.cloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @Author: Zzaki
 * @Description: 类加载工具类
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public class ClassLoaderUtil {

    ClassLoaderUtil(){
    }


    /**
     * @Author :Zzaki
     * @Description: 获取类加载器 ：先获取线程上下文的classloader，如果没有，则获取当前类的的classloader
     * @Date: 2018/5/28
     * @Params: []
     * @Return: java.lang.ClassLoader
     * @Company: DeepWise
     */
    public static ClassLoader getClassLoader(){
        ClassLoader cls = Thread.currentThread().getContextClassLoader();
        return cls != null ? cls : ClassLoaderUtil.class.getClassLoader();
    }

    /**
     * @Author :Zzaki
     * @Description: 获取配置文件的inputstream
     * @Date: 2018/5/28
     * @Params: [name]
     * @Return: java.io.InputStream
     * @Company: DeepWise
     */
    public static InputStream getResourceAsStream(String name) throws FileNotFoundException {
        String configLocalPath = ConfUtil.getConfigLocalPath();
        if(!StringUtil.isBlank(configLocalPath) && new File(configLocalPath,name).exists())
            return new FileInputStream(new File(configLocalPath,name));
        return getClassLoader().getResourceAsStream(name);
    }
}
