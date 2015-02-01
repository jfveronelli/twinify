package ar.org.crossknight.twinify.util;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormatterTest {

    @Test
	public void formatAndParseDateShouldBeReversibleAndHaveCustomSymbols() throws ParseException {
	    Date now = new Date();
	    long millis = now.getTime() / 1000L * 1000L;

	    String text = Formatter.format(now);
	    Date date = Formatter.parseDate(text);

	    assertEquals(19, text.length());
	    assertEquals('-', text.charAt(4));
	    assertEquals('-', text.charAt(7));
	    assertEquals(' ', text.charAt(10));
	    assertEquals(':', text.charAt(13));
	    assertEquals(':', text.charAt(16));
	    assertEquals(millis, date.getTime());
	}

}