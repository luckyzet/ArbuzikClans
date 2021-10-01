package me.luckyzz.arbuzikclans.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public final class DurationUtil {

    private static final Map<String, Duration> DURATIONS;

    static {
        DURATIONS = new HashMap<>();
        DURATIONS.put("d", ChronoUnit.DAYS.getDuration());
        DURATIONS.put("h", ChronoUnit.HOURS.getDuration());
        DURATIONS.put("m", ChronoUnit.MINUTES.getDuration());
        DURATIONS.put("s", ChronoUnit.SECONDS.getDuration());
    }

    private DurationUtil() {
        throw new UnsupportedOperationException();
    }

    private static int getDeclensionGroup(final long n) {
        long k = n % 10;
        return k == 0 || k >= 5 || (n >= 11 && n <= 20) ? 2 : k == 1 ? 1 : 3;
    }

    public static String formatDuration(Duration duration) {
        StringBuilder builder = new StringBuilder();
        long days = duration.toDays();
        if (days > 0) {
            int group = getDeclensionGroup(days);
            builder.append(days).append(" ").append(group == 1 ? "день" : group == 2 ? "дней" : "дня").append(" ");
            duration = duration.minusDays(days);
        }
        long hours = duration.toHours();
        if (hours > 0) {
            int group = getDeclensionGroup(hours);
            builder.append(hours).append(" ").append(group == 1 ? "час" : group == 2 ? "часов" : "часа").append(" ");
            duration = duration.minusHours(hours);
        }
        long min = duration.toMinutes();
        if (min > 0) {
            int group = getDeclensionGroup(min);
            builder.append(min).append(" ").append(group == 1 ? "минуту" : group == 2 ? "минут" : "минуты").append(" ");
            duration = duration.minusMinutes(min);
        }
        long sec = duration.getSeconds();
        if (sec > 0) {
            int group = getDeclensionGroup(sec);
            builder.append(sec).append(" ").append(group == 1 ? "секунду" : group == 2 ? "секунд" : "секунды").append(" ");
        }
        return builder.toString().trim();
    }

    public static Duration parseDuration(String time) {
        Duration duration = Duration.ZERO;
        for (String value : time.toLowerCase().split(",")) {
            for (String got : DURATIONS.keySet()) {
                if (!value.endsWith(got)) {
                    continue;
                }
                duration = duration.plus(DURATIONS.get(got).multipliedBy(Long.parseLong(value.substring(0, value.length() - got.length()))));
                break;
            }
        }
        return duration;
    }

}
