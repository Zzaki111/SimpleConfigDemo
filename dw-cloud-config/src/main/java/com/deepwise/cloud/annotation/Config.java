package com.deepwise.cloud.annotation;

import java.lang.annotation.*;

/**
 * @Author: Zzaki
 * @Description: 配置类自动映射注解
 * @Date: Created on 2018/5/28
 * @Company: DeepWise
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Config {

    /**
     * key的前缀。如 ：deepwise.cloud.password = 123456,prefix = "deepwise.cloud"
     *
     * @return
     */
    String prefix();

    /**
     * 配置文件名称
     *
     * @return
     */
    String file() default "";
}
