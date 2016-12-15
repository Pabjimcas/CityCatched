package com.pabji.citycatched.presentation.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pablo Jiménez Casado on 04/12/2016.
 */

public class StringFormat {
    public static String timemilisToDate(long val) {
        Date date = new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(date);
        return dateText;
    }

    public static String truncate2Decimals(float number){
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }
}
