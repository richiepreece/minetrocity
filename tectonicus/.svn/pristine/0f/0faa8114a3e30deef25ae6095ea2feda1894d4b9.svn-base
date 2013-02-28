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
import java.awt.event.*;

/** 
 *  Used for accessing keyboard and mouse at anytime.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IRender3D
 */
public interface IDirectInput 
{   
	/**
	 * Gets the mouse x position relative to this DirectInput component.
	 * 
	 * @return mouse pos x
	 */
	 public int getMouseX();
	 
	/**
	 * Gets the mouse y position relative to this DirectInput component.
	 * 
	 * @return mouse pos y
	 */
	 public int getMouseY();
	 
	/**
	 * Gets the mouse first button state.
	 * 
	 * @return true if first mouse button is pressed
	 */
	 public boolean isMouseB1();	 
	 
	/**
	 * Gets the mouse second button state.
	 * 
	 * @return true if seconde mouse button is pressed
	 */
	 public boolean isMouseB2();
	 
	/**
	 * Gets the mouse third button state.
	 * 
	 * @return true if third mouse button is pressed
	 */
	 public boolean isMouseB3();

	/**
	 * Gets the mouse first button click state within the given time range.
	 *
	 * @param time time range in ms 
	 *
	 * @return true if first mouse button was clicked in the given ms time range
	 */
	 public boolean isMouseB1Click(long time);	 
	 
	/**
	 * Gets the mouse second button click state within the given time range.
	 *
	 * @param time time range in ms 
	 *
	 * @return true if seconde mouse button was clicked in the given ms time range
	 */
	 public boolean isMouseB2Click(long time);
	 
	/**
	 * Gets the mouse third button click state within the given time range.
	 *
	 * @param time time range in ms 
	 *
	 * @return true if third mouse button was clicked in the given ms time range
	 */
	 public boolean isMouseB3Click(long time);

	 
	/**
	 * Gets the mouse dragging state.
	 * 
	 * @return true if user is dragging
	 */	
	 public boolean isMouseDrag();
	 
	/**
	 * Gets a key state
	 * 
	 * @param num keycode of the key to return state.
	 * @return true if key n is pressed
	 */	
	 public boolean isKey(int num);
	 
	/**
	 * Gets a key state within a specified ms range
	 * 
	 * @param num keycode of the key to return state.
	 * @param maxTime time range in ms.
	 * @return true if key n was pressed in the last maxTime range
	 */		 
	 public boolean isKey(int num,long maxTime);
	 
	/**
	 * Gets the mouse x position relative to this DirectInput component.
	 * 
	 * @return mouse pos x
	 */
	 public int getMouseDragX();
	 
	/**
	 * Gets the mouse y position relative to this DirectInput component.
	 * 
	 * @return mouse pos y
	 */
	 public int getMouseDragY();
}
