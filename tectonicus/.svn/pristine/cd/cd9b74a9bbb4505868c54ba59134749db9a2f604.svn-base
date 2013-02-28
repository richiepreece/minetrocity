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


public class SolidSphere3DResult implements ISolidSphere3DResult
{
	boolean hit;				//Set to true is something has been hit
	double impactDistance;		//Distance to the slide plane from origine
	double slideDistanceOver;	//Over-distance to the slide plane from end (origine+displace vector) point
	Point3D sphereP;			//3D point where sphere hit (relative to the starting pos)
	Point3D planeP;				//3D point where plane hit
	Point3D slidePlane;			//Sliding plane equation parameter : a,b,c
	double slidePlaneOffset;	//Sliding plane equation parameter : d
	Point3D displace;			//Real possible displacement == planeP-sphereP
	Point3D newDisplace;		//Rebound (new displacement vector after hit)
	int meshId;					//Hitted mesh id
	int faceId;					//Hitted face id


	public SolidSphere3DResult()
	{
		
		this.sphereP=new Point3D();
		this.planeP=new Point3D();
		this.slidePlane=new Point3D();
		this.displace=new Point3D();
		this.newDisplace=new Point3D();
		this.resetImpact();
	}
	
	public void resetImpact()
	{
		this.sphereP.set(0,0,0);
		this.planeP.set(0,0,0);
		this.slidePlane.set(0,0,0);
		this.displace.set(0,0,0);
		this.newDisplace.set(0,0,0);
		this.hit=false;
		this.impactDistance=-1;
		this.slideDistanceOver=-1;		
		this.slidePlaneOffset=0;
		this.meshId=-1;
		this.faceId=-1;
		
		
	}
	
	public ISolidSphere3DResult copy(SolidSphere3DResult r)
	{
		this.hit=r.hit;
		this.impactDistance=r.impactDistance;
		this.slideDistanceOver=r.slideDistanceOver;
		this.sphereP.copy(r.sphereP);
		this.planeP.copy(r.planeP);
		this.slidePlane.copy(r.slidePlane);
		this.slidePlaneOffset=r.slidePlaneOffset;
		this.displace.copy(r.displace);
		this.newDisplace.copy(r.newDisplace);
		this.meshId=r.meshId;
		this.faceId=r.faceId;
		return this;
	}
	
	public int getMesh3DId()
	{
		return this.meshId;
	}
	
	public int getFace3DId()
	{
		return this.faceId;
	}	

	public boolean isImpact()
	{
		return this.hit;
	}

	public double getDistance()
	{
		return this.impactDistance;
	}

	public double getSlideDistanceOver()
	{
		return this.slideDistanceOver;
	}

	public IPoint3D getSlidePlane()
	{
		return this.slidePlane;
	}

	public double getSlidePlaneOffset()
	{
		return this.slidePlaneOffset;
	}

	public IPoint3D getPosition()
	{
		return this.planeP;
	}

	public IPoint3D getResponse()
	{
		return this.newDisplace;
	}
}