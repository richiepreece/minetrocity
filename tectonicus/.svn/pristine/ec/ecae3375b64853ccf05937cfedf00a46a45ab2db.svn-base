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
import net.dzzd.DzzD;

import java.awt.*;


public final class Render3DSW extends Render3D
{	
	//Render size related variables
	private int 		renderPixelWidth;		//Rendered image width in pixel
	private int 		renderPixelHeight;		//Rendered image height in pixel
	private double 		renderPixelWidthDiv2;	//Rendered image width in pixel divided by 2
	private double 		renderPixelHeightDiv2;	//Rendered image height in pixel divided by 2
	
	//Buffers	 
	private int zBuffer[];			//Segment zBuffer
	private double zBufferIzMin[];	//Segment zBuffer Z min
	private double zBufferIzInc[];	//Segment zBuffer Z max
	private int zBufferO[];			//Object ID buffer related to segment zBuffer 
	private int zBufferP[];			//Face ID buffer related to segment zBuffer
	private int zBufferF[];			//Face ID segment buffer used for drawing and made with zBuffer once all faces have been past to zBuffer
	//private int zBufferFA[];		//Face ID segment buffer used for drawing and made with zBufferAlpha once all faces have been past to zBufferAlpha
	
	//
	private double noFaceZ;
	private double noFaceIZ;
	private double noFaceX1;
	private int noFaceY1;
	private double noFaceX2;
	private int noFaceY2;

	//Rendered image information
	private int nbRenderedFace;		//Number of rendered faces (with at least one visible pixel on rendered frame) 
	private int nbRenderedMesh;		//Number of rendered meshes (with at least one visible pixel on rendered frame) 

	//Face to scanline converter variables
	private int vLinesBufferMin[];
	private double vLinesBufferMinX[];
	private double vLinesBufferMinIncX[];
	private double vLinesBufferMinIz[];
	private double vLinesBufferMinIncIz[];
	
	private int vLinesBufferMax[];
	private double vLinesBufferMaxX[];
	private double vLinesBufferMaxIncX[];
	private double vLinesBufferMaxIz[];
	private double vLinesBufferMaxIncIz[];
	
	private int hLinesBufferMin;				//Minimum Y value found for current face scanline conversion
	private int hLinesBufferMax;				//Maximum Y value found for current face scanline conversion
	
   
	private int zBufferMode;						//Mode de fonctionnement du zBuffer


	final static int ZB_WRITE = 1;	
	final static int ZB_ALPHA = 2;
	final static int ZB_TEST = 4;
	
	Drawer drawer;
	
	int lastRenderBackgroundOffset;		//Background last render offset
	int firstRenderBackgroundOffset;	//Background first render offset
	
	
	Face3D firstAlphaFace;
    Face3D lastAlphaFace;
    
    private CompiledMesh firstMeshToRender;
    private CompiledMesh compiledMeshes[];
    private CompiledMaterial compiledMaterials[];
    
    class CompiledFace
    {
    	Face3D face;
    	CompiledFace nextFaceToRender;
    	CompiledMaterial compiledMaterial;
    	int lastRenderImage;
    	int firstRenderOffset;
    	int lastRenderOffset;
    	int lastZbufferImage;
    	
    	
    	CompiledFace(Face3D face)
    	{
    		this.face=face;
    		this.nextFaceToRender=null;
    		this.lastRenderImage=-1;
    		this.firstRenderOffset=-1;
    		this.lastRenderOffset=-1;
    		this.lastZbufferImage=-1;
    		if(face.material!=null)
    			this.compiledMaterial=compiledMaterials[face.material.id];
    		else
    			this.compiledMaterial=compiledMaterials[0];
    	}
    }
    
    class CompiledMesh3DOctree
    {
    	int lastVisibleImage;
    	Mesh3DOctree tree;
    	
    	CompiledMesh3DOctree(Mesh3DOctree tree)
    	{
    		this.tree=tree;
    		this.lastVisibleImage=-1;
    	}
    	
    	
    }
    
	class CompiledMesh
	{
		Mesh3D mesh;
		CompiledMesh nextMeshToRender;
		int lastRenderImage;	
		int nbRenderFace;
		
		CompiledFace compiledFaces[];		
		CompiledFace firstFaceToRender;
		CompiledMesh3DOctree compiledMesh3DOctrees[];
		
		CompiledMesh(Mesh3D mesh)
		{
			this.mesh=mesh;
			this.nextMeshToRender=null;
			this.nbRenderFace=0;
			this.lastRenderImage=-1;
			this.compiledFaces=new CompiledFace[mesh.getNbFace3D()];
			this.compiledMesh3DOctrees=null;
			for(int n=0;n<mesh.getNbFace3D();n++)
				this.compiledFaces[n]=new CompiledFace((Face3D) mesh.getFace3D(n));
			
			if(mesh.getMesh3DOctree()!=null)
			{
				int nbChildrens=1+mesh.octree.getNbChildren(true);
				this.compiledMesh3DOctrees=new CompiledMesh3DOctree[nbChildrens];
				IMesh3DOctree m[]=mesh.octree.getMesh3DOctreeArray(new Mesh3DOctree[nbChildrens]);
				for(int x=0;x<nbChildrens;x++)
					this.compiledMesh3DOctrees[x]=new CompiledMesh3DOctree((Mesh3DOctree)m[x]);
				
			}
		}
		
	}	
	
	class CompiledMaterial
	{
		private static final int MAT_SIZE=1024;
		Material material;
		int specularLightMap[];
		
		CompiledMaterial(Material material)
		{
			this.material=material;
			this.specularLightMap=new int[MAT_SIZE];
			this.initSpecularLightMap();
		}
		
		public void initSpecularLightMap()
		{	
			//int matSize=MAT_SIZE>>1;
			for(int n=0;n<MAT_SIZE;n++)
			{
				//int cos=n-(MAT_SIZE>>2);
				//if (cos<0) cos=0;
				//if (cos>(MAT_SIZE>>1)) cos=MAT_SIZE>>1;
				//double lightdouble=0.8+1.2*((double)(cos-(MAT_SIZE>>2)))/(MAT_SIZE>>1);
					
				//double lightdouble=2.0*((double)(cos-(MAT_SIZE>>2)))/(MAT_SIZE>>1);	
				
				//TODO OOOOOOOOOOOOOOO
				//int cos=n;//Math.abs(n-(MAT_SIZE>>1));
				
				int sin=Math.min(n-(MAT_SIZE>>1),(MAT_SIZE>>2));
				
				double lightdouble=1.0*((double)sin)/(MAT_SIZE>>2);
				//System.out.println(n+"  "+ sin +" "+lightdouble);
				
				
				if(lightdouble<0.0) lightdouble=0.0;
				if(lightdouble>1.0) lightdouble=1.0; 
				
				int light=((int)(lightdouble*255.0));
				light+=(this.material.selfIlluminationLevel*255)/100;
				if(light>255)
				light=255;
				if(this.material.specularLevel!=0)
				{				
					double redSpecularD=(this.material.specularColor&0xFF0000)>>16;				
					double greenSpecularD=(this.material.specularColor&0xFF00)>>8;
					double blueSpecularD=(this.material.specularColor&0xFF);
					int specularLight=specularLight=(int)(Math.pow(lightdouble,this.material.specularPower)*this.material.specularLevel);
				
					redSpecularD*=specularLight/256.0;
					greenSpecularD*=specularLight/256.0;
					blueSpecularD*=specularLight/256.0;
		
					int redSpecular=(int)redSpecularD;		
					int greenSpecular=(int)greenSpecularD;		
					int blueSpecular=(int)blueSpecularD;			
		
					if(redSpecular<0) redSpecular=0;		
					if(greenSpecular<0) greenSpecular=0;		
					if(blueSpecular<0)	blueSpecular=0;			
		
					if(redSpecular>255)		redSpecular=255;		
					if(greenSpecular>255)  	greenSpecular=255;		
					if(blueSpecular>255)	blueSpecular=255;
		
					this.specularLightMap[n]=(light<<24)|(((redSpecular<<16)|(greenSpecular<<8)|(blueSpecular))&0xFEFEFE);
				}
				else
					this.specularLightMap[n]=light<<24;
			}		
		}

	}
	
	
	public Render3DSW()
	{
		super();
		this.directInput=new DirectInput(this.canvas);
		this.drawer=new Drawer();
		this.initCompiledBuffers();
	}
	
	private final void initCompiledBuffers()
	{
		this.compiledMeshes=new CompiledMesh[65536];
		this.compiledMaterials=new CompiledMaterial[1024];		
		this.firstMeshToRender=null;
		this.firstRenderBackgroundOffset=-1;
		this.resetBuffers();
	}
	
	public final void clearScene(IScene scene)
	{
		super.clearScene(scene);
		this.initCompiledBuffers();		
	}
	
	protected final void disposeMesh3D(IMesh3D mesh)
	{
		//Log.log(this.getClass(),"Dispose Mesh3D (SOFTWARE) " + mesh.getName());
		int n=mesh.getId();
		CompiledMesh nextCompiledMesh=this.compiledMeshes[n+1];
		do
		{
			this.compiledMeshes[n++]=nextCompiledMesh;
			nextCompiledMesh=this.compiledMeshes[n+1];
		}
		while(nextCompiledMesh!=null);
	}
	
	protected final void disposeCamera3D(ICamera3D camera)
	{
		//Log.log(this.getClass(),"Dispose Camera3D (SOFTWARE) : " + camera.getName());
	}	
	
	protected final void disposeLight3D(ILight3D light)
	{
		//Log.log(this.getClass(),"Dispose Light3D (SOFTWARE) : " + light.getName());
	}		
	
	protected final void disposeTexture(ITexture texture)
	{
		//Log.log(this.getClass(),"Dispose ITexture (SOFTWARE) : " + texture.getName());
	}			
	
	protected final void disposeMaterial(IMaterial material)
	{
		//Log.log(this.getClass(),"Dispose Material (SOFTWARE) : " + material.getName());
		int n=material.getId();
		CompiledMaterial nextCompiledMaterial=this.compiledMaterials[n+1];
		do
		{
			this.compiledMaterials[n++]=nextCompiledMaterial;
			nextCompiledMaterial=this.compiledMaterials[n+1];
		}
		while(nextCompiledMaterial!=null);
	}				
	
	protected final void compileMesh3D(IMesh3D mesh)
	{
		super.compileMesh3D(mesh);
		//Log.log("Compile Mesh3D (Software):" + mesh.getName());
		this.compiledMeshes[mesh.getId()]=new CompiledMesh((Mesh3D)mesh);		
	}
	
	protected final void compileMaterial(IMaterial material)
	{
		super.compileMaterial(material);
		//Log.log(this, "Compile Material (Software):" + material.getName());
		CompiledMaterial m=new CompiledMaterial((Material)material);
		//m.initSpecularLightMap();
		this.compiledMaterials[material.getId()]=m;
	}

	public final void setSize(int viewPixelWidth,int viewPixelHeight,int maxAntialias)
	{
		super.setSize(viewPixelWidth,viewPixelHeight,maxAntialias);

		this.renderPixelWidth=this.viewPixelWidth;
		this.renderPixelHeight=this.viewPixelHeight;
		
		if((this.antialias&2)!=0)	
			this.renderPixelWidth<<=1;
			
		if((this.antialias&4)!=0)				
			this.renderPixelHeight<<=1;	
			
		this.minXValue=0;
		this.maxXValue=this.renderPixelWidth;
		this.minYValue=0;	
		this.maxYValue=this.renderPixelHeight-1;	
		this.renderPixelWidthDiv2=this.renderPixelWidth>>1;
		this.renderPixelHeightDiv2=this.renderPixelHeight>>1;
	
		this.initBuffers();
		this.resetBuffers();		
	}
	
	protected final void startFrame(IScene3D scene)
	{
		this.drawer.setRender3DSW(this);
		this.resetBuffers();
		
	}
	
	private final void renderFrameCoherence(IScene3D scene)
	{
		this.zBufferTest=false;
		//int nb=0;
		CompiledMesh compiledMesh=this.getFirstMeshToRender();
	
		while(compiledMesh!=null)
		{
			Mesh3D mesh=compiledMesh.mesh;
			if(mesh.isVisible())
			{
				if((mesh.renderMode&DzzD.RM_LIGHT)!=0)
					this.prepareMesh3DLocalLight3DBuffer(scene,mesh);
				
				if(mesh.getMesh3DViewGenerator()==null)
				{
					this.setCurrentMesh3D(mesh);
					CompiledFace compiledFace=compiledMesh.firstFaceToRender;
					while(compiledFace!=null)
					{
						this.setFaces3DToZBuffer(mesh.faces3D,compiledFace.face.id);
						//nb++;
						compiledFace.lastZbufferImage=this.numImage;
						compiledFace=compiledFace.nextFaceToRender;
					}
				}
			}
			compiledMesh=this.getNextMeshToRender(compiledMesh);
		}
		//Log.log(this.getClass(),"Coherence="+nb);
		this.zBufferTest=true;		
	}

	protected final void renderFrame(IScene3D scene)
	{
		this.zBufferMode = ZB_WRITE;
		this.firstAlphaFace=null;
		this.lastAlphaFace=null;
		this.renderFrameCoherence(scene);
		super.renderFrame(scene);
	}	
	
	protected final void endFrame(IScene3D scene)
	{		
		if(!this.isPixelUpdateEnabled)
			return;
			
		this.prepareFaceBuffer(scene);

		if(scene.isBackgroundEnabled())
			this.renderBackground(scene);
		
		this.renderMesh3D(scene);
		
		this.drawer.antialiasPixels();
		
		if(this.isScreenUpdateEnabled)
			this.drawer.drawPixelsOnCanvas(this.canvas);
	}
	
	private final void initBuffers()
	{					
		this.zBuffer=new int[this.renderPixelHeight*this.renderPixelWidth];
		this.zBufferIzMin=new double[this.renderPixelHeight*this.renderPixelWidth];
		this.zBufferIzInc=new double[this.renderPixelHeight*this.renderPixelWidth];	
		this.zBufferO=new int[this.renderPixelHeight*this.renderPixelWidth];
		this.zBufferP=new int[this.renderPixelHeight*this.renderPixelWidth];
		this.zBufferF=new int[this.renderPixelHeight*this.renderPixelWidth];
		//this.zBufferFA=new int[this.renderPixelHeight*this.renderPixelWidth];
		
		this.vLinesBufferMin=new int[this.renderPixelHeight];
		this.vLinesBufferMinX=new double[this.renderPixelHeight];
		this.vLinesBufferMinIncX=new double[this.renderPixelHeight];
		this.vLinesBufferMinIz=new double[this.renderPixelHeight];
		this.vLinesBufferMinIncIz=new double[this.renderPixelHeight];
		
		this.vLinesBufferMax=new int[this.renderPixelHeight];
		this.vLinesBufferMaxX=new double[this.renderPixelHeight];
		this.vLinesBufferMaxIncX=new double[this.renderPixelHeight];
		this.vLinesBufferMaxIz=new double[this.renderPixelHeight];
		this.vLinesBufferMaxIncIz=new double[this.renderPixelHeight];
				
		this.drawer.setBuffers(this,this.zBuffer,this.zBufferF);//,this.zBufferFA);
		
	}
	
	private final void resetBuffers()
	{
		int yOfs=0;
		for(int y=0;y<this.renderPixelHeight;y++)
		{
			this.zBuffer[yOfs]=this.renderPixelWidth;
			this.zBufferIzMin[yOfs]=this.iZMax;
			this.zBufferIzInc[yOfs]=0.0;
			this.zBufferO[yOfs]=-1;
			this.zBufferP[yOfs]=-1;
			yOfs+=this.renderPixelWidth;
		}
		
	}	
	
	public final void setAntialiasLevel(int level)
	{
		if(level==this.antialias)
			return;
		
		this.firstMeshToRender=null;
		this.firstRenderBackgroundOffset=-1;
		super.setAntialiasLevel(level);
		
	}

	protected final void renderBackground(IScene3D scene)
	{
		if(this.firstRenderBackgroundOffset==-1)
			return;		
		this.drawer.drawBackground(this.firstRenderBackgroundOffset,this.lastRenderBackgroundOffset,scene.getBackgroundColor());
	
	}

	protected final void setMesh3DToZBuffer(IMesh3D mesh)
	{
		this.setFaces3DToZBuffer(mesh.getFaces3D(),-1);
	}
	
	protected final void setMesh3DOctreeToZBuffer(IMesh3DOctree tree)
	{
		this.setFaces3DToZBuffer(tree.getFaces3D(),-1);
	}	

	private boolean zBufferTest;
	
	protected final int isSphereVisible(double cx,double cy,double cz,double radius)
	{
		int visible=super.isSphereVisible(cx,cy,cz,radius);

		if(!this.zBufferTest || visible==0)
			return visible;
		/*	
		if(this.cMesh3DOctree!=null)
		{
			if((this.numImage-this.cMesh3DOctree.lastVisibleImage)<3)
				return 1;
		}
		else	
		{
			if((this.numImage-this.compiledMeshes[this.cMesh3D.id].lastVisibleImage)<3)
				return 1;
		}
		*/
		
		visible=isSphereVisibleInZBuffer(cx,cy,cz,radius);	
		/*
		if(visible==1)
			this.compiledMeshes[this.cMesh3D.id].lastVisibleImage=this.numImage;
			
		if(this.cMesh3DOctree!=null)
		{
			if(visible==1)
				this.cMesh3DOctree.lastVisibleImage=this.numImage;
				
		}
		*/
		return visible;	
	}
	
	private final int isSphereVisibleInZBuffer(double cx,double cy,double cz,double radius)
	{
		double cpx=ox+axx*cx+ayx*cy+azx*cz;	//Compute from object space to camera space
		double cpy=oy+axy*cx+ayy*cy+azy*cz;
		double cpz=oz+axz*cx+ayz*cy+azz*cz;	

		//IF IN SPHERE SET VISIBLE
		if(cpx*cpx+cpy*cpy+cpz*cpz<=(radius*radius))
			return 1;

		double pzmin=cpz-radius;
		if(pzmin<=this.zMin) return 1;
		
		
		double pzmax=cpz+radius;	
		double piZ=1.0/cpz;
		double piZmin=1.0/pzmin;	
		double piZmax=1.0/pzmax;
			
		//TODO: optimize on size remove if sizeY to large
		double sizeX=radius*this.screenZoomXFocus*piZmin;
		double sizeY=radius*this.screenZoomYFocus*piZmin;
		if(sizeX>renderPixelWidthDiv2 && sizeY>renderPixelHeightDiv2)	
			return 1;
		
			
		//Compute pxmin,pxmax,pymin,pymax
		int px1=(int)((cpx-radius)*this.screenZoomXFocus*piZmin+this.renderPixelWidthDiv2);
		int px2=(int)((cpx+radius)*this.screenZoomXFocus*piZmin+this.renderPixelWidthDiv2);
		int px3=(int)((cpx-radius)*this.screenZoomXFocus*piZmax+this.renderPixelWidthDiv2);
		int px4=(int)((cpx+radius)*this.screenZoomXFocus*piZmax+this.renderPixelWidthDiv2);		
		int py1=(int)((cpy-radius)*this.screenZoomXFocus*piZmin+this.renderPixelHeightDiv2);
		int py2=(int)((cpy+radius)*this.screenZoomXFocus*piZmin+this.renderPixelHeightDiv2);
		int py3=(int)((cpy-radius)*this.screenZoomXFocus*piZmax+this.renderPixelHeightDiv2);
		int py4=(int)((cpy+radius)*this.screenZoomXFocus*piZmax+this.renderPixelHeightDiv2);	
			
		int pxmin=px1;
		if(px2<pxmin) pxmin=px2;
		if(px3<pxmin) pxmin=px3;
		if(px4<pxmin) pxmin=px4;
		
		int pxmax=px1;
		if(px2>pxmax) pxmax=px2;
		if(px3>pxmax) pxmax=px3;
		if(px4>pxmax) pxmax=px4;
		
		int pymin=py1;
		if(py2<pymin) pymin=py2;
		if(py3<pymin) pymin=py3;
		if(py4<pymin) pymin=py4;
		
		int pymax=py1;
		if(py2>pymax) pymax=py2;
		if(py3>pymax) pymax=py3;
		if(py4>pymax) pymax=py4;
		
		if(this.minXValue>pxmax) return 0;
		if(this.maxXValue<pxmin) return 0;
		if(this.minYValue>pymax) return 0;
		if(this.maxYValue<pymin) return 0;		
		
		if(this.minXValue>pxmin) pxmin=this.minXValue;
		if(this.maxXValue<pxmax) pxmax=this.maxXValue; 
		if(this.minYValue>pymin) pymin=this.minYValue; 
		if(this.maxYValue<pymax) pymax=this.maxYValue;
		
		return isSquareVisibleInZBuffer(pxmin,pymin,pxmax,pymax,piZmin);
	}
	
	private final int isSquareVisibleInZBuffer(int pxmin,int pymin,int pxmax,int pymax,double piZ)
	{		
		this.hLinesBufferMin=pymin;
		this.hLinesBufferMax=pymax;
		
		this.vLinesBufferMin[pymin]=pymax;
		this.vLinesBufferMinX[pymin]=pxmin;
		this.vLinesBufferMinIncX[pymin]=0.0;
		this.vLinesBufferMinIz[pymin]=piZ;
		this.vLinesBufferMinIncIz[pymin]=0.0;
		
		this.vLinesBufferMax[pymin]=pymax;
		this.vLinesBufferMaxX[pymin]=pxmax;
		this.vLinesBufferMaxIncX[pymin]=0.0;
		this.vLinesBufferMaxIz[pymin]=piZ;
		this.vLinesBufferMaxIncIz[pymin]=0.0;


		int zBufferMode=this.zBufferMode;
		this.zBufferMode=ZB_TEST;
		int result=pasteHLine(null);
		
	/*
		Graphics g=this.canvas.getGraphics();
		if(result==0)
		{
		
			g.setColor(Color.RED);
			g.drawRect(pxmin,renderPixelHeight-pymax,pxmax-pxmin,pymax-pymin);
		}/*
		else
			g.setColor(Color.BLUE);
			
		g.drawRect(pxmin,renderPixelHeight-pymax,pxmax-pxmin,pymax-pymin);
		*/
		/*
	System.out.println(result);
	try
	{
	
	Thread.sleep(1);
	}
	catch(InterruptedException ie)
	{
	}*/
		/*
		if(result==0)
		{
			Graphics g=this.getGraphics();
			g.setColor(Color.RED);
			g.drawRect(pxmin,renderPixelHeight-pymax,pxmax-pxmin,pymax-pymin);	
		}
		*/
		this.zBufferMode=zBufferMode;
		
		
		return result;	
	}	

	private final CompiledFace getCompiledFace(int meshId,int faceId)
	{
		return this.compiledMeshes[meshId].compiledFaces[faceId];
	}
	
	protected final int setFaces3DToZBuffer(IFace3D faceList1[],int faceNum)
	{
		
		int result=0;
		//Bench		
//		Bench.setFaces3DToZBuffer++;

		Face3D[] faceList=(Face3D[])faceList1;
		//this.cMesh3D=faceList[0].getId();
		double clipzmin1xs=0;
		double clipzmin1ys=0;
		double clipzmin2xs=0;
		double clipzmin2ys=0;
		double x;
		double y;
		double z;
		
		Vertex3D p,p1,p2;
		
		
		int nPol=faceList.length; 
		int numP=0;	
		if(faceNum>=0)
		{
			numP=faceNum;
			nPol=faceNum+1;	
		}
		
		//int n=0;
		
		for(;numP<nPol;numP++)
		{
			
			Face3D po=faceList[numP];
			
			if(((this.zBufferMode & ZB_ALPHA) == 0) && (po.material!=null && po.material.alphaLevel != 0))
			{
				po.nextAlphaFace=null;
				if(this.firstAlphaFace==null)
				{
					this.firstAlphaFace=po;
					this.lastAlphaFace=po;	
				}
				else
				{
					po.nextAlphaFace=this.firstAlphaFace;
					this.firstAlphaFace=po;
				}
				continue;
			}			
			
			CompiledFace compiledFace=this.getCompiledFace(po.object.id,po.id);
			
			if(compiledFace.lastZbufferImage==this.numImage)
				continue;
				
			compiledFace.lastZbufferImage=this.numImage;
			
			
			if(compiledFace.lastRenderImage==this.numImage)
				continue;

			
			//Compute z distance of camera from face, negative value mean camera is behind face
			//We use -10 rather than 0 to void preciion bug
			//double dPlan=po.pa*px+po.pb*py+po.pc*pz+po.pd;
	
			double dPlan=po.pa*px+po.pb*py+po.pc*pz+po.pd;
			if(dPlan<-0.0)
			{
				continue;
			}
			
			
			double viewAngle=po.pa*(this.nx-this.px)+po.pb*(this.ny-this.py)+po.pc*(this.nz-this.pz);
			if(viewAngle>this.maxFaceViewAngleCos)
			{
				continue;
			}
			

			double yPMin=maxYValue;
			double yPMax=minYValue;
			double xPMin=maxXValue;
			double xPMax=minXValue;
			double zPMin=this.zMax;
			double zPMax=this.zMin;
			
			double xs0,ys0,zp0;
			double xs1,ys1,zp1;
			double xs2,ys2,zp2;
			double xp0,yp0;

			hLinesBufferMin=(Integer.MAX_VALUE-1);
			hLinesBufferMax=-(Integer.MAX_VALUE-1);	
//System.out.println("render"+((this.zBufferMode & ZB_ALPHA) == 0) + " " + po.material.alphaLevel);
//po.material.alphaLevel=50;									

				//po.lastZbufferImage=this.numImage;
				p=po.p2;
				if(p.cameraPositionEvaluated!=this.numImage)
				{
					p.cameraPositionEvaluated=this.numImage;
					x=p.x;
					y=p.y;
					z=p.z;				
					double tX=ox+axx*x+ayx*y+azx*z;
					double tY=oy+axy*x+ayy*y+azy*z;
					zp2=oz+axz*x+ayz*y+azz*z;
					double pIz=1/zp2;
					xs2=(tX*this.screenZoomXFocus*pIz+this.renderPixelWidthDiv2);	//Calcul x1 sur l'ecran
					ys2=(tY*this.screenZoomYFocus*pIz+this.renderPixelHeightDiv2);	//Calcul y1 sur l'ecran		
					if(zp2<0)
					{	
						xs2=-xs2;
						ys2=-ys2;						
					}
					p.tX=tX;
					p.tY=tY;
					p.xs=xs2;						
					p.ys=ys2;						
					p.iZs=pIz;
					p.tZ=zp2;
					
				}
				else
				{
					xs2=p.xs;	
					ys2=p.ys;
					zp2=p.tZ;
				}

				double p2x=p.tX;
				double p2y=p.tY;
				double p2z=p.tZ;
				
				double radius=po.sphereBox;
				double pzmin=p2z-radius;
				double pzmax=p2z+radius;
				
		
				//FAR CLIP	
				if(pzmax<=this.zMin)
					continue;
		
				//NEAR CLIP		
				if(pzmin>=this.zMax)
					continue;
				
				//RIGHT CLIP
				if((RA*p2x+RB*p2y+RC*p2z)>=radius)
					continue;
		
				//LEFT CLIP
				if((LA*p2x+LB*p2y+LC*p2z)>=radius)
					continue;
		
				//UP CLIP
				if((UA*p2x+UB*p2y+UC*p2z)>=radius)
					continue;
		
				//DOWN CLIP
				if((DA*p2x+DB*p2y+DC*p2z)>=radius)
					continue;						
							
				
				p=po.p0;												
				if(p.cameraPositionEvaluated!=this.numImage)
				{
					p.cameraPositionEvaluated=this.numImage;
					x=p.x;
					y=p.y;
					z=p.z;				
					xp0=ox+axx*x+ayx*y+azx*z;
					yp0=oy+axy*x+ayy*y+azy*z;
					zp0=oz+axz*x+ayz*y+azz*z;
					double pIz=1/zp0;
					xs0=(xp0*this.screenZoomXFocus*pIz+this.renderPixelWidthDiv2);	//Calcul x1 sur l'ecran
					ys0=(yp0*this.screenZoomYFocus*pIz+this.renderPixelHeightDiv2);	//Calcul y1 sur l'ecran		
					if(zp0<0)
					{	
						xs0=-xs0;
						ys0=-ys0;						
					}					
					p.tX=xp0;
					p.tY=yp0;
					p.xs=xs0;						
					p.ys=ys0;						
					p.iZs=pIz;
					p.tZ=zp0;
				}
				else
				{
					xs0=p.xs;	
					ys0=p.ys;
					zp0=p.tZ;
					xp0=p.tX;
					yp0=p.tY;
				}	
				
			
					
				p=po.p1;							
				if(p.cameraPositionEvaluated!=this.numImage)
				{
					p.cameraPositionEvaluated=this.numImage;
					x=p.x;
					y=p.y;
					z=p.z;				
					double tX=ox+axx*x+ayx*y+azx*z;
					double tY=oy+axy*x+ayy*y+azy*z;
					zp1=oz+axz*x+ayz*y+azz*z;
					double pIz=1/zp1;
					xs1=(tX*this.screenZoomXFocus*pIz+this.renderPixelWidthDiv2);	//Calcul x1 sur l'ecran
					ys1=(tY*this.screenZoomYFocus*pIz+this.renderPixelHeightDiv2);	//Calcul y1 sur l'ecran		
					if(zp1<0)
					{	
						xs1=-xs1;
						ys1=-ys1;						
					}					
					p.tX=tX;
					p.tY=tY;						
					p.xs=xs1;						
					p.ys=ys1;
					p.iZs=pIz;
					p.tZ=zp1;
				}
				else
				{
					xs1=p.xs;	
					ys1=p.ys;
					zp1=p.tZ;
				}					

				
				if(zp0>zPMax) zPMax=zp0;
				if(zp0<zPMin) zPMin=zp0;
				if(zp1>zPMax) zPMax=zp1;
				if(zp1<zPMin) zPMin=zp1;
				if(zp2>zPMax) zPMax=zp2;																
				if(zp2<zPMin) zPMin=zp2;
								
				
				if(zPMax<=this.zMin) continue;
				if(zPMin>=this.zMax) continue;
				
												
				if(ys0<yPMin) yPMin=ys0;
				if(ys0>yPMax) yPMax=ys0;
				if(ys1<yPMin) yPMin=ys1;
				if(ys1>yPMax) yPMax=ys1;					
				if(ys2<yPMin) yPMin=ys2;
				if(ys2>yPMax) yPMax=ys2;
				
				if(yPMax<=minYValue) continue;
				if(yPMin>=maxYValue) continue;

				if(yPMax<=(yPMin+0.0005)) continue;
			
				if(xs0<xPMin) xPMin=xs0;
				if(xs0>xPMax) xPMax=xs0;
				if(xs1<xPMin) xPMin=xs1;
				if(xs1>xPMax) xPMax=xs1;					
				if(xs2<xPMin) xPMin=xs2;
				if(xs2>xPMax) xPMax=xs2;
				
				if(xPMax<=minXValue) continue;
				if(xPMin>=maxXValue) continue;
					
				if(xPMax<=(xPMin+0.0005)) continue;

				if(zPMin>=this.zMin)
				{
					setHLigne(xs0,ys0,po.p0.iZs,xs1,ys1,po.p1.iZs);
					setHLigne(xs1,ys1,po.p1.iZs,xs2,ys2,po.p2.iZs);
					setHLigne(xs2,ys2,po.p2.iZs,xs0,ys0,po.p0.iZs);
				}
				else			
				{
					int clipZMin=0;
					int nVertex=0;
					p1=po.p0;
					p2=po.p1;
					
					while(nVertex<3)
					{
						double z1=p1.tZ;
						double z2=p2.tZ;
						if(z1>=this.zMin&&z2>=this.zMin)
						{					
							setHLigne(p1.xs,p1.ys,p1.iZs,p2.xs,p2.ys,p2.iZs);									
						}
						else
							if(z1<zMin&&z2>=zMin)
							{
								double dx,dy,dz;
								double p1tX=p1.tX;
								double p1tY=p1.tY;
								dx=p2.tX-p1tX;
								dy=p2.tY-p1tY;
								dz=p2.tZ-z1;
								double idz=1.0/dz;
								double tX=(zMin-z1)*dx*idz+p1tX;
								double tY=(zMin-z1)*dy*idz+p1tY;
														
								clipzmin1xs=(tX*this.screenZoomXFocus*this.iZMin+this.renderPixelWidthDiv2);	//Calcul x1 clipzmin sur l'ecran
								clipzmin1ys=(tY*this.screenZoomYFocus*this.iZMin+this.renderPixelHeightDiv2);	//Calcul y1 clipzmin sur l'ecran	
								setHLigne(clipzmin1xs,clipzmin1ys,this.iZMin,p2.xs,p2.ys,p2.iZs);
								clipZMin=1;
							}
							else
								if(z2<zMin&&z1>=zMin)
								{
									double dx,dy,dz;
									double p2tX=p2.tX;
									double p2tY=p2.tY;
									dx=p1.tX-p2tX;
									dy=p1.tY-p2tY;
									dz=p1.tZ-z2;
									double idz=1.0/dz;
									double tX=(zMin-z2)*dx*idz+p2tX;
									double tY=(zMin-z2)*dy*idz+p2tY;
									clipzmin2xs=(tX*this.screenZoomXFocus*this.iZMin+this.renderPixelWidthDiv2);	//Calcul x2 clipzmin sur l'ecran
									clipzmin2ys=(tY*this.screenZoomYFocus*this.iZMin+this.renderPixelHeightDiv2);	//Calcul y2 clipzmin sur l'ecran	
									setHLigne(p1.xs,p1.ys,p1.iZs,clipzmin2xs,clipzmin2ys,this.iZMin);						
									clipZMin=1;
								}	
								
						p1=p2;
						if(nVertex==0)
							p2=po.p2;
						else
							p2=po.p0;							
						nVertex++;
					}
	
					if(clipZMin!=0)
						setHLigne(clipzmin2xs,clipzmin2ys,this.iZMin,clipzmin1xs,clipzmin1ys,this.iZMin);
					else 
						continue;
						
				}

				if(hLinesBufferMax<=hLinesBufferMin)
					continue;

				compiledFace.lastRenderOffset=-1;
				compiledFace.firstRenderOffset=-1;

				if(pasteHLine(compiledFace)!=0)
					result=1;

				if((this.zBufferMode & ZB_ALPHA) !=0 && compiledFace.firstRenderOffset!=-1)
				{
					
					this.drawer.setMesh3D(po.object);
					if((po.object.renderMode&DzzD.RM_LIGHT)!=0)
						this.prepareMesh3DLocalLight3DBuffer(po.object.getScene3D(),po.object);					
					this.drawer.drawFace3D(this.compiledMeshes[po.object.id].compiledFaces[po.id],true);
					
				}

	}
	return result;
}

	double p21yd=0.0;
	double coeffAX=0.0;
	double coeffBX=0.0;
	double coeffAZ=0.0;
	double coeffBZ=0.0;		

	private final void setHLigne(double p1xd,double p1yd,double p1izd,double p2xd,double p2yd,double p2izd)
	{
		
		boolean down=true;
		if(p1yd>p2yd)
		{
			down=false;
			double tp1xd=p1xd;
			double tp1yd=p1yd;
			double tp1izd=p1izd;
			
			p1xd=p2xd;
			p1yd=p2yd;
			p1izd=p2izd;
			p2xd=tp1xd;
			p2yd=tp1yd;
			p2izd=tp1izd;
		}
	
		if(p1yd>this.maxYValue)
			return;
		if(p2yd<this.minYValue)
			return;
		if(p2yd-p1yd==0.0)
			return;
	
		p21yd=1.0/(p2yd-p1yd);
		coeffAX=(p2xd-p1xd)*p21yd;
		coeffBX=p1xd-coeffAX*p1yd;
		coeffAZ=(p2izd-p1izd)*p21yd;
		coeffBZ=p1izd-coeffAZ*p1yd;
	
		//Clip en Y
		if(p1yd<this.minYValue)
		{
			p1xd=coeffAX*this.minYValue+coeffBX;
			p1izd=coeffAZ*this.minYValue+coeffBZ;
			p1yd=this.minYValue;
		}
						
		if(p2yd>this.maxYValue)
		{
			p2xd=coeffAX*this.maxYValue+coeffBX;
			p2izd=coeffAZ*this.maxYValue+coeffBZ;
			p2yd=this.maxYValue;	
		}

		int p1y=(int)p1yd;
		int p2y=(int)p2yd;
		if(p1y==p2y) return;

		if(p2y>hLinesBufferMax) hLinesBufferMax=p2y;	//Ajuste hLinesBufferMax	
		if(p1y<hLinesBufferMin) hLinesBufferMin=p1y;	//Ajuste hLinesBufferMin

		
		if(down) //Set min values if going down (on screen)
		{
			
			this.vLinesBufferMin[p1y]=p2y;
			this.vLinesBufferMinX[p1y]=p1xd;
			this.vLinesBufferMinIncX[p1y]=coeffAX;
			this.vLinesBufferMinIz[p1y]=p1izd;
			this.vLinesBufferMinIncIz[p1y]=coeffAZ;

		}
		else //Set max value if going up (on screen)
		{

			this.vLinesBufferMax[p1y]=p2y;
			this.vLinesBufferMaxX[p1y]=p1xd;
			this.vLinesBufferMaxIncX[p1y]=coeffAX;
			this.vLinesBufferMaxIz[p1y]=p1izd;
			this.vLinesBufferMaxIncIz[p1y]=coeffAZ;
		}
			
	}	

	private final int pasteHLine(CompiledFace compiledFace)
	{	  
		double x1=0;	//Set x1 to line starting x offset
		double x2=0;	//Set x2 to line ending x offset
		int nSeg;			
		int sSeg;
		int dSeg=0;
		int ndSeg=0;         
		double xa;		//Starting x offset for current line as double
		double xb;		//Ending x offset for current line as double
		int xai;		//Starting x offset for current line as int
		int xbi;		//Ending x offset for current line as int
        
        int ofsY=this.hLinesBufferMin*this.renderPixelWidth;//-this.renderPixelWidth;
        
		int endMinVLineY=this.vLinesBufferMin[this.hLinesBufferMin];
		double pXMin0=this.vLinesBufferMinX[this.hLinesBufferMin];
		double pXMinIncX=this.vLinesBufferMinIncX[this.hLinesBufferMin];
		double pXMinIz0=this.vLinesBufferMinIz[this.hLinesBufferMin];
		double pXMinIncIz=this.vLinesBufferMinIncIz[this.hLinesBufferMin];
        
		int endMaxVLineY=this.vLinesBufferMax[this.hLinesBufferMin];
		double pXMax0=this.vLinesBufferMaxX[this.hLinesBufferMin];
		double pXMaxIncX=this.vLinesBufferMaxIncX[this.hLinesBufferMin];
		double pXMaxIz0=this.vLinesBufferMaxIz[this.hLinesBufferMin];
		double pXMaxIncIz=this.vLinesBufferMaxIncIz[this.hLinesBufferMin];
		
        double pXMin=pXMin0;
        double pXMax=pXMax0;
		double pXMinIz=pXMinIz0;
        double pXMaxIz=pXMaxIz0;        
  		
		//Loop on y range defined by hLinesBufferMin and hLinesBufferMax
        int dyMin=0;
        int dyMax=0;
        
        nextLine: 
		for(int y=this.hLinesBufferMin;y<this.hLinesBufferMax;y++,dyMin++,dyMax++)
		{
			if(y!=this.hLinesBufferMin)
			{
				ofsY+=this.renderPixelWidth;
								
				pXMin=pXMin0+pXMinIncX*dyMin;
				pXMax=pXMax0+pXMaxIncX*dyMax;
				pXMinIz=pXMinIz0+pXMinIncIz*dyMin;
				pXMaxIz=pXMaxIz0+pXMaxIncIz*dyMax;				
				
				if(y==endMinVLineY)
				{
					endMinVLineY=this.vLinesBufferMin[y];
					pXMin0=this.vLinesBufferMinX[y];
					pXMinIncX=this.vLinesBufferMinIncX[y];
					pXMinIz0=this.vLinesBufferMinIz[y];
					pXMinIncIz=this.vLinesBufferMinIncIz[y];
					dyMin=0;		
        			pXMin=pXMin0;
					pXMinIz=pXMinIz0;				
				}
				
				if(y==endMaxVLineY)
				{
					endMaxVLineY=this.vLinesBufferMax[y];
					pXMax0=this.vLinesBufferMaxX[y];
					pXMaxIncX=this.vLinesBufferMaxIncX[y];
					pXMaxIz0=this.vLinesBufferMaxIz[y];
					pXMaxIncIz=this.vLinesBufferMaxIncIz[y];		
					dyMax=0;
			        pXMax=pXMax0;
        			pXMaxIz=pXMaxIz0;        
					
				}				
				
			}

			xa=pXMin;
			xb=pXMax;
			
		
			/**
			 * Verify if current line fit into x clipping area and clip x range if needed
			 */
			 
			double ofsxa=0;
			double ofsxb=0;
			double xab=(double)(xb-xa);
			 
			if(xa>=this.maxXValue)
			{
				if(pXMinIncX>0) return 0;
				continue;
			}
			else
				if(xa<this.minXValue)
				{
					ofsxa=((this.minXValue-xa));
					xa=this.minXValue;
				}

			if(xb<=this.minXValue)
			{
				if(pXMaxIncX<0) return 0;
				continue;
			}
			else		
				if(xb>this.maxXValue)
				{
					ofsxb=((xb-this.maxXValue));
					xb=this.maxXValue;
				}
				
			//TODO: produce some artefact, must be reviewed (point)
			
			xai=((int)(xa));
			xbi=((int)(xb));
			if(xbi<=xai) {continue nextLine;}
			

			/**
			 * Update current line iz min, iz max and inc iz
			 */
			double piZXA;
			double piZXB;
			double piZMin;	
			double piZMax;	
			double piZInc;
			
			piZXA=pXMinIz;
			piZXB=pXMaxIz;
			
			piZInc=(piZXB-piZXA)/xab;
			
			piZXA+=piZInc*ofsxa;
			piZXB-=piZInc*ofsxb;
							
			if(piZInc>0f)
			{
				piZMin=piZXA;
				piZMax=piZXB;
			}	
			else
			{
				piZMin=piZXB;
				piZMax=piZXA;
			}	

					
			//xa*=65536;
			//xb*=65536;
			
			int segC=0;					//0 - if no segment has been started, 1 - if new segment has been started

			/**
			 * Look for first segment
			 */
			int fSeg=0;
			int fSegEnd=this.zBuffer[ofsY]&0xFFFF;
			while(xai>=fSegEnd )//&& fSegEnd<this.renderPixelWidth)
			{
				fSeg=fSegEnd;
				fSegEnd=this.zBuffer[ofsY+fSegEnd]&0xFFFF;
			}
			sSeg=fSeg;
			nextSeg:
			do
			{
				nSeg=sSeg;

				if(xbi<=nSeg || nSeg>=this.renderPixelWidth)
					break;	
			
				/** 
				 * Read next segment position
				 */				
				int ofsSSeg=ofsY+nSeg;
				sSeg=zBuffer[ofsSSeg]&0xFFFF;

				/** 
				 * Read segment informations
				 */	
				double piZX1S=this.zBufferIzMin[ofsSSeg];
				double piZIncS=this.zBufferIzInc[ofsSSeg];
				double piZX2S=piZX1S+piZIncS*(sSeg-nSeg);
				double piZMinS=piZX1S;	
				double piZMaxS=piZX2S;	
				if(piZIncS>0.0f)
				{
					piZMinS=piZX1S;
					piZMaxS=piZX2S;
				}
				else
				{
					piZMinS=piZX2S;
					piZMaxS=piZX1S;
				}
				
				
					
				boolean derriere=(piZMax<=piZMinS);
				boolean devant=(piZMin>=piZMaxS);
				
		
		//debug[debugn++]=((int)nSeg)+(this.renderPixelHeight-1-y)*this.renderPixelWidth;
										
				/**
				 * If no new segment started than we look if we need to start one
				 */	
				if(segC==0)
				{
					
					if(derriere)
					{						
						if(xbi<=sSeg)
							continue nextLine;
						else
							continue nextSeg;
						
					}
						
					if(xai<=nSeg)		
					{
						if(devant) 
						{
							
							x1=nSeg;
							segC=1;	
							
						}
						else
						if((piZXA+(nSeg-xai)*piZInc)+piZInc>piZX1S+piZIncS)
						{
							x1=nSeg;
							segC=1;	
	
						}
						else
						{
							
							if(piZInc<=piZIncS)
								if(xbi<=sSeg)
									continue nextLine;
								else
									continue nextSeg;

								
							if((piZXA+(sSeg-xai)*piZInc)-piZInc>piZX2S-piZIncS)
							{
								double dxt=((piZX1S-nSeg*piZIncS)-(piZXA-xai*piZInc))/(piZInc-piZIncS);
	

								int xt=((int)dxt);
								if(xt<=nSeg)
								{
									if(xbi>sSeg)
										continue nextSeg;
									else
										continue nextLine;
								}

								if(xt<sSeg&&xt<xbi)
								{
									x1=dxt;
									segC=1;	
								}											
							}
						}	

					}	
					else
					{
						if(devant)
						{
							x1=xa;
							segC=1;	
						}
						else
						if((piZXA+piZInc>(piZX1S+(xai-nSeg)*piZIncS)+piZIncS ))
						{
							x1=xa;
							segC=1;	
						}
						else
						{
							if(piZInc<=piZIncS)
								if(xbi<=sSeg)
									continue nextLine;
								else
									continue nextSeg;


							if((piZXA+(sSeg-xai)*piZInc)-piZInc>piZX2S-piZIncS)
							{
								double dxt=((piZX1S-nSeg*piZIncS)-(piZXA-xai*piZInc))/(piZInc-piZIncS);

								int xt=((int)dxt);
								if(xt>xai&&xt<sSeg&&xt<xbi)
								{
									x1=dxt;
									segC=1;										
								}											
							}
						}	
					}
				
					/**
					 * If not new segment started continue	
					 */
					if(segC==0)
						continue nextSeg;
					else
						if((this.zBufferMode & this.ZB_TEST)!=0)
							return 1;
							
					//debug[debugn++]=((int)x1)+(this.renderPixelHeight-1-y)*this.renderPixelWidth;
					
					dSeg=((int)x1);
					ndSeg=nSeg;
					
					//if(xbi>sSeg) continue;	
					//if(cFace3D==14007)
					//	debug[debugn++]=((int)dSeg)+(this.renderPixelHeight-1-y)*this.renderPixelWidth;
														
				}
					


				if(devant)
				{
					
					if(xbi>sSeg) continue nextSeg;
					
					x2=xbi;
					//debug[debugn++]=((int)x2)+(this.renderPixelHeight-1-y)*this.renderPixelWidth;
					segC=0;					
				}
				else				
				if(derriere)
				{	
				
					x2=nSeg;
					segC=0;
					
				}
				else
				if(dSeg<nSeg&&(piZXA+(nSeg-xai)*piZInc+piZInc<piZX1S+piZIncS))
				{
					x2=nSeg;
					segC=0;
					
				}
				else
				{
					
					if(xbi<=sSeg)
					{
						if(piZXB-piZInc>=(piZX1S+(xbi-nSeg-1)*piZIncS))
						{
							x2=xb;
							segC=0;	
							
							
						}
						else
						{
							
							double dxt=((piZX1S-nSeg*piZIncS)-(piZXA-xai*piZInc))/(piZInc-piZIncS);

							int xt=((int)dxt);						
							if(xt>nSeg&&xt>dSeg&&xt<=xbi)
							{
							
								x2=dxt;
								
							}
							else
							{
								if(xt>=xbi)
								{
									x2=xb;
								}
								else
								{
									if(dSeg>nSeg)
									{
									
										x2=(dSeg+1);
									}
									else
									{
										x2=(nSeg+1);
										
									}
								}
								
							}
							segC=0;
							
						}										
									
					}
					else
					{
						
						
						if(piZXA+(sSeg-xai+1)*piZInc<piZX2S+piZIncS)
						{
							double dxt=((piZX1S-nSeg*piZIncS)-(piZXA-xai*piZInc))/(piZInc-piZIncS);

							int xt=((int)dxt);								
							if(xt>nSeg&&xt>dSeg&&xt<=sSeg)
							{
								x2=dxt;
								
								segC=0;
							}
							
						}							 
					}
			
				}
			
			
				/**
				 * If new segment not closed continue
				 */
				if(segC!=0)	continue;	
				//if(cFace3D==14007)
				//debug[debugn++]=((int)x2)+(this.renderPixelHeight-1-y)*this.renderPixelWidth;

				/**
				 * If new segment closed, write it
				 */
				int iX1=(int)(x1*65536f);
				//int px1=iX1>>8&0xFF;
				int px1=(int)(xa*65536f)>>8&0xFF;
				
				int iX2=(int)(x2*65536f);
				//int px2=iX2>>8&0xFF;
				int px2=(int)(xb*65536f)>>8&0xFF;
				
				int eSeg=iX2>>16;

				if(eSeg==dSeg)
				{
					segC=0;
					continue;
				}

															
				if((this.zBufferMode & this.ZB_WRITE)!=0 || ((this.zBufferMode & ZB_ALPHA)!=0) )
				{
					/**
					 * Update current segment new ending offset if needed
					 */
					if(dSeg!=ndSeg)
					{
						int pX1S=zBuffer[ofsY+ndSeg]>>16&0xFF;
						zBuffer[ofsY+ndSeg]=dSeg|(pX1S<<16);
					}

					int ofsSeg=ofsY+nSeg;
					int objId=zBufferO[ofsSeg];
					int faceId=zBufferP[ofsSeg];
					int ofsX1=ofsY+dSeg;
					zBufferO[ofsX1]=cMesh3D.id;					
					zBufferP[ofsX1]=compiledFace.face.id;					
					zBuffer[ofsX1]=eSeg|(px1<<16);				
					
					this.zBufferIzMin[ofsX1]=piZXA+piZInc*(dSeg-xai);
					this.zBufferIzInc[ofsX1]=piZInc;

						
					/**
					 * Update current segment new starting offset if needed
					 */					
					if(eSeg!=sSeg)
					{
						
						int ofsX2=ofsY+eSeg;
						this.zBuffer[ofsX2]=sSeg|(px2<<16); 			
						this.zBufferO[ofsX2]=objId;
						this.zBufferP[ofsX2]=faceId;						
						this.zBufferIzMin[ofsX2]=piZX1S+piZIncS*(eSeg-nSeg);
						this.zBufferIzInc[ofsX2]=piZIncS;
					}
				}
				
				if(((this.zBufferMode & ZB_ALPHA)!=0))
				{
					
					if(eSeg!=dSeg)
					{
						//zBufferAlpha[ofsY+dSeg]=eSeg;
						//zBuffer[ofsY+dSeg]=eSeg;
						//Face3D polRef=this.cFace3DObjet;
						//compiledFace
						if(compiledFace.lastRenderImage!=this.numImage)
						{
							compiledFace.lastRenderImage=this.numImage;
							compiledFace.firstRenderOffset=dSeg|(y<<16&0xFFFF0000);
							compiledFace.lastRenderOffset=compiledFace.firstRenderOffset;
						}
						else
						{
							int ofsX=compiledFace.lastRenderOffset;
							int ofsYF=(ofsX>>16)&0xFFFF;
							ofsX&=0xFFFF;
							int ofsXY=ofsX+ofsYF*this.renderPixelWidth;
							//this.zBuffer[ofsXY]=dSeg|(y<<16&0xFFFF0000);
							this.zBufferF[ofsXY]=dSeg|(y<<16&0xFFFF0000);
							
							compiledFace.lastRenderOffset=dSeg|(y<<16&0xFFFF0000);
						}		
					}

				}
				sSeg=eSeg;								
				
			}
			while(true);	
			if(segC!=0) Log.log("erreur");
		}
		return 0;
	}

	private final void prepareFaceBuffer(IScene3D scene)
	{
		CompiledMesh compiledMesh=null;
		CompiledFace compiledFace=null;
		
		this.firstRenderBackgroundOffset=-1;
		this.firstMeshToRender=null;
		
		this.nbRenderedFace=0;		
		this.nbRenderedMesh=0;
				
		int lastMeshId=-1;
		int lastFaceId=-1;
		
		for(int y=0;y<this.renderPixelHeight;y++)
		{
			int startX=0;
			int startY=y*this.renderPixelWidth;
			
			while (startX!=this.renderPixelWidth)
			{
				int startXY=startY+startX;
				int endX=zBuffer[startXY]&0xFFFF;
				int meshId=zBufferO[startXY];
				
				if(meshId!=-1)	//Mesh 
				{
					
					if(meshId!=lastMeshId)
					{
						//objRef=(Mesh3D)scene.getMesh3DById(obj);
						compiledMesh=this.compiledMeshes[meshId];
						lastMeshId=meshId;
						lastFaceId=-1;
					}
					
					if(compiledMesh.lastRenderImage!=this.numImage)
					{
						compiledMesh.lastRenderImage=this.numImage;
						//compiledMesh.lastVisibleImage=this.numImage;
						compiledMesh.nbRenderFace=0;				
						compiledMesh.firstFaceToRender=null;					
						compiledMesh.nextMeshToRender=this.firstMeshToRender;
						this.firstMeshToRender=compiledMesh;
						this.nbRenderedMesh++;
					}

					int faceId=zBufferP[startXY];
					
					if(faceId!=lastFaceId)
					{
						compiledFace=compiledMesh.compiledFaces[faceId];
						//face=mesh.faces3D[faceId];
						lastFaceId=faceId;
					}
					if(compiledFace.lastRenderImage!=this.numImage)
					{
						
						//Mesh3DOctree octree=compiledFace.face.objectOctree;
						/*
						while(octree!=null)// && octree.lastVisibleImage!=this.numImage)
						{
							//octree.lastVisibleImage=this.numImage;
							octree=octree.parent;							
						}
						*/
						
						compiledFace.lastRenderImage=this.numImage;
						compiledFace.nextFaceToRender=compiledMesh.firstFaceToRender;
						compiledMesh.firstFaceToRender=compiledFace;
						compiledMesh.nbRenderFace++;
						compiledFace.firstRenderOffset=startX|(y<<16&0xFFFF0000);
						compiledFace.lastRenderOffset=compiledFace.firstRenderOffset;
						this.nbRenderedFace++;
					}
					else
					{
						int ofsX=compiledFace.lastRenderOffset;
						int ofsY=(ofsX>>16)&0xFFFF;
						ofsX&=0xFFFF;
						int ofsXY=ofsX+ofsY*this.renderPixelWidth;
						this.zBufferF[ofsXY]=startX|(y<<16&0xFFFF0000);
						compiledFace.lastRenderOffset=startX|(y<<16&0xFFFF0000);
					}					
				}
				else //Background 
				{
					if(this.firstRenderBackgroundOffset==-1)
					{
						this.firstRenderBackgroundOffset=startX|(y<<16&0xFFFF0000);
						this.lastRenderBackgroundOffset=this.firstRenderBackgroundOffset;
					}
					else
					{
						int ofsX=this.lastRenderBackgroundOffset;
						int ofsY=(ofsX>>16)&0xFFFF;
						ofsX&=0xFFFF;
						int ofsXY=ofsX+ofsY*this.renderPixelWidth;
						this.zBufferF[ofsXY]=startX|(y<<16&0xFFFF0000);
						this.lastRenderBackgroundOffset=startX|(y<<16&0xFFFF0000);
					}
				}
				startX=endX;
			}
			
		}
		
		int ofsX=this.lastRenderBackgroundOffset;
		int ofsY=(ofsX>>16)&0xFFFF;
		ofsX&=0xFFFF;
		int ofsXY=ofsX+ofsY*this.renderPixelWidth;
		this.zBufferF[ofsXY]=(this.renderPixelWidth-1)|((this.renderPixelHeight-1)<<16&0xFFFF0000);
		this.lastRenderBackgroundOffset=(this.renderPixelWidth-1)|((this.renderPixelHeight-1)<<16&0xFFFF0000);	
	}
	
	private final CompiledMesh getFirstMeshToRender()
	{
		return this.firstMeshToRender;
	}
	
	private final CompiledMesh getNextMeshToRender(CompiledMesh mesh)
	{
		return mesh.nextMeshToRender;	
	}

	private final void renderMesh3D(IScene3D scene)
	{
		CompiledMesh compiledMesh=this.getFirstMeshToRender();
	
		this.zBufferMode=ZB_WRITE;
		while(compiledMesh!=null)
		{
			Mesh3D mesh=compiledMesh.mesh;
			if((mesh.renderMode&DzzD.RM_LIGHT)!=0)
				this.prepareMesh3DLocalLight3DBuffer(scene,mesh);
			
			this.drawer.setMesh3D(mesh);
			CompiledFace compiledFace=compiledMesh.firstFaceToRender;
			while(compiledFace!=null)
			{
				this.drawer.drawFace3D(compiledFace,false);
				compiledFace=compiledFace.nextFaceToRender;
			}
			compiledMesh=this.getNextMeshToRender(compiledMesh);
		}

		this.zBufferMode=ZB_ALPHA;//|ZB_WRITE;
		for(Face3D alphaFace=this.firstAlphaFace;alphaFace!=null;alphaFace=alphaFace.nextAlphaFace)
		{
			this.setCurrentMesh3D(alphaFace.object);
			this.setFaces3DToZBuffer(alphaFace.object.faces3D,alphaFace.id);
		}
	}	
	
	public final String getImplementationName()
	{
		return "SOFT";
	}
		
	/** Get object ID rendered at the specified location 
	 *  @param x 
	 *  @param y
	 *  @return object ID at x,y	 
 	 */			
	public final int getRenderedMesh3DIdAt(int x,int y)
	{
		if((this.antialias&2)!=0)	
			x*=2;
		if((this.antialias&4)!=0)				
			y*=2;	
		y=this.renderPixelHeight-y-1;
		int numMesh3D=-1;
		if(x<0) x=0;
		if(x>=this.renderPixelWidth)	
			x=this.renderPixelWidth-1;
		if(y<0) y=0;
		if(y>=this.renderPixelHeight)	
			y=this.renderPixelHeight-1;

		int startX=0;
		int startY=y*this.renderPixelWidth;
		int posXY=startY+x;
		while (startX!=renderPixelWidth)
		{
			int startXY=startY+startX;
			int endX=zBuffer[startXY]&=0xFFFF;
			int endXY=startY+endX;
			if(posXY>=startXY&&posXY<endXY)
			{
				numMesh3D=zBufferO[startXY];
				break;
			}
			startX=endX;		
		}
		return numMesh3D;
	}
	
	/** Get face ID rendered at the specified location 
	 *  @param x 
	 *  @param y
	 *  @return face ID at x,y	 
 	 */			
	public final int getRenderedFace3DIdAt(int x,int y)
	{
		y=this.renderPixelHeight-y-1;
		int numFace3D=-1;
		if(x<0) x=0;
		if(x>=this.renderPixelWidth)	
			x=this.renderPixelWidth-1;
		if(y<0) y=0;
		if(y>=this.renderPixelHeight)	
			y=this.renderPixelHeight-1;

		int startX=0;
		int startY=y*this.renderPixelWidth;
		int posXY=startY+x;
		while (startX!=renderPixelWidth)
		{
			int startXY=startY+startX;
			int endX=zBuffer[startXY]&=0xFFFF;
			int endXY=startY+endX;
			if(posXY>=startXY&&posXY<endXY)
			{
				numFace3D=zBufferP[startXY];
				break;
			}
			startX=endX;		
		}
		return numFace3D;
	}	

	/** Get z value at the specified location 
	 *  @param x 
	 *  @param y
	 *  @return z	 
 	 */				
	public final double getZAt(int x,int y)
	{
		double z=0;
		y=this.renderPixelHeight-y-1;
		int numFace3D=-1;
		if(x<0) x=0;
		if(x>=this.renderPixelWidth)	
			x=this.renderPixelWidth-1;
		if(y<0) y=0;
		if(y>=this.renderPixelHeight)	
			y=this.renderPixelHeight-1;

		int startX=0;
		int startY=y*this.renderPixelWidth;
		int posXY=startY+x;
		while (startX!=renderPixelWidth)
		{
			int startXY=startY+startX;
			int endX=zBuffer[startXY]&=0xFFFF;
			int endXY=startY+endX;
			if(posXY>=startXY&&posXY<endXY)
			{
				int obj=zBufferO[startXY];
				int pol=zBufferP[startXY];
				if(obj==-1)
				{
					z=this.zMax;
					break;
				}
				double piZX1S=this.zBufferIzMin[startXY];
				double piZIncS=this.zBufferIzInc[startXY];
				double piZ=piZX1S+piZIncS*(x-startX);
				z=1f/piZ;
			
				break;
			}
			startX=endX;		
		}
		return z;
	}	

}