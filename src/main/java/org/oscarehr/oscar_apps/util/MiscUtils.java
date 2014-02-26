/*
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
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
 * This software was written for 
 * MB Software, margaritabowl.com
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.oscar_apps.util;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class MiscUtils
{
	public static final String ENCODING = "UTF-8";

	/**
	 * This method should only really be called once per context in the context startup listener.
	 */
	protected static void addLoggingOverrideConfiguration(String contextPath)
	{
		String configLocation = System.getProperty("log4j.override.configuration");
		if (configLocation != null)
		{
			if (contextPath != null)
			{
				if (contextPath.length() > 0 && contextPath.charAt(0) == '/') contextPath = contextPath.substring(1);
				if (contextPath.length() > 0 && contextPath.charAt(contextPath.length() - 1) == '/')
					contextPath = contextPath.substring(0, contextPath.length() - 2);
			}

			String resolvedLocation = configLocation.replace("${contextName}", contextPath);
			getLogger().info("loading additional override logging configuration from : "+resolvedLocation);
			DOMConfigurator.configureAndWatch(resolvedLocation);
		}
	}

	/**
	 * This method will return a logger instance which has the name based on the class that's calling this method.
	 */
	public static Logger getLogger()
	{
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		String caller = ste[2].getClassName();
		return(Logger.getLogger(caller));
	}
	
	public static String getBuildDateTime()
	{
		return(ConfigUtils.getProperty(MiscUtils.class, "buildDateTime"));
	}
}
