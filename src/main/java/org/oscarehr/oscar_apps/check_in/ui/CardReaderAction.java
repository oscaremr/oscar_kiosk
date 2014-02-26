package org.oscarehr.oscar_apps.check_in.ui;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.oscar_apps.check_in.card_parsers.HealthCardMagneticParser;
import org.oscarehr.oscar_apps.check_in.card_parsers.MagneticParser;
import org.oscarehr.oscar_apps.check_in.card_parsers.McMasterStudentCardParser;
import org.oscarehr.oscar_apps.check_in.card_parsers.ParserManager;
import org.oscarehr.oscar_apps.util.AdtA10;
import org.oscarehr.oscar_apps.util.ConfigUtils;
import org.oscarehr.oscar_apps.util.DateHolder;
import org.oscarehr.oscar_apps.util.MiscUtils;
import org.oscarehr.oscar_apps.util.SendingUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;

public class CardReaderAction
{
	private static Logger logger = MiscUtils.getLogger();
	private static final String serviceName = ConfigUtils.getProperty("checkIn.eDataServiceName");
	private static final String url = ConfigUtils.getProperty("checkIn.eDataUrl");
	private static final String serviceKeyBase64 = ConfigUtils.getProperty("checkIn.eDataServiceKeyBase64");
	private static final String oscarKeyBase64 = ConfigUtils.getProperty("checkIn.eDataOscarKeyBase64");

	private static final String SENDER = "Check In Kiosk : " + serviceName;
	private static final char PATIENT_TYPE = 'P';
	private static final String ROOM = "WAITING_ROOM";

	/**
	 * This method will attempt to process the magnetic card data passed in.
	 * If for *any* reason it fails, it will return false, true otherwise.
	 * Reasons for failure may include bad card data, oscar server not available,
	 * user not found in oscar, etc... The idea is that any type of failure 
	 * would tell the client to go see a receptionist so we don't really need
	 * to know why it failed.
	 */
	public static boolean processCardData(String magneticCardData)
	{
		magneticCardData = StringUtils.trimToNull(magneticCardData);

		if (magneticCardData == null)
		{
			logger.debug("magneticCardData was blank / null");
			return(false);
		}

		try
		{
			MagneticParser parser = ParserManager.getParser(magneticCardData);

			if (parser instanceof HealthCardMagneticParser)
			{
				return(sendHealthCardInfo((HealthCardMagneticParser)parser));
			}
			else if (parser instanceof McMasterStudentCardParser)
			{
				return(sendChartNo((McMasterStudentCardParser)parser));
			}
			else
			{
				throw(new IllegalStateException("Ummm I've misseed a case. class=" + parser.getClass().getName()));
			}
		}
		catch (Exception e)
		{
			logger.debug("connection problem ", e);
			return(false);
		}
	}

	private static boolean sendChartNo(McMasterStudentCardParser parser) throws HL7Exception, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException
	{
		ADT_A09 hl7Message = AdtA10.makeAdtA10(SENDER, PATIENT_TYPE, ROOM, parser.getStudentId());
		logger.debug("msg " + hl7Message + " url " + url + " oscarkey " + oscarKeyBase64 + " serv " + serviceKeyBase64 + " servNAme " + serviceName);
		int statusCode = SendingUtils.send(hl7Message, url, oscarKeyBase64, serviceKeyBase64, serviceName);
		logger.debug("statusCode" + statusCode);
		return(HttpServletResponse.SC_OK == statusCode);
	}

	private static boolean sendHealthCardInfo(HealthCardMagneticParser parser) throws HL7Exception, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException
	{
		DateHolder healthCardEffectiveDate = new DateHolder();
		healthCardEffectiveDate.year = parser.getCardIssueYear();
		healthCardEffectiveDate.month = parser.getCardIssueMonth();
		healthCardEffectiveDate.day = parser.getCardIssueDay();

		DateHolder healthCardExpiryDate = new DateHolder();
		healthCardExpiryDate.year = parser.getCardExpiryYear();
		healthCardExpiryDate.month = parser.getCardExpiryMonth();

		DateHolder birthDate = new DateHolder();
		birthDate.year = parser.getBirthYear();
		birthDate.month = parser.getBirthMonth();
		birthDate.day = parser.getBirthDay();

		ADT_A09 hl7Message = AdtA10.makeAdtA10(SENDER, PATIENT_TYPE, ROOM, parser.getHealthNumber(), parser.getCardVersion(), parser.getProvince(), healthCardEffectiveDate, healthCardExpiryDate, parser.getLastName(), parser.getFirstName(), birthDate,
				parser.getGender());
		logger.debug("msg " + hl7Message + " url " + url + " oscarkey " + oscarKeyBase64 + " serv " + serviceKeyBase64 + " servNAme " + serviceName);
		int statusCode = SendingUtils.send(hl7Message, url, oscarKeyBase64, serviceKeyBase64, serviceName);
		logger.debug("statusCode" + statusCode);
		return(HttpServletResponse.SC_OK == statusCode);
	}
}
