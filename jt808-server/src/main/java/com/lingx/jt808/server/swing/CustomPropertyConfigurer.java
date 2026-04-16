package com.lingx.jt808.server.swing;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.lingx.utils.PropUtils;

public class CustomPropertyConfigurer extends PropertySourcesPlaceholderConfigurer {

	@Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String ip=PropUtils.getProp("tracseek.database.ip","127.0.0.1");
        String port=PropUtils.getProp("tracseek.database.port","3306");
        String username=PropUtils.getProp("tracseek.database.username","root");
        String password=PropUtils.getProp("tracseek.database.password","123456");
        String template="jdbc:mysql://%s:%s/tracseek?useSSL=false&useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&characterSetResults=utf8mb4&serverTimezone=Asia/Shanghai&autoReconnect=true&rewriteBatchedStatements=true";
        // 创建新的属性并覆盖目标值
        Properties props = new Properties();
        props.setProperty("database.type", "mysql"); 
        props.setProperty("database.name", "tracseek"); 
        props.setProperty("database.driver.class", "com.mysql.cj.jdbc.Driver"); 
        props.setProperty("database.username", username); 
        props.setProperty("database.password", password);
        props.setProperty("database.url", String.format(template, ip,port));
        props.setProperty("database.initialSize", "1");
        props.setProperty("database.maxActive", "20");
        props.setProperty("database.filters", "stat");
        props.setProperty("database.validationQuery", "select 'x'");
        props.setProperty("database.testWhileIdle", "true");

        // 将新属性添加到当前环境
        setProperties(props);
        // 调用父类方法继续处理
        super.postProcessBeanFactory(beanFactory);
    }
}
