package com.fcn.fanscoin.helper;

import com.fcn.fanscoin.constant.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Date;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.YEARS;

public final class DateHelper {

    private static final String MIN_TIMESTAMP_SQL = "01/01/1970 00:00:01";
    private static final String MAX_TIMESTAMP_SQL = "19/01/2038 03:14:07";
    private static final int START_YEAR = 2016;
    private static long minTimestamp = 0L;
    private static long maxTimestamp = 0L;

    static {
        try {
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            minTimestamp = sdf.parse(MIN_TIMESTAMP_SQL).getTime();
            maxTimestamp = sdf.parse(MAX_TIMESTAMP_SQL).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private DateHelper() {
    }

    public static Instant toDateTime(final Long dateTime) {
        return dateTime != null ? Instant.ofEpochMilli(dateTime) : Instant.now();
    }

    public static long toMillis(final Instant dateTime) {
        return dateTime != null ? dateTime.toEpochMilli() : 0;
    }

    public static long toMillis(final Date date) {
        return date != null ? date.getTime() : 0;
    }

    public static boolean validTimestamp(final Long timestamp) {
        return timestamp != null && timestamp >= minTimestamp && timestamp <= maxTimestamp;
    }

    public static Instant toDateWithTimeAtStartOfDay(final Long dateTime) {
        return DateHelper.toDateTime(dateTime).truncatedTo(DAYS);
    }

    public static Instant toDateWithTimeAtStartOfDay(final Instant dateTime) {
        return dateTime.truncatedTo(DAYS);
    }

    public static Instant toDateWithDayAtStartOfYear(final Long dateTime) {
        return LocalDateTime.ofInstant(dateTime != null ? Instant.ofEpochMilli(dateTime) : Instant.now(),
                                       UTC).withYear(START_YEAR).withDayOfYear(1).toInstant(UTC);
    }

    public static Instant getStartTimeOfCurrentYear() {
        return LocalDateTime.now().withDayOfYear(1).toInstant(UTC);
    }

    public static Instant getEndTimeOfCurrentYear() {
        return getStartTimeOfCurrentYear().plus(1, YEARS).minusMillis(1);
    }

    public static Instant toDateTime(final String dateTime) {
        try {
            Long millis = Long.parseLong(dateTime);
            if (validTimestamp(millis)) {
                return Instant.ofEpochMilli(millis);
            } else {
                return null;
            }
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static Instant toDateWithTimeAtEndOfDay(final Long dateTime) {
        return toDateWithTimeAtStartOfDay(dateTime).plus(1, DAYS).minus(1, MILLIS);
    }

    public static Instant toDateWithTimeAtEndOfDay(final Instant dateTime) {
        return dateTime.truncatedTo(DAYS).plus(1, DAYS).minus(1, MILLIS);
    }

    public static int getFieldFromInstant(final Instant dateTime, final ChronoField chronoField) {
        return LocalDateTime.ofInstant(dateTime != null ? dateTime : Instant.now(), UTC).get(chronoField);
    }

    public static Instant getCurrentDateTimeAtStartOfDay(final ZoneId zoneId) {
        LocalDate currentDate = LocalDate.now(zoneId);
        return currentDate.atStartOfDay(zoneId).toInstant();
    }

    public static Instant getCurrentDateTimeAtEndOfDay(final ZoneId zoneId) {
        return getCurrentDateTimeAtStartOfDay(zoneId).plus(1, DAYS).minus(1, MILLIS);
    }

    public static Date toDate(final Long time) {
        if (time == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
            return dateFormat.parse(dateFormat.format(time));
        } catch (Exception exception) {
            return null;
        }
    }
}
