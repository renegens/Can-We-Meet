package gepalcreations.canwemeet;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Set;

/**
 * Created by renegens on 5/10/15.
 */
public class JodaZones {

	String city;

	Set<String> zoneIds = DateTimeZone.getAvailableIDs();
	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("ZZ");
}

