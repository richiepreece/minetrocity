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

import java.util.*;

/** 
 *  This class represent an Octree for a Mesh3D
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
public final class Mesh3DOctree implements IMesh3DOctree
{
	int id;
	Point3D center;	
	double visibilitySphereBoxRadius;
	double visibilitySphereBoxRadius2;
	double boxSize;
	Mesh3DOctree childrens[];
	int nbChildren;
	Face3D parentFaces[];
	Face3D faces[];
	int nbFaceIn;
	int octreeCellNum[];	
	//private Hashtable properties;	
	private int build;
	//int lastVisibleImage;
	Mesh3DOctree parent;
	IMesh3D mesh;
	
	Mesh3DOctree(IMesh3D mesh,int id,double cx,double cy,double cz,double radius,double boxSize,Face3D parentFaces[],int octreeCellNum[],Mesh3DOctree parent)
	{
		this.mesh=mesh;
		this.id=id;
		this.parent=parent;
		this.center=new Point3D(cx,cy,cz);
		this.visibilitySphereBoxRadius=radius;
		this.visibilitySphereBoxRadius2=radius*radius;
		this.boxSize=boxSize;
		this.childrens=new Mesh3DOctree[8];
		this.parentFaces=parentFaces;
		this.nbFaceIn=0;
		this.octreeCellNum=octreeCellNum;
		this.faces=null;
	    //this.properties=new Hashtable();	
	    this.build=-1;
	    this.nbChildren=8;	
	}
	
	
	public int initMesh3DOctreeId(int id)
	{
		this.id=id++;
		
		this.nbChildren=0;
		for(int x=0;x<this.childrens.length;x++)
			if(this.childrens[x]!=null)
				this.nbChildren++;
				
		Mesh3DOctree newChildrens[]=new Mesh3DOctree[this.nbChildren];
		int numChildren=0;
		for(int x=0;x<this.childrens.length;x++)
			if(this.childrens[x]!=null)
				newChildrens[numChildren++]=this.childrens[x];
		this.childrens=newChildrens;
		
		for(int x=0;x<this.childrens.length;x++)
			id=this.childrens[x].initMesh3DOctreeId(id++);
			
		return id;
	}
	
	public IMesh3DOctree[] getMesh3DOctreeArray(IMesh3DOctree[] octrees)
	{
		octrees[this.id]=this;
		for(int x=0;x<this.childrens.length;x++)
			this.childrens[x].getMesh3DOctreeArray(octrees);		
		return octrees;
	}
	
	public int getNbChildren(boolean recursive)
	{
		int total=this.nbChildren;
		if(recursive)
			for(int x=0;x<this.childrens.length;x++)
				total+=this.childrens[x].getNbChildren(recursive);
		return total;
	}
	
	void fillMesh3DOctreeArray()
	{
	}
	
	int generate()
	{
		Thread.yield();
		this.nbFaceIn=0;
		int total=0;
		int insideFace[]=new int[this.parentFaces.length];
		
		//Count total face inside
		for(int num=0;num<this.parentFaces.length;num++)
		{
			Face3D f=this.parentFaces[num];
			int in=0;
			if(this.include(f)==1)
			{
				in=1;
				this.nbFaceIn++;
			}
			insideFace[num]=in;
		}
		
		//Not root Octree and less than n face return => do not subdivide
		//if(this.id!=0 && this.nbFaceIn<=12)
		 //return 0;
		
		if(this.nbFaceIn>=12 && id<(1<<16))
		{
			double cx=this.center.x;
			double cy=this.center.y;
			double cz=this.center.z;
			this.childrens[0]=new Mesh3DOctree(this.mesh,(this.id<<3)+1,cx-this.boxSize/4.0,cy-this.boxSize/4.0,cz-this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[1]=new Mesh3DOctree(this.mesh,(this.id<<3)+2,cx+this.boxSize/4.0,cy-this.boxSize/4.0,cz-this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[2]=new Mesh3DOctree(this.mesh,(this.id<<3)+3,cx-this.boxSize/4.0,cy+this.boxSize/4.0,cz-this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[3]=new Mesh3DOctree(this.mesh,(this.id<<3)+4,cx+this.boxSize/4.0,cy+this.boxSize/4.0,cz-this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[4]=new Mesh3DOctree(this.mesh,(this.id<<3)+5,cx-this.boxSize/4.0,cy-this.boxSize/4.0,cz+this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[5]=new Mesh3DOctree(this.mesh,(this.id<<3)+6,cx+this.boxSize/4.0,cy-this.boxSize/4.0,cz+this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[6]=new Mesh3DOctree(this.mesh,(this.id<<3)+7,cx-this.boxSize/4.0,cy+this.boxSize/4.0,cz+this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
			this.childrens[7]=new Mesh3DOctree(this.mesh,(this.id<<3)+8,cx+this.boxSize/4.0,cy+this.boxSize/4.0,cz+this.boxSize/4.0,visibilitySphereBoxRadius/2.0,boxSize/2.0,this.parentFaces,this.octreeCellNum,this);
	
			for(int x=0;x<this.childrens.length;x++)
			{
				int nb=this.childrens[x].generate();
				if(nb==0)
					this.childrens[x]=null;
				else
					total+=nb;
			}
		}
		
		//Count face in current octree node and not in childrens nodes
		this.nbFaceIn=0;
		for(int num=0;num<this.parentFaces.length;num++)
		{
			//Face3D f=this.parentFaces[num];
			//if(this.include(f)==1)
			if(insideFace[num]==1)
				if(this.octreeCellNum[num]==-1)
					this.nbFaceIn++;
		}		
		total+=this.nbFaceIn;
		
		//Not root Octree and less than 12 faces(with childrens) return 
		if(this.id!=0 && total<=12)
			return 0;
		
		//Update octree num array with current id for faces not in childrens
		for(int num=0;num<this.parentFaces.length;num++)
		{
			//if(this.include(f)==1)
			if(insideFace[num]==1)
				if(this.octreeCellNum[num]==-1)
				{
					this.octreeCellNum[num]=this.id;
				//	Face3D f=this.parentFaces[num];
				//	f.objectOctree=this;
				}
		}		
				
		//Return total faces in node+childrens faces
		return total;
	}
	
	public int generateFaces()
	{
		Thread.yield();
		this.nbFaceIn=0;
		int total=0;
		
		//Generate childrens and count there faces
		for(int x=0;x<this.childrens.length;x++)
			if(this.childrens[x]!=null)
			{
				int nb=this.childrens[x].generateFaces();
				if(nb==0)
					this.childrens[x]=null;
				else
					total+=nb;
			}
		
		//Update faces count with current node faces		
		for(int x=0;x<this.octreeCellNum.length;x++)
			if(this.octreeCellNum[x]==this.id)
				this.nbFaceIn++;
		
		//Create faces array for current node
		this.faces=new Face3D[this.nbFaceIn];
		int num=0;
		for(int x=0;x<this.octreeCellNum.length;x++)
			if(this.octreeCellNum[x]==this.id)
				this.faces[num++]=this.parentFaces[x];				
		
		//Clear all data wich are not further needed
		this.parentFaces=null;
		this.octreeCellNum=null;
		
		//return total faces count+children
		total+=this.nbFaceIn;
		return total;
	}
	
	public int print()
	{
		int total=this.nbFaceIn;
		//for(int s=0;s<(int)(Math.log(this.id+7)/2.0794415416798359282516963643745);s++)
		// System.out.print(" ");
		System.out.println("ID=" + this.id + " : " + this.nbFaceIn);		
		
		for(int x=0;x<this.childrens.length;x++)
			if(this.childrens[x]!=null)
				total+=this.childrens[x].print();
		
		return total;
		
	}	
	
	int include(Face3D f)
	{
		if(f.p0.distance2(this.center)*0.99<=this.visibilitySphereBoxRadius2)
			if(f.p1.distance2(this.center)*0.99<=this.visibilitySphereBoxRadius2)
				if(f.p2.distance2(this.center)*0.99<=this.visibilitySphereBoxRadius2)
					return 1;	
		return 0;
	}
/*
	public void setProperty(String key,Object value)
	{
		if(value==null)
			this.properties.remove(key);
		else
			this.properties.put(key,value);
	}
	
	public Object getProperty(String key)
	{
		return this.properties.get(key);
	}	
	
	public void clearProperties()
	{
		this.properties=new Hashtable();
	}
	*/
	public int getId()
	{
		return this.id;
	}
	
	public void build()
	{
		this.build++;
	}	
	
	public int getBuild()
	{
		return this.build;
	}

	public IFace3D[] getFaces3D()
	{
		return this.faces;
	}

	public int getNbFace3D()
	{
		return faces.length;
	}
	
	public IMesh3DOctree getChildren(int n)
	{
		if(childrens==null || n<0 || n>this.childrens.length)
			return null;
		return childrens[n];		
	}

	public IMesh3D getMesh3D()
	{
		return this.mesh;
	}
}