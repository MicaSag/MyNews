package com.android.sagot.mynews.Utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * class utilities for calculations and date conversions
 *
 * @author Michaël SAGOT
 * @version 1.0
 */

public class DateUtilities {

    private static final String TAG = DateUtilities.class.getSimpleName();

    /**
     * @since 1.0
     *
     * Return date in format string JJ/MM/AA
     *
     * @param date
     *          String starting with a date in the format SSAAMMJJ
     *
     * @return String Date in format JJ/MM/AA
     */
    public static String dateReformat(String date){

        String JJ = date.substring(6,8);            // Day
        String MM = date.substring(4,6);            // Month
        String AA = date.substring(2,4);            // Year

        return JJ+"/"+MM+"/"+AA;                    // JJ/MM/AA
    }

    /**
     * @since 1.0
     *
     * Return date in format string SSAAMMJJ
     *
     * @param date
     *          String starting with a date in the format begin SSAA-MM-JJ...
     *
     * @return String Date in format SSAAMMJJ
     */
    public static String dateReformatSSAAMMJJ(String date){

        String SSAA = date.substring(0,4);           // Year
        String MM = date.substring(5,7);             // Month
        String JJ = date.substring(8,10);            // Day

        return SSAA+MM+JJ;
    }

    /**
     * @since 1.0
     *
     * Return Hour of Day
     *
     * @return String in : hour of day
     */
    public static int getHourOfDay() {

        Date date = new Date();
        Log.d(TAG, "newCalendar: Date = "+date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Log.d(TAG, "newCalendar: Calendar.getTime = "+cal.get(Calendar.HOUR_OF_DAY));
        return cal.get(Calendar.HOUR_OF_DAY);
    }
}
