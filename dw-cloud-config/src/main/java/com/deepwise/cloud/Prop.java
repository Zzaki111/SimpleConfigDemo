package com.deepwise.cloud;

import com.deepwise.cloud.util.ClassLoaderUtil;
import com.deepwise.cloud.util.ValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public class Prop {
    private transient static final Logger LOG = LoggerFactory.getLogger(Prop.class);
    private static final String DEFAULT_ENCODING = "UTF-8";
    private Properties properties = null;

    public Prop(String fileName){
        this(fileName,DEFAULT_ENCODING);
    }

    /**
     * @Author :Zzaki
     * @Description: 通过classpath下找到配置文件来load配置
     * @Date: 2018/5/28
     * @Params: [fileName, encoding]
     * @Return:
     * @Company: DeepWise
     */
    public Prop(String fileName, String encoding){
        InputStream inputStream = null;
        try{
            inputStream = ClassLoaderUtil.getResourceAsStream(fileName);
            if (inputStream == null){
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream,encoding));
        }catch (IOException e){
            throw new RuntimeException("Error loading properties file.",e);
        }
    }

    public Prop(File file){
        this(file,DEFAULT_ENCODING);
    }

    /**
     * @Author :Zzaki
     * @Description: 通过文件load配置信息
     * @Date: 2018/5/29
     * @Params: [file, encoding]
     * @Return:
     * @Company: DeepWise
     */
    public Prop(File file,String encoding){
        if (file == null){
            throw new IllegalArgumentException("File can not be null");
        }
        if (!file.isFile()){
            throw new IllegalArgumentException("File not found: "+file.getName());
        }

        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream,encoding));
        } catch (Exception e) {
            throw new RuntimeException("Error loading properties file.",e);
        }
        finally {
            if (inputStream != null) try{inputStream.close();}catch (IOException e){LOG.error(e.getMessage(),e);}
        }
    }

    public Prop append(Prop prop){
        if (prop == null){
            throw new IllegalArgumentException("prop can not be null");
        }
        properties.putAll(prop.getProperties());
        return this;
    }

    public Prop append(String fileName){
        return append(fileName,DEFAULT_ENCODING);
    }

    public Prop append(String fileName,String encoding){
        return append(new Prop(fileName,encoding));
    }

    public Prop appendIfExists(String fileName){
        return appendIfExists(fileName,DEFAULT_ENCODING);
    }

    public Prop appendIfExists(String fileName,String encoding){
        try{
            return append(new Prop(fileName,encoding));
        }catch (Exception e){
            return this;
        }
    }

    public Prop append(File file) {
        return append(file, DEFAULT_ENCODING);
    }

    public Prop append(File file, String encoding) {
        return append(new Prop(file, encoding));
    }

    public Prop appendIfExists(File file) {
        return appendIfExists(file, DEFAULT_ENCODING);
    }

    public Prop appendIfExists(File file, String encoding) {
        if (file.exists()) {
            append(new Prop(file, encoding));
        }
        return this;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key,defaultValue);
    }

    public Integer getInt(String key){
        return getInt(key,null);
    }

    public Integer getInt(String key, Integer defaultValue){
        String value = properties.getProperty(key);
        if(value != null){
            return Integer.valueOf(value.trim());
        }
        return defaultValue;
    }

    public Long getLong(String key){
        return getLong(key,null);
    }

    public Long getLong(String key, Long defaultValue){
        String value = properties.getProperty(key);
        if(value != null){
            return Long.valueOf(value.trim());
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            value = value.toLowerCase().trim();
            if (ValueUtil.STR_ONE.equals(value) || ValueUtil.STR_TRUE.equals(value)) {
                return Boolean.TRUE;
            } else if (ValueUtil.STR_ZERO.equals(value) || ValueUtil.STR_FALSE.equals(value)) {
                return Boolean.FALSE;
            }
            throw new RuntimeException("The value can not parse to Boolean : " + value);
        }
        return defaultValue;
    }

    public Properties getProperties(){
        return properties;
    }
}
