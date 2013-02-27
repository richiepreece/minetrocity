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

import java.awt.Canvas;
import java.awt.event.*;
import java.awt.Cursor;
import java.util.*;


public class Render3D extends Render2D implements IRender3D
{
	
	
	protected Axis3D axes;	 		//Axis copy for current camera
	protected double focus;			//Focal length for current camera (in scene unit)
	double iFocus;					//Inverse of focal length for current camera
//	double screenZoomX;				//Screen horizontal zoom for current camera (in scene unit)
//	double screenZoomY;				//Screen vertical zoom for current camera (in scene unit)
	double iZoomX;					//Inverse of horizontal zoom for current camera : 1/zoomX
	double iZoomY;					//Inverse of vertical zoom for current camera : 1/zoomY
	

	protected double screenZoomXFocus;	//Screen horizontal zoom multiplied by current focal length : zoomX*focus;
	protected double screenZoomYFocus;	//Screen vertical zoom multiplied by current focal length : zoomY*focus;
	protected double zMax;			//Current camera z max (in scene unit)
	protected double iZMax;			//Inverse of current camera z max 
	protected double zMin;			//Current camera z min (in scene unit)
	protected double iZMin;			//Inverse of current camera z min 
	protected double zoomX;
	protected double zoomY;

	protected int render3DMode;		//Current render 3D mode

	protected Mesh3D cMesh3D;
	protected double RA,RB,RC,LA,LB,LC,UA,UB,UC,DA,DB,DC;	//Clipping plane for current camera in camera space 
	protected double maxFaceViewAngleCos;

	protected Mesh3DLight3D mesh3DLocalLight3DBuffer[]; 	//Mesh local light buffer that store lights positions in this object space for each rendered mesh.
	protected int nbLocalLight3D;


	/**************************************************************
	 * CURRENT RENDERED SCENE AND TEMPORARY RELATED VAR
	 **************************************************************/
	protected int colorFog;
	protected double startZFog;
	protected double endZFog; 

	
	/**
     * Internal class used to store localy scene lighting informations in object space
     */
	protected class Mesh3DLight3D
	{
		public double x;					//Light pos x	
		public double y;					//Light pos y
		public double z;					//Light pos z
		public double axx;					//Light axis x x value
		public double axy;					//Light axis x y value
		public double axz;					//Light axis x z value
		public double ayx;					//Light axis y x value
		public double ayy;					//Light axis y y value
		public double ayz;					//Light axis y z value
		public double azx;					//Light axis z x value
		public double azy;					//Light axis z y value
		public double azz;					//Light axis z z value				
	}

	//Current Mesh3D camera axis and camera position in object space
	double ox;	
	double oy;
	double oz;
	double axx;	
	double axy;
	double axz;
	double ayx;		
	double ayy;
	double ayz;			
	double azx;	
	double azy;
	double azz;	
	double px;
	double py;
	double pz;
	double nx;
	double ny;
	double nz;
	
	protected void setCurrentMesh3D(Mesh3D mesh)
	{
		//Compute camera position in this object space
		this.cMesh3D=mesh;
		Axis3D axis=mesh.axes;	
		
		Point3D ax=axis.axeX;
		Point3D ay=axis.axeY;
		Point3D az=axis.axeZ;
		Point3D o=axis.origine;	
		this.ox=o.x;		
		this.oy=o.y;
		this.oz=o.z;
		this.axx=ax.x-ox;		
		this.axy=ax.y-oy;
		this.axz=ax.z-oz;
		this.ayx=ay.x-ox;		
		this.ayy=ay.y-oy;
		this.ayz=ay.z-oz;			
		this.azx=az.x-ox;		
		this.azy=az.y-oy;
		this.azz=az.z-oz;	
		this.px=-(axx*ox+axy*oy+axz*oz);		//Camera x position in object space
		this.py=-(ayx*ox+ayy*oy+ayz*oz);		//Camera y position in object space
		this.pz=-(azx*ox+azy*oy+azz*oz);		//Camera z position in object space			
		this.nx=-(axx*ox+axy*oy+axz*(oz-1));	//Camera x normal in object space
		this.ny=-(ayx*ox+ayy*oy+ayz*(oz-1));	//Camera y normal in object space
		this.nz=-(azx*ox+azy*oy+azz*(oz-1));	//Camera z normal in object space		
	}
	
	public Render3D()
	{
		super();
		this.render3DMode=DzzD.RM_ALL;	
		this.axes=new Axis3D();
		this.colorFog=0x808080;
		this.startZFog=0;
		this.endZFog=Double.MAX_VALUE; 		
		this.mesh3DLocalLight3DBuffer=new Mesh3DLight3D[64];
		for(int n=0;n<this.mesh3DLocalLight3DBuffer.length;n++)
			this.mesh3DLocalLight3DBuffer[n]=new Mesh3DLight3D();
	}
	
	public void setCamera3D(ICamera3D camera3D)
	{
		Camera3D camera=(Camera3D)camera3D;
		this.axes.axeX.copy(camera.axes.axeX);
		this.axes.axeY.copy(camera.axes.axeY);
		this.axes.axeZ.copy(camera.axes.axeZ);
		this.axes.origine.copy(camera.axes.origine);

		this.zMax=camera.zMax;
		this.iZMax=1/this.zMax;		

		//System.out.println(this.iZMin);
		
		this.zMin=camera.zMin;//Math.max(camera.zMin,this.focus);
		this.iZMin=1/this.zMin;			
		//System.out.println(this.iZMin);
		//System.out.println(camera);
		
		//double FOVV05=camera.getFOV()*0.5;
		double SINFOV05=MathX.sin(camera.getFOV()*0.5*MathX.PI/180.0);
		
		double FOVV05=Math.asin(SINFOV05/camera.zoomX);
		//System.out.println(camera);
		
		double cosFOVV05=MathX.cos(FOVV05);
		double sinFOVV05=MathX.sin(FOVV05);
		
		double FOVH05=Math.asin(SINFOV05/camera.zoomY);
		//System.out.println("FOVH="+(Math.asin(FOVH05*2)*180/Math.PI));
		//double FOVH05=FOVH*0.5;
		double cosFOVH05=MathX.cos(FOVH05);
		double sinFOVH05=MathX.sin(FOVH05);
		
		double maxFOV=Math.asin(Math.sqrt(sinFOVH05*sinFOVH05+sinFOVV05*sinFOVV05));
		//System.out.println("Max fov=" +(maxFOV*180/MathX.PI));
		this.maxFaceViewAngleCos=MathX.cos(maxFOV);
		
		//System.out.println("FOVH " + Math.tan(camera.getFOV()*0.5*Math.PI/180.0) );
		//this.iFocus=Math.tan(camera.getFOV()*0.5*Math.PI/180.0);
		
		
		//this.iFocus=Math.tan(FOVV05);
		this.iFocus=Math.tan(FOVV05);
		//this.iFocus=(Math.tan(2.0*FOVV05*Math.PI/360.0));
		
		//System.out.println("FOVH2 " + this.iFocus);
		this.focus=1.0/this.iFocus;///0.70710678118654752440084436210485;//1.0;//0.707;//this.camera3D.focus*r;
		
		this.zoomX=camera.zoomX;
		this.zoomY=camera.zoomY;
		
		double screenZoomX=this.viewPixelWidth*0.5*this.zoomX;
		double screenZoomY=this.viewPixelHeight*0.5*this.zoomY;
		
		if((this.antialias&2)!=0)	
			screenZoomX*=2;
		if((this.antialias&4)!=0)				
			screenZoomY*=2;	

		this.iZoomX=1.0/screenZoomX;
		this.iZoomY=1.0/screenZoomY;	
	
		this.screenZoomXFocus=screenZoomX*this.focus;		
		this.screenZoomYFocus=screenZoomY*this.focus;		
		//System.out.println("FOVH=" + (2.0*180.0*FOVH05/Math.PI));
		//System.out.println("FOVV=" + (2.0*180.0*FOVV05/Math.PI));
		
		//UPDATE CLIPPING PLANE (FRUSTRUM)
		
		cosFOVV05=this.focus*camera.zoomX;
		sinFOVV05=1.0;
		cosFOVH05=this.focus*camera.zoomY;
		sinFOVH05=1.0;

		//System.out.println("FOV=" + (2.0*180.0*FOVV05/Math.PI));
		//RIGHT
		this.RA=1.0*cosFOVV05;
		this.RB=0;
		this.RC=-1.0*sinFOVV05;
		double RN=1.0/Math.sqrt(RA*RA+RB*RB+RC*RC);
		this.RA*=RN;
		this.RB*=RN;
		this.RC*=RN;
		//System.out.println("FOVV05 " + FOVV05);
		//System.out.println("RA " + RA);
		//System.out.println("RC " + RC);

		//LEFT
		this.LA=-1.0*cosFOVV05;
		this.LB=0;
		this.LC=-1.0*sinFOVV05;
		double LN=1.0/Math.sqrt(LA*LA+LB*LB+LC*LC);
		this.LA*=LN;
		this.LB*=LN;
		this.LC*=LN;

		//UP
		this.UA=0;
		this.UB=1.0*cosFOVH05;
		this.UC=-1.0*sinFOVH05;
		double UN=1.0/Math.sqrt(UA*UA+UB*UB+UC*UC);
		this.UA*=UN;
		this.UB*=UN;
		this.UC*=UN;

		//DOWN
		this.DA=0;
		this.DB=-1.0*cosFOVH05;
		this.DC=-1.0*sinFOVH05;
		double DN=1.0/Math.sqrt(DA*DA+DB*DB+DC*DC);
		this.DA*=DN;
		this.DB*=DN;
		this.DC*=DN;				
	}
	
	protected void compileMesh3D(IMesh3D mesh)
	{
		Log.log("Compile Mesh3D :" + mesh.getName());
	}
	
	private void compileMesh3DOctrees(IMesh3DOctree tree)
	{
		Log.log("Compile Mesh3DOctrees ");
		Integer build=new Integer(tree.getBuild());
		if(!build.equals(this.compiledBuild.get(tree)))
		{
			this.compileMesh3DOctree(tree);
			this.compiledBuild.put(tree,new Integer(tree.getBuild()));
		}
		for(int x=0;x<tree.getNbChildren(false);x++)
			if(tree.getChildren(x)!=null)
				this.compileMesh3DOctrees(tree.getChildren(x));		
	}
		
	protected void compileMesh3DOctree(IMesh3DOctree tree)
	{
		Log.log("Compile Mesh3DOctree Cell ");
	}
	
	protected void compileLight3D(ILight3D light)
	{
		Log.log("Compile Light3D :" + light.getName());
	}		
	
	protected void compileCamera3D(ICamera3D camera)
	{
		Log.log("Compile Camera3D :" + camera.getName());
	}	
	
	protected void disposeMesh3D(IMesh3D mesh)
	{
		Log.log("Dispose Mesh3D :" + mesh.getName());	
	}
	
	protected void disposeCamera3D(ICamera3D camera)
	{
		Log.log("Dispose Camera3D :" + camera.getName());	
	}	
	
	protected void disposeLight3D(ILight3D light)
	{
		Log.log("Dispose Light3D :" + light.getName());	
	}		
		
	protected void compileScene3DObject(IScene3D scene)
	{
		this.compileScene2DObject(scene);
		int nbMesh3D=scene.getNbMesh3D();
		for(int n=0;n<nbMesh3D;n++)
		{
			IMesh3D so=scene.getMesh3DById(n);
			Integer build=new Integer(so.getBuild());
			if(!build.equals(this.compiledBuild.get(so)))
			{
				this.compileMesh3D(so);
				this.compiledBuild.put(so,new Integer(so.getBuild()));
				if(so.getMesh3DOctree()!=null)
					this.compileMesh3DOctrees(so.getMesh3DOctree());
			}
		}	
		
		int nbLight3D=scene.getNbLight3D();
		for(int n=0;n<nbLight3D;n++)
		{
			ILight3D so=scene.getLight3DById(n);
			Integer build=new Integer(so.getBuild());
			if(!build.equals(this.compiledBuild.get(so)))
			{
				this.compileLight3D(so);
				this.compiledBuild.put(so,new Integer(so.getBuild()));
			}
		}
		
		int nbCamera3D=scene.getNbCamera3D();
		for(int n=0;n<nbCamera3D;n++)
		{
			ICamera3D so=scene.getCamera3DById(n);
			Integer build=new Integer(so.getBuild());
			if(!build.equals(this.compiledBuild.get(so)))
			{
				this.compileCamera3D(so);
				this.compiledBuild.put(so,new Integer(so.getBuild()));
			}
		}			
	}
	
	public void removeSceneObject(ISceneObject sceneObject)
	{
		super.removeSceneObject(sceneObject);
		if(sceneObject instanceof IMesh3D)
			this.disposeMesh3D((IMesh3D) sceneObject);
		if(sceneObject instanceof ICamera3D)
			this.disposeCamera3D((ICamera3D) sceneObject);			
		if(sceneObject instanceof ILight3D)
			this.disposeLight3D((ILight3D) sceneObject);		
			
	}	
	
	public void renderScene3D(IScene3D scene)
	{
		if(this.getWidth()<=0 || this.getHeight()<=0)
			return;
		
		this.rendering=true;
		
		this.compileScene3DObject(scene);
		
		this.startFrame(scene);
		this.renderFrame(scene);
		this.endFrame(scene);
		
		this.numImage++;
		
		this.rendering=false;
	}	

	protected void startFrame(IScene3D scene)
	{
	
	}

	protected void renderFrame(IScene3D scene)
	{
		for(int no=0;no<scene.getNbMesh3D();no++)
		{	
			Mesh3D ob=(Mesh3D)scene.getMesh3DById(no);
			if(!ob.isVisible())
				continue;
			
			this.setCurrentMesh3D(ob);
			
			if((ob.renderMode&DzzD.RM_LIGHT)!=0)
				this.prepareMesh3DLocalLight3DBuffer(ob.getScene3D(),ob);
			
			if(ob.meshViewGenerator!=null)
			{
				ob.meshViewGenerator.generateForView(px,py,pz,nx,ny,nz,this.focus,this.viewPixelWidth);
				this.setMesh3DListToZBuffer(ob);
				continue;	
			}
			else
			{
				if(ob.octree==null)
					this._setMesh3DToZBuffer(ob);
				else
					this._setMesh3DOctreeToZBuffer(ob,ob.octree);
			}
		}
	}
	
	protected void endFrame(IScene3D scene)
	{
	
	}
		
	protected void prepareMesh3DLocalLight3DBuffer(IScene3D scene,Mesh3D ob)
	{
		Point3D ax=ob.axes.axeX;
		Point3D ay=ob.axes.axeY;
		Point3D az=ob.axes.axeZ;
		Point3D o=ob.axes.origine;	
		this.ox=o.x;		
		this.oy=o.y;
		this.oz=o.z;
		this.axx=ax.x-ox;		
		this.axy=ax.y-oy;
		this.axz=ax.z-oz;
		this.ayx=ay.x-ox;		
		this.ayy=ay.y-oy;
		this.ayz=ay.z-oz;			
		this.azx=az.x-ox;		
		this.azy=az.y-oy;
		this.azz=az.z-oz;	
					
		int nbLight=scene.getNbLight3D();
		for(int nl=0;nl<nbLight;nl++)
		{
			Light3D li=(Light3D)scene.getLight3DById(nl);//lights[nl];
			Mesh3DLight3D lo=this.mesh3DLocalLight3DBuffer[nl];
			
			Point3D p;
			
			p=li.axes.origine;
			double x=p.x-ox;
			double y=p.y-oy;
			double z=p.z-oz;			
			lo.x=axx*x+axy*y+axz*z;
			lo.y=ayx*x+ayy*y+ayz*z;
			lo.z=azx*x+azy*y+azz*z;	
								
			//Todo: can be remove for unidirectional (axeX,axeY)
			
			p=li.axes.axeX;
			x=p.x-ox;
			y=p.y-oy;
			z=p.z-oz;			
			lo.axx=axx*x+axy*y+axz*z;
			lo.axy=ayx*x+ayy*y+ayz*z;
			lo.axz=azx*x+azy*y+azz*z;
						
			p=li.axes.axeY;
			x=p.x-ox;
			y=p.y-oy;
			z=p.z-oz;			
			lo.ayx=axx*x+axy*y+axz*z;
			lo.ayy=ayx*x+ayy*y+ayz*z;
			lo.ayz=azx*x+azy*y+azz*z;
			
			p=li.axes.axeZ;
			x=p.x-ox;
			y=p.y-oy;
			z=p.z-oz;			
			lo.azx=axx*x+axy*y+axz*z;
			lo.azy=ayx*x+ayy*y+ayz*z;
			lo.azz=azx*x+azy*y+azz*z;					
			
			lo.axx-=lo.x;
			lo.axy-=lo.y;
			lo.axz-=lo.z;

			lo.ayx-=lo.x;
			lo.ayy-=lo.y;
			lo.ayz-=lo.z;
			
			lo.azx-=lo.x;
			lo.azy-=lo.y;
			lo.azz-=lo.z;
			
			//End Todo
			
			/*
			p=li.axes.axeZ;
			x=p.x-ox;
			y=p.y-oy;
			z=p.z-oz;			
			lo.azx=axx*x+axy*y+axz*z;
			lo.azy=ayx*x+ayy*y+ayz*z;
			lo.azz=azx*x+azy*y+azz*z;					
							
			lo.azx-=lo.x;
			lo.azy-=lo.y;
			lo.azz-=lo.z;
			*/			
									
			
		}
	}  
	
	private void _setMesh3DToZBuffer(Mesh3D ob)
	{
		double cx=ob.center.x;
		double cy=ob.center.y;
		double cz=ob.center.z;
		double radius=ob.sphereBox;
		if(ob.getNbFace3D()>0 && this.isSphereVisible(cx,cy,cz,radius)==1)
		{
			/*
			Integer build=new Integer(ob.getBuild());
			if(!build.equals(this.compiledBuild.get(ob)))
			{
				this.compileMesh3D(ob);
				this.compiledBuild.put(ob,new Integer(ob.getBuild()));
			}
			*/
			this.setMesh3DToZBuffer(ob);		
		}
	}
	
	private void _setMesh3DOctreeToZBuffer(Mesh3D ob,Mesh3DOctree tree)
	{
		
		double cx=tree.center.x;
		double cy=tree.center.y;
		double cz=tree.center.z;
		double radius=tree.visibilitySphereBoxRadius;
		if(this.isSphereVisible(cx,cy,cz,radius)==1)
		{
			if(tree.getNbFace3D()>0)
				this.setMesh3DOctreeToZBuffer(tree);

			for(int x=0;x<tree.getNbChildren(false);x++)
				if(tree.childrens[x]!=null)
					this._setMesh3DOctreeToZBuffer(ob,tree.childrens[x]);
		}		
	}	
	
	protected void setMesh3DToZBuffer(IMesh3D mesh)
	{
		//this.setFaces3DToZBuffer(mesh.getFaces3D(),-1);
	}
	
	protected void setMesh3DOctreeToZBuffer(IMesh3DOctree tree)
	{
		//this.setFaces3DToZBuffer(tree.getFaces3D(),-1);
	}
		
	protected int setFaces3DToZBuffer(IFace3D faceList[],int faceNum)
	{
		return 0;
	}
	
	private void setMesh3DListToZBuffer(Mesh3D ob)
	{
		Face3DList fl=(Face3DList)ob.meshViewGenerator.getViewFace3DList();
		while(fl!=null)
		{
			this.setFaces3DToZBuffer(ob.faces3D,fl.face.id);		
			fl=fl.nextFaceList;
		}		
	}

	protected int isSphereVisible(double cx,double cy,double cz,double radius)
	{

		double cpx=ox+axx*cx+ayx*cy+azx*cz;	//Compute from object space to camera space
		double cpy=oy+axy*cx+ayy*cy+azy*cz;
		double cpz=oz+axz*cx+ayz*cy+azz*cz;	

	
		double pzmax=cpz+radius;
		

		//FAR CLIP	
		if(pzmax<=this.zMin)
			return 0;
			
		//IF IN SPHERE SET VISIBLE
		if(cpx*cpx+cpy*cpy+cpz*cpz<=(radius*radius))
			return 1;
			
			
		double pzmin=cpz-radius;

		//NEAR CLIP		
		if(pzmin>=this.zMax)
			return 0;
			
		//RIGHT CLIP
		if((RA*cpx+RB*cpy+RC*cpz)>=radius)
			return 0;

		//LEFT CLIP
		if((LA*cpx+LB*cpy+LC*cpz)>=radius)
			return 0;

		//UP CLIP
		if((UA*cpx+UB*cpy+UC*cpz)>=radius)
			return 0;

		//DOWN CLIP
		if((DA*cpx+DB*cpy+DC*cpz)>=radius)
			return 0;
/*
		//SIZE CLIP
		if(pzmin>0.0 && ((radius*this.zoomXFocus/pzmin)<1.0))
			return 0;
	*/		

		return 1;
	}
		
	public int getRenderedMesh3DIdAt(int x,int y)
	{
		return -1;
	}
		
	public int getRenderedFace3DIdAt(int x,int y)
	{
		return -1;
	}
	
	public double getZAt(int x,int y)
	{
		return this.zMax;
	}
		
	public void setFogColor(int colorFog)
	{
		this.colorFog=colorFog;
	}	
	
	public void setFogStart(double startZFog)	
	{
		this.startZFog=startZFog;
	}	
	
	public void setFogEnd(double endZFog)		
	{
		this.endZFog=endZFog;
	}	
	
	
	/**
	 * Render mode interface
	 */
	public IRender3DMode getRender3DMode()
	{
		return this;
	}

	public void enableRender3DMode(int flag)
	{
		this.render3DMode|=flag;
	}

	public void disableRender3DMode(int flag)	
	{
		this.render3DMode&=(flag^DzzD.RM_ALL);
	}

	public void setRender3DModeFlags(int flag)
	{
		this.render3DMode=flag;
	}
	
	public int getRender3DModeFlags()
	{
		return this.render3DMode;
	}			
			
}