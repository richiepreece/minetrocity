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
 *  Used for accessing to a SolidSphere3DResult.
 * 
 *  SolidSphere3DResult are used by SolidSphere3D.<br>
 *  <br>
 *  SolidSphere3DResult give information about an impact
 *  <br>
 *  Some of the informations given are : the distance to the impact point,new displacement vector for sliding, possible displacement without impact, impact sliding plane equation, impact position on scene and on this SolidSphere3D,etc...<br>
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *  @see ISolidSphere3D
 *  @see IScene3D
 *
 *  Copyright Bruno Augier 2005 
 */
public interface ISolidSphere3DResult
{

	/**
	 * Reset object.
	 * <br>
	 * reset to object default value.
	 *
	 * @return interface to acces to x,y,z components of the x axis (ax)
	 * @since 1.0
	 */
	public void resetImpact();

	/**
	 * Gets the impact result.
	 * 
	 * @return true if an impact happen.
	 * @since 1.0
	 */
	public boolean isImpact();

	/**
	 * Gets the distance to impact.
	 * <br>
	 * If an impact happened, return the distance to this impact.
	 * 
	 * @return distance to the impact in scene unit.
	 * @since 1.0
	 */
	public double getDistance();

	/**
	 * Gets how much the hitted plane is traversed.
	 * <br>
	 * If an impact happened, return the distance over the plane (shortest distance).
	 * 
	 * @return distance over the plane for the last move.
	 * @since 1.0
	 */
	public double getSlideDistanceOver();

	/**
	 * Gets the hitted face plane normal.
	 * <br>
	 * If an impact happened, return normal of the hitted face plane, normal contain the parameter a,b,c of the plane equation: a*x+b*y+c*z+d=0.
	 * 
	 * @return normal of the hitted face plane.
	 * @since 1.0
	 */
	public IPoint3D getSlidePlane();

	/**
	 * Gets the plane "d" parameter in the equation a*x+b*y+c*z+d=0 for the hitted face.
	 * <br>
	 * If an impact happened, return the offset value for the hitted face plane equation.
	 * 
	 * @return offset of the hitted face plane.
	 * @since 1.0
	 */
	public double getSlidePlaneOffset();

	/**
	 * Gets the impact position.
	 * <br>
	 * If an impact happened, return the position of that impact.
	 * 
	 * @return position of impact.
	 * @since 1.0
	 */
	public IPoint3D getPosition();

	/**
	 * Gets a the response displacement vector .
	 * <br>
	 * If an impact happend, return a vector that can be used recursivly to perform sliding or bouncing displacement.
	 * 
	 * @return response displacement vector.
	 * @since 1.0
	 */
	public IPoint3D getResponse();
	
	
	/**
	 * Gets id of the Mesh3D hitted in the scene.
	 * <br>
	 * If an impact happend, return id of the hitted Mesh3D.
	 * 
	 * @return id of Mesh3D in Scene3D
	 * @since 1.0
	 */
	 public int getMesh3DId();
	 
	/**
	 * Gets id of the Face3D hitted in the Mesh3D.
	 * <br>
	 * If an impact happend, return id of the hitted Face3D.
	 * 
	 * @return id of Face3D in Mesh3D
	 * @since 1.0
	 */	
	 public int getFace3DId();

}