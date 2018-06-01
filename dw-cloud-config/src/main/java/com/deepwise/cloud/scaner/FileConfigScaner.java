package com.deepwise.cloud.scaner;

import com.deepwise.cloud.ConfigScanerConf;
import com.deepwise.cloud.PropInfo;
import com.deepwise.cloud.util.ConfUtil;
import com.deepwise.cloud.util.ValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: Zzaki
 * @Description: 本地文件扫描类
 * @Date: Created on 2018/5/29
 * @Company: DeepWise
 */
public class FileConfigScaner extends AbstractConfigScaner{
    private static transient final Logger LOG = LoggerFactory.getLogger(FileConfigScaner.class);

    @Override
    protected boolean scan() {
        File dir = new File(getLocalPath());
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return false;
        Arrays.stream(getScanKeys()).forEach(k->{
            File file = new File(dir,k);
            if (file.exists() && file.isFile() && file.getName().toLowerCase().endsWith(".properties")){
                try {
                    putScan(file.getCanonicalPath().substring(getLocalPath().length()+1),ConfUtil.getVersion(file));
                }catch (IOException e) {
                    LOG.error(e.getMessage(),e);
                }
            }
        });
        return true;
    }

    @Override
    protected PropInfo getPropInfo(String key) {
        return new PropInfo(new File(getLocalPath(),key));
    }

    @Override
    protected String[] getScanKeys() {
        return ValueUtil.getValue(ConfigScanerConf.conf.getKeys(),ValueUtil.EMPTY).split(",");
    }

    @Override
    protected String getLocalPath() {
        return ConfUtil.getConfigLocalPath();
    }

    @Override
    protected long getIntervalSec() {
        return ValueUtil.getValue(ConfigScanerConf.conf.getIntervalSec(),30L);
    }
}
