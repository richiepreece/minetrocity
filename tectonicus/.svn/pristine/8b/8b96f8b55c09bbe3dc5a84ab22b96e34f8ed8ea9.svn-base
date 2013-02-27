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

public final class SphereToEdgeImpact
{
	private SphereToSphereImpact si1;
	private SphereToSphereImpact si2;
	private RootSolver root;
	public boolean hit;
	public double hitX;
	public double hitY;
	public double hitZ;
	public double hitSphereX;
	public double hitSphereY;
	public double hitSphereZ;	
	public double hitTime;

	
	public SphereToEdgeImpact()
	{
		this.root=new RootSolver();
		this.hit=false;
		this.hitX=0;
		this.hitY=0;
		this.hitZ=0;
		this.hitSphereX=0;
		this.hitSphereY=0;
		this.hitSphereZ=0;			
		this.hitTime=0;
		this.si1=new SphereToSphereImpact();
		this.si2=new SphereToSphereImpact();
	}
	
	private double length2(double x1,double y1,double z1,double x2,double y2,double z2)
	{
		double dx=x2-x1;
		double dy=y2-y1;
		double dz=z2-z1;
		return (dx*dx+dy*dy+dz*dz);
	}
	
	public final boolean doImpact(double sphereAX,double sphereAY,double sphereAZ,double radiusA,double vax,double vay,double vaz,double x1,double y1,double z1,double x2,double y2,double z2)
	{
		this.hitTime=Double.MAX_VALUE;
		double radiusA2=radiusA*radiusA;
			
		//Store Sphere pos and speed
		double x10=sphereAX;
		double y10=sphereAY;
		double z10=sphereAZ;
		double vx1=vax;
		double vy1=vay;
		double vz1=vaz;
		
		
		//Compute edge direction vector (p1=>p2) and length
		double edgeX=x2-x1;
		double edgeY=y2-y1;
		double edgeZ=z2-z1;
		double edge2=edgeX*edgeX+edgeY*edgeY+edgeZ*edgeZ;
		double edge=Math.sqrt(edge2);
		
		//Compute sphere vector from first edge border
		double p1sX=sphereAX-x1;
		double p1sY=sphereAY-y1;
		double p1sZ=sphereAZ-z1;
		double p1s2=p1sX*p1sX+p1sY*p1sY+p1sZ*p1sZ;
					
		
		//Make a unitary vector with the edge direction (p1=>p2) vector 
		// this will be used to simulate a sphere that move along the edge
		double iEdge=1.0/edge;
		double vx2=edgeX*iEdge;
		double vy2=edgeY*iEdge;
		double vz2=edgeZ*iEdge;
		
		
		//Compute scalar product to perform a projection onto the edge 
		// of the sphere vector from first edge border and than 
		// found the starting pos of the sphere along the edge
		double no=vx2*p1sX+vy2*p1sY+vz2*p1sZ;
		double x20=x1+vx2*no;
		double y20=y1+vy2*no;
		double z20=z1+vz2*no;
		
		//Compute scalar product of both speed vector to find
		// the velocity vector of the sphere moving along the edge
		double nv=vx2*vx1+vy2*vy1+vz2*vy2;
		vx2*=nv;
		vy2*=nv;
		vz2*=nv;	
		
				
		//Radius of edge sphere == 0
	//	double radiusB=0.1;
		
		//Now we have a sphere moving along the edge
		// with the good velocity to hit our first given sphere
		//Compute the two spheres impact using a polynome
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
		
		this.hit=false;
		
		//VERIFY IF SPHERE IS ALREADY IN CONTACT
		if((dx0*dx0+dy0*dy0+dz0*dz0)<radiusA2)
		{
			
			this.hitSphereX=x10;
			this.hitSphereY=y10;
			this.hitSphereZ=z10;	
			this.hitX=x20;
			this.hitY=y20;
			this.hitZ=z20;
			
			//Verify that the hit is not outside of the edge borders
			double d1x=(this.hitX-x1);
			double d1y=(this.hitY-y1);
			double d1z=(this.hitZ-z1);
			double d1c=d1x*edgeX+d1y*edgeY+d1z*edgeZ;
							
			double d2x=(this.hitX-x2);
			double d2y=(this.hitY-y2);
			double d2z=(this.hitZ-z2);
			double d2c=d2x*-edgeX+d2y*-edgeY+d2z*-edgeZ;
			
			if(d1c>=0 && d2c>=0)
			{
				this.hitTime=0.0;
				this.hit=true;
				//System.out.println("IMMEDIATE");
				return true;
			}
		}
			
		if(this.length2(x10,y10,z10,x1,y1,z1)<radiusA2)
		{
			this.hitTime=0.0;
			this.hitSphereX=x10;
			this.hitSphereY=y10;
			this.hitSphereZ=z10;	
			this.hitX=x1;
			this.hitY=y1;
			this.hitZ=z1;
			this.hit=true;
			System.out.println("IMMEDIATE C1");
			return true;
		}
		
		if(this.length2(x10,y10,z10,x2,y2,z2)<radiusA2)
		{
			this.hitTime=0.0;
			this.hitSphereX=x10;
			this.hitSphereY=y10;
			this.hitSphereZ=z10;	
			this.hitX=x2;
			this.hitY=y2;
			this.hitZ=z2;
			this.hit=true;
			System.out.println("IMMEDIATE C2");
			return true;
		}
		
		
					
		this.root.solve(a,b,c-radiusA2);

		if(this.root.nbSol!=0 && this.root.r1>=0)
		{	
			double hitTime=this.root.r1;
			this.hitSphereX=x10+vx1*hitTime;
			this.hitSphereY=y10+vy1*hitTime;
			this.hitSphereZ=z10+vz1*hitTime;	
			this.hitX=x20+vx2*hitTime;
			this.hitY=y20+vy2*hitTime;
			this.hitZ=z20+vz2*hitTime;
			
			
			//Verify that the hit is not outside of the edge borders
			double d1x=(this.hitX-x1);
			double d1y=(this.hitY-y1);
			double d1z=(this.hitZ-z1);
			double d1c=d1x*edgeX+d1y*edgeY+d1z*edgeZ;
			
			double d2x=(this.hitX-x2);
			double d2y=(this.hitY-y2);
			double d2z=(this.hitZ-z2);
			double d2c=d2x*-edgeX+d2y*-edgeY+d2z*-edgeZ;

			if(d1c>=0 && d2c>=0)
			{
				this.hit=true;
				this.hitTime=hitTime;
				//System.out.println("INSIDE "+ this.hitTime);
				return true;
			}
		//	System.out.println(this.root.r1);
		//	System.out.println("d1c"+d1c);
		//	System.out.println("d2c"+d2c);
				
		}
			
		//Compute edge borders hits
		si1.doImpact(sphereAX,sphereAY,sphereAZ,radiusA,vax,vay,vaz,x1,y1,z1,0,0,0,0);
		si2.doImpact(sphereAX,sphereAY,sphereAZ,radiusA,vax,vay,vaz,x2,y2,z2,0,0,0,0);
		
		boolean si1hit=false;
		boolean si2hit=false;
		if(si1.hit && si1.hitTime>0)
		{
			if(si2.hit && si2.hitTime>0)
			{
				if(si1.hitTime<si2.hitTime)
				{
					si1hit=true;
				}
				else
				{
					si2hit=true;
				}
				
			}
			else
			{
				si1hit=true;
			}
		}
		else
		{
			if(si2.hit && si2.hitTime>0)
			{
				si2hit=true;
			}
		}
		
		if(si1hit)
		{
			this.hitTime=si1.hitTime;
			this.hitSphereX=si1.hitSphereAX;
			this.hitSphereY=si1.hitSphereAY;
			this.hitSphereZ=si1.hitSphereAZ;
			this.hitX=si1.hitX;
			this.hitY=si1.hitY;
			this.hitZ=si1.hitZ;
		//	System.out.println("corner 1");	
			this.hit=true;		
		}
		
		if(si2hit)
		{
			this.hitTime=si2.hitTime;
			this.hitSphereX=si2.hitSphereAX;
			this.hitSphereY=si2.hitSphereAY;
			this.hitSphereZ=si2.hitSphereAZ;
			this.hitX=si2.hitX;
			this.hitY=si2.hitY;
			this.hitZ=si2.hitZ;	
		//	System.out.println("corner 2");	
			this.hit=true;						
		}

		return this.hit;			
		
	}
	
}	
