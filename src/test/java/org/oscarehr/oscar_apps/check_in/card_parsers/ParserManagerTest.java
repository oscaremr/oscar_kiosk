package org.oscarehr.oscar_apps.check_in.card_parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ParserManagerTest
{
	@Test
	public void testParserManagerSearchingForParser() throws InstantiationException, IllegalAccessException
	{
		// no need to test all parsers here, just checking that it can distinguish between 2 properly is enough
		
		MagneticParser parser=ParserManager.getParser(BcMagneticParserTest.VALID_BC_CARD_DATA);
		assertEquals(BcMagneticParser.class, parser.getClass());
		
		parser=ParserManager.getParser(OntarioMagneticParserTest.VALID_ONTARIO_CARD_DATA);
		assertEquals(OntarioMagneticParser.class, parser.getClass());

		try
		{
			parser=ParserManager.getParser("some random data blah blah");
			fail("The code should have thrown an exception here.");
		}
		catch (IllegalArgumentException e)
		{
			// this is okay and is what is expected.
		}
	}
}
