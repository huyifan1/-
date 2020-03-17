package com.uboxol.cloud.pandorasBox.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

/**
 * @author liyunde
 * @since 2019-05-16 11:23
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtils {

    public static final int CENTURY = 946656000;

    public static final long THOUSAND = 1000L;

    public static final int MILLISECOND_LEN = 12;

    public static LocalDateTime toLocalDateTime(long timestamp) {

        if (timestamp <= CENTURY) {
            timestamp *= THOUSAND;
        }

        if (String.valueOf(timestamp).length() < MILLISECOND_LEN) {
            logger.debug("时间不是毫秒戳:{} , *1000", timestamp);
            timestamp *= THOUSAND;
        }

        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static long toTimestamp(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }
    
    //获取当日日期
    public static String getToday() {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
	    Calendar c = Calendar.getInstance();
        return sf.format(c.getTime());
    }
    public static void main(String[] args) {
		System.out.println(getToday());
	}
    
}
