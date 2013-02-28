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
 *  Used for accessing to a Camera3D.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IScene3DObject
 *	@see IRender3D
 */
public interface ICamera3D extends IScene3DObject
{
	/**
	 * Gets the camera field of view in degree.
	 *
	 * @return camera horizontal field of view angle in degree (0-180)
	 */		
	public double getFOV();

	/**
	 * Sets the camera field of view in degree.
	 * <p>
	 * this method will modify the focal length according to the camera screen width
	 *
	 * @param val camera horizontal field of view angle in degree (0-180)
	 */			
	public void setFOV(double val);
	
	/**
	 * Gets the camera focal length in scene unit.
	 *
	 * @return camera focal length in scene unit.
	 */			
	public double getFocus();

	/**
	 * Gets the camera screen width expressed in scene unit.
	 *
	 * @return the camera screen width expressed in scene unit.
	 */				
	public double getWidth();

	/**
	 * Gets the camera screen height expressed in scene unit.
	 *
	 * @return the camera screen height expressed in scene unit.
	 */				
	public double getHeight();
	
	/**
	 * Gets the camera horizontal zoom expressed in scene unit.
	 *
	 * @return the camera horizontal zoom expressed in scene unit.
	 */					
	public double getZoomX();
	
	/**
	 * Gets the camera vertical zoom expressed in scene unit.
	 *
	 * @return the camera vertical zoom expressed in scene unit.
	 */						
	public double getZoomY();
	
	/**
	 * Gets the camera maximum z clipping value expressed in scene unit.
	 *
	 * @return the camera maximum z clipping value expressed in scene unit.
	 */						
	public double getZMax();

	/**
	 * Gets the camera minimum z clipping value expressed in scene unit.
	 *
	 * @return the camera minimum z clipping value expressed in scene unit.
	 */						
	public double getZMin();
	
	/**
	 * Sets the camera focal length expressed in scene unit.
	 *
	 * @param val camera focal length expressed in scene unit.
	 */			
	public void setFocus(double val);
	
	/**
	 * Sets the camera screen width expressed in scene unit.
	 *
	 * @param val the camera screen width expressed in scene unit.
	 */					
	public void setWidth(double val);
	
	/**
	 * Set the camera screen height expressed in scene unit.
	 *
	 * @param val the camera screen height expressed in scene unit.
	 */						
	public void setHeight(double val);
	
	/**
	 * Sets the camera horizontal zoom expressed in scene unit.
	 *
	 * @param val the camera horizontal zoom expressed in scene unit.
	 */						
	public void setZoomX(double val);
	
	/**
	 * Sets the camera vertical zoom expressed in scene unit.
	 *
	 * @param val the camera vertical zoom expressed in scene unit.
	 */							
	public void setZoomY(double val);
	
	/**
	 * Sets the camera maximum z clipping value expressed in scene unit.
	 *
	 * @param val the camera maximum z clipping value expressed in scene unit.
	 */							
	public void setZMax(double val);
	
	/**
	 * Sets the camera minimum z clipping value expressed in scene unit.
	 *
	 * @param val the camera minimum z clipping value expressed in scene unit.
	 */								
	public void setZMin(double val);

	/**
	 * Sets the camera target.
	 *
	 * @param target target that camera must look at, null to make this camera non-target (freehand).
	 */		
	public void setTarget(IPoint3D target);


	/**
	 * Gets the camera target.
	 *
	 * @return target target that camera look at, or none if it is not a target camera (freehand).
	 */		
	public IPoint3D getTarget();

}