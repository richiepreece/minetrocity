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

package net.dzzd.core;

import net.dzzd.access.*;


/** 
 *  This class represent an object point with 3 dimension x,y,z and a normal vx,vy,vz
 *  in 2 differents spaces (object & caméra)
 *  @version 1.0
 *  @since 1.0
 *  @see Point3D
 *  @see Face3D
 *  @see Mesh3D
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
public final class Vertex3D extends Point3D implements IVertex3D
{
	int id;	
	
	//TODO: hum basically all of those attibutes must be removed and put into compiled version at render time....
							//Vertex id relative to object
	double tX;
	double tY;
	double tZ;				//3D rendering position (current render caméra space)
	
	//runtime
	int cameraPositionEvaluated;	//Last screen/caméra image number for wich position has been updated
	double xs;						//When rendering screen x position (only if z>zMin)
	double ys;						//When rendering screen x position (only if z>zMin)
	double iZs;						//When rendering 1/z position (only if z>zMin)
	
	
	public Vertex3D()
	{
		super();
		this.cameraPositionEvaluated=-1;
		this.xs=0;
		this.ys=0;
		this.iZs=0;
		this.id=0;
	}

	public Vertex3D (double x,double y,double z)
	{
		super(x,y,z);
		this.cameraPositionEvaluated=-1;
	}
	
	public IPoint3D getClone()
	{
		Vertex3D v=new Vertex3D(this.x,this.y,this.z);
		return v;
	}
}