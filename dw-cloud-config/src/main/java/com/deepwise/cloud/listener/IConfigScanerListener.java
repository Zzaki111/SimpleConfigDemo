package com.deepwise.cloud.listener;

/**
 * @Author: Zzaki
 * @Description: 配置扫描监听器
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public interface IConfigScanerListener {


    /**
     * @Author :Zzaki
     * @Description:
     * @Date: 2018/5/29
     * @Params: [key, propertyKey, oldValue, newValue]
     * key:配置文件的id，propertyKey:配置文件中配置项的key
     * oldValue：配置文件中配置项的旧值，newValue：配置文件中配置项的新值
     * @Return: void
     * @Company: DeepWise
     */
    void onChange(String key, String propertyKey, String oldValue, String newValue);
}
