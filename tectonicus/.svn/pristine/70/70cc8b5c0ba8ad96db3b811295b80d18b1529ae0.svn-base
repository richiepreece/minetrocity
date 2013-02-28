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
 *  Used for accessing to a Track3D.
 *  <br>
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IPoint3D
 *	@see DzzD
 */
 
public interface ITrack3D extends IPoint3D
{
	/**
	 * Gets the starting time of this Track3D.
	 *
	 * @return start time in ms or -1 if not started
	 */
	 public long getStartTime();
	
	/**
	 * Gets the pause time for this Track3D.
	 *
	 * @return pause time in ms or -1 if not paused
	 */
	 public long getPauseTime();
	
	/**
	 * Sets the default interpolation mode for this Track3D
	 *
	 * @param i default interpolation between new keys.
	 */
	 public void setDefaultInterpolation(int i);
	 
	/**
	 * Sets timer to use with for this Track3D.
	 *
	 * @param t new timer to use
	 */
	 public void setTimer(ITimer t);
	
	/**
	 * Sets the loop time for this Track3D.
	 *
	 * @param loopTime loop time in ms or -1 ne disable loop.
	 */
	 public void setLoop(long loopTime);
	
	/**
	 * Pause this Track3D to its current Timer time.
	 */
	 public void pause();
	
	/** 
	 * Resume this Track3D id previously paused (startTime is updated)
	 */
	 public void resume();

	/**
	 * Create a a new static key and add it to this Track3D.
	 * 
	 * @param x x value for this new key	
	 * @param y z value for this new key	
	 * @param z z value for this new key	
	 * @param time time ofset for this new key	
	 * 
	 * @return index of the newly created key
	 */
	 public int addKey(double x,double y,double z,long time);
	
	/**
	 * Create a a new static key and add it to this Track3D.
	 * 
	 * @param x x value for this new key	
	 * @param y z value for this new key	
	 * @param z z value for this new key	
	 * @param time time ofset for this new key
	 * @param interpolation type of interpolation 0=none,1=linear,2=cosin,3=bicubic		
	 * 
	 * @return index of the newly created key
	 */
	 public int addKey(double x,double y,double z,long time,int interpolation);
	
	/**
	 * Create a a new linked key and add it to this Track3D.
	 *	
	 * @param source source for this new key	
	 * @param time time ofset for this new key	
	 * 
	 * @return index of the newly created key
	 */
	 public int addKey(IPoint3D source,long time);

	/**
	 * Create a a new linked key and add it to this Track3D.
	 * 
	 * @param source source for this linked key	
	 * @param time time offset for this new key
	 * @param interpolation type of interpolation 0=none,1=linear,2=cosin,3=bicubic		
	 * 
	 * @return index of the newly created key
	 */	
	 public int addKey(IPoint3D source,long time,int interpolation);
	
	/**
	 * Remove a key from this Track3D.
	 *
	 * @param n index of the key to remove from this animation
	 */	
	 public void removeKey(int n);
	
	/**
	 * Start or restart this Track3D.
	 */
	 public void start();
	 
	/**
	 * Start or restart this Track3D with an offset.
	 *
	 * @param startTime offset time
	 *
	 */ 
	 public void start(long startTime);	 
	
	/**
	 * Play this Track3D using current time for its Timer.
     *
	 * Compute and update the internal(x,y,z) value using the time given by the ITimer (internal or external).
	 */ 
	 public void play();

	/**
	 * Stop this Track3D.
	 */ 
	 public void stop();
	
	/**
	 * Play this Track3D using the given time .
	 *
	 * @param time time to compute x,y,z in ms
	 *
	 */ 
	 public void playAt(long time);

	/**
	 * Get number of keys .
	 *
	 * @return number of keys
	 *
	 */ 	 
	public int getNbKey();

}