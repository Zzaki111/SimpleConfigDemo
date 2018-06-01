package com.deepwise.cloud;

import com.deepwise.cloud.annotation.Config;
import com.deepwise.cloud.listener.IConfigScanerListener;
import com.deepwise.cloud.scaner.AbstractConfigScaner;
import com.deepwise.cloud.scaner.FileConfigScaner;
import com.deepwise.cloud.util.StringUtil;
import com.deepwise.cloud.util.ValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: Zzaki
 * @Description: 配置管理类
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public class ConfigManager {
    private static transient final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private static HashMap<String ,Method> key2Methods = new HashMap<>();
    private static HashMap<Class<?>,Object> configs = new HashMap<>();
    private static AbstractConfigScaner scaner;
    private static Properties properties = new Properties();
    private static ConfigManagerScanerLinstener listener = new ConfigManagerScanerLinstener();

    static {
        try {
            //scaner = (AbstractConfigScaner) Class.forName("com.conf4j.file.scaner.ConfigFileScaner").newInstance();
            scaner = (AbstractConfigScaner) Class.forName(ConfigScanerConf.conf.getClassName()).newInstance();
            scaner.addListener(listener);
        } catch (Exception e){
            LOG.error("init scaner failed !",e);
        }
    }

    /**
     * @Author :Zzaki
     * @Description: 开始扫描配置
     * @Date: 2018/5/30
     * @Params: []
     * @Return: void
     * @Company: DeepWise
     */
    public static void startScaner(){
        if (scaner == null)
            return;
        scaner.working();
        scaner.start();
    }

    /**
     * @Author :Zzaki
     * @Description: 停止扫描
     * @Date: 2018/5/30
     * @Params: []
     * @Return: void
     * @Company: DeepWise
     */
    public static void stopScaner(){
        if (scaner != null)
            scaner.stop();
    }

    /**
     * @Author :Zzaki
     * @Description: 添加properties
     * @Date: 2018/5/30
     * @Params: [properties]
     * @Return: void
     * @Company: DeepWise
     */
    public static void putProperties(Properties prop) {
        properties.putAll(prop);
    }

    /**
     * @Author :Zzaki
     * @Description: 添加配置对象
     * @Date: 2018/5/30
     * @Params: [config]
     * @Return: void
     * @Company: DeepWise
     */
    public static void addConfig(Object config){
        Class<?> clazz = config.getClass();
        configs.put(clazz,config);
        String prefix = getPropertyKeyPrefix(clazz);
        Collection<Method> setMethods = getClassSetMethod(clazz);
        setMethods.stream().forEach(m->{
            key2Methods.put(getKeyByMethod(prefix,m),m);
        });
    }

    /**
     * @Author :Zzaki
     * @Description: 获取配置对象
     * @Date: 2018/5/30
     * @Params: [clazz]
     * @Return: T
     * @Company: DeepWise
     */
    public static <T>T config(Class<?> clazz){
        T config = (T)configs.get(clazz);
        if (config != null)
            return config;
        T instance = null;
        try {
            instance = (T) clazz.newInstance();
        }catch (Exception e){
            return null;
        }
        config = (T) configs.get(clazz);
        if (config != null)
            return config;
        addConfig(instance);
        return instance;
    }

    /**
     * @Author :Zzaki
     * @Description: 通过key获取配置中的value，key需要带上前缀
     * @Date: 2018/5/30
     * @Params: [key]
     * @Return: java.lang.String
     * @Company: DeepWise
     */
    public static String getValueByKey(String key){
        String value = "";
        if (scaner != null)
            value = (String) scaner.getProps().get(key);
        if (!StringUtil.isBlank(value))
            return value;
        return properties.getProperty(key,ValueUtil.EMPTY);
    }

    /**
     * @Author :Zzaki
     * @Description: 返回key的前缀
     * @Date: 2018/5/30
     * @Params: [clazz]
     * @Return: java.lang.String
     * @Company: DeepWise
     */
    public static String getPropertyKeyPrefix(Class<?> clazz){
        Config annotation = clazz.getAnnotation(Config.class);
        String prefix = annotation == null ? clazz.getName() : annotation.prefix();
        return prefix;
    }

    /**
     * @Author :Zzaki
     * @Description: 获取config类中的set方法
     * @Date: 2018/5/30
     * @Params: [clazz]
     * @Return: java.util.Collection<java.lang.reflect.Method>
     * @Company: DeepWise
     */
    public static Collection<Method> getClassSetMethod(Class<?> clazz){
        Method[] methods = clazz.getMethods();
        Collection<Method> setMethods = new HashSet<>();
        Arrays.stream(methods).forEach(m->{
            if (m.getName().startsWith("set")
                    && m.getName().length()>3
                    && m.getParameterCount() == 1){
                setMethods.add(m);
            }
        });
        return setMethods;
    }

    /**
     * @Author :Zzaki
     * @Description: 通过前缀和set方法获取配置文件中的key
     * @Date: 2018/5/30
     * @Params: [prefix, method]
     * @Return: java.lang.String
     * @Company: DeepWise
     */
    public static String getKeyByMethod(String prefix, Method method){
        String key = StringUtil.firstCharToLowerCase(method.getName().substring(3));
        if(!StringUtil.isBlank(prefix))
            key = prefix.trim() + "." +key;
        return key;
    }


    private final static class ConfigManagerScanerLinstener implements IConfigScanerListener{

        @Override
        public void onChange(String key, String propertyKey, String oldValue, String newValue) {
            if (newValue == null)
                properties.remove(propertyKey);
            else
                properties.put(propertyKey,newValue);

            Method method = key2Methods.get(propertyKey);
            if (method == null)
                return;
            Object obj = configs.get(method.getDeclaringClass());
            if (obj == null)
                return;
            try {
                method.invoke(obj,
                        newValue == null ? null : ValueUtil.convert(newValue,method.getParameterTypes()[0]));
            } catch (Exception e){
                LOG.error(String.format("配置类[%s]属性[%s]注入失败", obj.getClass().getName(), propertyKey), e);
            }
        }
    }

}
