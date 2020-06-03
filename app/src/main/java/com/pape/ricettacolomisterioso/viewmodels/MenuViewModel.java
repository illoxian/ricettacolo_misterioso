package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MenuViewModel extends ViewModel {
    private final String TAG= "MenuViewModel";

    private int weekOffset;

    private MutableLiveData<List<Date>> days;

    public MenuViewModel() {
        weekOffset = 0;
    }

    public MutableLiveData<List<Date>> getDays() {
        if (days == null) {
            days = new MutableLiveData<>();
        }
        return days;
    }

    public void ChangeWeek(int offset){
        weekOffset += offset;
        getWeekDays();
    }

    private void getWeekDays(){
        Calendar c1 = Calendar.getInstance();
        int weekOfYear = c1.get(Calendar.WEEK_OF_YEAR);

        weekOfYear += weekOffset;
        c1.set(Calendar.WEEK_OF_YEAR, weekOfYear);

        List<Date> dayList = new ArrayList<>();
        for(int i=0; i<7; i++){
            int day = ((Calendar.MONDAY + 7 + i - 1) % 7)+1;
            c1.set(Calendar.DAY_OF_WEEK, day);
            dayList.add(c1.getTime());
        }
        days.postValue(dayList);
    }

    public String getWeekRangeString() {
        SimpleDateFormat format = new SimpleDateFormat("d MMM", Locale.getDefault());

        if (getDays().getValue().size() > 0) {
            String startDayString = format.format(getDays().getValue().get(0).getTime());
            String endDayString = format.format(getDays().getValue().get(getDays().getValue().size()-1).getTime());
            return startDayString + " - " + endDayString;
        }
        return null;
    }
}
