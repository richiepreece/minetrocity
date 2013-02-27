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

public final class RootSolver
{
	public double a;
	public double b;
	public double c;
	public double D;
	public double r1;
	public double r2;
	public int nbSol;
	
	public RootSolver()
	{
		this.a=0;
		this.b=0;
		this.c=0;
		this.D=0;
	}
	
	public final void solve(double a,double b,double c)
	{
		
		if(a==0)
		{
			if(b==0)
			{
			 this.nbSol=0;
			 return;
			}
			
			this.nbSol=1;
			this.r1=-c/b;
			return;
			
		}
		
		this.D=b*b-4.0*a*c;
		if(D<0.0)
		{
			this.nbSol=0;
			return;	
		}
		if(D==0.0)
		{
			this.nbSol=1;
			this.r1=(-b-Math.sqrt(D))/(2.0*a);
			return;	
		}
		if(D>0.0)
		{
			this.nbSol=2;
			double sqrtD=Math.sqrt(D);
			double i2A=1.0/(2.0*a);
			this.r1=(-b-sqrtD)*i2A;
			this.r2=(-b+sqrtD)*i2A;
			
			if(this.r2<this.r1)
			{
				double r=r2;
				this.r2=this.r1;
				this.r1=r;
			}
			
		}			
			
	}
}
