package org.oscarehr.oscar_apps.check_in.card_parsers;

import org.apache.log4j.Logger;
import org.oscarehr.oscar_apps.util.MiscUtils;

/**
 * I've been told that the magnetic data looks like the following 2 examples
 * %000548174^0212?;000548174=0212?
 * ;000040167=0212?
 * 
 * We've been told that the delimiters sometimes change? but the data lengths don't... sounds odd but that's what we've been told.
 * So the assumption is that the following also needs to parse (where I've randomly replaces all the delimiters with ! as a new delimiter).
 * !000548174!0212!!000548174!0212!
 * !000040167!0212!
 */
public class McMasterStudentCardParser extends MagneticParser
{
	private static Logger logger = MiscUtils.getLogger();

	private static final String LONG_PATTERN = "^\\W[0-9]{9}\\W[0-9]{4}\\W\\W[0-9]{9}\\W0212\\W$";
	private static final String SHORT_PATTERN = "^\\W[0-9]{9}\\W0212\\W$";

	private String studentId;

	@Override
	public boolean isValidDataType(String magneticCardData)
	{
		return(isValidShortForm(magneticCardData) || isValidLongForm(magneticCardData));
	}

	private boolean isValidLongForm(String magneticCardData)
	{
		// %000548174^0212?;000548174=0212?

		boolean result=magneticCardData.matches(LONG_PATTERN);
		logger.debug("isValidLongForm:"+magneticCardData+":"+result);
		return(result);
	}

	private boolean isValidShortForm(String magneticCardData)
	{
		// ;000040167=0212?

		boolean result=magneticCardData.matches(SHORT_PATTERN);
		logger.debug("isValidLongForm:"+magneticCardData+":"+result);
		return(result);
	}

	@Override
	public void parseMagneticCardData()
	{
		studentId = magneticCardData.substring(1, 10);

		// This is to change something like 0001234 to 1234
		studentId = String.valueOf(Integer.parseInt(studentId));
	}

	public String getStudentId()
	{
		return(studentId);
	}
}
