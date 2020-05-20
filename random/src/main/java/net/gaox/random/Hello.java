package net.gaox.random;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试接口 </p>
 *
 * @author gaox·Eric
 * @date 2020/5/20 22:21
 */
@Slf4j
@RestController
public class Hello {

    @GetMapping
    public String index() {
        log.info("有人访问了！");
        return "👏👏欢迎！";
    }
}
