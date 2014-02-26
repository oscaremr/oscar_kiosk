package org.oscarehr.oscar_apps.check_in.card_parsers;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.oscar_apps.util.MiscUtils;

/**
 * This parser is based on this information that I tried to make up from the specs... I hope it's correct.
 * %B610043<hin(10)>0^<last_name>/<first_name>(26)^<expiry(YYMM)><issueDate(YYMM/0000)><birthday(CCYYMM(DD/00))>0000000000000?00
 */
public final class BcMagneticParser extends HealthCardMagneticParser
{
	private static final Logger logger = MiscUtils.getLogger();

	@Override
	void parseMagneticCardData()
	{
		if (!isValidDataType(magneticCardData)) throw(new IllegalArgumentException("Invalid magneticCardData for this parser."));
		
		cardIssuer = magneticCardData.substring(2, 8);
		healthNumber = magneticCardData.substring(8, 18);

		String tempString = magneticCardData.substring(20, 46);
		int index = tempString.indexOf('/');
		lastName = StringUtils.trimToNull(tempString.substring(0, index));
		firstName = StringUtils.trimToNull(tempString.substring(index + 1));

		try
		{
			// figure out the year now, assume anything 80-99 is 19th centry, where everything else is 20th centry
			tempString = magneticCardData.substring(47, 49);
			int tempNumber = Integer.parseInt(tempString);
			if (tempNumber > 80) tempString = "19" + tempString;
			else tempString = "20" + tempString;
			cardExpiryYear = Integer.parseInt(tempString);

			cardExpiryMonth = Integer.parseInt(magneticCardData.substring(49, 51));
		}
		catch (Exception e)
		{
			logger.error("Unexpected error. string=" + magneticCardData.substring(47, 51), e);
		}

		try
		{
			// all cards after 1991 have issue date : year=00 month=00
			tempString = magneticCardData.substring(51, 53);
			int tempNumber = Integer.parseInt(tempString);
			if (tempNumber != 0)
			{
				cardIssueYear = Integer.parseInt("19" + tempString);
				cardIssueMonth = Integer.parseInt(magneticCardData.substring(53, 55));
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected error. string=" + magneticCardData.substring(51, 55), e);
		}

		try
		{
			birthYear = Integer.parseInt(magneticCardData.substring(55, 59));
			birthMonth = Integer.parseInt(magneticCardData.substring(59, 61));
			int tempNumber = Integer.parseInt(magneticCardData.substring(61, 63));
			if (tempNumber != 0) birthDay = tempNumber;
		}
		catch (Exception e)
		{
			logger.error("Unexpected error. string=" + magneticCardData.substring(45, 63), e);
		}

	}

	@Override
	boolean isValidDataType(String magneticCardData)
	{
		return(magneticCardData.startsWith("%B610043"));
	}

	@Override
	public String getProvince()
	{
		return("BC");
	}
}
