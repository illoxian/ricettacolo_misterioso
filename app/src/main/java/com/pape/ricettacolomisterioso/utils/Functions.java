package com.pape.ricettacolomisterioso.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.pape.ricettacolomisterioso.R;

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

    public static @ColorInt int getThemeColor(Context context, int ResId){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(ResId, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static String getProductCategoryString(Context context, int categoryId){
        return context.getResources().getStringArray(R.array.categoriesString)[categoryId];
    }
}
