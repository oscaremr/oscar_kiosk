package org.oscarehr.oscar_apps.check_in.card_parsers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.oscarehr.oscar_apps.util.ConfigUtils;
import org.oscarehr.oscar_apps.util.MiscUtils;

/**
 * This is the main class people should be using when they need card data parsed.
 * This class will be responsible for finding a good parser and returning an instance
 * of the parser initialised with the given data.
 *
 * Typical usage would be as follows:
 * String cardData=...;
 * HealthCardMagneticParser parser=ParserManager.getParser(cardData);
 * parser.getFirstName();
 * etc...
 */
public final class ParserManager
{
	private static final Logger logger=MiscUtils.getLogger();
	private static ArrayList<MagneticParser> parsers=getParsers();
		
	private static ArrayList<MagneticParser> getParsers()
	{
		ArrayList<MagneticParser> results=new ArrayList<MagneticParser>();
		
		try
		{
			String cardParsers=ConfigUtils.getProperty("checkIn.cardParsers");
			String[] classNames=cardParsers.split(",");
			for (String className : classNames)
			{
				Class<?> c=Class.forName(className.trim());
				try
				{
					MagneticParser parser=(MagneticParser)c.newInstance();
					logger.info("Initialising card parser : "+c.getSimpleName());
					results.add(parser);					
				}
				catch (Exception e)
				{
					logger.error("Unexpected error processing card parser, className="+className, e);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Error initialising health card parsers.", e);
		}
		
		return(results);
	}
	
	public static MagneticParser getParser(String magneticCardData) throws InstantiationException, IllegalAccessException
	{
		magneticCardData=magneticCardData.toUpperCase();
		
		for (MagneticParser parser : parsers)
		{
			if (parser.isValidDataType(magneticCardData))
			{
				MagneticParser newParser=parser.getClass().newInstance();
				newParser.setMagneticCardData(magneticCardData);
				return(newParser);
			}
		}
		
		String errorMsg="No card parsers found capable of parsing this data : "+magneticCardData;
		logger.error(errorMsg);
		throw(new IllegalArgumentException(errorMsg));
	}
}
