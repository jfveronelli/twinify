package ar.org.crossknight.twinify.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Formatter {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private Formatter() {}

	public static final String format(Date date) {
		return dateFormat.format(date);
	}

	public static final Date parseDate(String text) throws ParseException {
		return dateFormat.parse(text);
	}

}