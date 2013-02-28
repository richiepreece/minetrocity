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
import net.dzzd.utils.*;

/** 
 *  A class representing a 3D point x,y,z 
 *  @version 1.0
 *  @since 1.0
 *  @see Vertex3D
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
  
public class Point3D implements IPoint3D
{
	public double x,y,z;	
	
	public Point3D ()
	{
		this.set(0,0,0);	
	}
		
	public Point3D (double x,double y,double z)
	{
		this.set(x,y,z);
	}
	
	public void set(double x,double y,double z)
	{
		this.x=x;	
		this.y=y;
		this.z=z;
	}

	/** Rotate the point around the x axis 
	 *  @param angle radian angle for the rotation 
 	 *  @return the same point rotated
 	 */
	public IPoint3D rotateX(double angle)
	{
		double tY=y,tZ=z;
		double cosa=MathX.cos(angle);
		double sina=MathX.sin(angle);
		y=tY*cosa + tZ*sina;
		z=-tY*sina + tZ*cosa;
		return this;
	}
	
	/** Rotate the point around the y axis 
	 *  @param angle radian angle for the rotation 
 	 *  @return the same point rotated
 	 */	
	public IPoint3D rotateY(double angle)
	{
		double tX=x,tZ=z;
		double cosa=MathX.cos(angle);
		double sina=MathX.sin(angle);
		x=tX*cosa - tZ*sina;
		z=tX*sina + tZ*cosa;
		return this;
	}
	
	/** Rotate the point around the z axis 
	 *  @param angle radian angle for the rotation 
 	 *  @return the same point rotated
 	 */	
	public IPoint3D rotateZ(double angle)
	{
		double tY=y,tX=x;
		double cosa=MathX.cos(angle);
		double sina=MathX.sin(angle);
		x=tX*cosa + tY*sina;
		y=-tX*sina + tY*cosa;
		return this;
	}
	
	public IPoint3D add(double x,double y,double z)
	{
		this.x+=x;	
		this.y+=y;
		this.z+=z;
		return this;
		
	}
	
	public IPoint3D cross(IPoint3D p)
	{
		double tX,tY,tZ;
		double x=p.getX();
		double y=p.getY();
		double z=p.getZ();
		tZ=this.x*y-this.y*x;
		tY=this.z*x-this.x*z;
		tX=this.y*z-this.z*y;
		this.x=tX;
		this.y=tY;
		this.z=tZ;		
		return this;		
	}
	
	public double dot(IPoint3D p2)
	{
		return this.x*p2.getX()+this.y*p2.getY()+this.z*p2.getZ();
	}
		
	
	public double length()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}
	public double norm()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public double length2()
	{
		return x*x+y*y+z*z;
	}

	public boolean equals(IPoint3D p)
	{
		if(this.x!=p.getX())	return false;
		if(this.y!=p.getY())	return false;
		if(this.z!=p.getZ())	return false;
		return true;
	}
	
	public IPoint3D mul(double n)
	{
		this.x*=n;
		this.y*=n;
		this.z*=n;
		return this;		
	}
	
	public IPoint3D mul(IPoint3D n)
	{
		this.x*=n.getX();
		this.y*=n.getY();
		this.z*=n.getZ();
		return this;		
	}

	
	public IPoint3D div(double n)
	{
		double d=1.0/n;
		this.x*=d;
		this.y*=d;
		this.z*=d;
		return this;		
	}
		
	public double distance2(IPoint3D p)
	{
		double dx=this.x-p.getX();
		double dy=this.y-p.getY();
		double dz=this.z-p.getZ();
		return dx*dx+dy*dy+dz*dz;	
	}	
	
	public double dist(IPoint3D p)
	{
		return Math.sqrt(this.distance2(p));	
	}		
	
	public Point3D toLocalAxe(Axis3D a)
	{
		Point3D o=a.origine;		
		Point3D ax=a.axeX;
		Point3D ay=a.axeY;
		Point3D az=a.axeZ;	
		double ox=o.x;		
		double oy=o.y;
		double oz=o.z;
		double axx=ax.x-ox;		
		double axy=ax.y-oy;
		double axz=ax.z-oz;
		double ayx=ay.x-ox;		
		double ayy=ay.y-oy;
		double ayz=ay.z-oz;			
		double azx=az.x-ox;		
		double azy=az.y-oy;
		double azz=az.z-oz;	
		double x=this.x-ox;
		double y=this.y-oy;
		double z=this.z-oz;			
		this.x=axx*x+axy*y+axz*z;
		this.y=ayx*x+ayy*y+ayz*z;
		this.z=azx*x+azy*y+azz*z;			
		return this;
	}	
	
	public String toString()
	{
		return "(" + x + " , " + y + " , " + z + ")";
	}
	
	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public double getZ()
	{
		return this.z;
	}
	
	public void setX(double val)
	{
		this.x=val;
	}

	public void setY(double val)
	{
		this.y=val;
	}

	public void setZ(double val)
	{
		this.z=val;
	}

	public IPoint3D copy(IPoint3D p)
	{
		this.x=p.getX();
		this.y=p.getY();
		this.z=p.getZ();	
		return this;
	}
													
	public IPoint3D add(IPoint3D p)
	{
		this.x+=p.getX();
		this.y+=p.getY();
		this.z+=p.getZ();	
		return this;
	}

	public IPoint3D sub(IPoint3D p)
	{
		this.x-=p.getX();
		this.y-=p.getY();
		this.z-=p.getZ();	
		return this;
	}

	public IPoint3D normalize()
	{
		double iLen=1.0/this.norm();
		this.x*=iLen;
		this.y*=iLen;
		this.z*=iLen;
		return this;
	}
	
	public IPoint3D zoom(double x,double y,double z)
	{
		this.x*=x;	
		this.y*=y;
		this.z*=z;
		return this;
	}
	
	public IPoint3D getClone()
	{
		return new Point3D(this.x,this.y,this.z);
	}	
}