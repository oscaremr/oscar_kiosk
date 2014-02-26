package org.oscarehr.oscar_apps.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This class is used to log user actions that happen in the system for auditing purposes.
 * This is not a code / debug logger. This is meant to log things that are generally
 * user prompted interactions. As an example with the patient check in app, we would
 * user this to log "health card scanned" and "client checked in" or
 * "client not checked in".
 */
public final class ActionLog
{
	private static Logger actionLogger=LogManager.getLogger("OSCAR_APPS_ACTION_LOG");
	
	public static void logAction(String msg)
	{
		actionLogger.info(msg);
	}
}
