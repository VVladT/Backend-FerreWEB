package pe.edu.utp.backendferreweb.util.conversion;

public class TimeConverter {
    public static int getMillis(int days, int hours, int minutes) {
        return getMillisFromDays(days) + getMillisFromHours(hours) + getMillisFromMinutes(minutes);
    }

    public static int getMillisFromDays(int days) {
        return getMillisFromHours(24) * days;
    }

    public static int getMillisFromHours(int hours) {
        return getMillisFromMinutes(60) * hours;
    }

    public static int getMillisFromMinutes(int minutes) {
        return getMillisFromSeconds(60) * minutes;
    }

    public static int getMillisFromSeconds(int seconds) {
        return 1000 * seconds;
    }
}
