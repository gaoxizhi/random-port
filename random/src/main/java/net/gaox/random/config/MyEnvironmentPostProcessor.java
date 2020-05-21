package net.gaox.random.config;

import net.gaox.random.os.OSInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * <p> 动态管理配置文件扩展接口 </p>
 * 配合[resources/META-INF/spring.factories]文件使用
 *
 * @author gaox·Eric
 * @date 2020/5/21 23:28
 */
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //MapPropertySource
        Properties properties = new Properties();
        properties.put("server.port", OSInfo.getAvailablePort());
        System.out.println(String.format("server.port=%s\n", properties.get("server.port")));
        PropertiesPropertySource source = new PropertiesPropertySource("source", properties);
        environment.getPropertySources().addLast(source);
        //environment.getPropertySources().addAfter();
    }
}
