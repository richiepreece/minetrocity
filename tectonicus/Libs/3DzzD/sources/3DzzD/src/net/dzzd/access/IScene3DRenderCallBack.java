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
 * Used for accessing to a Scene3DRenderCallBack.
 * <br>
 * Implements this interface to control events launched by a Scene3DRender.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IRender3D
 */
public interface IScene3DRenderCallBack
{
	/**
	 * Called once before all others events to inform this object that it must now handle callback.
	 * <p>
	 * This method will be called once each times this CallBack handler will be linked to a Render3D 
     * <br>
     *  This event may be used to load scene into rendere or initialise object.
	 * 
	 * @param r Render3D object source interface
	 * @since 1.0
	 */	
	public void render3DstartCallBack(IScene3DRender r);

	/**
	 * Called for each rendered frame when a new rendering process start.
	 * <p>
	 * This method will be called before all other events.
	 * 
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DStart(IScene3DRender r);

	/**
	 * Called for each rendered frame when all scene 3D Object are in world space.
	 * <p>
	 * This method will be called for each rendered frame when all 
     *  scene 3D Object are in world space : Mesh,Camera,Light, etc...
	 * 
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DWorldSpace(IScene3DRender r);
	
	/**
	 * Called for each rendered frame when all scene 3D Object are in camera space.
	 * <p>
	 * This method will be called for each rendered frame when all 
     *  scene 3D Object are in camera space : Mesh,Camera,Light, etc...
	 * 
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DCameraSpace(IScene3DRender r);	
	
	/**
	 * Called for each rendered frame just before drawing to internal pixels array.
	 * <p>
	 * This method will be called for each rendered frame just before
	 *  drawing scene object to internal pixels array.
	 *
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DPixelsUpdate(IScene3DRender r);	
	
	/**
	 * Called for each rendered frame just after internal pixels array has been updated with new frame.
	 * <p>
	 * This method will be called for each rendered frame just after
	 *  scene object have been drawn to internal pixels array.
	 *
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DPixelsUpdated(IScene3DRender r);	
	
	/**
	 * Called for each rendered frame when final image has been drawn to screen.
	 * <p>
	 * This method will be called for each rendered frame after all others events.
	 *
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DEnd(IScene3DRender r);		

	/**
	 * Called once when a request to switch Render3D implementation has been performed.
	 * <p>
	 * Render3D switching is asynchronous, this event will be launched once the request has been performed even if it has failed..
	 *
	 * @param r Render3D object source interface
	 * @since 1.0
	 */
	public void render3DSwitched(IScene3DRender r);		
}