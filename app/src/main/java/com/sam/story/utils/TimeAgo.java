package com.sam.story.utils;

import java.util.Date;

/**
 * Convert timestamps to time ago
 */

public class TimeAgo {

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long YEAR = WEEK * 52;

    public static String timeAgo(long timestamp) {

        Date now = new Date();
        Date date = new Date(timestamp);

        long timeAgo = now.getTime() - date.getTime();

        if (timeAgo < MINUTE)
            return (int) Math.floor(timeAgo / SECOND) + "s";
        if (timeAgo < HOUR)
            return (int) Math.floor(timeAgo / MINUTE) + "m";
        if (timeAgo < DAY)
            return (int) Math.floor(timeAgo / HOUR) + "h";
        if (timeAgo < WEEK)
            return (int) Math.floor(timeAgo / DAY) + "d";
        if (timeAgo < YEAR)
            return (int) Math.floor(timeAgo / WEEK) + "w";

        return (int) Math.floor(timeAgo / YEAR) + "y";
    }
}
