package net.gaox.random;

import lombok.extern.slf4j.Slf4j;
import net.gaox.random.os.OSInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

/**
 * <p> 系统启动时使用随机端口 </p>
 *
 * @author gaox·Eric
 * @date 2020/5/20 21:36
 */
@Slf4j
@EnableEurekaClient
@SpringBootApplication
public class RandomApplication {

    final Environment environment;

    public RandomApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        OSInfo.randomOrStartPort(args);
        SpringApplication.run(RandomApplication.class, args);
    }
}
