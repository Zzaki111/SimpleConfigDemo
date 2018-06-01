import com.deepwise.cloud.ConfigManager;
import com.deepwise.cloud.ConfigScanerConf;

import java.util.Properties;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/5/30
 * @Company: DeepWise
 */
public class ConfigTestMain {
    public static void main(String[] args) throws InterruptedException {

        // 通过配置类的config获取实例调用配置属性
        //System.out.println(ConfigScanerConf.conf);
        // 通过ConfigManager.config(配置类)获取实例调用配置属性
        //System.out.println(ConfigManager.config(ConfigScanerConf.class).toString());
        // 通过ConfigManager.putProperties()追加自定义全局配置属性
        Properties properties = new Properties();
        String testPropertyKey = "test.add.property.key";
        properties.setProperty(testPropertyKey, "test.add.property.value");
        ConfigManager.putProperties(properties);
        System.out.println(testPropertyKey + "=" + ConfigManager.getValueByKey(testPropertyKey));
        System.out.println("className" + "=" + ConfigManager.getValueByKey("config.scaner.className"));


        //启动配置扫描
        ConfigManager.startScaner();
        int i = 0;
        while (i++ < 100) {
            Thread.sleep(2000);
            System.out.println(ConfigScanerConf.conf);
        }
    }

}
