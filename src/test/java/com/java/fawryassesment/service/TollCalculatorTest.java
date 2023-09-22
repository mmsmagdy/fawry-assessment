package com.java.fawryassesment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.java.fawryassesment.model.Car;
import com.java.fawryassesment.model.Motorbike;

public class TollCalculatorTest {


    private TollCalculator calculator;
    private Car car;
    private Motorbike motorbike;

    @BeforeEach
    public void setUp() {
        calculator = new TollCalculator();
        car = new Car();
        motorbike = new Motorbike();
    }

    @Test
    public void testTollCalculator() {
        // Test a vehicle with no fee-free dates or vehicles
        Date[] dates1 = {createDate(2023, Calendar.SEPTEMBER, 22, 6, 15), createDate(2023, Calendar.SEPTEMBER, 22, 7, 45), createDate(2023, Calendar.SEPTEMBER, 22, 8, 30)};
        int totalFee1 = calculator.getTollFee(car, dates1);
        assertEquals(34, totalFee1);

        // Test a fee-free vehicle
        Date[] dates2 = {createDate(2023, Calendar.SEPTEMBER, 22, 6, 15), createDate(2023, Calendar.SEPTEMBER, 22, 7, 45), createDate(2023, Calendar.SEPTEMBER, 22, 8, 30)};
        int totalFee2 = calculator.getTollFee(motorbike, dates2);
        assertEquals(0, totalFee2);

        // Test fee-free dates (Saturday and Sunday)
        Date[] dates3 = {createDate(2023, Calendar.SEPTEMBER, 23, 6, 15), createDate(2023, Calendar.SEPTEMBER, 24, 7, 45),};
        int totalFee3 = calculator.getTollFee(car, dates3);
        assertEquals(0, totalFee3);

        // Test maximum daily fee
        Date[] dates4 = {createDate(2023, Calendar.SEPTEMBER, 22, 6, 15), createDate(2023, Calendar.SEPTEMBER, 22, 7, 45), createDate(2023, Calendar.SEPTEMBER, 22, 8, 30), createDate(2023, Calendar.SEPTEMBER, 22, 9, 15), createDate(2023, Calendar.SEPTEMBER, 22, 10, 30)};
        int totalFee4 = calculator.getTollFee(car, dates4);
        assertEquals(42, totalFee4);

        // Test a car with multiple passes in the same hour (highest fee applies)
        Date[] dates5 = {createDate(2023, Calendar.SEPTEMBER, 22, 6, 15), createDate(2023, Calendar.SEPTEMBER, 22, 6, 45), createDate(2023, Calendar.SEPTEMBER, 22, 7, 30)};
        int totalFee5 = calculator.getTollFee(car, dates5);
        assertEquals(31, totalFee5);

        // Test a car with passes spanning multiple hours
        Date[] dates6 = {createDate(2023, Calendar.SEPTEMBER, 22, 6, 45), createDate(2023, Calendar.SEPTEMBER, 22, 8, 30), createDate(2023, Calendar.SEPTEMBER, 22, 10, 15)};
        int totalFee6 = calculator.getTollFee(car, dates6);
        assertEquals(21, totalFee6);
    }

    private Date createDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTime();
    }
}
