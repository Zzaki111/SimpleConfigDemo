package com.deepwise.cloud;

import com.deepwise.cloud.annotation.Config;
import com.deepwise.cloud.util.ConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Zzaki
 * @Description: 配置文件扫描规则的配置类
 * @Date: Created on 2018/5/29
 * @Company: DeepWise
 */
@Config(file="config.properties",prefix = "config.scaner")
public class ConfigScanerConf {
    private static transient final Logger LOG = LoggerFactory.getLogger(ConfigScanerConf.class);
    public final static ConfigScanerConf conf = new ConfigScanerConf();

    static {
        try {
            ConfUtil.handler(ConfigScanerConf.class);
        }catch (Exception e){
            LOG.error(String.format("handler %s failed.", ConfigScanerConf.class.getName()), e);
        }
    }

    private String className;
    private String url;
    private String user;
    private String password;
    private Long intervalSec;
    private String keys;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getIntervalSec() {
        return intervalSec;
    }

    public void setIntervalSec(Long intervalSec) {
        this.intervalSec = intervalSec;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "ConfigScanerConf{" +
                "className='" + className + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", intervalSec='" + intervalSec + '\'' +
                ", keys='" + keys + '\'' +
                '}';
    }
}
