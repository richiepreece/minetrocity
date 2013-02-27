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
 *  Used for accessing to a SolidSphere3D.
 * 
 *  SolidSphere3D sphere give an easy way to perform simple physics aswell as more complexe physics by using multiple instance of SolidSphere3D.<br>
 *  <br>
 *  SolidSphere3D can be moved trought a scene and returns useful informations about last impacts performed:<br>
 *  <br>
 *  Some informations returned are : the distance to the impact point,new displacement vector for sliding, possible displacement without impact, impact sliding plane equation, impact position on scene and on this SolidSphere3D,etc...<br>
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *  @see ISolidSphere3DResult
 *  @see IScene3D
 *
 *  Copyright Bruno Augier 2005 
 */
public interface ISolidSphere3D extends IScene3DObject
{
	public void setScene3D(IScene3D scene);
	public void setRadius(double radius);
	public double getRadius();
	public IPoint3D getSource();
	public IPoint3D getDestination();
	public void setSource(IPoint3D source);
	public void setDestination(IPoint3D destination);
	public void setSource(double x,double y,double z);
	public void setDestination(double x,double y,double z);
	public ISolidSphere3DResult move();
	public ISolidSphere3DResult moveSlide(int nbLoop);
	public ISolidSphere3DResult moveBounce(int nbLoop,double bounceFactor);
	public ISolidSphere3DResult getResult();
}