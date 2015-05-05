package gepalcreations.canwemeet;

import java.util.Calendar;
import java.util.TimeZone;


public class Clock {

    Calendar current = Calendar.getInstance();


    public int getMinutes() {
        int minute = current.get(Calendar.MINUTE);
        return minute;
    }

    public int getHours (){

        int hour = current.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

	//Getting Local time zone from system
	public int getTimeZone() {

		TimeZone tzCurrent = current.getTimeZone();
		return tzCurrent.getRawOffset() / 3600000;//seconds per day

	}



}
