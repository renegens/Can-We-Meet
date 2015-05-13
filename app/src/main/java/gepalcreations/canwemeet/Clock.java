package gepalcreations.canwemeet;

import java.util.Calendar;
import java.util.TimeZone;


public class Clock {

	Calendar current = Calendar.getInstance();



	public int getMinutes() {
		return current.get(Calendar.MINUTE);
	}

	public int getHours() {

		return current.get(Calendar.HOUR_OF_DAY);
	}

	//Getting Local time zone from system
	public int getTimeZone() {

		TimeZone tzCurrent = current.getTimeZone();
		return tzCurrent.getRawOffset() / 3600000;//seconds per day

	}
}