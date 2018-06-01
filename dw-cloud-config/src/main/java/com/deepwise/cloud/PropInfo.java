package com.deepwise.cloud;

import com.deepwise.cloud.util.ConfUtil;

import java.io.File;
import java.util.Properties;

/**
 * @Author: Zzaki
 * @Description: prop配置文件的信息以及版本号 只要本地文件发生变化 版本号就改变
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public class PropInfo {

    private String version;
    private Properties properties;

    public PropInfo(){
    }

    public PropInfo(String version, Properties properties){
        this.version = version;
        this.properties = properties;
    }

    public PropInfo(File file){
        this.version = ConfUtil.getVersion(file);
        this.properties = new Prop(file).getProperties();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPropValueByKey(Object key){
        return (String)getProperties().get(key);
    }

}
