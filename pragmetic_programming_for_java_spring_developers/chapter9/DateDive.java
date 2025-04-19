package chapter9;

import java.util.Date;

public class DateDive {
    public static void main(String[] args) {
        Date date = new Date();
        date.setYear(2001);
        date.setMonth(2);
        date.setDate(12);
        System.out.println("date = " + date);
    }
}
