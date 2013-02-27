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

package net.dzzd.access;

/** 
 *  Used for accessing to a Timer.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 */

public interface ITimer
{
	/**
	 * Gets the current time for this timer expressed in millisecond.
	 * 
	 * @return the current time for this timer expressed in millisecond.
	 */		
	public long getTime();
}