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
 *  Used for accessing to a Render3D.
 *
 *  @version 1.0
 *  @author Bruno Augier
 */
public interface IRender3D extends IRender2D,IRender3DMode
{
	
	/**
	 * Sets this render3D current camera3D view point.
	 *
	 * @param camera camera3D to use to render scene
	 */	
	public void setCamera3D(ICamera3D camera);
	
	/**
	 * Sets fog color.
	 *
	 * @param colorFog fog color
	 */		
	public void setFogColor(int colorFog);	
	
	/**
	 * Sets fog start distance
	 *
	 * @param startZFog z distance start of fog
	 */		
	public void setFogStart(double startZFog);	
	
	/**
	 * Sets fog end distance
	 *
	 * @param endZFog z distance start of fog
	 */		
	public void setFogEnd(double endZFog);		
		
	/**
	 * Render the given scene3D.
	 *
	 * @param scene scene3D to render.
	 */			
	public void renderScene3D(IScene3D scene);
	
	/**
	 * Gets the RenderMode interface for this Render3D.
	 *
	 * RenderMode is used to enable/disable rendering features : lighting,texture,mipmapping.
	 * 
	 * @return RenderMode interface to control this Render3D rendering.
	 */				
	public IRender3DMode getRender3DMode();
	
		
	/** Gets object ID rendered at the specified screen location 
	 *  <br>
	 *  NOT IMPLEMENTED IN HARDWARE RENDERER
	 *  @param x 
	 *  @param y
	 *  @return Mesh3D ID at x,y	 
 	 */			
	public int getRenderedMesh3DIdAt(int x,int y);
	
	/** Gets face ID rendered at the specified screen location 
	 *  <br>
	 *  NOT IMPLEMENTED IN HARDWARE RENDERER
	 *  @param x 
	 *  @param y
	 *  @return Face3D ID at x,y	 
 	 */			
	public int getRenderedFace3DIdAt(int x,int y);
	
	/** Gets z value at the specified screen location 
	 *  <br>
	 *  NOT IMPLEMENTED IN HARDWARE RENDERER
	 *  @param x 
	 *  @param y
	 *  @return z	 
 	 */				
	public double getZAt(int x,int y);		
	
}
