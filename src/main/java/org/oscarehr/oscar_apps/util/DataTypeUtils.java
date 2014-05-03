package org.oscarehr.oscar_apps.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.CX;
import ca.uhn.hl7v2.model.v26.datatype.DT;
import ca.uhn.hl7v2.model.v26.datatype.DTM;
import ca.uhn.hl7v2.model.v26.datatype.FT;
import ca.uhn.hl7v2.model.v26.datatype.XAD;
import ca.uhn.hl7v2.model.v26.datatype.XPN;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.NTE;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.SFT;

public final class DataTypeUtils
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final String HEALTH_NUMBER = "HEALTH_NUMBER";
	private static final String CHART_NUMBER = "CHART_NUMBER";
	public static final String HL7_VERSION_ID = "2.6";
	public static final int NTE_COMMENT_MAX_SIZE = 65536;
	public static final String ACTION_ROLE_SENDER = "SENDER";
	public static final String ACTION_ROLE_RECEIVER = "RECEIVER";
	private static final Base64 base64 = new Base64();

	/**
	 * Don't access this formatter directly, use the getAsFormattedString method, it provides synchronisation
	 */
	private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

	private DataTypeUtils()
	{
		// not meant to be instantiated by anyone, it's a util class
	}

	public static String encodeToBase64String(byte[] b) throws UnsupportedEncodingException
	{
		return(new String(base64.encode(b), MiscUtils.ENCODING));
	}

	public static byte[] decodeBase64(String s) throws UnsupportedEncodingException
	{
		return(base64.decode(s.getBytes(MiscUtils.ENCODING)));
	}

	public static String encodeToBase64String(String s) throws UnsupportedEncodingException
	{
		return(new String(base64.encode(s.getBytes(MiscUtils.ENCODING)), MiscUtils.ENCODING));
	}

	public static String decodeBase64StoString(String s) throws UnsupportedEncodingException
	{
		return(new String(base64.decode(s.getBytes(MiscUtils.ENCODING)), MiscUtils.ENCODING));
	}

	public static String getAsHl7FormattedString(Date date)
	{
		synchronized (dateTimeFormatter)
		{
			return(dateTimeFormatter.format(date));
		}
	}

	public static GregorianCalendar getCalendarFromDTM(DTM dtm) throws DataTypeException
	{

		// hl7/hapi returns 0 for no date
		if (dtm.getYear() == 0 || dtm.getMonth() == 0 || dtm.getDay() == 0) return(null);

		GregorianCalendar cal = new GregorianCalendar();
		// zero out fields we don't use
		cal.setTimeInMillis(0);
		cal.set(GregorianCalendar.YEAR, dtm.getYear());
		cal.set(GregorianCalendar.MONTH, dtm.getMonth() - 1);
		cal.set(GregorianCalendar.DAY_OF_MONTH, dtm.getDay());
		cal.set(GregorianCalendar.HOUR_OF_DAY, dtm.getHour());
		cal.set(GregorianCalendar.MINUTE, dtm.getMinute());
		cal.set(GregorianCalendar.SECOND, dtm.getSecond());

		// force materialisation of values
		cal.getTimeInMillis();

		return(cal);
	}

	/**
	 * @param xad
	 * @param streetAddress i.e. 123 My St. unit 554
	 * @param city
	 * @param province 2 digit province / state code as defined by postal service, in canada it's in upper case, i.e. BC, ON
	 * @param country iso 3166, 3 digit version / hl70399 code, i.e. USA, CAN, AUS in upper case.
	 * @param addressType hlt0190 code, i.e. O=office, H=Home
	 * @throws DataTypeException
	 */
	public static void fillXAD(XAD xad, StreetAddressDataHolder streetAddressDataHolder, String addressType) throws DataTypeException
	{
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(streetAddressDataHolder.streetAddress));
		xad.getCity().setValue(StringUtils.trimToNull(streetAddressDataHolder.city));
		xad.getStateOrProvince().setValue(StringUtils.trimToNull(streetAddressDataHolder.province));
		xad.getCountry().setValue(StringUtils.trimToNull(streetAddressDataHolder.country));
		xad.getZipOrPostalCode().setValue(StringUtils.trimToNull(streetAddressDataHolder.postalCode));
		xad.getAddressType().setValue(addressType);
	}

	/**
	 * @param msh
	 * @param dateOfMessage
	 * @param facilityName facility.getName();
	 * @param messageCode i.e. "REF"
	 * @param triggerEvent i.e. "I12"
	 * @param messageStructure i.e. "REF_I12"
	 * @param hl7VersionId is the version of hl7 in use, i.e. "2.6"
	 */
	public static void fillMsh(MSH msh, Date dateOfMessage, String facilityName, String messageCode, String triggerEvent, String messageStructure, String hl7VersionId) throws DataTypeException
	{
		msh.getFieldSeparator().setValue("|");
		msh.getEncodingCharacters().setValue("^~\\&");
		msh.getVersionID().getVersionID().setValue(hl7VersionId);

		msh.getDateTimeOfMessage().setValue(getAsHl7FormattedString(dateOfMessage));

		msh.getSendingApplication().getNamespaceID().setValue("OSCAR");

		msh.getSendingFacility().getNamespaceID().setValue(facilityName);

		// message code "REF", event "I12", structure "REF I12"
		msh.getMessageType().getMessageCode().setValue(messageCode);
		msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
		msh.getMessageType().getMessageStructure().setValue(messageStructure);
	}

	/**
	 * @param sft
	 * @param version major version if available
	 * @param build build date or build number if available
	 */
	public static void fillSft(SFT sft, String version, String build) throws DataTypeException
	{
		sft.getSoftwareVendorOrganization().getOrganizationName().setValue("OSCARMcMaster");
		sft.getSoftwareCertifiedVersionOrReleaseNumber().setValue(version);
		sft.getSoftwareProductName().setValue("OSCAR");
		sft.getSoftwareBinaryID().setValue(build);
	}

	/**
	 * @param nte
	 * @param subject should be a short string denoting what's in the comment data, i.e. "REASON_FOR_REFERRAL" or "ALLERGIES", max length is 250
	 * @param fileName should be the file name if applicable, can be null if it didn't come from a file.
	 * @param data and binary data, a String must pass in bytes too as it needs to be base64 encoded for \n and \r's
	 * @throws HL7Exception
	 * @throws UnsupportedEncodingException
	 */
	public static void fillNte(NTE nte, String subject, String fileName, byte[] data) throws HL7Exception, UnsupportedEncodingException
	{

		nte.getCommentType().getText().setValue(subject);
		if (fileName != null) nte.getCommentType().getNameOfCodingSystem().setValue(fileName);

		String stringData = encodeToBase64String(data);
		int dataLength = stringData.length();
		int chunks = dataLength / DataTypeUtils.NTE_COMMENT_MAX_SIZE;
		if (dataLength % DataTypeUtils.NTE_COMMENT_MAX_SIZE != 0) chunks++;
		logger.debug("Breaking Observation Data (" + dataLength + ") into chunks:" + chunks);

		for (int i = 0; i < chunks; i++)
		{
			FT commentPortion = nte.getComment(i);

			int startIndex = i * DataTypeUtils.NTE_COMMENT_MAX_SIZE;
			int endIndex = Math.min(dataLength, startIndex + DataTypeUtils.NTE_COMMENT_MAX_SIZE);

			commentPortion.setValue(stringData.substring(startIndex, endIndex));
		}
	}

	public static byte[] getNteCommentsAsSingleDecodedByteArray(NTE nte) throws UnsupportedEncodingException
	{
		FT[] fts = nte.getComment();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fts.length; i++)
		{
			sb.append(fts[i].getValue());
		}
		
		return(decodeBase64(sb.toString()));
	}

	/**
	 * @param pid
	 * @param pidNumber pass in the # of this pid for the sequence, i.e. normally it's 1 if you only have 1 pid entry. if this is a list of pids, then the first one is 1, second is 2 etc..
	 * @param healthNumberProvince use the 2 digit province code
	 * @throws HL7Exception
	 */
	public static void fillPid(PID pid, int pidNumber, String healthNumber, String healthCardVersionCode, String healthNumberProvince, Integer healthCardEffectiveYear, Integer healthCardEffectiveMonth, Integer healthCardEffectiveDay, Integer healthCardExpiryYear, Integer healthCardExpiryMonth, String lastName, String firstName, Integer birthYear, Integer birthMonth, Integer birthDay, Character gender) throws HL7Exception
	{
		// defined as first pid=1 second pid=2 etc
		pid.getSetIDPID().setValue(String.valueOf(pidNumber));

		CX cx = pid.getPatientIdentifierList(0);
		// health card string, excluding version code
		cx.getIDNumber().setValue(healthNumber);
		cx.getIdentifierTypeCode().setValue(HEALTH_NUMBER);
		// blank for everyone but ontario use version code
		if (healthCardVersionCode != null) cx.getIdentifierCheckDigit().setValue(healthCardVersionCode);
		// province
		cx.getAssigningJurisdiction().getIdentifier().setValue(healthNumberProvince);

		setDate(cx.getEffectiveDate(), healthCardEffectiveYear, healthCardEffectiveMonth, healthCardEffectiveDay);
		setDate(cx.getExpirationDate(), healthCardExpiryYear, healthCardExpiryMonth, null);

		XPN xpn = pid.getPatientName(0);
		xpn.getFamilyName().getSurname().setValue(lastName);
		xpn.getGivenName().setValue(firstName);
		// Value Description
		// -----------------
		// A Alias Name
		// B Name at Birth
		// C Adopted Name
		// D Display Name
		// I Licensing Name
		// L Legal Name
		// M Maiden Name
		// N Nickname /Call me Name/Street Name
		// P Name of Partner/Spouse - obsolete (DO NOT USE)
		// R Registered Name (animals only)
		// S Coded Pseudo-Name to ensure anonymity
		// T Indigenous/Tribal/Community Name
		// U Unspecified
		xpn.getNameTypeCode().setValue("L");

		setDate(pid.getDateTimeOfBirth() , birthYear, birthMonth, birthDay);

		// Value Description
		// -----------------
		// F Female
		// M Male
		// O Other
		// U Unknown
		// A Ambiguous
		// N Not applicable
		if (gender != null) pid.getAdministrativeSex().setValue(gender.toString());
	}

	/**
	 * @param pid
	 * @param pidNumber pass in the # of this pid for the sequence, i.e. normally it's 1 if you only have 1 pid entry. if this is a list of pids, then the first one is 1, second is 2 etc..
	 * @param healthNumberProvince use the 2 digit province code
	 * @throws HL7Exception
	 * @throws ParseException 
	 */
	public static void fillPidForA08(PID pid, HttpServletRequest request) throws HL7Exception, ParseException
	{
		// defined as first pid=1 second pid=2 etc
		pid.getSetIDPID().setValue(request.getParameter("demoNo"));

		CX cx = pid.getPatientIdentifierList(0);
		// health card string, excluding version code
		cx.getIDNumber().setValue(request.getParameter("hin"));
		cx.getIdentifierTypeCode().setValue(HEALTH_NUMBER);
		// blank for everyone but ontario use version code
		if (request.getParameter("ver") != null) cx.getIdentifierCheckDigit().setValue(request.getParameter("ver"));
		// province
		cx.getAssigningJurisdiction().getIdentifier().setValue(request.getParameter("province"));

		setDate(cx.getEffectiveDate(), Integer.parseInt(request.getParameter("effYear"))
				, Integer.parseInt(request.getParameter("effMonth")), Integer.parseInt(request.getParameter("effDay")));
		setDate(cx.getExpirationDate(), Integer.parseInt(request.getParameter("expiryYear")), Integer.parseInt(request.getParameter("expiryMonth")), null);

		XPN xpn = pid.getPatientName(0);
		xpn.getFamilyName().getSurname().setValue(request.getParameter("last_name"));
		xpn.getGivenName().setValue(request.getParameter("first_name"));
		// Value Description
		// -----------------
		// A Alias Name
		// B Name at Birth
		// C Adopted Name
		// D Display Name
		// I Licensing Name
		// L Legal Name
		// M Maiden Name
		// N Nickname /Call me Name/Street Name
		// P Name of Partner/Spouse - obsolete (DO NOT USE)
		// R Registered Name (animals only)
		// S Coded Pseudo-Name to ensure anonymity
		// T Indigenous/Tribal/Community Name
		// U Unspecified
		xpn.getNameTypeCode().setValue("L");
		
		Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("dob"));

		setDate(pid.getDateTimeOfBirth() , dob.getYear() + 1900, dob.getMonth() - 1, dob.getDate());

		// Value Description
		// -----------------
		// F Female
		// M Male
		// O Other
		// U Unknown
		// A Ambiguous
		// N Not applicable
		if (request.getParameter("sex") != null) pid.getAdministrativeSex().setValue(request.getParameter("sex"));
	}

	/**
	 * @param pid
	 * @param pidNumber pass in the # of this pid for the sequence, i.e. normally it's 1 if you only have 1 pid entry. if this is a list of pids, then the first one is 1, second is 2 etc..
	 * @param chartNumber as in the oscar demographic.chartNo
	 * @throws HL7Exception
	 */
	public static void fillPid(PID pid, int pidNumber, String chartNumber) throws HL7Exception
	{
		// defined as first pid=1 second pid=2 etc
		pid.getSetIDPID().setValue(String.valueOf(pidNumber));

		CX cx = pid.getPatientIdentifierList(0);
		cx.getIDNumber().setValue(chartNumber);
		cx.getIdentifierTypeCode().setValue(CHART_NUMBER);
	}

	private static void setDate(DTM date, Integer year, Integer month, Integer day) throws DataTypeException
	{
		StringBuilder sb = new StringBuilder();
		if (year != null) sb.append(year);
		else sb.append("0000");

		if (month != null)
		{
			if (month < 10) sb.append('0');

			sb.append(month);
		}
		else sb.append("00");

		if (day != null)
		{
			if (day < 10) sb.append('0');

			sb.append(day);
		}
		else sb.append("00");

		date.setValue(sb.toString());
	}

	private static void setDate(DT date, Integer year, Integer month, Integer day) throws DataTypeException
	{
		if (year != null && month != null && day != null) date.setYearMonthDayPrecision(year, month, day);
		else if (year != null && month != null) date.setYearMonthPrecision(year, month);
		else if (year != null) date.setYearPrecision(year);
	}
}
