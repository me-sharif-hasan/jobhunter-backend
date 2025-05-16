package com.iishanto.jobhunterbackend.domain.utility;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateNormalizer {

    private static final List<DateTimeFormatter> formatters = new ArrayList<>();

    static {
        formatters.add(DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d'th' MMMM yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d'th' MMM yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d'th' MMMM, yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d'th' MMM, yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("d-MM-yyyy", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH));
        formatters.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH));
    }

    public static Timestamp normalizeToTimestamp(String input) {
        if(input == null || input.isEmpty()) {
            return null;
        }
        String cleaned = input.trim()
                .replaceAll("(?i)(\\d+)(st|nd|rd|th)", "$1")
                .replaceAll("Open until Filled.*", "")
                .trim();

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate date = LocalDate.parse(cleaned, formatter);
                LocalDateTime dateTime = date.atStartOfDay();
                return Timestamp.valueOf(dateTime);
            } catch (DateTimeParseException ignored) {}
        }

        return null;
    }
}
