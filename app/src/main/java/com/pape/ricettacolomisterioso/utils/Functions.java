package com.pape.ricettacolomisterioso.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Functions {
    public static int time_in_day_remain(Date expiring, Date today){
        long time_in_millisecond = expiring.getTime() -  today.getTime();
        return (int) TimeUnit.DAYS.convert(time_in_millisecond, TimeUnit.MILLISECONDS);
    }


    public static int percentual_for_bar(Date purchase, Date expiring, Date today){
        long exp_pur = expiring.getTime() - purchase.getTime();
        long tod_pur = today.getTime() - purchase.getTime();
        double percentual_value_for_progress_bar = ((double)tod_pur/(double)exp_pur)*100;
        return (int)Math.round(percentual_value_for_progress_bar);
    }

    public static Date ExcludeTime(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);
        c.clear();
        c.set(year, month, day);
        return c.getTime();
    }

}
