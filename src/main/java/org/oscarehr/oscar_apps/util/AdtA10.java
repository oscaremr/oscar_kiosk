package org.oscarehr.oscar_apps.util;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.SAD;
import ca.uhn.hl7v2.model.v26.datatype.XAD;
import ca.uhn.hl7v2.model.v26.datatype.XTN;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;
import ca.uhn.hl7v2.model.v26.segment.EVN;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.PV1;

public final class AdtA10
{
	private AdtA10()
	{
		// not meant to be instantiated
	}
	
	/**
	 * AdtA10 uses the ADTA09 structure.
	 * 
	 * @param senderName is anything identifying the sender, like "oscar_app check in". It's just a human readable string, nothing coded against.
	 * @throws HL7Exception 
	 */
	public static ADT_A09 makeAdtA10(String senderName, char patientClass, String room, String healthNumber, String healthCardVersionCode, String healthNumberProvince, DateHolder healthCardEffectiveDate, DateHolder healthCardExpiryDate, String lastName, String firstName, DateHolder birthDate, Character gender) throws HL7Exception
	{
		ADT_A09 adtA09=new ADT_A09();
		
		DataTypeUtils.fillMsh(adtA09.getMSH(), new Date(), senderName, "ADT", "A10", "ADT_A09", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(adtA09.getSFT(), "oscar_apps", MiscUtils.getBuildDateTime());

		fillEvn(adtA09.getEVN());
		DataTypeUtils.fillPid(adtA09.getPID(), 1, healthNumber, healthCardVersionCode, healthNumberProvince, healthCardEffectiveDate.year, healthCardEffectiveDate.month, healthCardEffectiveDate.day, healthCardExpiryDate.year, healthCardExpiryDate.month, lastName, firstName, birthDate.year, birthDate.month, birthDate.day, gender);
		fillPv1(adtA09.getPV1(), patientClass, room);
		
		return(adtA09);
	}

	/**
	 * AdtA10 uses the ADTA09 structure.
	 * 
	 * @param senderName is anything identifying the sender, like "oscar_app check in". It's just a human readable string, nothing coded against.
	 * @param chartNumber is the oscar demographic.chartNo
	 * @throws HL7Exception 
	 */
	public static ADT_A09 makeAdtA10(String senderName, char patientClass, String room, String chartNumber) throws HL7Exception
	{
		ADT_A09 adtA09=new ADT_A09();
		
		DataTypeUtils.fillMsh(adtA09.getMSH(), new Date(), senderName, "ADT", "A10", "ADT_A09", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(adtA09.getSFT(), "oscar_apps", MiscUtils.getBuildDateTime());

		fillEvn(adtA09.getEVN());
		DataTypeUtils.fillPid(adtA09.getPID(), 1, chartNumber);
		fillPv1(adtA09.getPV1(), patientClass, room);
		
		return(adtA09);
	}

	public static ADT_A09 makeAdtA08(String senderName, char patientClass, String room, HttpServletRequest request) throws HL7Exception, ParseException
	{
		ADT_A09 adtA09=new ADT_A09();
		
		DataTypeUtils.fillMsh(adtA09.getMSH(), new Date(), senderName, "ADT", "A08", "ADT_A09", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(adtA09.getSFT(), "oscar_apps", MiscUtils.getBuildDateTime());

		fillEvn(adtA09.getEVN());
		
		
		DataTypeUtils.fillPidForA08(adtA09.getPID(), request);
		fillAddrAndPhone(adtA09.getPID(), request.getParameter("street"), request.getParameter("city")
				, request.getParameter("province"), request.getParameter("postal")
				, request.getParameter("phone"), request.getParameter("cell"));
		fillPv1(adtA09.getPV1(), patientClass, room);
		
		return(adtA09);
	}
	
	private static void fillAddrAndPhone(PID pid, String street, String city
			, String province, String postal, String phone, String cell) 
	{
		if (pid == null) 
		{
			return;
		}
		try 
		{
			XAD xad = pid.getPid11_PatientAddress(0);
			if (xad != null) 
			{
				SAD sad = xad.getXad1_StreetAddress();
				if (sad != null && sad.getSad1_StreetOrMailingAddress() != null) 
				{
					sad.getSad1_StreetOrMailingAddress().setValue(street);
				}
				
				if (xad.getXad3_City() != null) 
				{
					xad.getXad3_City().setValue(city);
				}
				
				if (xad.getXad4_StateOrProvince() != null) 
				{
					xad.getXad4_StateOrProvince().setValue(province);
				}
				
				if (xad.getXad5_ZipOrPostalCode() != null) 
				{
					xad.getXad5_ZipOrPostalCode().setValue(postal);
				}
			}
			XTN xtn = pid.getPid13_PhoneNumberHome(0);
			if (xtn != null) 
			{
				if (xtn.getXtn1_TelephoneNumber() != null) 
				{
					xtn.getXtn1_TelephoneNumber().setValue(phone);
				}
			}
			xtn = pid.getPid14_PhoneNumberBusiness(0);
			if (xtn != null)
			{
				if (xtn.getXtn1_TelephoneNumber() != null) 
				{
					xtn.getXtn1_TelephoneNumber().setValue(cell);
				}
			}
		} 
		catch (Exception e) 
		{
			MiscUtils.getLogger().info(e.toString());
		}
	}
	
	private static void fillPv1(PV1 pv1, char patientClass, String room) throws DataTypeException
	{
		//Value Description
		//  E   Emergency
		//  I   Inpatient
		//  O   Outpatient
		//  P   Preadmit
		//  R   Recurring patient
		//  B   Obstetrics
		//  C   Commercial Account
		//  N   Not Applicable
		//  U   Unknown
		pv1.getPatientClass().setValue(String.valueOf(patientClass));

		pv1.getTemporaryLocation().getRoom().setValue(room);
	}

	private static void fillEvn(EVN evn) throws DataTypeException
	{
		evn.getRecordedDateTime().setValue(DataTypeUtils.getAsHl7FormattedString(new Date()));
	}	
}
