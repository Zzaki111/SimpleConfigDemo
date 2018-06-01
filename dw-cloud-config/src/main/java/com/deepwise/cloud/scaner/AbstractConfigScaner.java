package com.deepwise.cloud.scaner;


import com.deepwise.cloud.PropInfo;
import com.deepwise.cloud.listener.IConfigScanerListener;
import com.deepwise.cloud.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Zzaki
 * @Description: 配置扫描类
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
public abstract class AbstractConfigScaner {
    private static transient final Logger LOG = LoggerFactory.getLogger(AbstractConfigScaner.class);
    private Timer timer;
    protected int tryTimes = 5;
    private int scanFailTimes = 0;
    protected boolean running = false;
    //key : id, value : version
    private final Map<String, String> preScan = new HashMap<>();
    private final Map<String, String> curScan = new HashMap<>();
    private ConcurrentHashMap<String, PropInfo> propInfoMap = new ConcurrentHashMap<>();
    private final Properties props = new Properties();
    private Collection<IConfigScanerListener> listeners = new HashSet<>();

    private void init(){
        String[] scanKeys = getScanKeys();
        Arrays.stream(scanKeys).forEach(k->{
            PropInfo newPropInfo = getPropInfo(k);
            propInfoMap.put(k,newPropInfo);
            props.putAll(newPropInfo.getProperties());
        });
    }

    private void clear(){
        propInfoMap.clear();
        props.clear();
    }

    protected void putScan(String key, String version) {
        curScan.put(key, version);
    }

    public void working(){
        try {
            LOG.debug("--------------config scaner start working-------------------");
            if(!scan()){
                if (++scanFailTimes > tryTimes)
                    clear();
                return;
            }
            if (scanFailTimes > tryTimes){
                init();
            }
            scanFailTimes = 0;

            compare();

            preScan.clear();
            preScan.putAll(curScan);
            curScan.clear();
            LOG.debug("config scaner finish working.");
        }catch (Exception e){
            LOG.error("--------------config scaner error working-------------------",e);
        }
    }

    private void compare(){
        //有文件被修改或新增了文件
        getChangedKeys().stream().forEach(c->{
            PropInfo newPropInfo =getPropInfo(c);
            PropInfo oldPropInfo = propInfoMap.put(c, newPropInfo);//获取修改之前的值
            newPropInfo.getProperties().forEach((k,v)->{
                String oldPropertyValue = oldPropInfo == null ? null:oldPropInfo.getPropValueByKey(k.toString());
                String newPropertyValue = v.toString();
                //如果旧值和新值不相等
                if (!StringUtil.equals(oldPropertyValue,newPropertyValue)){
                    props.put(k.toString(),newPropertyValue);
                    onChange(c,k.toString(),oldPropertyValue,newPropertyValue);
                }
            });
            if (oldPropInfo == null)
                return;
            oldPropInfo.getProperties().forEach((k,v)->{
                if (newPropInfo.getPropValueByKey(k.toString()) != null)
                    return;
                props.remove(k);
                String oldPropertyValue = v.toString();
                // 新的配置文件移除了配置项k
                onChange(c,k.toString(),oldPropertyValue,null);
            });

        });
        //有文件被删除了
        getDeleteKeys().stream().forEach(c->{
            PropInfo propInfo = propInfoMap.get(c);
            propInfo.getProperties().forEach((k,v)->{
                props.remove(k);
                onChange(c,k.toString(),v.toString(),null);
            });
        });
    }
    /**
     * @Author :Zzaki
     * @Description: 记录被删除的文件id
     * @Date: 2018/5/28
     * @Params: []
     * @Return: java.util.List<java.lang.String>
     * @Company: DeepWise
     */
    private List<String> getDeleteKeys() {
        List<String> deleteKeys = new ArrayList<String>();
        preScan.forEach((k,v)->{
            if (curScan.get(k) == null){
                deleteKeys.add(k);
            }
        });
        return deleteKeys;
    }

    /**
     * @Author :Zzaki
     * @Description: 记录被修改或新增的文件id
     * @Date: 2018/5/28
     * @Params: []
     * @Return: java.util.List<java.lang.String>
     * @Company: DeepWise
     */
    private List<String> getChangedKeys() {
        List<String> changedKeys = new ArrayList<String>();
        curScan.forEach((k,v)->{
            String version = v;
            if (preScan.get(k) == null){
                //新增的文件
                changedKeys.add(k);
            }else if (!version.equals(preScan.get(k))){
                //文件被修改了
                changedKeys.add(k);
            }
        });
        return changedKeys;
    }

    public void start() {
        if (running)
            return;
        timer = new Timer("Config-Scaner", true);
        schedule(0);
        running = true;
    }

    private void schedule(long intervalSec) {
        timer.schedule(new TimerTask() {
            public void run() {
                working();
                if (getIntervalSec() > 0)
                    schedule(getIntervalSec());
            }
        }, 1000 * intervalSec);
    }

    public void stop() {
        if (timer == null || !running)
            return;
        timer.cancel();
        running = false;
    }

    private void onChange(String key, String propertyKey, String oldValue, String newValue) {
        listeners.stream().forEach(l->{
            l.onChange(key,propertyKey,oldValue,newValue);
        });
    }

    public final void addListener(IConfigScanerListener listener){
        this.listeners.add(listener);
    }


    protected abstract boolean scan();

    protected abstract PropInfo getPropInfo(String key);

    protected abstract String[] getScanKeys();

    protected abstract String getLocalPath();

    protected abstract long getIntervalSec();

    /**
     * @Author :Zzaki
     * @Description: 获取配置props
     * @Date: 2018/5/29
     * @Params: []
     * @Return: java.util.Properties
     * @Company: DeepWise
     */
    public final Properties getProps(){
        return props;
    }
}
