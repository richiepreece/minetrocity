/*
* This file is part of 3DzzD http://dzzd.net/.
*
* Released under LGPL
*
* 3DzzD is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* 3DzzD is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with 3DzzD.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2005 - 2009 Bruno Augier
*/

/*
 * Purpose:
 * Provides a simple logging facility, so we can easily collect debug info and stream it to whatever channel we like
 * 
 * Author: Matthijs Blaas
 *
 * TODO: add logging message state's, so we can also refine our logging to certain messages (fx info messages or exception messages)
 *       or maybe have this implemented using priorities (1 = message, 2 = hint, 3 = warning, 4 = error etc) and just set what type of messages must be logged for this session
 *
 */
package net.dzzd.utils;

import java.io.PrintStream;
import java.util.Date;

public class Log
{
	private static PrintStream logout = System.out;
	private static boolean debug = false, enabled = true, showAll = true;
	private static String[] debugClasses = null;
	private static int debugClassIndex = 0;

	
	/**
	 * Set the output stream to write to
	 * 
	 * @param out The output stream we'll write to
	 */
	public static void setOutputStream(PrintStream out) {
		logout = out;
	}


	/**
	 * Set the output stream to write to console
	 * 
	 * @param val Wether we should print info to the console
	 */
	public static void setPrintDebug(boolean val) {
		debug = val;
	}


	/**
	 * Set whether all debug info should be printed, or just the classes that are monitored
	 * 
	 * @param val Whether we should print all debug info
	 */
	public static void showAll(boolean val) {
		showAll = val;
	}


	/**
	 * Adds a class to the debug list. Specific classes can be tagged to monitor for debug information.
	 * 
	 * @param cl The class we want to monitor
	 */
	public static void addDebugClass(Class cl)
	{
		if(debugClasses == null) {
			debugClasses = new String[100];
		}

		if(isMonitored(cl))
		{
			if(debug) logout.println("Class "+cl.toString()+" is already tagged for debug info");
			return;
		}
		
		// Check if we still have enough room to store another debug class
		if(debugClassIndex > debugClasses.length)
		{
			String[] tmp = new String[debugClasses.length];
			System.arraycopy(debugClasses, 0, tmp, 0, debugClasses.length);
			debugClasses = new String[debugClasses.length+100];
			System.arraycopy(tmp, 0, debugClasses, 0, tmp.length);
		}
		
		// add the class to the debug list
		debugClasses[debugClassIndex++] = cl.toString();
	}


	/**
	 * Enable or disable writing to the outputstream
	 * 
	 * @param val Wether we should write to the printstream or not
	 */
	public static void enable(boolean val)
	{
		enabled = val;
	}
	
	
	public synchronized static void log(String m)
	{
		logout.println(m);
	}
	
	public synchronized static void log(Throwable t)
	{
		t.printStackTrace(logout);
	}	
	

	/**
	 * Log the a throwable exception for a given object reference
	 * 
	 * @param caller The object that tries to log this throwable
	 * @param e The throwable to be logged
	 */
	private synchronized static void log(Object caller, Throwable e)
	{
		log(caller.getClass(), e);
	}


	/**
	 * Log the specified message given an object reference
	 * 
	 * @param caller The object that tries to log this message
	 * @param message The message to be logged
	 */
	public synchronized static void log(Object caller, String message)
	{
		log(caller.getClass(), message);
	}


	/**
	 * Log an exception and its associated stack trace
	 * 
	 * @param e The exception to log
	 * @param caller The calling class we should check wether being monitored
	 */
	private synchronized static void log(Class caller, Throwable e)
	{
		if(caller == null)
		{
			System.err.println("Cannot log event, calling class missing: ");
			e.printStackTrace();
		}
		else
		{
			if ((showAll || isMonitored(caller)))
			{
				if(debug)
				{
					System.err.println(new Date(System.currentTimeMillis()) + " - " + caller.getPackage().getName() + "." + caller.getName() + ": ");
					e.printStackTrace();
				}
				
				if (logout != null && enabled)
				{
					e.printStackTrace(logout);
				}
			}
		}
	}

	/**
	 * Log an exception and its associated stack trace
	 * 
	 * @param e The exception to log
	 * @param caller The calling class we should check wether being monitored
	 */
	public synchronized static void log(Class caller, Exception e)
	{
		if(caller == null)
		{
			System.err.println("Cannot log event, calling class missing: ");
			e.printStackTrace();
		}
		else
		{
			if ((showAll || isMonitored(caller)))
			{
				if(debug)
				{
					System.err.println(new Date(System.currentTimeMillis()) + " - " + caller.getPackage().getName() + "." + caller.getName() + ": ");
					e.printStackTrace();
				}
				
				if (logout != null && enabled)
				{
					e.printStackTrace(logout);
				}
			}
		}
	}
	
	
	/**
	 * Log the specified message
	 * 
	 * @param message The message to be logged
	 * @param caller The calling class we should check wether being monitored
	 */
	private synchronized static void log(Class caller, String message)
	{
		if(caller == null)
		{
			System.err.println("Cannot log message, calling class missing: ");
		}
		else
		{
			if(showAll || isMonitored(caller))
			{
				if(debug)
				{
					System.err.println(new Date(System.currentTimeMillis()) + " - " + caller.getPackage().getName() + "." + caller.getName() + ": ");
					System.err.println(message);
				}
				
				if (logout != null && enabled)
				{
					logout.println(message);
				}
			}
		}
	}
	
	
	/**
	 * Check whether a given class is monitored for logging
	 * 
	 * @param cl The Class to check if being monitored
	 */	
	private static boolean isMonitored(Class cl)
	{
		if(debugClasses == null)
			return false;
		
		for(int i=0;i<debugClasses.length;i++)
		{
			if(debugClasses[i]==cl.toString())
			{
				return true;
			}
		}
		
		return false;
	}

	

	
	public synchronized static void log(Exception e)
	{
		e.printStackTrace(logout);
	}
}