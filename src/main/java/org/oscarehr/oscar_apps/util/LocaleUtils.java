/*
 * Copyright (c) 2010. Department of Family Medicine, McMaster University. All Rights Reserved.
 * 
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. 
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.oscar_apps.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

/**
 * This class helps retrieve localised versions of strings. The BASE_NAME specifies a resource bundles base name.
 * If you create a string table file of <BASE_NAME>.properties it will use it as the default value. This means if 
 * as an example you have that as the english version and a <BASE_NAME>_fr.properties for a french version, if a
 * specific string is missing it will just use the english version, this is how resource bundles work, it will not
 * give you any warnings or errors. In general this is a bad idea because you'll never know something is missing
 * unless a user complains. Instead do NOT create a <BASE_NAME>.properties at all. Create specifically
 * <BASE_NAME>_en.properties and <BASE_NAME>_fr.properties. Then make sure the DEFAULT_LOCAL variale is set to 
 * engish. This will have the same behaviour for the end user, i.e. if a string is missing in french they 
 * will have the english version displayed. It will however provide an error in the log files so as a developer
 * you are aware that a translation string is missing.
 */
public final class LocaleUtils
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final String BASE_NAME = "oscar_app_strings";

	public static String getMessage(ServletRequest request, String key)
	{
		return(getMessage(request.getLocale(), key));
	}

	public static String getMessage(String isoLanguageCode, String key)
	{
		return(getMessage(new Locale(isoLanguageCode), key));
	}

	public static String getMessage(Locale locale, String key)
	{
		// try the requested locale
		try
		{
			String result=ResourceBundle.getBundle(BASE_NAME, locale).getString(key);
			return(result);
		}
		catch (MissingResourceException e)
		{
			// if not found, use the default locale, and log and error
			String message = "Resource not found. BASE_NAME=" + BASE_NAME + ", Locale=" + locale + ", key=" + key;
			logger.error(message);
			logger.debug(message, e);
			try
			{
				String result=ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE).getString(key);
				return(result);
			}
			catch (MissingResourceException e1)
			{
				message = "Resource not found. BASE_NAME=" + BASE_NAME + ", DEFAULT_LOCALE=" + DEFAULT_LOCALE + ", key=" + key;
				logger.error(message);
				logger.debug(message, e);
				return(key);
			}
		}
	}
}