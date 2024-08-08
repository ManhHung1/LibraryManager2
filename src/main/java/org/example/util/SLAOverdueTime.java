package org.example.util;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class SLAOverdueTime {

    private static final LocalTime MORNING_START = LocalTime.of(8, 0);
    private static final LocalTime MORNING_END = LocalTime.of(12, 0);
    private static final LocalTime AFTERNOON_START = LocalTime.of(13, 30);
    private static final LocalTime AFTERNOON_END = LocalTime.of(17, 30);

    private static final Set<LocalDate> DAYS_OFF = new HashSet<>();

    static {
        // Monday
        DAYS_OFF.add(LocalDate.of(2020,1,27));
        DAYS_OFF.add(LocalDate.of(2021,5,3));
        DAYS_OFF.add(LocalDate.of(2021,2,15));
        DAYS_OFF.add(LocalDate.of(2022,5,2));
        DAYS_OFF.add(LocalDate.of(2022,9,5));
        DAYS_OFF.add(LocalDate.of(2022,1,3));
        DAYS_OFF.add(LocalDate.of(2022,4,11));
        DAYS_OFF.add(LocalDate.of(2023,5,1));
        DAYS_OFF.add(LocalDate.of(2023,9,4));
        DAYS_OFF.add(LocalDate.of(2023,1,2));
        DAYS_OFF.add(LocalDate.of(2023,1,23));
        DAYS_OFF.add(LocalDate.of(2024,1,1));
        DAYS_OFF.add(LocalDate.of(2024,2,12));
        DAYS_OFF.add(LocalDate.of(2024,9,2));
        DAYS_OFF.add(LocalDate.of(2024,4,29));

        // Tuesday
        DAYS_OFF.add(LocalDate.of(2020,1,28));
        DAYS_OFF.add(LocalDate.of(2020,12,22));
        DAYS_OFF.add(LocalDate.of(2021,2,16));
        DAYS_OFF.add(LocalDate.of(2021,6,1));
        DAYS_OFF.add(LocalDate.of(2022,5,3));
        DAYS_OFF.add(LocalDate.of(2022,1,31));
        DAYS_OFF.add(LocalDate.of(2023,5,2));
        DAYS_OFF.add(LocalDate.of(2023,1,24));
        DAYS_OFF.add(LocalDate.of(2024,2,13));
        DAYS_OFF.add(LocalDate.of(2024,4,30));
        DAYS_OFF.add(LocalDate.of(2024,9,3));

        // Wednesday
        DAYS_OFF.add(LocalDate.of(2022,9,2));
        DAYS_OFF.add(LocalDate.of(2020,1,1));
        DAYS_OFF.add(LocalDate.of(2020,1,29));
        DAYS_OFF.add(LocalDate.of(2021,2,10));
        DAYS_OFF.add(LocalDate.of(2021,4,21));
        DAYS_OFF.add(LocalDate.of(2021,12,22));
        DAYS_OFF.add(LocalDate.of(2022,2,1));
        DAYS_OFF.add(LocalDate.of(2022,6,1));
        DAYS_OFF.add(LocalDate.of(2023,5,3));
        DAYS_OFF.add(LocalDate.of(2023,1,25));
        DAYS_OFF.add(LocalDate.of(2024,2,14));
        DAYS_OFF.add(LocalDate.of(2024,5,1));

        // Thursday
        DAYS_OFF.add(LocalDate.of(2020,4,30));
        DAYS_OFF.add(LocalDate.of(2020,1,23));
        DAYS_OFF.add(LocalDate.of(2020,4,2));
        DAYS_OFF.add(LocalDate.of(2021,9,2));
        DAYS_OFF.add(LocalDate.of(2021,2,11));
        DAYS_OFF.add(LocalDate.of(2022,2,2));
        DAYS_OFF.add(LocalDate.of(2022,12,22));
        DAYS_OFF.add(LocalDate.of(2023,1,26));
        DAYS_OFF.add(LocalDate.of(2023,6,1));
        DAYS_OFF.add(LocalDate.of(2024,2,8));
        DAYS_OFF.add(LocalDate.of(2024,4,18));

        // Friday
        DAYS_OFF.add(LocalDate.of(2020,5,1));
        DAYS_OFF.add(LocalDate.of(2020,1,24));
        DAYS_OFF.add(LocalDate.of(2021,4,30));
        DAYS_OFF.add(LocalDate.of(2021,9,3));
        DAYS_OFF.add(LocalDate.of(2021,1,1));
        DAYS_OFF.add(LocalDate.of(2021,2,12));
        DAYS_OFF.add(LocalDate.of(2022,9,2));
        DAYS_OFF.add(LocalDate.of(2022,2,3));
        DAYS_OFF.add(LocalDate.of(2023,12,22));
        DAYS_OFF.add(LocalDate.of(2023,1,20));
        DAYS_OFF.add(LocalDate.of(2023,9,1));
        DAYS_OFF.add(LocalDate.of(2024,2,9));

    }

    public static LocalDateTime calculateSLAOverdue(LocalDateTime triggerTime, BigDecimal stepValue) {
        LocalDateTime newTime = triggerTime;

        // Convert stepValue to minutes
        BigDecimal minutesInAnHour = new BigDecimal("60");
        BigDecimal stepMinutesDecimal = stepValue.multiply(minutesInAnHour);

        // Convert the BigDecimal to long for calculation
        long stepMinutes = stepMinutesDecimal.longValue();

        while (stepMinutes > 0){

            // loop to get to the next monday, ignoring weekend
            while (newTime.getDayOfWeek() == DayOfWeek.SATURDAY || newTime.getDayOfWeek() == DayOfWeek.SUNDAY || DAYS_OFF.contains(newTime.toLocalDate())) {
                newTime = newTime.plusDays(1).withHour(MORNING_START.getHour()).withMinute(MORNING_START.getMinute());
            }
            LocalTime currentTime = newTime.toLocalTime();

            // checking if it is currently working time
            if (isWorkingHours(currentTime)){
                long remainingMinutesToday = calculateRemainingMinutes(currentTime);
                if (stepMinutes <= remainingMinutesToday) {
                    newTime = newTime.plusMinutes(stepMinutes);
                    stepMinutes = 0;
                } else {
                    newTime = newTime.plusMinutes(remainingMinutesToday);
                    stepMinutes -= remainingMinutesToday;
                }
            }
            else {
                newTime = getNextWorkingHourStart(newTime);
            }
        }

        return newTime;
    }

    public static boolean isWorkingHours(LocalTime time) {
        return (time.equals(MORNING_START) || time.isAfter(MORNING_START) && time.isBefore(MORNING_END))
                || (time.equals(AFTERNOON_START) || time.isAfter(AFTERNOON_START) && time.isBefore(AFTERNOON_END));
    }

    public static long calculateRemainingMinutes(LocalTime time) {
        if (time.isBefore(MORNING_END)) {
            return ChronoUnit.MINUTES.between(time, MORNING_END);
        } else if (time.isBefore(AFTERNOON_END)) {
            return ChronoUnit.MINUTES.between(time, AFTERNOON_END);
        } else {
            return 0;
        }
    }

    public static LocalDateTime getNextWorkingHourStart(LocalDateTime newTime){
        LocalTime time = newTime.toLocalTime();
        if (time.isBefore(MORNING_START)) {
            return newTime.withHour(MORNING_START.getHour()).withMinute(MORNING_START.getMinute());
        }
        if (time.equals(MORNING_END) || time.isBefore(AFTERNOON_START)){
            return newTime.withHour(AFTERNOON_START.getHour()).withMinute(AFTERNOON_START.getMinute());
        }
        if (time.equals(AFTERNOON_END) || time.isAfter(AFTERNOON_END)){
            return newTime.plusDays(1).withHour(8).withMinute(0);
        }
        return newTime;
    }

    public static void printing(LocalDateTime triggerTime, BigDecimal stepValue){
        LocalDateTime expiration = calculateSLAOverdue(triggerTime, stepValue);
        System.out.println("Total hours of work: " + stepValue);
        System.out.println("SLA Overdue Time expires at : " +  expiration.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm")));
    }

}


