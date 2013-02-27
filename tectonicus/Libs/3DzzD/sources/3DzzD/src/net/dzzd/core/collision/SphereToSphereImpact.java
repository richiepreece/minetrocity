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

package net.dzzd.core.collision;

public final class SphereToSphereImpact
{
	private RootSolver root;
	public boolean hit;
	public double hitX;
	public double hitY;
	public double hitZ;
	public double hitSphereAX;
	public double hitSphereAY;
	public double hitSphereAZ;
	public double hitSphereBX;
	public double hitSphereBY;
	public double hitSphereBZ;
	public double hitTime;				
	
	public SphereToSphereImpact()
	{
		this.root=new RootSolver();
		this.hitX=0;
		this.hitY=0;
		this.hitZ=0;
		this.hitSphereAX=0;
		this.hitSphereAY=0;
		this.hitSphereAZ=0;	
		this.hitSphereBX=0;
		this.hitSphereBY=0;
		this.hitSphereBZ=0;			
		this.hitTime=0;			
	}
	
	public final boolean doImpact(double sphereAX,double sphereAY,double sphereAZ,double radiusA,double vax,double vay,double vaz,double sphereBX,double sphereBY,double sphereBZ,double radiusB,double vbx,double vby,double vbz)
	{
		double x10=sphereAX;
		double y10=sphereAY;
		double z10=sphereAZ;
		double x20=sphereBX;
		double y20=sphereBY;
		double z20=sphereBZ;
		double vx1=vax;
		double vy1=vay;
		double vz1=vaz;
		double vx2=vbx;
		double vy2=vby;
		double vz2=vbz;	
		
		double dx0=x20-x10;
		double dvx=vx2-vx1;
		double k1=dx0*dx0;
		double k2=dvx*dvx;
		double k3=dx0*dvx*2;
		
		double dy0=y20-y10;
		double dvy=vy2-vy1;
		double l1=dy0*dy0;
		double l2=dvy*dvy;
		double l3=dy0*dvy*2;
		
		double dz0=z20-z10;
		double dvz=vz2-vz1;
		double m1=dz0*dz0;
		double m2=dvz*dvz;
		double m3=dz0*dvz*2;
		
		double a=k2+l2+m2;		
		double b=k3+l3+m3;
		double c=k1+l1+m1;	
		
		double radiusAB=(radiusA+radiusB);
		
		this.root.solve(a,b,c-radiusAB*radiusAB);

		if(this.root.nbSol==0)
		{
			this.hit=false;
			return this.hit;
		}
		
		this.hitTime=this.root.r1;
		this.hitSphereAX=x10+vx1*this.hitTime;
		this.hitSphereAY=y10+vy1*this.hitTime;
		this.hitSphereAZ=z10+vz1*this.hitTime;	
		this.hitSphereBX=x20+vx2*this.hitTime;
		this.hitSphereBY=y20+vy2*this.hitTime;
		this.hitSphereBZ=z20+vz2*this.hitTime;
		double iradiusAB=1.0/radiusAB;
		this.hitX=this.hitSphereAX+(this.hitSphereBX-this.hitSphereAX)*radiusA*iradiusAB;
		this.hitY=this.hitSphereAY+(this.hitSphereBY-this.hitSphereAY)*radiusA*iradiusAB;
		this.hitZ=this.hitSphereAZ+(this.hitSphereBZ-this.hitSphereAZ)*radiusA*iradiusAB;						
		this.hit=true;
		return this.hit;			
		
	}
	
}	
