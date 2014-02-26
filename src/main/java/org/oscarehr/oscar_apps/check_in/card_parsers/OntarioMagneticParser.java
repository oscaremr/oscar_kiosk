package org.oscarehr.oscar_apps.check_in.card_parsers;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.oscar_apps.util.MiscUtils;

/**
 * This parser is based on this information that I tried to make up from the specs... I hope it's correct. 
 * %b610054<hin(10)>^<last_name>/<first_name>(26)^<expiry(YYMM/0000)>799<sex(1=M,2=F)><birthday(YYYYMMDD)><cardVersion(XX/blank)><shortName(5)><issueDate(YYMMDD)><language(01=en,02=fr)>?0
 */
public final class OntarioMagneticParser extends HealthCardMagneticParser
{
	private static Logger logger = MiscUtils.getLogger();

	@Override
	void parseMagneticCardData()
	{
		cardIssuer = magneticCardData.substring(2, 8);
		healthNumber = magneticCardData.substring(8, 18);

		String tempString = magneticCardData.substring(19, 45);
		int index = tempString.indexOf('/');
		lastName = StringUtils.trimToNull(tempString.substring(0, index));
		firstName = StringUtils.trimToNull(tempString.substring(index + 1));

		try
		{
			// check if month is 00, we have to check because 0000 is used for no expiry which means we can't tell based on the year (year 2000 = 00)
			tempString = magneticCardData.substring(48, 50);
			int tempNumber = Integer.parseInt(tempString);
			if (tempNumber != 0)
			{
				cardExpiryMonth = tempNumber;

				// figure out the year now, assume anything 80-99 is 19th centry, where everything else is 20th centry
				tempString = magneticCardData.substring(46, 48);
				tempNumber = Integer.parseInt(tempString);
				if (tempNumber > 80) tempString = "19" + tempString;
				else tempString = "20" + tempString;
				cardExpiryYear = Integer.parseInt(tempString);
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected error. string=" + magneticCardData.substring(46, 50), e);
		}

		char c = magneticCardData.charAt(53);
		if ('1' == c) gender = 'M';
		else if ('2' == c) gender = 'F';
		else logger.error("Error parsing gender, encountered value=" + c);

		try
		{
			birthYear = Integer.parseInt(magneticCardData.substring(54, 58));
			birthMonth = Integer.parseInt(magneticCardData.substring(58, 60));
			birthDay = Integer.parseInt(magneticCardData.substring(60, 62));
		}
		catch (Exception e)
		{
			logger.error("Unexpected error. string=" + magneticCardData.substring(54, 62), e);
		}

		cardVersion = StringUtils.trimToNull(magneticCardData.substring(62, 64));

		try
		{
			// figure out the year now, assume anything 80-99 is 19th centry, where everything else is 20th centry
			tempString = magneticCardData.substring(69, 71);
			int tempNumber = Integer.parseInt(tempString);
			if (tempNumber > 80) tempString = "19" + tempString;
			else tempString = "20" + tempString;
			cardIssueYear = Integer.parseInt(tempString);

			cardIssueMonth = Integer.parseInt(magneticCardData.substring(71, 73));
			cardIssueDay = Integer.parseInt(magneticCardData.substring(73, 75));
		}
		catch (Exception e)
		{
			logger.error("Unexpected error. string=" + magneticCardData.substring(69, 75), e);
		}
	}

	@Override
	boolean isValidDataType(String magneticCardData)
	{
		return(magneticCardData.startsWith("%B610054"));
	}

	@Override
	public String getProvince()
	{
		return("ON");
	}
}
