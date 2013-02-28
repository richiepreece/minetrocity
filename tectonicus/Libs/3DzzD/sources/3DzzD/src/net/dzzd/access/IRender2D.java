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
import java.awt.*;

/** 
 *  Used for accessing to a Render2D.
 *
 *  @version 1.0
 *  @author Bruno Augier
 */
public interface IRender2D extends IRender,IRender2DMode
{
	/**
	 * Gets this Render2D canvas.
	 *
	 * @return this Render2D canvas
	 */
	public Canvas getCanvas();
	
	/**
	 * Sets this Render2D size and its max antialias level.
	 *
	 * @param viewPixelWidth width expressed in pixels
	 * @param viewPixelHeight height expressed in pixels
	 * @param maxAntialias maximum antialias : (0-7)
	 */	
	public void setSize(int viewPixelWidth,int viewPixelHeight,int maxAntialias);

	/**
	 * Sets this Render2D size .
	 *
	 * @param viewPixelWidth width expressed in pixels
	 * @param viewPixelHeight height expressed in pixels
	 */		
	public void setSize(int viewPixelWidth,int viewPixelHeight);
	
	/**
	 * Sets antialias level.
	 *
	 * @param level antialias level : (0-7)
	 */		
	public void setAntialiasLevel(int level);
	
	/**
	 * Gets this Render2D width 
	 *
	 * @return width expressed in pixel
	 */
	public int getWidth();
	
	/**
	 * Gets this Render2D height 
	 *
	 * @return height expressed in pixel
	 */	
	public int getHeight();
	
	/**
	 * Sets this Render2D cursor 
	 *
	 * @param cursor cursor object ot use as cursor when mouse over this Render2D canvas expressed in pixel
	 */		
	public void setCursor(Cursor cursor);
	
	/**
	 * Render the given Scene2D.
	 *
	 * @param scene Scene2D to render.
	 */			
	public void renderScene2D(IScene2D scene);
	
	/**
	 * Gets the RenderMode interface for this Render2D.
	 *
	 * RenderMode is used to enable/disable rendering features : lighting,texture,mipmapping.
	 * 
	 * @return RenderMode interface to control this Render2D rendering.
	 */				
	public IRender2DMode getRender2DMode();
	
	/**
	 * Gets this Render2D implementation name.
	 *
	 * @return name of the implementation used for this Render2D.
	 */					
	public String getImplementationName();
	
	/**
	 * Gets this Render2D directInput.
	 *
	 * @return a directInput interface to read mouse and keyboard informations at anytime.
	 */						
	public IDirectInput getDirectInput();
	
	/** Gets the screenUpdateEnabled flag .
	 *  <br>
	 *  set this flag to false to disable onscreen rendering.<br>
	 *
	 *  @return screenUpdateEnabled flag 	 
 	 */	
	public boolean isScreenUpdateEnabled();

	/** Sets the screenUpdateEnabled flag .
	 *  <br>
	 *  set this flag to false to disable onscreen rendering.<br>
	 *
	 *  @param flag screenUpdateEnabled flag 	 
 	 */		
	public void setScreenUpdateEnabled(boolean flag);
	
	/** Gets the pixelUpdateEnabled flag .
	 *  <br>
	 *  set this flag to false to disable pixels rendering.<br>
	 *
	 *  @return pixelUpdateEnabled flag 	 
 	 */	
	public boolean isPixelUpdateEnabled();

	/** Sets the pixelUpdateEnabled flag .
	 *  <br>
	 *  set this flag to false to disable pixels rendering.<br>
	 *
	 *  @param flag pixelUpdateEnabled flag 	 
 	 */		
	public void setPixelUpdateEnabled(boolean flag);		
	
}
