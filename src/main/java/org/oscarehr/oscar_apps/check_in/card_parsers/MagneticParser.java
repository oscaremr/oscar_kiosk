package org.oscarehr.oscar_apps.check_in.card_parsers;

import org.apache.log4j.Logger;
import org.oscarehr.oscar_apps.util.MiscUtils;

/**
 * The idea here is that specific parsers will subclass this class and provide the setMagneticCardData() method.
 * That method should then populate all the protected variables. After that all the getter methods should work as expected.
 * Instances of this class should not be reused with different card data. The setMagneticCardData method should disallow it
 * (The reason being it's hard to guarantee the parseData method will reinitialise all variables every time). 
 */
public abstract class MagneticParser
{
	private static final Logger logger=MiscUtils.getLogger();
	
	protected String magneticCardData;
		
	/**
	 * This method should return true if the data appears to match the parsing expectations of the concrete class.
	 * For our purposes we will most likely just check the card issuer and assume that is like a province identifier.
	 */
	abstract boolean isValidDataType(final String magneticCardData);
	
	/**
	 * This method should populate all the member variables with data parsed from the magneticCardData.
	 */	
	abstract void parseMagneticCardData();
	
	public void setMagneticCardData(final String magneticCardData)
	{
		if (this.magneticCardData!=null) throw(new IllegalStateException("Instances of this class should not be reused, make a new class instance for each magneticCardData."));
		
		if (!isValidDataType(magneticCardData)) throw(new IllegalArgumentException("Card data not parsable by this parser."));
		
		logger.debug("magnetic card data (between quotes) '"+magneticCardData+'\'');
		
		this.magneticCardData=magneticCardData;
		parseMagneticCardData();
	}
}
