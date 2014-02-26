package org.oscarehr.oscar_apps.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.datatype.CX;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;
import ca.uhn.hl7v2.model.v26.segment.PID;

public class DataTypeUtilsTest
{
	@Test
	public void testDateWithNullDayValues() throws HL7Exception
	{
		ADT_A09 adtA09=new ADT_A09();
		DataTypeUtils.fillPid(adtA09.getPID(), 1, "1", "1", "1", 1920, 1, null, 1872, null, "lastName", "firstName", 1877, 03, null, null);

		PID pid=adtA09.getPID();
		CX cx = pid.getPatientIdentifierList(0);

		assertEquals(1920, cx.getEffectiveDate().getYear());
		assertEquals(1, cx.getEffectiveDate().getMonth());
		assertEquals(0, cx.getEffectiveDate().getDay());

		assertEquals(1872, cx.getExpirationDate().getYear());
		assertEquals(0, cx.getExpirationDate().getMonth());
		assertEquals(0, cx.getExpirationDate().getDay());

               
		assertEquals("18770300", pid.getDateTimeOfBirth().getValue());
	}
}
