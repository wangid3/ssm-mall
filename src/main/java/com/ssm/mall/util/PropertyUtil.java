package com.ssm.mall.util;

import com.ssm.mall.common.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    static {
        String fileName = "properties/ssm.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            logger.error(Result.CONFIG_READ_ERROR.getMsg(),e);
        }
    }
    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;}
        return value.trim();
    }
    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;}
        return value.trim();
    }
}
