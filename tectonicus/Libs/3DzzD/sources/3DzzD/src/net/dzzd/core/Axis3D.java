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
 *  A class representing 3D axis 
 *  @version 1.0
 *  @since 1.0
 *  @see Point3D
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
 
public final class Axis3D implements IAxis3D
{
	Point3D origine;	//Origin
	Point3D axeX;		//Axis x
	Point3D axeY;		//Axis y
	Point3D axeZ;		//Axis z
	
	public Axis3D()
	{
		this.origine=new Point3D();
		this.axeX=new Point3D();	
		this.axeY=new Point3D();	
		this.axeZ=new Point3D();	
		this.init();
	}
		
	IAxis3D normalize()
	{
		this.axeX.sub(this.origine).normalize().add(this.origine);
		this.axeY.sub(this.origine).normalize().add(this.origine);
		this.axeZ.sub(this.origine).normalize().add(this.origine);						
		return this;			
	}	


	IAxis3D set(IPoint3D position)
	{		
		this.add(position.getX(),position.getY(),position.getZ());
		return this;			
	}

	IAxis3D getPosition(IPoint3D position)
	{
		position.setX(this.origine.x);
		position.setY(this.origine.y);
		position.setZ(this.origine.z);
		return this;
	}				
	
	/** Transform this axis values into the local axis a
	 *  @param a an axis to transform axis to
 	 *  @return same axis "viewed" by axis a
 	 */																		
	public IAxis3D toLocalAxis(IAxis3D a)
	{
		IPoint3D p;
		IPoint3D o=a.getOrigin();		
		IPoint3D ax=a.getAX();
		IPoint3D ay=a.getAY();
		IPoint3D az=a.getAZ();	
		double ox=o.getX();		
		double oy=o.getY();
		double oz=o.getZ();
		double axx=ax.getX()-ox;		
		double axy=ax.getY()-oy;
		double axz=ax.getZ()-oz;
		double ayx=ay.getX()-ox;		
		double ayy=ay.getY()-oy;
		double ayz=ay.getZ()-oz;			
		double azx=az.getX()-ox;		
		double azy=az.getY()-oy;
		double azz=az.getZ()-oz;	
		
		p=this.getOrigin();
		double x=p.getX()-ox;
		double y=p.getY()-oy;
		double z=p.getZ()-oz;			
		p.setX(axx*x+axy*y+axz*z);
		p.setY(ayx*x+ayy*y+ayz*z);
		p.setZ(azx*x+azy*y+azz*z);
				
		p=this.getAX();
		x=p.getX()-ox;
		y=p.getY()-oy;
		z=p.getZ()-oz;			
		p.setX(axx*x+axy*y+axz*z);
		p.setY(ayx*x+ayy*y+ayz*z);
		p.setZ(azx*x+azy*y+azz*z);
			
		p=this.getAY();
		x=p.getX()-ox;
		y=p.getY()-oy;
		z=p.getZ()-oz;			
		p.setX(axx*x+axy*y+axz*z);
		p.setY(ayx*x+ayy*y+ayz*z);
		p.setZ(azx*x+azy*y+azz*z);
		
		p=this.getAZ();
		x=p.getX()-ox;
		y=p.getY()-oy;
		z=p.getZ()-oz;			
		p.setX(axx*x+axy*y+axz*z);
		p.setY(ayx*x+ayy*y+ayz*z);
		p.setZ(azx*x+azy*y+azz*z);
		
		return this;
	}
	
	/** Put this axis in the given axis space
	 *  @param a an axis to transform to its space
 	 *  @return same axis in axis parameter space
 	 */																		
	public IAxis3D toAxis(IAxis3D a)
	{
		IPoint3D p;
		IPoint3D o=a.getOrigin();		
		IPoint3D ax=a.getAX();
		IPoint3D ay=a.getAY();
		IPoint3D az=a.getAZ();	
		double ox=o.getX();		
		double oy=o.getY();
		double oz=o.getZ();
		double axx=ax.getX()-ox;		
		double axy=ax.getY()-oy;
		double axz=ax.getZ()-oz;
		double ayx=ay.getX()-ox;		
		double ayy=ay.getY()-oy;
		double ayz=ay.getZ()-oz;			
		double azx=az.getX()-ox;		
		double azy=az.getY()-oy;
		double azz=az.getZ()-oz;	
		
		p=this.getOrigin();
		double x=p.getX();
		double y=p.getY();
		double z=p.getZ();			
		p.setX(ox+axx*x+ayx*y+azx*z);
		p.setY(oy+axy*x+ayy*y+azy*z);
		p.setZ(oz+axz*x+ayz*y+azz*z);
				
		p=this.getAX();
		x=p.getX();
		y=p.getY();
		z=p.getZ();				
		p.setX(ox+axx*x+ayx*y+azx*z);
		p.setY(oy+axy*x+ayy*y+azy*z);
		p.setZ(oz+axz*x+ayz*y+azz*z);
				
		p=this.getAY();
		x=p.getX();
		y=p.getY();
		z=p.getZ();				
		p.setX(ox+axx*x+ayx*y+azx*z);
		p.setY(oy+axy*x+ayy*y+azy*z);
		p.setZ(oz+axz*x+ayz*y+azz*z);
				
		p=this.getAZ();
		x=p.getX();
		y=p.getY();
		z=p.getZ();				
		p.setX(ox+axx*x+ayx*y+azx*z);
		p.setY(oy+axy*x+ayy*y+azy*z);
		p.setZ(oz+axz*x+ayz*y+azz*z);
		
		return this;
	}		
	
	public IAxis3D init()
	{
		this.origine.set(0,0,0);
		this.axeX.set(1,0,0);	
		this.axeY.set(0,1,0);	
		this.axeZ.set(0,0,1);	
		return this;			
	}
	
	public IPoint3D getAX()
	{
		return this.axeX;
	}
	
	public IPoint3D getAY()
	{
		return this.axeY;
	}
	
	public IPoint3D getAZ()
	{
		return this.axeZ;
	}		
	
	public IPoint3D getOrigin()
	{
		return this.origine;
	}
		
	public IAxis3D copy(IAxis3D a)
	{
		this.origine.copy(a.getOrigin());
		this.axeX.copy(a.getAX());
		this.axeY.copy(a.getAY());
		this.axeZ.copy(a.getAZ());
		return this;
		
	}

	public IAxis3D add(double x,double y,double z)
	{
		this.origine.add(x,y,z);
		this.axeX.add(x,y,z);
		this.axeY.add(x,y,z);
		this.axeZ.add(x,y,z);
		return this;
	}

	public IAxis3D sub(double x,double y,double z)
	{
		return this.add(-x,-y,-z);
	}

	public IAxis3D add(IPoint3D point)
	{
		return this.add(point.getX(),point.getY(),point.getZ());
	}

	public IAxis3D sub(IPoint3D point)
	{
		return this.add(-point.getX(),-point.getY(),-point.getZ());
	}

	public IAxis3D rotateX(double angle)
	{
		this.origine.rotateX(angle);
		this.axeX.rotateX(angle);;
		this.axeY.rotateX(angle);
		this.axeZ.rotateX(angle);
		return this;
	}

	public IAxis3D rotateY(double angle)
	{
		this.origine.rotateY(angle);
		this.axeX.rotateY(angle);;
		this.axeY.rotateY(angle);
		this.axeZ.rotateY(angle);
		return this;
	}
	
	public IAxis3D rotateZ(double angle)
	{
		this.origine.rotateZ(angle);
		this.axeX.rotateZ(angle);;
		this.axeY.rotateZ(angle);
		this.axeZ.rotateZ(angle);
		return this;
	}

	public IAxis3D rotate(double angle,double px,double py,double pz,double x,double y,double z)
	{
		this.add(-px,-py,-pz);
		this.rotate(angle,x-px,y-py,z-pz);
		this.add(px,py,pz);
		return this;
	}

	public IAxis3D rotate(double angle,IPoint3D pivot,IPoint3D axis)
	{
		return this.rotate(angle,pivot.getX(),pivot.getY(),pivot.getZ(),axis.getX(),axis.getY(),axis.getZ());
	}

	public IAxis3D rotate(double angle,double px,double py,double pz,IPoint3D axis)
	{
		return this.rotate(angle,px,py,pz,axis.getX(),axis.getY(),axis.getZ());
	}

	public IAxis3D rotate(double angle,IPoint3D axis)
	{
		return this.rotate(angle,axis.getX(),axis.getY(),axis.getY());
	}
		
	public IAxis3D rotate(double angle,double x,double y,double z)
	{
		
		double n=Math.sqrt(x*x+y*y+z*z);
		if(n==0.0) return this;
		double in=1.0/n;
		x*=in;
		y*=in;
		z*=in;
		
		double nzx=Math.sqrt(x*x+z*z);
		//if(nzx==0.0) return this;
			
		double rx=Math.asin(y);
		double ry=0;
		if(nzx!=0.0)
		{
			ry=-Math.acos(z/nzx);
			if(x<0)
				ry=-ry;
		}
		else
		{	if(y>0)
				rx=Math.PI*0.5;
			else
				rx=-Math.PI*0.5;
			ry=0;
		}

		this.rotateY(-ry);
		this.rotateX(-rx);
		this.rotateZ(angle);
		this.rotateX(rx);
		this.rotateY(ry);
	
		return this;		
	}
	
	/** Past the rotation (rx,ry,rz) of this axis in the given point
	 *  @param rotation a point to received this axis rotation (rx,ry,rz)
 	 *  @return the same axis
 	 */					
	IAxis3D getRotationXYZ(IPoint3D rotation)
	{
		this.getRotationXZY(rotation);
		double rz=rotation.getY();
		rotation.setY(rotation.getZ());
		rotation.setZ(rz);
		return this;
	}
		
	public IAxis3D getRotationXZY(IPoint3D rotation)
	{
		Point3D o=this.origine;		
		Point3D ax=this.axeX;
		Point3D ay=this.axeY;
		Point3D az=this.axeZ;				
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
				
		//TODO normalize only if n² != 1.0
		double den=Math.sqrt((axx*axx)+(axy*axy)+(axz*axz));
		double sz=-axy/den;
		
		
				
		double rz=0.0;
		if(sz>1.0) sz=1.0;
		if(sz<-1.0) sz=-1.0;
			rz=Math.asin(sz);
				 
		den=Math.sqrt((axx*axx)+(axz*axz));
		if(den>1.0) den=1.0;
		double rx=0.0;
		double ry=0.0;
		if(Math.abs(den)>0.0);//Double.MIN_VALUE*10)
		{
			double cx=ayy/den;			
			if(cx>1.0) cx=1.0;
			if(cx<-1.0) cx=-1.0;
			rx=Math.acos(cx);
			if(azy<0.0)
			 rx=-rx;
			 			
	
			double cy=axx/den;			
			if(cy>1.0) cy=1.0;
			if(cy<-1.0) cy=-1.0;					
			ry=Math.acos(cy);
			if(axz<0.0)
			 ry=-ry;
		}
		
		if(Math.abs(rx)>Math.PI*0.5 && Math.abs(ry)>Math.PI*0.5)
		{
			rx=-Math.PI+rx;
			ry=-Math.PI+ry;
			rz=Math.PI-rz;
		}
		
		rotation.set(rx,ry,rz);
		return this;		
	}

	public IAxis3D set(IPoint3D ipivot,IPoint3D iposition,IPoint3D irotation)
	{
		Point3D pivot=(Point3D)ipivot;
		Point3D position=(Point3D)iposition;
		Point3D rotation=(Point3D)irotation;
		
		this.add(-pivot.x,-pivot.y,-pivot.z);		
		
		this.rotateX(rotation.x);
		this.rotateZ(rotation.z);
		this.rotateY(rotation.y);		
		this.add(position.x,position.y,position.z);
		return this;			
	}

	public IAxis3D set(IPoint3D iposition,IPoint3D irotation)
	{
		Point3D position=(Point3D)iposition;
		Point3D rotation=(Point3D)irotation;				
		
		this.rotateX(rotation.x);		
		this.rotateZ(rotation.z);
		this.rotateY(rotation.y);		
		this.add(position.x,position.y,position.z);
		return this;			
	}

	
	
}