package com.example.springai.ch11.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class DateTimeTools {

    @Tool(description = "현재 날짜와 시간 정보를 제공합니다.")
    public String getCurrentDateTime() {
        log.info("getCurrentDateTime start..");
        String nowTime = LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();

        log.info("현재시간 : {}", nowTime);
        return nowTime;
    }

    @Tool(description = "지정된 시간에 알람을 설정합니다.")
    public void SetAlarm(
            @ToolParam(description = "ISO-8601 형식의 시간", required = true) String time) {
        log.info("param Time : {}", time);

        if(time.contains("T24")) {
            int tIndex = time.indexOf("T");
            String datePart = time.substring(0, tIndex);
            String timePart = time.substring(tIndex+1);

            LocalDate date = LocalDate.parse(datePart);
            date = date.plusDays(1);

            timePart = timePart.replace("24:", "00");
            time = date + "T" + timePart;
        }

        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        log.info("alarmTime : {}", alarmTime);
    }
}
