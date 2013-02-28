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

public final class Face3D implements IFace3D
{	
	Mesh3D object;				//Face parent object (owner of this face)
	//Mesh3DOctree objectOctree;	//Face parent object (owner of this face)
	
	int id;						//Face id relative to parent object
	
	Vertex3D p0,p1,p2;			//Face vertices  
	
	int smoothGroupMask;		//Face smoothing group mask
	
	float p0nx,p0ny,p0nz;		//Face normal for vertex p0
	float p1nx,p1ny,p1nz;		//Face normal for vertex p1
	float p2nx,p2ny,p2nz;		//Face normal for vertex p2

	float p0tx,p0ty,p0tz;		//Face tangent for vertex p0
	float p1tx,p1ty,p1tz;		//Face tangent for vertex p1
	float p2tx,p2ty,p2tz;		//Face tangent for vertex p2
	
	float p0bx,p0by,p0bz;		//Face bitangent for vertex p0
	float p1bx,p1by,p1bz;		//Face bitangent for vertex p1
	float p2bx,p2by,p2bz;		//Face bitangent for vertex p2	

	
	float u0,u1,u2;				//U Mapping coordinate Original mapping coordinate
	float v0,v1,v2;				//V Mapping coordinate Original mapping coordinate
	
	float pa,pb,pc,pd; 			//Face plane equation in object space pa,pb,pc=face normal pd=nearest distance of the face from 0,0

	Material material;			//Face material
	
	float sphereBox;			//Face spherebox related to p0
	
	
	//TEMPORARY VARIABLES USED BY SOFTWARE RENDERER - UPDATED FOR EACH NEW FRAME
	//TODO: this should be removed, Mesh3D must handle this in a FaceList
	Face3D nextAlphaFace;	

	public Face3D()
	{
		this.object=null;
//		this.objectOctree=null;
		this.p0=null;
		this.p1=null;
		this.p2=null;
		this.smoothGroupMask=0;
		this.material=null;
		this.u0=0.0f;
		this.u1=1.0f;
		this.u2=1.0f;
		this.v0=0.0f;
		this.v1=0.0f;
		this.v2=1.0f;		
	}
	
	public Face3D(IVertex3D p0,IVertex3D p1,IVertex3D p2)
	{
		this.object=null;
//		this.objectOctree=null;
		this.p0=(Vertex3D)p0;
		this.p1=(Vertex3D)p1;
		this.p2=(Vertex3D)p2;
		this.smoothGroupMask=0;
		this.material=null;
		this.u0=0.0f;
		this.u1=1.0f;
		this.u2=1.0f;
		this.v0=0.0f;
		this.v1=0.0f;
		this.v2=1.0f;
	}
	
	
	Face3D setNormal()
	{
		//Compute normal
		Point3D normal=new Point3D();
		Point3D centre=new Point3D();
		centre.copy(p1);		
		normal.copy(p2);
		centre.sub(p0);
		normal.sub(p0);		
		centre.cross(normal);
		centre.normalize();		
		normal.copy(centre);
		
		/*
		if(normal.norme()!=1.0)
		{
			centre.copy(p2);		
			normal.copy(p0);
			centre.moins(p1);
			normal.moins(p1);		
			centre.vectoriel(normal);
			centre.normalize();		
			normal.copy(centre);
			//if(normal.norme()==1.0)
				//	System.out.println("a:"+id);
			if(normal.norme()!=1.0)
			{
				centre.copy(p0);		
				normal.copy(p1);
				centre.moins(p2);
				normal.moins(p2);		
				centre.vectoriel(normal);
				centre.normalize();		
				normal.copy(centre);
			//	if(normal.norme()==1.0)
					//System.out.println("b:"+id);
				
			}
			
			
		}
*/
		this.pa=(float)normal.x;
		this.pb=(float)normal.y;
		this.pc=(float)normal.z;
		this.pd=(float)-(normal.dot(this.p0));
		/*
		double d0=-(normal.scalaire(this.p0));
		double d1=-(normal.scalaire(this.p1));
		double d2=-(normal.scalaire(this.p2));
		
		if(d1<this.pd)
			this.pd=(float)d1;
		if(d2<this.pd)
			this.pd=(float)d2;
		
			
		
		if(d0!=d1 || d0!=d2)
		{
		
			//System.out.println(id);
			//System.out.println(d0);
			//System.out.println(d1);
			//System.out.println(d2);
		}
		
		
		
		if(this.pa==0.0f) this.pa=Float.MIN_VALUE;
		if(this.pb==0.0f) this.pb=Float.MIN_VALUE;
		if(this.pc==0.0f) this.pc=Float.MIN_VALUE;
		*/
		
		
/*
		double len=normal.norme();//this.pa*this.pa+this.pb*this.pb+this.pc*this.pc;
		if(len!=1.0)
		{
		
			System.out.println("erreur normal" + len);
		}
*/


		this.p0nx=(float)normal.x;
		this.p0ny=(float)normal.y;
		this.p0nz=(float)normal.z;
		
		this.p1nx=(float)normal.x;
		this.p1ny=(float)normal.y;
		this.p1nz=(float)normal.z;
		
		this.p2nx=(float)normal.x;
		this.p2ny=(float)normal.y;
		this.p2nz=(float)normal.z;
		
		/*
		*/
		
		
		//System.out.println("normal");
		//normal.print();
		
		//TODO: tangent vector
		
		
		
		//Compute initital texture axis (TBN Matrix) in object space
		double t2t1=this.u1-this.u0;
		double b2b1=this.v1-this.v0;
		double t3t1=this.u2-this.u0;
		double b3b1=this.v2-this.v0;
		
		//float den=t2t1*b3b1-t3t1*b2b1;
		
		//Compute tangent vector	
		Point3D T1=new Point3D();
		T1.copy(this.p1);
		T1.sub(this.p0);
		T1.mul(-b3b1);
		//T1.multiplier(b3b1);
		
		Point3D T2=new Point3D();
		T2.copy(this.p2);
		T2.sub(this.p0);
		T2.mul(-b2b1);
		
		T1.sub(T2);
		
		//Align tangent vector to normal & normalize it.
		double scalt=T1.dot(normal);
		//T2.copy(normal).multiplier(scalt);
		//T1.moins(T2);
		T1.normalize();
		
		this.p0tx=(float)T1.x;
		this.p0ty=(float)T1.y;
		this.p0tz=(float)T1.z;
		this.p1tx=(float)T1.x;
		this.p1ty=(float)T1.y;
		this.p1tz=(float)T1.z;
		this.p2tx=(float)T1.x;
		this.p2ty=(float)T1.y;
		this.p2tz=(float)T1.z;	
		
		/*
		T2.copy(T1);
		T2.vectoriel(normal);
		T2.normalize();
		this.p0bx=(float)T2.x;
		this.p0by=(float)T2.y;
		this.p0bz=(float)T2.z;
		this.p1bx=(float)T2.x;
		this.p1by=(float)T2.y;
		this.p1bz=(float)T2.z;
		this.p2bx=(float)T2.x;
		this.p2by=(float)T2.y;
		this.p2bz=(float)T2.z;		
		
		*/	
		//Compute bitangent vector	
		
		Point3D B1=new Point3D();
		B1.copy(this.p1);
		B1.sub(this.p0);
		B1.mul(t3t1);
		
		Point3D B2=new Point3D();
		B2.copy(this.p2);
		B2.sub(this.p0);
		B2.mul(t2t1);
		
		B1.sub(B2);
		
		//Align bitangent vector to normal & normalize it.
		double scalb=B1.dot(normal);
		//B2.copy(normal).multiplier(scalb);
		//B1.moins(B2);
		B1.normalize();
		
		this.p0bx=(float)B1.x;
		this.p0by=(float)B1.y;
		this.p0bz=(float)B1.z;
		this.p1bx=(float)B1.x;
		this.p1by=(float)B1.y;
		this.p1bz=(float)B1.z;
		this.p2bx=(float)B1.x;
		this.p2by=(float)B1.y;
		this.p2bz=(float)B1.z;	
				
		return this;	
	}
	
	void initSphereBox()
	{
		this.sphereBox=(float)this.p2.dist(this.p0);
		double distance21=this.p2.dist(this.p1);
		if(distance21>this.sphereBox)
			this.sphereBox=(float)distance21;
	}
	
	/*
	 *INTERFACE IFace3D
	 */

	public void flipNormal()
	{
		Vertex3D tmpP0=this.p0;
		this.p0=this.p2;
		this.p2=tmpP0;
		
		float tmpU0=this.u0;
		this.u0=this.u2;
		this.u2=tmpU0;
		
		float tmpV0=this.v0;
		this.v0=this.v2;
		this.v2=tmpV0;
	}

	public double getSphereBox()
	{
		return this.sphereBox;
	}

	public void setMaterial(IMaterial m)
	{
		this.material=(Material)m;
	}
	 
	public IMaterial getMaterial()
	 {
	 	return this.material;
	 }

	public IVertex3D getVertex3D0()
	{
		return this.p0;
	}	
	public IVertex3D getVertex3D1()
	{
		return this.p1;
	}	
	public IVertex3D getVertex3D2()
	{
		return this.p2;
	}	

	public IMesh3D getMesh3D()
	{
		return this.object;
	}
	
	public double getPA()
	{
		return this.pa;
	}

	public double getPB()
	{
		return this.pb;
	}

	public double getPC()
	{
		return this.pc;
	}

	public double getPD()
	{
		return this.pd;
	}
	
	public float getMappingU(int numVertex)
	{
		switch(numVertex)
		{
			case 0:
				return this.u0;
			case 1:
				return this.u1;
			case 2:
				return this.u2;	
			default:
				return 0f;
		}
	}	

	public float getMappingV(int numVertex)
	{
		switch(numVertex)
		{
			case 0:
				return this.v0;
			case 1:
				return this.v1;
			case 2:
				return this.v2;	
			default:
				return 0f;
		}
	}		
	
	public void setMappingU(int numVertex,float val)		
	{
		switch(numVertex)
		{
			case 0:
				this.u0=val;
			break;
			case 1:
				this.u1=val;
			break;
			case 2:
				this.u2=val;
			break;
			default:
				
		}
	}	
		
	public void setMappingV(int numVertex,float val)
	{
		switch(numVertex)
		{
			case 0:
				this.v0=val;
			break;
			case 1:
				this.v1=val;
			break;
			case 2:
				this.v2=val;
			break;
			default:
				
		}
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public float getVertex3D0Nx()
	{
		return this.p0nx;
	}		
	
	public float getVertex3D0Ny()
	{
		return this.p0ny;
	}		
	
	public float getVertex3D0Nz()
	{
		return this.p0nz;
	}	
	
	public float getVertex3D1Nx()
	{
		return this.p1nx;
	}		
	
	public float getVertex3D1Ny()
	{
		return this.p1ny;
	}		
	
	public float getVertex3D1Nz()
	{
		return this.p1nz;
	}
	
	public float getVertex3D2Nx()
	{
		return this.p2nx;
	}		
	
	public float getVertex3D2Ny()
	{
		return this.p2ny;
	}		
	
	public float getVertex3D2Nz()
	{
		return this.p2nz;
	}							
}