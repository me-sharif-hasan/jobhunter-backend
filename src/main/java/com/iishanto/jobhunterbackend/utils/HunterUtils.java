package com.iishanto.jobhunterbackend.utils;

import com.google.common.base.CharMatcher;

public class HunterUtils {
    public static String sanitizeMarkdown(String markdown) {
        return CharMatcher.javaIsoControl() // Remove non-printable control characters
                .and(CharMatcher.isNot('\n'))
                .and(CharMatcher.isNot('\r'))
                .and(CharMatcher.isNot('\t'))
                .removeFrom(markdown)
                .replaceAll("\\(data:(image|video)/[^)]+\\)", "") // Remove base64 media links
                .replace("\"", "\\\"");
    }

    public static String sanitizeJsonString(String json){
        return CharMatcher.javaIsoControl().and(CharMatcher.isNot('\n'))
                .and(CharMatcher.isNot('\r'))
                .and(CharMatcher.isNot('\t'))
                .removeFrom(json);
    }
}
