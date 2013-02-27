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
 *  Interface to a ProgressListener instance.
 *  <br>
 *  ProgressListener is used to monitor progress while executing certain actions for an object.
 *  The actions & progress are abstract and can include various actions.
 *  Some examples of actions we could monitor progress for include:
 *    -Downloading of a file
 *    -Decompressing of a filede
 *    -Compiling an object.
 *  <br>
 *
 *  @author Bruno Augier, Matthijs Blaas
 *  @version 1.0, 25/10/2007
 *  @since 1.0
 *	@see DzzD
 */

public interface IProgressListener 
{
	public static final int ACTION_UNKNOWN    		= -1;
	public static final int ACTION_FILE_DOWNLOAD    = 0;
	public static final int ACTION_FILE_UPLOAD      = 1;
	public static final int ACTION_FILE_DECOMPRESS  = 2;
	public static final int ACTION_FILE_COMPRESS    = 3;
	public static final int ACTION_FILE_COMPILE     = 4;
	
	public static final int ACTION_MESH_BUILD  = 10;
	public static final int ACTION_MESH_OCTREE_BUILD  = 10;
	
	/**
	 * Sets the resource name for the current object.
	 *
	 * @param name The name of the resource
	 */
	public void setName(String name);

	/**
	 * Gets the resource name for the current object.
	 *
	 * @return The name of the resource
	 */
	public String getName();

	/**
	 * Sets the action for the current resource (downloading, (de)compressing etc)
	 *
	 * @param action The name of the action
	 */
	public void setAction(int action);

	/**
	 * Gets the current action for this resource (downloading, (de)compressing etc)
	 *
	 * @return The name of the current action in progress
	 */
	public int getAction();

	/**
	 * Sets the unit identifier in which we measure progress (for example %, KB, seconds etc)
	 *
	 * @param unit The string representation for unit measurement
	 */
	public void setUnit(String unit);

	/**
	 * Gets the measure unit identifier
	 *
	 * @return The string representation for the unit measure identifier
	 */
	public String getUnit();
	
	/**
	 * Sets maximum progress values.
	 * <br>
	 * default value=100;
	 * <br>
	 * Note: Progress in percent can be computed: <BR>
	 * progressPercent=(100*getProgress())/getMaximumProgress()
	 *
	 * @param size maximum value that progress can reach
	 */
	public void setMaximumProgress(int size);
	
	/**
	 * Gets the maximum progress value.
	 * <br>
	 *
	 * @return maximum value that progress can reach
	 */
	public int getMaximumProgress();	
		
	/**
	 * Sets progress of the current object.
	 *
	 * @param progress Progress of the current object action
	 */
	public void setProgress(int progress);

	/**
	 * Gets progress of the current object.
	 *
	 * @return progress The progress of the current object action 0% to 100%
	 */
	public int getProgress();
	
	/**
	 * Returns whether the current object action is started
	 *
	 * @return true if object has started it's current action
	 */
	public boolean getStarted();
		
	/**
	 * Sets the started flag for the current object action
	 *
	 * @param flag set to true to indicate that object has started is current action
	 */	
	public void setStarted(boolean flag);	
	
	
	/**
	 * Gets the finished flag for the current action the object is executing
	 *
	 * @return true if object has finished it's current action
	 */
	public boolean getFinished();
		
	/**
	 * Sets the finished flag for the current object action
	 *
	 * @param flag set to true to indicates the object has finished it's current action
	 */	
	public void setFinished(boolean flag);	
	
	/**
	 * Checks whether the error flag was set during the current action
	 *
	 * @return true indicates if something went wrong while executing the objects action
	 */
	public boolean getError();
	
	/**
	 * Sets the error flag for the current action
	 *
	 * @param flag set to true to indicate an error occured during the current action
	 */	
	public void setError(boolean flag);	
	
	/**
	 * Resets the progresslisteners state
	 */
	public void reset();
	
	/**
	 * Copy source ProgressListener in this ProgressListener.
	 * <br>
	 * 
	 * @param source The source progresslistener to copy into this progresslistener
	 */				
	public void copy(IProgressListener source);	
	
}