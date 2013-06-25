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
import net.dzzd.utils.Log;

import java.io.Serializable;

public class Mesh3D extends Scene3DObject implements IMesh3D
{
	
	Vertex3D vertices3D[];			//Vertex array
	Face3D faces3D[];				//Face array
	Mesh3DOctree octree;			//Mesh octree (null if none)

	public IMesh3DViewGenerator meshViewGenerator;
	public IMesh3DCollisionGenerator meshCollisionGenerator;
	
	boolean castShadowFlag;
	boolean recvShadowFlag;
		
	public Mesh3D()
	{
		super();
		this.castShadowFlag=true;
		this.recvShadowFlag=true;
		this.vertices3D=new Vertex3D[0];	
		this.faces3D=new Face3D[0];	
		this.meshViewGenerator=null;
		this.meshCollisionGenerator=null;
		this.octree=null;
	}
	
	public Mesh3D(IVertex3D vertices3D[],IFace3D faces3D[])
	{
		super();
		this.castShadowFlag=true;
		this.recvShadowFlag=true;		
		this.vertices3D=(Vertex3D[])vertices3D;	
		this.faces3D=(Face3D[])faces3D;	
		this.meshViewGenerator=null;
		this.meshCollisionGenerator=null;
		this.octree=null;
		this.buildVertexId();
		this.buildFaceId();
	}


	/*
	 *INTERFACE IMesh3D
	 */	
	public void build()
	{
		super.build();
		this.buildVertexId();
		this.buildFaceId();
		this.removeDuplicateVertices();
		//this.removeInvalideFaces();		
		this.buildFacesNormals();
		this.buildSphereBoxAndCenter();
		//Auto octree
		if(this.meshViewGenerator==null)
			this.buildMesh3DOctree();
		
	}

	public void buildVertexId()
	{
		super.build();
		for(int np=0;np<this.vertices3D.length;np++)
			this.vertices3D[np].id=np;	
		
	}

	public void buildFaceId()
	{
		super.build();
		for(int np=0;np<faces3D.length;np++)
			faces3D[np].id=np;	
	}
	
	public void buildSphereBoxAndCenter()
	{
		super.build();
		this.sphereBox=0;
		double xmin=Double.MAX_VALUE;
		double xmax=-Double.MAX_VALUE;
		double ymin=Double.MAX_VALUE;
		double ymax=-Double.MAX_VALUE;
		double zmin=Double.MAX_VALUE;
		double zmax=-Double.MAX_VALUE;	
		
		for(int x=0;x<this.vertices3D.length;x++)
		{
			Point3D p=this.vertices3D[x];
			if(p.x>xmax) xmax=p.x;
			if(p.x<xmin) xmin=p.x;
			if(p.y>ymax) ymax=p.y;
			if(p.y<ymin) ymin=p.y;
			if(p.z>zmax) zmax=p.z;
			if(p.z<zmin) zmin=p.z;	
		}
		
		double cx=(xmax+xmin)/2.0;
		double cy=(ymax+ymin)/2.0;
		double cz=(zmax+zmin)/2.0;
		this.center.set(cx,cy,cz);

		for(int x=0;x<this.vertices3D.length;x++)
		{
			Point3D p=this.vertices3D[x];
			double distance=this.center.dist(p);
			if(distance>this.sphereBox)
				this.sphereBox=distance;
		}

		double dxmax=(xmax/2.0-xmin/2.0);
		double dymax=(ymax/2.0-ymin/2.0);
		double dzmax=(zmax/2.0-zmin/2.0);
		double dmax2=dxmax*dxmax+dymax*dymax+dzmax*dzmax;
		this.sphereBox=Math.sqrt(dmax2);
		for(int n=0;n<this.faces3D.length;n++)
		{
			this.faces3D[n].initSphereBox();
			
		}
		
	}
	
	public void removeInvalideFaces()
	{
		super.build();
		int validatePol[]=new int[faces3D.length];
		for(int np=0;np<validatePol.length;np++)
			validatePol[np]=1;	
		
		int nbValid=0;
		for(int np=0;np<faces3D.length;np++)
		{
			
			Face3D pol=faces3D[np];
			if(pol.p0.equals(pol.p1))
				validatePol[np]=0;
			else
				if(pol.p0.equals(pol.p2))
					validatePol[np]=0;
				else
					if(pol.p1.equals(pol.p2))
						validatePol[np]=0;	
					else
						nbValid++;
					
			if(validatePol[np]==0)
				System.out.println("*********************\n*********\nremove face :" +np);
				
		}		
		Log.log("Object name " + this.nom);
		Log.log("Valide Faces " + (nbValid) + "/" + faces3D.length);
		int nf=0;
		Face3D validPol3D[]=new Face3D[nbValid];
		for(int np=0;np<faces3D.length;np++)
		{
			if(validatePol[np]==1)
			{
				validPol3D[nf++]=faces3D[np];
			}
				
		}
		this.faces3D=validPol3D;
		
		this.buildFaceId();
		
		
	}

	public void removeDuplicateVertices()
	{
		
		this.buildVertexId();
		Vertex3D tmpV[]=new Vertex3D[this.vertices3D.length];
		int	idReplace[]=new int[this.vertices3D.length];
		
		for(int n=0;n<idReplace.length;n++)
			idReplace[n]=-1;
			
		int nbTmpV=0;
		
		for(int np=0;np<this.vertices3D.length;np++)
		{
			Vertex3D p=this.vertices3D[np];
			
			boolean vertexDuplicate=false;
			
			for(int npt=0;npt<nbTmpV;npt++)
			{
				if(tmpV[npt].equals(p))
				{
					vertexDuplicate=true;
					idReplace[p.id]=npt;
					break;
				}	
			}
			
			if(!vertexDuplicate)
			{
				idReplace[p.id]=nbTmpV;
				tmpV[nbTmpV++]=p;
			}
			
		}
				
		this.vertices3D=new Vertex3D[nbTmpV];
		for(int npt=0;npt<nbTmpV;npt++)
		{
			this.vertices3D[npt]=tmpV[npt];
		}
		
		//this.buildVertexId();
		for(int np=0;np<this.faces3D.length;np++)
		{
			Face3D pol=this.faces3D[np];
			pol.p0=this.vertices3D[idReplace[pol.p0.id]];
			pol.p1=this.vertices3D[idReplace[pol.p1.id]];
			pol.p2=this.vertices3D[idReplace[pol.p2.id]];
		}
		this.buildVertexId();
		
	}
	
	public void buildFacesNormals()
    {
    	super.build();
    	
    	int nbSmooth[]=new int[this.vertices3D.length];
		
		for(int np=0;np<this.faces3D.length;np++)
		{
			Face3D pol=this.faces3D[np];
			pol.object=this;	
			pol.setNormal();
		}
		

		
		for(int smoothGroup=1;smoothGroup<32;smoothGroup++)
		{
			int smoothGroupMask=0;
			smoothGroupMask=1<<(smoothGroup-1);
			
			for(int np=0;np<this.vertices3D.length;np++)
			{
				nbSmooth[np]=0;
				Vertex3D p=this.vertices3D[np];
				p.id=np;
				p.tX=0;
				p.tY=0;
				p.tZ=0;				
			}
				
			for(int np=0;np<this.faces3D.length;np++)
			{
				Face3D pol=this.faces3D[np];	
				if((pol.smoothGroupMask != smoothGroupMask) && (pol.smoothGroupMask & smoothGroupMask)==0)
					continue;

				double x=pol.pa;
				double y=pol.pb;
				double z=pol.pc;

				Vertex3D cp=null;
				Vertex3D v1=null;
				Vertex3D v2=null;

				for(int n=0;n<3;n++)
				{
					switch(n)
					{
						case 0:
							cp=pol.p1;
							v1=pol.p0;
							v2=pol.p2;
						break;	
						case 1:
							cp=pol.p2;
							v1=pol.p1;
							v2=pol.p0;						
						break;	
						case 2:
							cp=pol.p0;
							v1=pol.p2;
							v2=pol.p1;												
						break;													
						
					}
					
					//Calcul l'angle entre les deux segments
					// partant du point, pour moduler l'influence 
					// de la normal du polygone sur la normale du vertex
					double v1x=v1.x-cp.x;
					double v1y=v1.y-cp.y;
					double v1z=v1.z-cp.z;
					double v2x=v2.x-cp.x;
					double v2y=v2.y-cp.y;
					double v2z=v2.z-cp.z;			
					double scal=v1x*v2x+v1y*v2y+v1z*v2z;
					double norm1=Math.sqrt(v1x*v1x+v1y*v1y+v1z*v1z);
					double norm2=Math.sqrt(v2x*v2x+v2y*v2y+v2z*v2z);
					if(norm1*norm2!=0.0)
					{				
						scal/=norm1*norm2;
						if(scal>1.0)
							scal=1.0;
						if(scal<-1.0)
							scal=-1.0;							
						double angle=Math.acos(scal);
						
						cp.tX+=x*angle;
						cp.tY+=y*angle;
						cp.tZ+=z*angle;	
						nbSmooth[cp.id]++;
					}
				}
								
			
			}
			for(int np=0;np<this.vertices3D.length;np++)
			{
				Vertex3D p=this.vertices3D[np];
				double norme=Math.sqrt(p.tX*p.tX+p.tY*p.tY+p.tZ*p.tZ);
				p.tX/=norme;
				p.tY/=norme;
				p.tZ/=norme;
			
			}
			
			for(int np=0;np<this.faces3D.length;np++)
			{
				Face3D pol=this.faces3D[np];				
				if((pol.smoothGroupMask & smoothGroupMask)==0)
					continue;
				
				Vertex3D p0=pol.p0;
				Vertex3D p1=pol.p1;
				Vertex3D p2=pol.p2;
				
				pol.p0nx=(float)(p0.tX);
				pol.p0ny=(float)(p0.tY);
				pol.p0nz=(float)(p0.tZ);
				pol.p1nx=(float)(p1.tX);
				pol.p1ny=(float)(p1.tY);
				pol.p1nz=(float)(p1.tZ);
				pol.p2nx=(float)(p2.tX);
				pol.p2ny=(float)(p2.tY);
				pol.p2nz=(float)(p2.tZ);					
				
			}					
				
			
		}
			/*
			
		for(int np=0;np<polygones3D.length;np++)
		{
			Face3D pol=polygones3D[np];				
			if((pol.smoothGroupMask)==0)
				continue;
			//if(validatePol[np]!=1)
			//	continue;
			
			Vertex3D p0=pol.p0;
			Vertex3D p1=pol.p1;
			Vertex3D p2=pol.p2;
			
			Vertex3D p;
			double norme;
			
			p=pol.p0;
			if(nbSmooth[p.id]!=0)
			{
				
				norme=Math.sqrt(p.tX*p.tX+p.tY*p.tY+p.tZ*p.tZ);
				p.tX/=norme;
				p.tY/=norme;
				p.tZ/=norme;
				pol.p0nx=(float)(p.tX);
				pol.p0ny=(float)(p.tY);
				pol.p0nz=(float)(p.tZ);
			}
			else
				System.out.println("f:"+pol.id+  " v:"+p.id);
			
			
			p=pol.p1;
			if(nbSmooth[p.id]!=0)
			{
				norme=Math.sqrt(p.tX*p.tX+p.tY*p.tY+p.tZ*p.tZ);
				p.tX/=norme;
				p.tY/=norme;
				p.tZ/=norme;
				pol.p1nx=(float)(p.tX);
				pol.p1ny=(float)(p.tY);
				pol.p1nz=(float)(p.tZ);
			}
			else
				System.out.println("f:"+pol.id+  " v:"+p.id);
			
			p=pol.p2;
			if(nbSmooth[p.id]!=0)
			{			
				norme=Math.sqrt(p.tX*p.tX+p.tY*p.tY+p.tZ*p.tZ);
				p.tX/=norme;
				p.tY/=norme;
				p.tZ/=norme;
				pol.p2nx=(float)(p.tX);
				pol.p2ny=(float)(p.tY);
				pol.p2nz=(float)(p.tZ);					
			}
			else
				System.out.println("f:"+pol.id+  " v:"+p.id);
			/*
			if(Double.isNaN(p.tX) || Double.isNaN(p.tY) || Double.isNaN(p.tZ))
			{
				System.out.println(this.nom+":"+np+"/"+polygones3D.length);
				System.out.println("smooth group:" + pol.smoothGroupMask);
				System.out.println(p0.tX);
				System.out.println(p0.tY);
				System.out.println(p0.tZ);
				System.out.println(p1.tX);
				System.out.println(p1.tY);
				System.out.println(p1.tZ);
				System.out.println(p2.tX);
				System.out.println(p2.tY);
				System.out.println(p2.tZ);								
				System.out.println(norme);
				pol.p0.print();
				pol.p1.print();
				pol.p2.print();
				
				//System.exit(0);
			}														
			
			
		}*/
	}

	public void buildMesh3DOctree()
	{
		super.build();
		if(this.meshViewGenerator!=null)	
			return;
			
		//Log.log("\nBuild " +this.getName() +" (" +this.getBuild()+")");
			
		double boxSize=Math.sqrt((this.sphereBox*this.sphereBox)/3.0)*2.0;
		int octreeCellNum[]=new int[this.faces3D.length];
		for(int x=0;x<octreeCellNum.length;x++)
			octreeCellNum[x]=-1;
		
		long time=System.currentTimeMillis();
		this.octree=new Mesh3DOctree(this,0,this.center.x,this.center.y,this.center.z,this.sphereBox,boxSize,this.faces3D,octreeCellNum,null);
		//Log.log("Mesh3DOctree: Mesh3DOctree time="+ (System.currentTimeMillis()-time));
		
		time=System.currentTimeMillis();
		this.octree.generate();
		//Log.log("Mesh3DOctree: generate time="+ (System.currentTimeMillis()-time));
		
		time=System.currentTimeMillis();
		this.octree.generateFaces();
		//Log.log("Mesh3DOctree: generateFaces="+ (System.currentTimeMillis()-time));
		
		time=System.currentTimeMillis();
		int nbOctree=this.octree.initMesh3DOctreeId(0);
		//Log.log("Mesh3DOctree: initMesh3DOctreeId="+ (System.currentTimeMillis()-time));
		//int total=this.octree.print();
		
		//this.octree.print();
	}

	public void flipNormals()
	{
		for(int np=0;np<this.faces3D.length;np++)
			this.faces3D[np].flipNormal();
	}
	 
	public void zoom(double x,double y,double z)
	{
		//Log.log("zoom="+x+","+y+","+z);
		for(int n=0;n<this.vertices3D.length;n++)
		{
			this.vertices3D[n].sub(this.pivot);
			this.vertices3D[n].zoom(x,y,z);
			this.vertices3D[n].add(this.pivot);
		}
		super.zoom(x,y,z);
		
		this.buildFacesNormals();
		this.buildSphereBoxAndCenter();
		if(this.octree!=null)
			this.buildMesh3DOctree();
	}

	public int getNbFace3D()
	{
		return this.faces3D.length;
	}

	public int getNbVertex3D()
	{
		return this.vertices3D.length;
	}
	
	public IFace3D getFace3D(int num)
	{
		
		if(num>=this.faces3D.length || num<0)
			return null;
		return this.faces3D[num];	
		
	}

	public IFace3D[] getFaces3D()
	{
		return this.faces3D;
	}

	public IVertex3D getVertex3D(int num)
	{
		return this.vertices3D[num];
	}
	
	public IVertex3D[] getVertex3D()
	{
		return this.vertices3D;
	}	
	
	public double getSphereBox()
	{
		return this.sphereBox;
	}
	
	public void copy(IMesh3D mesh)
	{
		Mesh3D m=(Mesh3D)mesh;
		
		//this.octree=m.octree;
		this.renderMode=m.renderMode;
		//this.meshViewGenerator=m.meshViewGenerator;
		//this.meshCollisionGenerator=m.meshCollisionGenerator;
		
		Face3D newF[]=new Face3D[m.faces3D.length];
		Vertex3D newV[]=new Vertex3D[m.vertices3D.length];
		
		for(int x=0;x<newV.length;x++)
		{
			newV[x]=(Vertex3D)(m.vertices3D[x].getClone());
			newV[x].id=x;
		}
		
		mesh.buildVertexId();
		
		//this.buildVertexId();	
		for(int x=0;x<newF.length;x++)
		{
			Face3D f=m.faces3D[x];
			Vertex3D pl[]=new Vertex3D[3];
			
			pl[0]=newV[f.p0.id];
			pl[1]=newV[f.p1.id];
			pl[2]=newV[f.p2.id];
			Face3D nf=new Face3D(pl[0],pl[1],pl[2]);
			
			nf.id=x;
			
			nf.smoothGroupMask=f.smoothGroupMask;
			nf.material=f.material;
			nf.object=this;
			
			nf.pa=f.pa;
			nf.pb=f.pb;
			nf.pc=f.pc;
			nf.pd=f.pd;
			
			nf.u0=f.u0;
			nf.v0=f.v0;				
			nf.u1=f.u1;
			nf.v1=f.v1;	
			nf.u2=f.u2;
			nf.v2=f.v2;					
			
			nf.p0nx=f.p0nx;
			nf.p0ny=f.p0ny;
			nf.p0nz=f.p0nz;
			nf.p1nx=f.p1nx;
			nf.p1ny=f.p1ny;
			nf.p1nz=f.p1nz;			
			nf.p2nx=f.p2nx;
			nf.p2ny=f.p2ny;
			nf.p2nz=f.p2nz;	
					
			newF[x]=nf;	
		}	
		this.faces3D=newF;
		this.vertices3D=newV;	
				
		
		super.copy(mesh);
		
	}
	
	public IScene3DObject getClone(boolean childrens)
	{			
		Mesh3D m=new Mesh3D();
		m.copy(this);
		
		if(childrens)
		{
			for(Scene3DObject o=this.firstChild;o!=null;o=o.nextChild)
				m.addChild(o.getClone(childrens));
		}

		return m;
	}

	public IMesh3DViewGenerator getMesh3DViewGenerator()
	{
		return this.meshViewGenerator;
	}

	public void setMesh3DViewGenerator(IMesh3DViewGenerator viewGenerator)
	{
		this.meshViewGenerator=viewGenerator;
	}

	public IMesh3DCollisionGenerator getMesh3DCollisionGenerator()
	{
		return this.meshCollisionGenerator;
	}

	public void setMesh3DCollisionGenerator(IMesh3DCollisionGenerator collisionGenerator)
	{
		this.meshCollisionGenerator=collisionGenerator;
	}
	
	public void setMaterial(IMaterial material)
	{
		Material m=(Material)material;
		for(int x=0;x<this.faces3D.length;x++)
			this.faces3D[x].material=m;
					
	}
	
	public void setCastShadow(boolean castShadowFlag)
	{
		this.castShadowFlag=castShadowFlag;
	}
	
	public boolean getCastShadow()
	{
		return this.castShadowFlag;
	}	
	
	public void setRecvShadow(boolean recvShadowFlag)
	{
		this.recvShadowFlag=recvShadowFlag;
	}
	
	public boolean getRecvShadow()
	{
		return this.recvShadowFlag;
	}
	
	public IMesh3DOctree getMesh3DOctree()
	{
		return this.octree;
	}	
}