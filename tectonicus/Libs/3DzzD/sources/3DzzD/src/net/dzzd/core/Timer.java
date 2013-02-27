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

package net.dzzd.core;

import net.dzzd.access.*;

import com.arcazoid.util.hirestimer.*;

/** 
 *  Generic class for a timer.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 */

public class Timer implements ITimer
{
	Object at;
	
	
	public Timer()
	{
		try
		{
		
			this.at=Class.forName("com.arcazoid.util.hirestimer.AdvancedTimer").newInstance();
			((com.arcazoid.util.hirestimer.AdvancedTimer)this.at).start();
		}
		catch (ClassNotFoundException e)
		{
		}
		catch (InstantiationException e)
		{
		}
		catch (IllegalAccessException e)
		{
		}

		
		
	} 
	
	/**
	 * Get the current time for this timer expressed in millisecond.
	 * 
	 * @return the current time for this timer expressed in millisecond.
	 * @since 1.0
	 */		
	public long getTime()
	{
		if(this.at!=null)
			return ((com.arcazoid.util.hirestimer.AdvancedTimer)this.at).getTimeElapsed();
			
		return System.currentTimeMillis();
	}
}