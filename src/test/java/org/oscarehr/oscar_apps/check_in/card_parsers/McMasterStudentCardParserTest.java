package org.oscarehr.oscar_apps.check_in.card_parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class McMasterStudentCardParserTest
{
	public static final String VALID_MCMASTER_CARD_DATA_1="%000548174^0212?;000548174=0212?";
	public static final String VALID_MCMASTER_CARD_DATA_2=";000040167=0212?";
	public static final String VALID_MCMASTER_CARD_DATA_3=";100040167=0212?";
	public static final String VALID_MCMASTER_CARD_DATA_4="!002548174!0212!!000548174!0212!";
	public static final String VALID_MCMASTER_CARD_DATA_5="!002040167!0212!";
	
	@Test
	public void testIssuerValidity()
	{
		String magneticData=VALID_MCMASTER_CARD_DATA_1;
		McMasterStudentCardParser parser=new McMasterStudentCardParser();
		assertTrue(parser.isValidDataType(magneticData));

		magneticData=VALID_MCMASTER_CARD_DATA_2;
		assertTrue(parser.isValidDataType(magneticData));

		magneticData="%000548174^0212*;000548174=0292?";
		assertFalse(parser.isValidDataType(magneticData));

		magneticData=";000040167$0219?";
		assertFalse(parser.isValidDataType(magneticData));

		magneticData=";00004017=0212?";
		assertFalse(parser.isValidDataType(magneticData));

		magneticData=";000040167=0252?";
		assertFalse(parser.isValidDataType(magneticData));
	}
	
	@Test
	public void testParsing()
	{
		String magneticData=VALID_MCMASTER_CARD_DATA_1;		
		McMasterStudentCardParser parser=new McMasterStudentCardParser();
		parser.setMagneticCardData(magneticData);
		assertEquals("548174", parser.getStudentId());

		magneticData=VALID_MCMASTER_CARD_DATA_2;		
		parser=new McMasterStudentCardParser();
		parser.setMagneticCardData(magneticData);
		assertEquals("40167", parser.getStudentId());

		magneticData=VALID_MCMASTER_CARD_DATA_3;		
		parser=new McMasterStudentCardParser();
		parser.setMagneticCardData(magneticData);
		assertEquals("100040167", parser.getStudentId());

		magneticData=VALID_MCMASTER_CARD_DATA_4;		
		parser=new McMasterStudentCardParser();
		parser.setMagneticCardData(magneticData);
		assertEquals("2548174", parser.getStudentId());

		magneticData=VALID_MCMASTER_CARD_DATA_5;		
		parser=new McMasterStudentCardParser();
		parser.setMagneticCardData(magneticData);
		assertEquals("2040167", parser.getStudentId());
	}
}
