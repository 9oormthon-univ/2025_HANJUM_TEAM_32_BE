package com.hanjum.newshanjumapi.domain.article.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {

    // 네이버 뉴스 API에서 내려오는 pubDate 포맷
    private static final DateTimeFormatter NAVER_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    // 기본으로 내려줄 출력 포맷
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /// 네이버 pubDate(String) → LocalDateTime

    public static LocalDateTime parseNaverDate(String pubDate) {
        if (pubDate == null || pubDate.isBlank()) {
            return null;
        }
        ZonedDateTime zdt = ZonedDateTime.parse(pubDate, NAVER_DATE_FORMATTER);
        return zdt.toLocalDateTime();
    }

    /// LocalDateTime → yyyy-MM-dd HH:mm 포맷의 String
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(OUTPUT_FORMATTER);
    }
}
