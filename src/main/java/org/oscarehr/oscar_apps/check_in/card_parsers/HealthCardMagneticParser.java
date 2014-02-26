package org.oscarehr.oscar_apps.check_in.card_parsers;

/**
 * The idea here is that specific parsers will subclass this class and provide the setMagneticCardData() method.
 * That method should then populate all the protected variables. After that all the getter methods should work as expected.
 * Instances of this class should not be reused with different card data. The setMagneticCardData method should disallow it
 * (The reason being it's hard to gurantee the parseData method will reinitialise all variables every time). 
 */
public abstract class HealthCardMagneticParser extends MagneticParser
{
	protected String cardIssuer;
	protected String healthNumber;
	protected String lastName;
	protected String firstName;
	protected Integer cardExpiryYear;
	protected Integer cardExpiryMonth;
	protected Character gender;
	protected Integer birthYear;
	protected Integer birthMonth;
	protected Integer birthDay;
	protected String cardVersion;
	protected Integer cardIssueYear;
	protected Integer cardIssueMonth;
	protected Integer cardIssueDay;
	
	/**
	 * This method should return a 2 character province/state code in upper case.
	 */
	public abstract String getProvince();	
	
	/** ISO assigned issuer */ 
	public String getCardIssuer()
	{
		return(cardIssuer);
	}
	
	/** plain health number, no version code etc */
	public String getHealthNumber()
	{
		return(healthNumber);
	}
	
	/** This maybe truncated if it's too long */
	public String getLastName()
	{
		return(lastName);
	}
	
	/** On some cards this is just the initial or First name initial followed by space followed by middle name initial */
	public String getFirstName()
	{
		return(firstName);
	}
	
	/** This may return null if not available, do not return 0, this is a 4 digit number not 2 */
	public Integer getCardExpiryYear()
	{
		return(cardExpiryYear);
	}
	
	/** This may return null if not available, do not return 0, Jan=1 ... Dec=12 */
	public Integer getCardExpiryMonth()
	{
		return(cardExpiryMonth);
	}

	/** M=Male, F=Female, null=not available */
	public Character getGender()
	{
		return(gender);
	}
		
	/** This may return null if not available, this is a 4 digit number not 2 */
	public Integer getBirthYear()
	{
		return(birthYear);
	}

	/** This may return null if not available, do not return 0, Jan=1 ... Dec=12 */
	public Integer getBirthMonth()
	{
		return(birthMonth);
	}
	
	/** This may return null if not available, do not return 0, This is the day of the month. */
	public Integer getBirthDay()
	{
		return(birthDay);
	}

	/** This may return null if not available */
	public String getCardVersion()
	{
		return(cardVersion);
	}
	
	/** This may return null if not available, do not return 0, this is a 4 digit number not 2 */
	public Integer getCardIssueYear()
	{
		return(cardIssueYear);
	}
	
	/** This may return null if not available, do not return 0, Jan=1 ... Dec=12 */
	public Integer getCardIssueMonth()
	{
		return(cardIssueMonth);
	}
	
	/** This may return null if not available, do not return 0, this is the day of the month */
	public Integer getCardIssueDay()
	{
		return(cardIssueDay);
	}
}
