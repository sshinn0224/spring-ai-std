package com.example.springai.ch11.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HeatingSystemTools {

    @Tool(description = """
            타겟 온도까지 난방 시스템을 가동합니다.
            난방 시스템 가동이 성공 되었을 경우 success를 반환 합니다.
            난방 시스템 가동이 실패했을 경우 failure를 반환 합니다.
            """)
    public String startHeatingSystemStart(
            @ToolParam(description = "타겟 온도", required = true) int tagetTemperature,
            ToolContext toolContext
    ) {
        String controlKey = (String) toolContext.getContext().get("controlKey");
        if(controlKey != null && controlKey.equals("heatingSystemKey")){
            log.info("{}도 까지 난방 시스템을 가동합니다.", tagetTemperature);
            return "success";
        } else {
            log.info("난방 시스템을 가동할 권한이 없습니다.");
            return "failure";
        }
    }
}
