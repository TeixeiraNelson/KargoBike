package ch.ribeironelson.kargobike.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp {

    public static String getTimeStamp(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String timestamp = df.format(c);

        return timestamp;
    }
}
