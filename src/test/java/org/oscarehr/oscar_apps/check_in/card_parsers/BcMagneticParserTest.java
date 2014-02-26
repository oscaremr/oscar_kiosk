package org.oscarehr.oscar_apps.check_in.card_parsers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.oscarehr.oscar_apps.check_in.card_parsers.BcMagneticParser;
import org.oscarehr.oscar_apps.check_in.card_parsers.HealthCardMagneticParser;

public class BcMagneticParserTest
{
	public static final String VALID_BC_CARD_DATA="%B610043hhhhhhhhhh0^last_name/first_name      ^99039704197506070000000000000?00";
	
	@Test
	public void testIssuerValidity()
	{
		String magneticData=VALID_BC_CARD_DATA;
		HealthCardMagneticParser parser=new BcMagneticParser();
		assertTrue(parser.isValidDataType(magneticData));
		
		magneticData="%x610043hhhhhhhhhh0^last_name/first_name      ^99039704197506070000000000000?00";
		assertFalse(parser.isValidDataType(magneticData));
	}

	@Test
	public void testParsing()
	{
		String magneticData=VALID_BC_CARD_DATA;
		
		HealthCardMagneticParser parser=new BcMagneticParser();
		parser.setMagneticCardData(magneticData);

		assertEquals("610043", parser.getCardIssuer());
		assertEquals("hhhhhhhhhh", parser.getHealthNumber());
		assertEquals("last_name", parser.getLastName());
		assertEquals("first_name", parser.getFirstName());
		assertEquals(1999, parser.getCardExpiryYear().intValue());
		assertEquals(3, parser.getCardExpiryMonth().intValue());
		assertNull(parser.getGender());
		assertEquals(1975, parser.getBirthYear().intValue());
		assertEquals(6, parser.getBirthMonth().intValue());
		assertEquals(7, parser.getBirthDay().intValue());
		assertNull(parser.getCardVersion());
		assertEquals(1997, parser.getCardIssueYear().intValue());
		assertEquals(4, parser.getCardIssueMonth().intValue());
		assertNull(parser.getCardIssueDay());
		
		
		magneticData="%B610043hhhhhhhhhh0^last_name/first_name      ^99030000197506000000000000000?00";
		parser=new BcMagneticParser();
		parser.setMagneticCardData(magneticData);
		assertNull(parser.getCardIssueYear());
		assertNull(parser.getCardIssueMonth());
		assertNull(parser.getCardIssueDay());
		assertNull(parser.getBirthDay());
	}
}
