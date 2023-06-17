package com.alphaomega.alphaomegarestfulapi.util;

public class DurationUtil {
    
    public static String getVideoDurationDisplayFormat(Integer secondDuration) {
        int hours = secondDuration / 3600;
        int minutes = (secondDuration % 3600) / 60;
        int remainingSeconds = secondDuration % 60;

        if (hours == 0) {
            return String.format("%dm %ds", minutes, remainingSeconds);
        } else {
            return String.format("%dh %dm", hours, minutes);
        }
    }
    
}
