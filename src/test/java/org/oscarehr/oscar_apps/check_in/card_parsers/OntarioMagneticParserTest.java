package org.oscarehr.oscar_apps.check_in.card_parsers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.oscarehr.oscar_apps.check_in.card_parsers.HealthCardMagneticParser;
import org.oscarehr.oscar_apps.check_in.card_parsers.OntarioMagneticParser;

public class OntarioMagneticParserTest
{
	public static final String VALID_ONTARIO_CARD_DATA="%B610054hhhhhhhhhh^last_name/first_name      ^9901799119800102JDsssss90030401?0";
	
	@Test
	public void testIssuerValidity()
	{
		String magneticData=VALID_ONTARIO_CARD_DATA;

		HealthCardMagneticParser parser=new OntarioMagneticParser();
		
		assertTrue(parser.isValidDataType(magneticData));

		magneticData="%B611054hhhhhhhhhh^last_name/first_name      ^9901799119800102JDsssss90030401?0";
		assertFalse(parser.isValidDataType(magneticData));
	}
	
	@Test
	public void testParsing()
	{
		String magneticData=VALID_ONTARIO_CARD_DATA;
		
		HealthCardMagneticParser parser=new OntarioMagneticParser();
		parser.setMagneticCardData(magneticData);

		assertEquals("610054", parser.getCardIssuer());
		assertEquals("hhhhhhhhhh", parser.getHealthNumber());
		assertEquals("last_name", parser.getLastName());
		assertEquals("first_name", parser.getFirstName());
		assertEquals(1999, parser.getCardExpiryYear().intValue());
		assertEquals(1, parser.getCardExpiryMonth().intValue());
		assertEquals('M', parser.getGender().charValue());
		assertEquals(1980, parser.getBirthYear().intValue());
		assertEquals(1, parser.getBirthMonth().intValue());
		assertEquals(2, parser.getBirthDay().intValue());
		assertEquals("JD", parser.getCardVersion());
		assertEquals(1990, parser.getCardIssueYear().intValue());
		assertEquals(3, parser.getCardIssueMonth().intValue());
		assertEquals(4, parser.getCardIssueDay().intValue());
		
		
		magneticData="%B610054hhhhhhhhhh^last_name/first_name      ^0000799119800102  sssss03040501?0";
		parser=new OntarioMagneticParser();
		parser.setMagneticCardData(magneticData);
		assertNull(parser.getCardExpiryYear());
		assertNull(parser.getCardExpiryMonth());
		assertNull(parser.getCardVersion());
		assertEquals(2003, parser.getCardIssueYear().intValue());
		assertEquals(4, parser.getCardIssueMonth().intValue());
		assertEquals(5, parser.getCardIssueDay().intValue());
	}
}
