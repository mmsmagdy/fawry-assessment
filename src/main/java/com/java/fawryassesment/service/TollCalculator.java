package com.java.fawryassesment.service;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.java.fawryassesment.model.Vehicle;

public class TollCalculator {

    // Constants for fee rates
    private static final int FEE_LOW = 8;
    private static final int FEE_MEDIUM = 13;
    private static final int FEE_HIGH = 18;
    private static final int MAX_DAILY_FEE = 60;

    /**
     * Calculate the total toll fee for one day
     *
     * @param vehicle - the vehicle
     * @param dates   - date and time of all passes on one day
     * @return - the total toll fee for that day
     */
    public int getTollFee(Vehicle vehicle, Date... dates) {
        Date intervalStart = dates[0];
        int totalFee = 0;
        for (Date date : dates) {
            int nextFee = getTollFee(date, vehicle);
            int tempFee = getTollFee(intervalStart, vehicle);

            long minutes = getMinutesDifference(date, intervalStart);

            if (minutes <= 60) {
                if (totalFee > 0)
                    totalFee -= tempFee;
                if (nextFee >= tempFee)
                    tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }
        }
        if (totalFee > MAX_DAILY_FEE)
            totalFee = MAX_DAILY_FEE;
        return totalFee;
    }

    private long getMinutesDifference(Date date1, Date date2) {
        TimeUnit timeUnit = TimeUnit.MINUTES;

        long diffInMillies = date1.getTime() - date2.getTime();
        long minutes = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return minutes; // Convert milliseconds to minutes
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null)
            return false;
        String vehicleType = vehicle.getType();
        return Arrays.asList(TollFreeVehicles.values()).stream().anyMatch(v -> v.getType().equals(vehicleType));
    }

    public int getTollFee(final Date date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle))
            return 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour == 6 && minute >= 0 && minute <= 29)
            return FEE_LOW;
        else if (hour == 6 && minute >= 30 && minute <= 59)
            return FEE_MEDIUM;
        else if (hour == 7 && minute >= 0 && minute <= 59)
            return FEE_HIGH;
        else if (hour == 8 && minute >= 0 && minute <= 29)
            return FEE_MEDIUM;
        else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59)
            return FEE_LOW;
        else if (hour == 15 && minute >= 0 && minute <= 29)
            return FEE_MEDIUM;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59)
            return FEE_HIGH;
        else if (hour == 17 && minute >= 0 && minute <= 59)
            return FEE_MEDIUM;
        else if (hour == 18 && minute >= 0 && minute <= 29)
            return FEE_LOW;
        else
            return 0;
    }

    private boolean isTollFreeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
            return true;

        if (year == 2013) {
            if (month == Calendar.JANUARY && day == 1 || month == Calendar.MARCH && (day == 28 || day == 29) || month == Calendar.APRIL && (day == 1 || day == 30) || month == Calendar.MAY && (day == 1 || day == 8 || day == 9) || month == Calendar.JUNE && (day == 5 || day == 6 || day == 21) || month == Calendar.JULY || month == Calendar.NOVEMBER && day == 1 || month == Calendar.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31)) {
                return true;
            }
        }
        return false;
    }

    private enum TollFreeVehicles {
        MOTORBIKE("Motorbike"), TRACTOR("Tractor"), EMERGENCY("Emergency"), DIPLOMAT("Diplomat"), FOREIGN("Foreign"), MILITARY("Military");

        private final String type;

        TollFreeVehicles(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}

