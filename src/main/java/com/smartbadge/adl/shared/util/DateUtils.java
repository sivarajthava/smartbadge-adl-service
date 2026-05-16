package com.smartbadge.adl.shared.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    public static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);

    private DateUtils() {
    }

    public static String currentDate() {
        return ISO_FMT.format(Instant.now());
    }
}
