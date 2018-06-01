package com.deepwise.cloud.util;

import com.deepwise.cloud.ConfigManager;
import com.deepwise.cloud.Prop;
import com.deepwise.cloud.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @Author: Zzaki
 * @Description: 配置工具类
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public final class ConfUtil {
    public static final String CONFIG_PROPERTIES = "config.properties";
    public static final String CONFIG_SCANER_LOCAL_PATH = "config.scaner.localPath";
    private static String configLocalPath;

    static{
        /**
         * -D参数或"config.properties"中的config.scaner.localPath = config/local/path
         * 该路径下面的配置文件覆盖类路径中对应的配置文件
         */
        configLocalPath = System.getProperty(ConfUtil.CONFIG_SCANER_LOCAL_PATH);
        if (StringUtil.isBlank(configLocalPath)){
            try{
                Prop prop = new Prop(ConfUtil.CONFIG_PROPERTIES);
                configLocalPath = prop.get(ConfUtil.CONFIG_SCANER_LOCAL_PATH);
            }catch (Exception e){
                // do nothing
            }
        }else{
            try {
                if(configLocalPath.startsWith("~/"))
                    configLocalPath = System.getProperty("user.home")+configLocalPath.substring(1);
                configLocalPath = new File(configLocalPath).getCanonicalPath();
            }catch (IOException e){
                throw new RuntimeException(e.getMessage(),e);
            }
        }
    }

    /**
     * @Author :Zzaki
     * @Description: 获取文件版本
     * @Date: 2018/5/28
     * @Params: [file]
     * @Return: java.lang.String
     * @Company: DeepWise
     */
    public static String getVersion(File file){
        return "version: "+file.lastModified();
    }

    /**
     * @Author :Zzaki
     * @Description: 获取本地文件扫描路径
     * @Date: 2018/5/28
     * @Params: []
     * @Return: java.lang.String
     * @Company: DeepWise
     */
    public static final String getConfigLocalPath(){
        return configLocalPath;
    }

    public static void handler(Class<?> clazz)throws Exception{
        Config annotation = clazz.getAnnotation(Config.class);
        if (annotation == null){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        fields[1].setAccessible(true);
        Object obj = fields[1].get(null);
        if (obj == null){
            obj = clazz.newInstance();
            fields[1].set(null,obj);
        }
        //添加配置
        ConfigManager.addConfig(obj);
        String fileName = annotation.file();
        if(StringUtil.isBlank(fileName))
            return;
        Prop prop = new Prop(fileName);
        ConfigManager.putProperties(prop.getProperties());
        String prefix = annotation.prefix();
        //通过反射注入属性
        Collection<Method> getMethods = ConfigManager.getClassSetMethod(clazz);
        for (Method method : getMethods){
            String value = prop.get(ConfigManager.getKeyByMethod(prefix,method));
            if (!StringUtil.isBlank(value)){
                Object val = ValueUtil.convert(value,method.getParameterTypes()[0]);
                method.invoke(obj,val);
            }
        }

    }
}
