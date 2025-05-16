package com.iishanto.jobhunterbackend.domain.utility;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateNormalizerTest {

    @Test
    void testVariousFormats() {
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2025, 5, 15, 0, 0)),
                DateNormalizer.normalizeToTimestamp("15 May, 2025"));

        assertEquals(Timestamp.valueOf(LocalDateTime.of(2025, 3, 10, 0, 0)),
                DateNormalizer.normalizeToTimestamp("10 March, 2025"));

        assertEquals(Timestamp.valueOf(LocalDateTime.of(2025, 5, 31, 0, 0)),
                DateNormalizer.normalizeToTimestamp("2025-05-31"));

        assertEquals(Timestamp.valueOf(LocalDateTime.of(2025, 1, 12, 0, 0)),
                DateNormalizer.normalizeToTimestamp("12 Jan 2025"));

        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, 12, 15, 0, 0)),
                DateNormalizer.normalizeToTimestamp("15th December 2024"));

        assertEquals(Timestamp.valueOf(LocalDateTime.of(2025, 4, 6, 0, 0)),
                DateNormalizer.normalizeToTimestamp("April 06, 2025"));
    }

    @Test
    void testInvalidFormatReturnsNull() {
        assertNull(DateNormalizer.normalizeToTimestamp("Open until Filled (reopened 1/23/2025)"));
        assertNull(DateNormalizer.normalizeToTimestamp("Not a date"));
        assertNull(DateNormalizer.normalizeToTimestamp(""));
        assertNull(DateNormalizer.normalizeToTimestamp("March"));
    }
}
