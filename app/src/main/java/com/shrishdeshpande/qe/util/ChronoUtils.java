package com.shrishdeshpande.qe.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class ChronoUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

    public static String convertUnixMillisToLocalDateTime(long unixMillis) {
        Instant instant = Instant.ofEpochMilli(unixMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(FORMATTER);
    }
}
