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
 * HeightMap3D : Dynamic height map tesselation and LOD generation
 *
 * @author Bruno Augier
 * @version 1.00
 */
 
public class HeightMap3D implements IHeightMap3D
{
	private IHeightMap hMap;			//HeightMap
	private FaceNode leftRootNode; 		//Root FaceNode (left)
	private FaceNode rightRootNode;		//Root FaceNode (right)
	private double lVariance[];			//Variance tree (left)
	private double rVariance[];			//Variance tree (right)
	private int minVarianceDepth;		//Minimum tesselation level
	private int maxVarianceDepth;		//Maximum tesselation level
	private double generateVariance;	//Generate variance
	private double tesselateVariance;	//Tesselate variance
	private int nbGenerateFace;			//Number of FaceNode for last generation
	private int nbTesselateFace;		//Number of FaceNode for last tesselation
	private double generateClipNear;	//Minimum distance allowed for generation
	private double generateClipFar;		//Maximum distance allowed for generation
	private int idGenerate;				//Current generation ID
	private double xMin;				//Bounds x min (0-1)
	private double xMax;				//Bounds x max (0-1)
	private double yMin;				//Bounds y min (0-1)
	private double yMax;				//Bounds y max (0-1)
	private double size3DX;				//Generated mesh size x
	private double size3DY;				//Generated mesh size y
	private double size3DZ;				//Generated mesh size z
	private double min3DX;				//Generated mesh min z
	private double min3DY;				//Generated mesh min z
	private double min3DZ;				//Generated mesh min z
	private int nbTesselateVertices;	//Number of vertices for last tesselation
	private VertexNode firstTesselateVertexNode;	//Tesselated vertex node list	
	private Vertex3D tesselateVertexArray[];		//Tesselated vertex array	
	private Face3D tesselateFaceArray[];			//Tesselated face array	
	private Face3DList tesselatedFaceList;			//Tesselated face List
	private Face3DList generatedFaceList;			
	private Face3DList generatedCollisionFaceList;
	private int nbGenerateCollisionFace;			//Number of FaceNode for last collision generation			
	
	/**
	 * Return an array containing all tesselated vertices 
	 * @return vertex array
	 */
	public IVertex3D[] getVertex3D()
	{
		return this.tesselateVertexArray;
	}
		
	/**
	 * Construct a new Grid3D
	 * @param minVarianceDepth minimum variance tree depth 
	 * @param maxVarianceDepth maximum variance tree depth
	 */
	public HeightMap3D(int minVarianceDepth,int maxVarianceDepth)
	{
		this.xMin=0.0;
		this.yMin=0.0;
		this.xMax=1.0;
		this.yMax=1.0;
		this.size3DX=1.0;
		this.size3DY=1.0;
		this.size3DZ=1.0;		
		this.init(minVarianceDepth,maxVarianceDepth);
		this.generateVariance=0;
		this.tesselateVariance=0;
	}
	
	/**
	 * Return an array containing all tesselated faces
	 * @return face array
	 */
	public IFace3D[] getFace3D()
	{
		return this.tesselateFaceArray;
	}	
	
	/**
	 * Return the list of face 	for last generation
	 * @return face list
	 */
	public IFace3DList getViewFace3DList()
	{
		return this.generatedFaceList;
	}

	/**
	 * Return the list of face 	for last generation
	 * @return face list
	 */
	public IFace3DList getCollisionFace3DList()
	{
		return this.generatedCollisionFaceList;
	}
	
	/**
	 * Set the size of the generated 3D Mesh in scene unit
	 * @param min3DX bounding box xmin
	 * @param min3DY bounding box ymin
	 * @param min3DZ bounding box zmin
	 * @param max3DX bounding box xmax
	 * @param max3DY bounding box ymax
	 * @param max3DZ bounding box zmax
	 */
	public void setMesh3DBounds(double min3DX,double min3DY,double min3DZ,double max3DX,double max3DY,double max3DZ)
	{
		this.min3DX=min3DX;
		this.min3DY=min3DY;
		this.min3DZ=min3DZ;
		this.size3DX=max3DX-min3DX;
		this.size3DY=max3DY-min3DY;
		this.size3DZ=max3DZ-min3DZ;
	}	
	
	/**
	 * Set the area of the height map to use for tesselation in percent ( 0,0,1,1 = full  , 0,0,0.5,0.5 = left-upper corner)
	 * @param xMin minimum height map x pos
	 * @param yMin minimum height map y pos
	 * @param xMax maximum height map x pos
	 * @param yMax maximum height map y pos
	 */
	public void setHeightMapBounds(double xMin,double yMin,double xMax,double yMax)
	{
		this.xMin=xMin;
		this.yMin=yMin;
		this.xMax=xMax;
		this.yMax=yMax;
	}

	/**
	 * Set current generate variance 
	 * @param generateVariance variance to use for futur mesh generation
	 */
	public void setMesh3DViewGeneratorQuality(double generateVariance)
	{
		this.generateVariance=generateVariance;
	}
	
	/**
	 * Get current generate variance 
	 * @return current generation variance
	 */	
	public double getMesh3DViewGeneratorQuality()
	{
		return this.generateVariance;
	}
	
	/**
	 * Set current tesselate variance 
	 * @param tesselateVariance variance to use for future base mesh tesselation
	 */
	public void setMesh3DGeneratorQuality(double tesselateVariance)
	{
		this.tesselateVariance=tesselateVariance;
	}
	
	/**
	 * Get current tesselate variance 
	 * @return current tesselation variance
	 */	
	public double getMesh3DGeneratorQuality()
	{
		return this.tesselateVariance;
	}	
	
	/**
	 * Get number of FaceNode for last generation 
	 * @return number of FaceNode for last generation
	 */	
	public int getNbViewFaces()
	{
		return this.nbGenerateFace;
	}

	public int getNbCollisionFaces()
	{
		return this.nbGenerateCollisionFace;
	}
	
	/**
	 * Get number of FaceNode for last tesselation 
	 * @return number of FaceNode for last tesselation
	 */	
	public int getNbFace3D()
	{
		return this.nbTesselateFace;
	}	
		
	/**
	 * Get number of vertices for last tesselation 
	 * @return number of vertices for last tesselation
	 */		
	public int getNbVertex3D()
	{
		return this.nbTesselateVertices;
	}
	
	/**
	 * Set the Heightmap interface to use
	 * @param hm height map
	 */
	public void setHeightMap(IHeightMap hm)
	{
		this.hMap=hm;
		if(this.hMap!=null)
			this.updateHeightMap();
	}
	
	public void updateHeightMap()
	{
		this.computeHeightMapVariance();
	}
		
	/**
	 * Compute Grid3D tesselation 
	 */
	public IMesh3D generate()
	{
		this.nbTesselateVertices=0;
		this.firstTesselateVertexNode=null;		
		this.nbTesselateFace=0;		
		this.tesselatedFaceList=null;
		this.resetTesselate();
		this.rTessellate(this.leftRootNode,this.xMin,this.yMax,this.xMax,this.yMin,this.xMin,this.yMin,1,this.lVariance);					
		this.rTessellate(this.rightRootNode,this.xMax,this.yMin,this.xMin,this.yMax,this.xMax,this.yMax,1,this.rVariance);	
		this.tesselatePostProcess();		
		this.createTesselateVertex3DArray();
		this.createTesselateFace3DArray();
		
		Mesh3D m=new Mesh3D(this.getVertex3D(),this.getFace3D());
		m.setMesh3DViewGenerator(this);
		m.setMesh3DCollisionGenerator(this);
		//m.build();
		return m;
	}
	
	public void generateForSolidSphere3DCollision(double dx,double dy,double dz,double tx,double ty,double tz,double radius)
	{
		this.generatedCollisionFaceList=null;
		this.nbGenerateCollisionFace=0;
		double mx=tx-dx;
		double my=ty-dy;
		double mz=tz-dz;
		double moveNorme=Math.sqrt(mx*mx+my*my+mz*mz);
		this.rGenerateCollision(this.leftRootNode,dx,dy,dz,tx,ty,tz,radius,moveNorme);
		this.rGenerateCollision(this.rightRootNode,dx,dy,dz,tx,ty,tz,radius,moveNorme);
	}

	/**
	 * Generate a new Mesh with the current generate variance 
	 * @param viewPosX x coordinate of camera
	 * @param viewPosY y coordinate of camera
	 * @param viewPosZ z coordinate of camera
	 * @param targetX x coordinate of camera target
	 * @param targetY y coordinate of camera target
	 * @param targetZ z coordinate of camera target
	 * @param focus focal length
	 * @param screenWidth screen width
	 * NB: FOV=2*Math.atan(screenWidth*0.5/focus)
	 */
	public void generateForView(double viewPosX,double viewPosY,double viewPosZ,double targetX,double targetY,double targetZ,double focus,double screenWidth)
	{
		this.generatedFaceList=null;
		this.nbGenerateFace=0;
		this.idGenerate++;
		//if(this.idGenerate==0)
		//this.idGenerate=1;

/*
		targetX=10000;
		targetY=10001;
		targetZ=3500;
		viewPosX=10000;
		viewPosY=10000;
		viewPosZ=3500;
*/


		
		//INITIALISE CLIPPING PLANE
		double pa=targetX;
		double pb=targetY;
		double pc=targetZ;
		pa-=viewPosX;
		pb-=viewPosY;
		pc-=viewPosZ;
		double n=this.norme(pa,pb,pc);
		pa/=n;
		pb/=n;
		pc/=n;
		double pd=-(pa*viewPosX+pb*viewPosY+pc*viewPosZ);
		
		//INITIALISE FOV CLIPPING PLANE (HORIZONTAL)
		double pha=pb;
		double phb=-pa;
		double phc=0;
		if(targetX==viewPosX && targetY==viewPosY)
		{
			pha=1;
			phb=0;
		}
		double nh=this.norme(pha,phb,phc);
		pha/=nh;
		phb/=nh;
		phc/=nh;		
		double phd=-(pha*viewPosX+phb*viewPosY+phc*viewPosZ);
		
		//INITIALISE FOV CLIPPING PLANE (VERTICAL)
		double pva=phb*pc-phc*pb;
		double pvb=phc*pa-pha*pc;
		double pvc=pha*pb-phb*pa;
		double nv=this.norme(pva,pvb,pvc);
		pva/=nv;
		pvb/=nv;
		pvc/=nv;		
		double pvd=-(pva*viewPosX+pvb*viewPosY+pvc*viewPosZ);	
		/*
		System.out.println("p="+pa+","+pb+","+pc);	
		System.out.println("ph="+pha+","+phb+","+phc);	
		System.out.println("pv="+pva+","+pvb+","+pvc);
		*/
		//INITIALISE LEFT CLIP PLANE
		double pla=0.4142*pa+pha;
		double plb=0.4142*pb+phb;
		double plc=0.4142*pc+phc;
		double nl=this.norme(pla,plb,plc);
		pla/=nl;
		plb/=nl;
		plc/=nl;		
		double pld=-(pla*viewPosX+plb*viewPosY+plc*viewPosZ);
		/*
		System.out.println("ph="+pha+","+phb+","+phc);
		System.out.println("pl="+pla+","+plb+","+plc);
		System.out.println("pv="+pva+","+pvb+","+pvc);	
		*/
		
		//INITIALISE RIGHT CLIP PLANE
		double pra=0.4142*pa-pha;
		double prb=0.4142*pb-phb;
		double prc=0.4142*pc-phc;
		double nr=this.norme(pra,prb,prc);
		pra/=nr;
		prb/=nr;
		prc/=nr;		
		double prd=-(pra*viewPosX+prb*viewPosY+prc*viewPosZ);		
		
		
		//INITIALISE BOTTOM CLIP PLANE
		double pba=0.4142*pa+pva;
		double pbb=0.4142*pb+pvb;
		double pbc=0.4142*pc+pvc;
		double nb=this.norme(pba,pbb,pbc);
		pba/=nb;
		pbb/=nb;
		pbc/=nb;		
		double pbd=-(pba*viewPosX+pbb*viewPosY+pbc*viewPosZ);
		
		//INITIALISE TOP CLIP PLANE
		double pta=0.4142*pa-pva;
		double ptb=0.4142*pb-pvb;
		double ptc=0.4142*pc-pvc;
		double nt=this.norme(pta,ptb,ptc);
		pta/=nt;
		ptb/=nt;
		ptc/=nt;		
		double ptd=-(pta*viewPosX+ptb*viewPosY+ptc*viewPosZ);				
		
		this.rGenerate(this.leftRootNode,viewPosX,viewPosY,viewPosZ,pa,pb,pc,pd,pla,plb,plc,pld,pra,prb,prc,prd,pba,pbb,pbc,pbd,pta,ptb,ptc,ptd,focus,screenWidth);
		this.rGenerate(this.rightRootNode,viewPosX,viewPosY,viewPosZ,pa,pb,pc,pd,pla,plb,plc,pld,pra,prb,prc,prd,pba,pbb,pbc,pbd,pta,ptb,ptc,ptd,focus,screenWidth);
		this.generatePostProcess();
		//System.out.println(nbGenerateFace);
	}

	public void setFarClip(double farClip)
	{
		this.generateClipFar=farClip;
	}
	
	public void setNearClip(double nearClip)
	{
		this.generateClipNear=nearClip;
	}	
	
	/**
	 * Remove ressource used for tesselation
	 */
	private void removeHeightMap()
	{
		this.lVariance=null;
		this.rVariance=null;
		this.hMap=null;
	}
	
	/**
	 * Create an store an array with all tesselated vertices
	 */		
	private void createTesselateVertex3DArray()
	{
		Vertex3D result[]=new Vertex3D[this.nbTesselateVertices];
		int nbv=this.nbTesselateVertices;
		for(VertexNode v=this.firstTesselateVertexNode;v!=null;v=v.nextTesselateVertexNode)
		{
			Vertex3D vert=new Vertex3D(v.x,v.y,v.z);
			result[--nbv]=vert;
		}
		this.tesselateVertexArray=result;
	}
	
	
	private void rCreateTesselateFace3DArray(FaceNode node)
	{
		if(node==null)
			return;
		Face3D f=node.tesselatedFaceList.face;	
		f.p0=this.tesselateVertexArray[node.p0];
		f.p1=this.tesselateVertexArray[node.p1];
		f.p2=this.tesselateVertexArray[node.p2];
		f.setNormal();
		this.rCreateTesselateFace3DArray(node.leftChild);					
		this.rCreateTesselateFace3DArray(node.rightChild);	
	}	
	
	/**
	 * Create an store an array with all tesselated faces
	 */	
	private void createTesselateFace3DArray()
	{
		this.rCreateTesselateFace3DArray(this.leftRootNode);					
		this.rCreateTesselateFace3DArray(this.rightRootNode);		
		
		this.tesselateFaceArray=new Face3D[this.nbTesselateFace];
		int numF=0;
		for(Face3DList fl=this.tesselatedFaceList;fl!=null;fl=fl.nextFaceList)
		{
			Face3D f=fl.face;
			this.tesselateFaceArray[numF++]=f;
		}
		
		for(int n=0;n<this.tesselateFaceArray.length;n++)
		{
			Face3D f=this.tesselateFaceArray[n];
			Vertex3D p0=f.p0;
			Vertex3D p1=f.p1;
			Vertex3D p2=f.p2;
			f.u0=(float)p0.x;
			f.u1=(float)p1.x;
			f.u2=(float)p2.x;			
			f.v0=(float)p0.y;
			f.v1=(float)p1.y;
			f.v2=(float)p2.y;							
		}
		
		
		for(int n=0;n<this.tesselateVertexArray.length;n++)
		{
			Vertex3D v=this.tesselateVertexArray[n];
			v.x-=this.xMin;
			v.x/=this.xMax-this.xMin;
			v.x*=this.size3DX;
			v.x+=this.min3DX;
			v.y-=this.yMin;
			v.y/=this.yMax-this.yMin;
			v.y*=this.size3DY;
			v.y+=this.min3DY;
			v.z*=this.size3DZ;
			v.z+=this.min3DZ;
			
		}
		
		for(int n=0;n<this.tesselateFaceArray.length;n++)
		{
			Face3D f=this.tesselateFaceArray[n];
			
			Vertex3D p0=f.p0;
			Vertex3D p1=f.p1;
			Vertex3D p2=f.p2;
			
			//f.sphereBoxSize=(float)this.norme((p0.x+p1.x)*0.5-p2.x,(p0.y+p1.y)*0.5-p2.y,(p0.z+p1.z)*0.5-p2.z);
			
			f.sphereBox=(float)this.norme(p0.x-p2.x,p0.y-p2.y,p0.z-p2.z);
			float distance12=(float)this.norme(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z);
			if(distance12>f.sphereBox)
				f.sphereBox=distance12;
			/*
			double v1x=p0.x-p2.x;
			double v1y=p0.y-p2.y;
			double v1z=p0.z-p2.z;
			double v2x=p1.x-p2.x;
			double v2y=p1.y-p2.y;
			double v2z=p1.z-p2.z;		
			double nz=v1x*v2y-v1y*v2x;
			double nx=v1y*v2z-v1z*v2y;
			double ny=v1z*v2x-v1x*v2z;
			double iLen=1.0/Math.sqrt(nx*nx+ny*ny+nz*nz);
			nx*=iLen;
			ny*=iLen;
			nz*=iLen;
			
			if(nz<0) {nx=-nx;ny=-ny;nz=-nz;}
			f.nx=(float)nx;
			f.ny=(float)ny;
			f.nz=(float)nz;
			*/
			
		}		
	}	

	/**
	 * Private class to store vertex list
	 */
	private class VertexNode
	{
		private VertexNode nextTesselateVertexNode;
		private double x;	//Vertex x
		private double y;	//Vertex y
		private double z;	//Vertex z		
	}

	/**
	 * Allocate,initialise and store in vertex list a new vertex
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return vertex index in list
	 */
	private int newVertexNode(double x,double y,double z)
	{
		VertexNode v=new VertexNode();
		v.x=x;
		v.y=y;
		v.z=z;
		this.pushTesselateVertexNode(v);
		return this.nbTesselateVertices++;
	}
	
	/**
	 * FaceNode class to store tesselation tree, and face list (tesselated and generated)
	 */			
	private class FaceNode
	{
	   	FaceNode leftChild; 			//Left Child FaceNode
    	FaceNode rightChild; 			//Right Child FaceNode
    	FaceNode base; 					//Base FaceNode (neighbors)
    	FaceNode left; 					//Left FaceNode (neighbors)
    	FaceNode right; 				//Right FaceNode (neighbors)
    	FaceNode parent;				//Parent FaceNode
		Face3DList tesselatedFaceList;	//Tesselated face List
    	int idGenerate;	    		//Last generation id
    	int idGenerateSplit;	   		//Last generation split id
    	double variance;				//Node variance		
    	int p0;
    	int p1;
    	int p2;
	}
	
	/**
	 * Evaluate length of a vector (double inputs)
	 * @param x vector component x
	 * @param y vector component y
	 * @param z vector component z
	 * @return vector length
	 */	 
	private double norme(double x,double y,double z)
	{
		return Math.sqrt(x*x+y*y+z*z);
	}	
		
	/**
	 * Compute variance trees (left & right),this is needed for tesselation process
	 */
	private void computeHeightMapVariance()
	{
	    this.rComputeVariance(this.xMin,this.yMax,this.hMap.getAt(0,this.yMax),this.xMax,this.yMin,this.hMap.getAt(this.xMax,0),this.xMin,this.yMin,this.hMap.getAt(0,0),1,this.lVariance,1.0);	
	    this.rComputeVariance(this.xMax,this.yMin,this.hMap.getAt(this.xMax,0),this.xMin,this.yMax,this.hMap.getAt(0,this.yMax),this.xMax,this.yMax,this.hMap.getAt(this.xMax,this.yMax),1,this.rVariance,1.0);		    
	}

	/**
	 * Initialise this Grid3D with a min and max tesselation level	
	 * @param minVarianceDepth minimum variance tree depth 
	 * @param maxVarianceDepth maximum variance tree depth
	 */
	private void init(int minVarianceDepth,int maxVarianceDepth)
	{
		this.hMap=null;
		this.leftRootNode=new FaceNode();
		this.rightRootNode=new FaceNode();	
		this.idGenerate=0;	
		this.nbGenerateFace=0;
		this.nbTesselateFace=0;
		this.nbTesselateVertices=0;
		this.tesselateVertexArray=null;
		this.tesselateFaceArray=null;
		this.tesselatedFaceList=null;	
		this.generatedFaceList=null;		
		this.firstTesselateVertexNode=null;
		this.maxVarianceDepth=maxVarianceDepth;
		this.minVarianceDepth=minVarianceDepth;
		this.lVariance=new double[1<<this.maxVarianceDepth];
		this.rVariance=new double[1<<this.maxVarianceDepth];
		this.generateClipNear=Double.MIN_VALUE;
		this.generateClipFar=Double.MAX_VALUE;
	}	
		
	/**
	 * Reset Tesselation
	 */
	private void resetTesselate()
	{
		this.leftRootNode.left=null;	
		this.leftRootNode.right=null;
		this.leftRootNode.base=null;
		this.leftRootNode.leftChild=null;
		this.leftRootNode.rightChild=null;
		this.rightRootNode.left=null;	
		this.rightRootNode.right=null;
		this.rightRootNode.base=null;
		this.rightRootNode.leftChild=null;
		this.rightRootNode.rightChild=null;
		this.rightRootNode.base=null;
		this.leftRootNode.base=null;			
		this.rightRootNode.base=this.leftRootNode;
		this.leftRootNode.base=this.rightRootNode;			
	}

	/**
	 * Allocate a new FaceNode  
	 * @return allocated and initialized FaceNode
	 */
	private FaceNode newFaceNode()
	{	
		FaceNode nNode=new FaceNode();
		nNode.left=null;	
		nNode.right=null;
		nNode.base=null;
		nNode.leftChild=null;
		nNode.rightChild=null;
		nNode.idGenerate=-1;
		nNode.idGenerateSplit=-1;
		nNode.parent=null;
		return nNode;
	}
	
	/**
	 * Push a FaceNode in the list of generated faces
	 * @param FaceNode face node to add
	 */ 
	private void pushGenerateFace(FaceNode faceNode)
	{
		this.nbGenerateFace++;	
		faceNode.tesselatedFaceList.nextFaceList=this.generatedFaceList;
		this.generatedFaceList=faceNode.tesselatedFaceList;
	}

	private void pushGenerateCollisionFace(FaceNode faceNode)
	{
		this.nbGenerateCollisionFace++;	
		faceNode.tesselatedFaceList.nextFaceList=this.generatedCollisionFaceList;
		this.generatedCollisionFaceList=faceNode.tesselatedFaceList;
	}
	
	/**
	 * Push a FaceNode in the list of tesselated faces
	 * @param FaceNode face node to add
	 */ 
	private void pushTesselateFace(FaceNode faceNode)
	{
		this.nbTesselateFace++;
		faceNode.tesselatedFaceList.nextFaceList=this.tesselatedFaceList;
		this.tesselatedFaceList=faceNode.tesselatedFaceList;
	}	
		
	/**
	 * Push a VertexNode in the list of VertexNode
	 * @param VertexNode to add
	 */ 
	private void pushTesselateVertexNode(VertexNode v)
	{
		v.nextTesselateVertexNode=this.firstTesselateVertexNode;	
		this.firstTesselateVertexNode=v;
	}	
		
	/**
	 * Split a FaceNode
	 * @param node the FaceNode to split
	 */
	private void split(FaceNode node)
	{	
		
		if(node.leftChild != null)	//Already split ? return
			return;
			
		node.leftChild  = this.newFaceNode();	//Allocate new left child
		node.rightChild = this.newFaceNode();	//Allocate new right child
	
		if (node.rightChild == null)			//Allocation problem? return
		{
			node.leftChild=null;
			return;
		}
					
		node.leftChild.parent=node;
		node.rightChild.parent=node;
			
		if((node.base != null) && (node.base.base != node))	//Base must be split? split base
			this.split(node.base);
			
		//Initialise new child  	
		node.leftChild.base  = node.left;	
		node.leftChild.left  = node.rightChild;
		node.rightChild.base  = node.right;
		node.rightChild.right = node.leftChild;

		//Modify left FaceNode neighbor
		if(node.left != null)
		{
			if (node.left.base == node)
				node.left.base = node.leftChild;
			else
			{
				if(node.left.left == node)
					node.left.left = node.leftChild;
				else 
					if(node.left.right == node)
						node.left.right = node.leftChild;
			}
		}
	
		//Modify right FaceNode neighbor
		if(node.right != null)
		{
			if (node.right.base == node)
				node.right.base = node.rightChild;
			else	
			{			
				if(node.right.right == node)
					node.right.right = node.rightChild;
				else
					if(node.right.left == node)
						node.right.left = node.rightChild;
			}
		}
	
		//Modify base FaceNode neighbor
		if(node.base != null)
		{
			if(node.base.leftChild != null)
			{
				node.base.leftChild.right = node.rightChild;
				node.base.rightChild.left = node.leftChild;
				node.leftChild.right = node.base.rightChild;
				node.rightChild.left = node.base.leftChild;
			}
			else
				this.split(node.base);  
		}
		else
		{
			node.leftChild.right = null;
			node.rightChild.left = null;
		}		
	
	
	}
	
	private double rComputeVariance( double leftX,double leftY,double leftZ,double rightX, double rightY,double rightZ,double apexX,double apexY,double apexZ,int node,double varianceTree[],double factor)
	{
		if (node >= (1<<this.maxVarianceDepth)) return 0.0;
			
		double centerX = (leftX + rightX) *0.5;
		double centerY = (leftY + rightY) * 0.5;
		double centerZ  = this.hMap.getAt(centerX,centerY);
		double nodeCenterZ=(leftZ + rightZ)*0.5;
		double variance=Math.abs(centerZ - nodeCenterZ)*factor;
		
		variance=Math.max( variance, this.rComputeVariance( apexX,   apexY,  apexZ, leftX, leftY, leftZ, centerX, centerY, centerZ,node<<1,varianceTree,factor*0.5));
		variance=Math.max( variance, this.rComputeVariance(  rightX, rightY, rightZ, apexX, apexY, apexZ, centerX, centerY, centerZ,1+(node<<1),varianceTree,factor*0.5));			
		varianceTree[node]=variance;
		return variance;
	}

	/**
	 * Generate post-process
	 * fill generate stack with generated face nodes
	 * automatically called after each call to generate()
	 */
	private void generatePostProcess()
	{
		this.generatedFaceList=null;
		this.nbGenerateFace=0;		
		this.rGeneratePostProcess(this.leftRootNode);
		this.rGeneratePostProcess(this.rightRootNode);
	}
	
	private void rGeneratePostProcess(FaceNode node)
	{
		if(node==null) return;
		
		if (node.idGenerateSplit==this.idGenerate) 					
		{	
			this.rGeneratePostProcess( node.leftChild);
			this.rGeneratePostProcess( node.rightChild);
			return;
		}
		this.pushGenerateFace(node);
		
	}

	private void rGenerateCollision(FaceNode node,double dx,double dy,double dz,double tx,double ty,double tz,double radius,double moveNorme)
	{
		Face3D f=node.tesselatedFaceList.face;
		Vertex3D p0=f.p0;
		Vertex3D p1=f.p1;
		Vertex3D p2=f.p2;
		double leftX=p0.x;
		double leftY=p0.y;
		double leftZ=p0.z;
		double rightX=p1.x;
		double rightY=p1.y;
		double rightZ=p1.z;
		double apexX=p2.x;
		double apexY=p2.y;
		double apexZ=p2.z;	
		double centerX = apexX;
		double centerY = apexY;
		double centerZ=  apexZ;
		double size=f.sphereBox;
		
		//TEST IF FACE/SUB FACE CAN BE HIT
		double moveSphereSize=radius+moveNorme+size;
		double dstx=centerX-dx;
		double dsty=centerY-dy;
		double dstz=centerZ-dz;
		if((dstx*dstx+dsty*dsty+dstz*dstz)>(moveSphereSize*moveSphereSize))
			return;		
		
		if (node.leftChild != null)					
		{
			this.rGenerateCollision( node.leftChild,dx,dy,dz,tx,ty,tz,radius,moveNorme);
			this.rGenerateCollision( node.rightChild,dx,dy,dz,tx,ty,tz,radius,moveNorme);
		}
		else
		{
			this.pushGenerateCollisionFace(node);
		}
	}	
		
	private void rGenerate(FaceNode node,double viewPosX,double viewPosY,double viewPosZ,double pa,double pb,double pc,double pd,double pla,double plb,double plc,double pld,double pra,double prb,double prc,double prd,double pba,double pbb,double pbc,double pbd,double pta,double ptb,double ptc,double ptd,double focus,double screenWidth)
	{
		
		Face3D f=node.tesselatedFaceList.face;
		Vertex3D p0=f.p0;
		Vertex3D p1=f.p1;
		Vertex3D p2=f.p2;
		double leftX=p0.x;
		double leftY=p0.y;
		double leftZ=p0.z;
		double rightX=p1.x;
		double rightY=p1.y;
		double rightZ=p1.z;
		double apexX=p2.x;
		double apexY=p2.y;
		double apexZ=p2.z;	
/*
		double centerX = (leftX + rightX) *0.5;
		double centerY = (leftY + rightY) *0.5;
		double centerZ=  (leftZ + rightZ) *0.5;					
*/		
		double centerX = apexX;
		double centerY = apexY;
		double centerZ=  apexZ;

		double size=f.sphereBox;
		double clipZ=pa*centerX+pb*centerY+pc*centerZ+pd;
		
		if(clipZ<(this.generateClipNear-size))
		 return;
		 
		 
		if(clipZ>(this.generateClipFar+size))
		 return;
		 
		double clipL=pla*centerX+plb*centerY+plc*centerZ+pld;
		
		if(clipL<-size)
		 return;
		
		double clipR=pra*centerX+prb*centerY+prc*centerZ+prd;
		
		if(clipR<-size)
		 return;
		  
		 
		double clipB=pba*centerX+pbb*centerY+pbc*centerZ+pbd;
		
		if(clipB<-size)
		 return;	 		 
		
		
		double clipT=pta*centerX+ptb*centerY+ptc*centerZ+ptd;
		
		if(clipT<-size)
		 return;	 		 
		 
		
		double corres=f.pa*pa+f.pb*pb+f.pc*pc; 
		//corres=(corres>=0)?corres:-corres;
		//if(corres>0.7) return;
		corres=(corres>=0.0)?corres:-corres;
		
		corres=1.0-corres;
		 
		//corres=corres*0.5+0.5;

		 //corres=1-corres;
		//corres=0.5+corres*0.5;
		double distance=clipZ;	
	
		if(distance<size && distance>-size)
		distance=0.0;
		
		double variance=0;
		if(distance==0.0) 
			variance=Double.MAX_VALUE;			
		else 
			variance=size3DZ*corres*node.variance/distance;
		//	variance=corres*size3DZ*node.variance/(distance*distance);
			
			
			
			
			//variance=node.variance*corres*(size/size3DZ)/distance;
			//variance=node.variance*corres/distance;
			//variance=node.variance/distance;
			//variance=focus*node.variance*size3DZ/(distance);
			//variance=focus*node.variance*(size/size3DZ)*corres/(distance);
			//variance=node.variance*corres/distance;
		
		if (node.leftChild != null && (variance>this.generateVariance))					
		{
			this.rGenerate( node.leftChild,viewPosX,viewPosY,viewPosZ,pa,pb,pc,pd,pla,plb,plc,pld,pra,prb,prc,prd,pba,pbb,pbc,pbd,pta,ptb,ptc,ptd,focus,screenWidth);
			this.rGenerate( node.rightChild,viewPosX,viewPosY,viewPosZ,pa,pb,pc,pd,pla,plb,plc,pld,pra,prb,prc,prd,pba,pbb,pbc,pbd,pta,ptb,ptc,ptd,focus,screenWidth);
		}
		else
			this.updateIdGenerateSplit(node.parent);	
	}	
	
	private void updateIdGenerateSplit(FaceNode node)
	{
		if(node==null) return;
		if(node.idGenerateSplit==this.idGenerate) return;
		FaceNode parent=node;
		
		if(parent.leftChild==null)
			parent=parent.parent;
			
		while(parent!=null && parent.idGenerateSplit!=this.idGenerate)
		{
			parent.idGenerateSplit=this.idGenerate;
			if(parent.base!=null) updateIdGenerateSplit(parent.base);
			parent=parent.parent;
		}
	}
	
	/**
	 * Tesselate post-process
	 * Initialise tesselated face nodes
	 * Automatically called after tesselation to update face nodes parameters
	 */
	private void tesselatePostProcess()
	{
		this.nbTesselateVertices=0;
		this.firstTesselateVertexNode=null;		
		this.nbTesselateFace=0;		
		this.tesselatedFaceList=null;
		this.rTesselatePostProcess(this.leftRootNode,this.xMin,this.yMax,this.xMax,this.yMin,this.xMin,this.yMin,1,-1,-1,-1,this.lVariance);					
		this.rTesselatePostProcess(this.rightRootNode,this.xMax,this.yMin,this.xMin,this.yMax,this.xMax,yMax,1,-1,-1,-1,this.rVariance);	
	}	
		
	private void rTesselatePostProcess(FaceNode nodeRef,double leftX,double leftY,double rightX,double rightY,double apexX,double apexY,int node,int v0,int v1,int v2,double varianceTree[])
	{
		double variance = 0;
		if (node < (1<<this.maxVarianceDepth)) 
			variance = varianceTree[node];

		double leftZ=this.hMap.getAt(leftX,leftY);
		double rightZ=this.hMap.getAt(rightX,rightY);
		double apexZ=this.hMap.getAt(apexX,apexY);
		double centerX = (leftX + rightX) *0.5;
		double centerY = (leftY + rightY) *0.5;						
		double centerZ = (leftZ + rightZ) *0.5;
		
		Face3D f=new Face3D();
		if(v0==-1) v0=this.newVertexNode(leftX,leftY,leftZ);
		if(v1==-1) v1=this.newVertexNode(rightX,rightY,rightZ);
		if(v2==-1) v2=this.newVertexNode(apexX,apexY,apexZ);
		
		nodeRef.p0=v0;
		nodeRef.p1=v1;
		nodeRef.p2=v2;	
		nodeRef.variance=variance;
		nodeRef.tesselatedFaceList=new Face3DList();
		nodeRef.tesselatedFaceList.face=f;				
		
		this.pushTesselateFace(nodeRef);			
	
		if (nodeRef.leftChild != null)
		{
			int newV=this.newVertexNode(centerX,centerY,this.hMap.getAt(centerX,centerY));
			this.rTesselatePostProcess( nodeRef.leftChild, apexX,  apexY, leftX, leftY, centerX, centerY,node<<1,v2,v0,newV,varianceTree);
			this.rTesselatePostProcess( nodeRef.rightChild, rightX, rightY, apexX, apexY, centerX, centerY, 1+(node<<1),v1,v2,newV,varianceTree);
		}
	}
		
	private void rTessellate(FaceNode nodeRef,double leftX,double leftY,double rightX,double rightY,double apexX,double apexY,int node,double varianceTree[])
	{	
		if (node >= (1<<this.maxVarianceDepth)) return;
		
		double variance = varianceTree[node];
		
		
		if ((node <= (1<<this.minVarianceDepth)) || (variance > this.tesselateVariance && (variance > 0.0)) )	
		{
			
			if(nodeRef.leftChild == null)
					this.split(nodeRef);
				
			if ( nodeRef.leftChild != null)
			{	
				double centerX = (leftX + rightX) *0.5;
				double centerY = (leftY + rightY) *0.5;																		
				this.rTessellate( nodeRef.leftChild, apexX,  apexY, leftX, leftY, centerX, centerY,    node<<1,varianceTree);
				this.rTessellate( nodeRef.rightChild, rightX, rightY, apexX, apexY, centerX, centerY, 1+(node<<1),varianceTree);
			}
				
		}	
		
	}		
	

}