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
 *  Used for accessing a Scene3DObjectAnimator.
 *  <br>
 *  Scene3DObjectAnimator is intended to animate a Scene3DObject.<br>
 *  <br>
 *  Scene3DObjectAnimator enable time-based animation on Scene3DObject<br>
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IPoint3D
 *	@see IScene3DObject
 *	@see DzzD
 */
public interface IScene3DObjectAnimator 
{
	/**
	 * Add position key.
	 * <br>
	 * @param keyTime time of the new key
	 * @param position 3D position for the new key
	 */
	public int addKeyPosition(int keyTime,IPoint3D position);

	/**
	 * Add rotation key.
	 * <br>
	 * @param keyTime time of the new key
	 * @param rotation 3D rotation for the new key (performed in order rx,rz,ry)
	 * @param axis 3D axis used to reach rotation
	 * @param angle angle to turn around the given axis given in radian
	 */
	public int addKeyRotation(int keyTime,IPoint3D rotation,IPoint3D axis,double angle);

	/**
	 * Play rotation track using the given time.
	 * <br>
	 * @param time time to play rotation track
	 */
	public void playRotationAt(int time);

	/**
	 * Play position track using the given time.
	 * <br>
	 * @param time time to play position track
	 */
	public void playPositionAt(int time);

	/**
	 * Play position and rotation track using the given time.
	 * <br>
	 * @param time time to play position and rotation track
	 */
	public void playAt(int time);

	/**
	 * Gets current Timer for this this Scene3DObjectAnimator.
	 * <br>
	 * @return current timer
	 */
	public ITimer getTimer();

	/**
	 * Sets Timer to use for this Scene3DObjectAnimator.
	 * <br>
	 * @return new Timer
	 */
	public void setTimer(ITimer t);

	/**
	 * Gets start time for this Scene3DObjectAnimator.
	 * <br>
	 * @return start time in ms or -1 if not started
	 */
	public long getStartTime();

	/**
	 * Gets pause time for this Scene3DObjectAnimator.
	 * <br>
	 * @return pause time in ms or -1 if not paused
	 */
	public long getPauseTime();

	/**
	 * Sets loop time for this Scene3DObjectAnimator.
	 * <br>
	 * @return loop time in ms or -1 if loop not enabled
	 */
	public void loopAt(long loopTime);

	/**
	 * Pause this Scene3DObjectAnimator.
	 */
	public void pause();

	/**
	 * Resume this Scene3DObjectAnimator at the last pause time (start time may be updated).
	 */
	public void resume();

	/**
	 * Start this Scene3DObjectAnimator .
	 */
	public void start();

	/**
	 * Start this Scene3DObjectAnimator starting at the given time.
	 *
	 * @param start starting time to begin play in ms
	 */
	public void start(long start);

	/**
	 * Start this Scene3DObjectAnimator and specify a time range to use.
	 *
	 * @param start starting time to begin play in ms
	 * @param end maximum time to play in ms
	 */
	public void start(long start,long end);

	/**
	 * Play position and rotation track using its Timer.
	 */
	public void play();

	/**
	 * Stop this Scene3DObjectAnimator.
	 */
	public void stop();
	
	/**
	 * Gets a clone of this Scene3DObjectAnimator.
	 *
	 *@return clone of this Scene3DObjectAnimator
	 */
	public IScene3DObjectAnimator getClone();	
	
	/**
	 * Copy keys and tracks infos from a source Scene3DObjectAnimator.
	 *
	 * @param source source Scene3DObjectAnimator
	 */
	public void copy(IScene3DObjectAnimator source);		
	
	/**
	 * Gets this Scene3DObjectAnimator current position (relative tolast play).
	 *
	 *@return position for last play
	 */	
	public IPoint3D getPosition();
	
	/**
	 * Gets this Scene3DObjectAnimator current rotation (relative tolast play).
	 *
	 *@return rotation for last play
	 */		
	public IPoint3D getRotation();
}