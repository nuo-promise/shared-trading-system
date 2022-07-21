package cn.suparking.common.api.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtils.
 */
public class DateUtils {

    private static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_DATETIME);

    /**
     * 获取几天前时间.
     * @param currentMillis {@link Long}
     * @param day {@link Integer}
     * @return {@link Long}
     */
    public static Long getBeforeDay(final long currentMillis, final Integer day) {
        Date date = new Date(currentMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 获取几天前时间.
     * @param currentMillis {@link Long}
     * @param day {@link Integer}
     * @return {@link Long}
     */
    public static Timestamp getBeforeTimestampDay(final long currentMillis, final Integer day) {
        Date date = new Date(currentMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 将秒转x天x时x分x秒.
     * @param seconds 秒
     * @return String
     */
    public static String formatSeconds(final Long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = ((seconds / 60) / 60) / 24;
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    /**
     * 秒转时间字符串.
     * @param second Long
     * @return String
     */
    public static String secondToDateTime(final Long second) {
        return SIMPLE_DATE_FORMAT.format(new Date(second * 1000L));
    }

    /**
     * 获取当前时间.
     * @return String
     */
    public static String timestamp() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return timestampFormat.format(new Date());
    }

    /**
     * sign date.
     * @return String
     */
    public static String currentDate() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd");
        return timestampFormat.format(currentTime());
    }

    /**
     * get current second.
     * @return {@link Long}
     */
    public static Long getCurrentSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * get current Mills.
     * @return {@link Long}
     */
    public static Long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * parse LocalDateTime.
     * out put format: yyyy-MM-dd HH:mm:ss
     *
     * @param dataTime date String
     * @return LocalDateTime: yyyy-MM-dd HH:mm:ss
     * @see LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(final String dataTime) {
        return LocalDateTime.parse(dataTime, DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * Parse local date time local date time.
     *
     * @param dataTime          the data time
     * @param dateTimeFormatter the date time formatter
     * @return the local date time
     */
    public static LocalDateTime parseLocalDateTime(final String dataTime, final String dateTimeFormatter) {
        return LocalDateTime.parse(dataTime, DateTimeFormatter.ofPattern(dateTimeFormatter));
    }

    /**
     * acquireMinutesBetween.
     *
     * @param start this is start date.
     * @param end   this is start date.
     * @return The number of days between start and end, if end is after start,
     *         returns a positive number, otherwise returns a negative number.
     */
    public static long acquireMinutesBetween(final LocalDateTime start, final LocalDateTime end) {
        return start.until(end, ChronoUnit.MINUTES);
    }

    /**
     * Acquire millis between long.
     *
     * @param start the start
     * @param end   the end
     * @return the long
     */
    public static long acquireMillisBetween(final LocalDateTime start, final LocalDateTime end) {
        return start.until(end, ChronoUnit.MILLIS);
    }

    /**
     * Format local date time from timestamp local date time.
     *
     * @param timestamp the timestamp
     * @return the local date time
     */
    public static LocalDateTime formatLocalDateTimeFromTimestamp(final Long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
    }

    /**
     * Format local date time from timestamp by system time zone.
     *
     * @param timestamp the timestamp
     * @return the local date time
     */
    public static LocalDateTime formatLocalDateTimeFromTimestampBySystemTimezone(final Long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * Format local date time to string.
     * use default pattern yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime the localDateTime
     * @return the format string
     */
    public static String localDateTimeToString(final LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    /**
     * Format local date time to string.
     *
     * @param localDateTime the localDateTime
     * @param pattern       formatter pattern
     * @return the format string
     */
    public static String localDateTimeToString(final LocalDateTime localDateTime, final String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    private static Date currentTime() {
        return new Date();
    }
}
