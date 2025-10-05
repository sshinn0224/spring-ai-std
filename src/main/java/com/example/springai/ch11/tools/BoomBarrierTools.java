package com.example.springai.ch11.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BoomBarrierTools {

    @Tool(description = "차단봉을 올립니다.")
    public void boomBarrierUp() {
        log.info("차단봉을 올립니다.");
    }

    @Tool(description = "차단봉을 내립니다.")
    public void boomBarrierDown() {
        log.info("차단봉을 내립니다.");
    }
}
