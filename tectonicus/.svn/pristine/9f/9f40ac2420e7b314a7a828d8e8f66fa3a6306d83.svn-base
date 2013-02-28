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
 *  Used for accessing rendering mode.
 *  <br>
 *  Use this interface to enable/disable rendering features : light,material,texture etc....<br>
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IRender3D
 *	@see IMesh3D
 *	@see DzzD
 */
public interface IRender3DMode
{
	/**
 	 * Enable rendering features.
	 * <br>
	 * to enable lighting and texture do : enableRender3DMode(DzzD.LIGHT|DzzD.TEXTURE) 
	 * 
	 * @param flag a bit mask representation of features to enable
	 */
	public void enableRender3DMode(int flag);

	/**
 	 * Disable rendering features.
	 * <br>
	 * to disable mipmapping do : disableRender3DMode(DzzD.MIPMAP) 
	 *
	 * @param flag a bit mask representation of features to disable
	 */
	public void disableRender3DMode(int flag);

	/**
 	 * Sets rendering mode.
	 * <br>
	 * to enable lighting and disable all other features do : setRenderMode(DzzD.LIGHT) 
	 *
	 * @param flag a bit mask representation of features enabled
	 */
	public void setRender3DModeFlags(int flag);
	
	/**
	 * Gets rendering mode.
	 *
	 * @return flag a bit mask representation of features enabled
	 */
	 public int getRender3DModeFlags();
	 
	/**
	 * Gets the RenderMode interface for this Mesh3D.
	 * <br>
	 * RenderMode is used to enable/disable rendering features : lighting,texture,mipmapping.
	 * 
	 * @return RenderMode interface to control this Mesh3D rendering.
	 */	
	public IRender3DMode getRender3DMode();	 



}