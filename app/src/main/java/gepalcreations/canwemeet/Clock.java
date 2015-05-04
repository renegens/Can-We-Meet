package gepalcreations.canwemeet;

import java.util.Calendar;


public class Clock {

    Calendar c = Calendar.getInstance();


    public int getSeconds() {
        int minute = c.get(Calendar.MINUTE);
        return minute;
    }

    public int getHours (){

        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour;
    }



}
