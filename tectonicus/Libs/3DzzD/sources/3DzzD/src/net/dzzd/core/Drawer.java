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
import net.dzzd.DzzD;

import java.awt.image.MemoryImageSource;
import java.awt.Image;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.image.DirectColorModel;
import java.awt.*;

class Drawer
{
	static int iZ[]=new int[65536];			//iZi[x] return 2147483647/x [0-65536]
	static int log2[]=new int[2048];		//log2[x] return le log2(x) [0-2048]
	static int normalMap[]=new int[65536];	//normalMap[x] 2147483647/Math.sqrt(x) [0-65536]	

	static
	{		
		
		// Init Lookup table log2(n)
		for(int x=0;x<Drawer.log2.length;x++)
			Drawer.log2[x]=(int)((Math.log(x)/(0.69314718055994530941723212145818)));
			
		// Init Lookup table 1/z
		for(int z=1;z<Drawer.iZ.length;z++)
			Drawer.iZ[z]=((int)(2147483647.0/(double)z))>>3;	
		
		// Init Lookup table normalMap		
		Drawer.normalMap[0]=1;
		for(int n=1;n<Drawer.normalMap.length;n++)
			Drawer.normalMap[n]=(int)(2147483647.0/Math.sqrt(n));	
			
	}

	//RENDER VARIABLE
	private boolean useMIS;						//Flag for MS memoryImageSource compatibility
	private MemoryImageSource imageMemoire;		//For MS memoryImageSource compatibility
	private Image image;						//Current image (equals to front or back image)
	private int pixels[];
	private int rPixels[];

	private int[] zBuffer;
	private int[] zBufferF;
	//private int[] zBufferFA;
	private int softBuffer[];
	private int softBufferR[];
	private int renderPixelWidth;
	private int renderPixelHeight;
	private double renderPixelWidthDiv2;
	private double renderPixelHeightDiv2;
	private double iFocus;
	private double iZoomX;
	private double iZoomY;
	private double zoomX;
	private double zoomY;
	private int antialias;
	private int nbSoftPoint;
	private int lastSoftPoint;
	private int renderMode;
	private int faceRenderMode;
	
	//DRAWER PARAMETERS
	private double textureLevel;
	
	//CURRENT OBJECT & FACE
	private Render3D.Mesh3DLight3D mesh3DLocalLight3DBuffer[];
	private Mesh3D objRef;	
	private Face3D polRef;
	private Material material;
	private MappingUV mapping;
	
	Render3DSW.CompiledMesh compiledMesh;
	Render3DSW.CompiledFace compiledFace;
	Render3DSW.CompiledMaterial compiledMaterial;
	
	//CURRENT OBJECT LOCAL AXIS
	private double ox;	
	private double oy;
	private double oz;
	private double axx;	
	private double axy;
	private double axz;
	private double ayx;	
	private double ayy;
	private double ayz;		
	private double azx;	
	private double azy;
	private double azz;
	
	//CURRENT FACE START & END OFFSET
	private int XBF;		//Current X segment in face buffer zBufferF
	private int YBF;		//Current Y segment in face buffer zBufferF
	private int startXYBF;	//First pixel screen offset to draw
	private int endXYBF;	//Last pixel screen offset to draw

	//iZ INTERPOLATION RELATED VARIABLES
	private double iZA,iZB,iZC;				//Equation 1/Z=iZA*xs+iZB*ys+iZC

	//LIGHTING INTERPOLATION RELATED VARIABLES
	private double iUBA,iUBB,iUBC;			//Equation ul/z=iUBA*xs+iUBB*ys+iUBC
	private double iVBA,iVBB,iVBC;			//Equation vl/z=iVBA*xs+iVBB*ys+iVBC
	private double iWBA,iWBB,iWBC;			//Equation wl/z=iWBA*xs+iWBB*ys+iWBC
	private double incUBDivZ,incVBDivZ,incWBDivZ;
	
	//MAPPING INTERPOLATION RELATED VARIABLE	
	private double iUA,iUB,iUC;				//Equation U/Z=a*xs+b*ys+c
	private double iVA,iVB,iVC;				//Equation V/Z=a*xs+b*ys+c
	private double incUDivZ,incVDivZ;	

	//MAPPING RELATED VARIABLE
	private double dpDivZ;					//Current texture/face texel/pixel ratio

	//MATERIAL
	private int specularLightMap[];	
	
	//DIFFUSE TEXTURES INFORMATIONS
	private int dTexturePixels[];
	private int dDecalLargeur;
	private int dDecalHauteur;
	private int dMaskHauteur;								
	private int dMaskLargeur;					
	private int dLargeurImage;	
	private int dHauteurImage;
	private int dNbMipMap;
	private int dMipMap[][];			

	//BUMP/NORMAL TEXTURES INFORMATIONS
	private int bTexturePixels[];
	private int bDecalLargeur;
	private int bDecalHauteur;
	private int bMaskHauteur;								
	private int bMaskLargeur;					
	private int bLargeurImage;	
	private int bHauteurImage;
	private int bNbMipMap;
	private int bMipMap[][];		
	
	//ENVIRONEMENT TEXTURES INFORMATIONS
	private int eTexturePixels[];
	private int eDecalLargeur;
	private int eDecalHauteur;
	private int eMaskHauteur;								
	private int eMaskLargeur;					
	private int eLargeurImage;	
	private int eHauteurImage;
	private int eNbMipMap;
	private int eMipMap[][];		
	
	//OTHER RENDERING FEATURES
	private double currentTextureLevel;
	private int fogColor;
	
		
	//Set this Drawer buffers for the given soft renderer
	public final void setBuffers(Render3DSW render,int zBuffer[],int zBufferF[])//,int zBufferFA[])
	{
		this.antialias=render.antialias;
		this.renderPixelWidth=render.getWidth();
		this.renderPixelHeight=render.getHeight();
		if((this.antialias&2)!=0)	
			this.renderPixelWidth<<=1;
		if((this.antialias&4)!=0)				
			this.renderPixelHeight<<=1;	
		this.renderPixelWidthDiv2=this.renderPixelWidth>>1;
		this.renderPixelHeightDiv2=this.renderPixelHeight>>1;

		this.initPixelsBuffer(render.viewPixelWidth,render.viewPixelHeight);	
				
		
		if(this.antialias>1)
			this.rPixels=new int[this.renderPixelWidth*this.renderPixelHeight];
		else
			this.rPixels=this.pixels;
System.out.println(this.renderPixelWidth*this.renderPixelHeight);
//DzzD.sleep(10000);
		this.zBuffer=zBuffer;
		this.zBufferF=zBufferF;
	//	this.zBufferFA=zBufferFA;

		this.softBuffer=new int[this.renderPixelWidth*100];
		this.softBufferR=new int[this.renderPixelWidth*100];
		
	}
	
	// Initialise this Drawer for the given soft renderer
	public final void setRender3DSW(Render3DSW render)
	{		
		this.renderMode=render.render3DMode;
		this.iFocus=render.iFocus;		
		this.iZoomX=render.iZoomX;
		this.iZoomY=render.iZoomY;
		this.zoomX=1.0/this.iZoomX;//render.screenZoomX;
		this.zoomY=1.0/this.iZoomY;//render.screenZoomY;
		this.textureLevel=4;
		this.mesh3DLocalLight3DBuffer=render.mesh3DLocalLight3DBuffer;	
		this.nbSoftPoint=0;	
	}
	
	public final void drawBackground(int firstRenderOffset,int lastRenderOffset,int color)
	{

		this.setStartSegment(firstRenderOffset);
		this.setEndSegment(lastRenderOffset);

		do
		{		
			int startY=(this.renderPixelHeight-this.YBF-1)*this.renderPixelWidth;
			int startYB=this.YBF*renderPixelWidth;
			int startX=this.XBF;
			int startXY=startY+startX;
			int startXYB=startYB+startX;
			int endX=zBuffer[startXYB];
			int poidDebut=(endX>>16&0xFF);
			endX&=0xFFFF;
			double y1=(this.YBF-this.renderPixelHeightDiv2)*iZoomY;	
			
			int endXY=startY+endX;
			while(startXY<endXY)
				this.rPixels[startXY++]=color;	

			
			if(XBF>0 && ((this.antialias&1) != 0))
			{
				softBuffer[nbSoftPoint]=startY+XBF;
				softBufferR[nbSoftPoint++]=poidDebut;	
			}
		}while(this.nextSegment());		
	}

	
	private final void setStartSegment(int firstRenderOffset)
	{
		this.XBF=firstRenderOffset;
		this.YBF=this.XBF>>16&0xFFFF;
		this.XBF&=0xFFFF;
		this.startXYBF=this.XBF+this.YBF*this.renderPixelWidth;	
		
	}
	
	private final boolean nextSegment()
	{
			this.XBF=zBufferF[this.startXYBF];
			this.YBF=this.XBF>>16&0xFFFF;
			this.XBF&=0xFFFF;			
			this.startXYBF=this.XBF+this.YBF*this.renderPixelWidth;	
			return this.startXYBF!=this.endXYBF;
	}
		
	private final void setEndSegment(int lastRenderOffset)
	{
		int endXBF=lastRenderOffset;
		int endYBF=endXBF>>16&0xFFFF;
		endYBF*=this.renderPixelWidth;
		endXBF&=0xFFFF;
		int endXYBF=endYBF+endXBF;

		endXBF=this.zBufferF[endXYBF];
		endYBF=endXBF>>16&0xFFFF;
		endYBF*=this.renderPixelWidth;
		endXBF&=0xFFFF;
		this.endXYBF=endYBF+endXBF;		
	}		
	/*
	private void setEndSegmentAlpha(int lastRenderOffset)
	{
		int endXBF=lastRenderOffset;
		int endYBF=endXBF>>16&0xFFFF;
		endYBF*=this.renderPixelWidth;
		endXBF&=0xFFFF;
		int endXYBF=endYBF+endXBF;

		endXBF=this.zBufferFA[endXYBF];
		endYBF=endXBF>>16&0xFFFF;
		endYBF*=this.renderPixelWidth;
		endXBF&=0xFFFF;
		this.endXYBF=endYBF+endXBF;		
	}			
*/
	public final void setMesh3D(Mesh3D objRef)
	{
		this.objRef=objRef;
		Axis3D axes=objRef.axes;
		Point3D ax=axes.axeX;
		Point3D ay=axes.axeY;
		Point3D az=axes.axeZ;
		Point3D o=axes.origine;	
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
	}

	public final void drawFace3D(Render3DSW.CompiledFace compiledFace,boolean isAlpha)
	{
		this.compiledFace=compiledFace;
		this.compiledMaterial=compiledFace.compiledMaterial;
		this.polRef=this.compiledFace.face;
		
		
		this.material=polRef.material;
		if(this.material!=null)
			this.mapping=this.material.mapping;
		else
			this.mapping=null;
			
		this.setStartSegment(this.compiledFace.firstRenderOffset);

	//	if(!isAlpha)
			this.setEndSegment(this.compiledFace.lastRenderOffset);
	//	else
		//	this.setEndSegmentAlpha(this.compiledFace.lastRenderOffset);

		//Compute render mode
		int allowedRenderMode=0;

		if(this.material!=null)
		{
			this.rDiffuseTexel=this.material.diffuseColor;
			this.alphaLevel=this.material.alphaLevel;
			
			this.specularLightMap=this.compiledMaterial.specularLightMap;	
			if(this.specularLightMap!=null)
			{
				
				allowedRenderMode|=DzzD.RM_MATERIAL|DzzD.RM_LIGHT;
				if(this.material.diffuseTexture!=null)
					if(this.material.diffuseTexture.pixels != null)
					{
						allowedRenderMode|=DzzD.RM_TEXTURE_DIFF|DzzD.RM_TEXTURE_MIPMAP|DzzD.RM_TEXTURE_BILINEAR;
						if(this.material.diffuseTextureDetail != null)	
							if(this.material.diffuseTextureDetail.pixels != null)
								allowedRenderMode|=DzzD.RM_DETAIL_TEXTURE;
					}
					
				if(this.material.bumpNormalTexture!=null)
					if(this.material.bumpNormalTexture.pixels != null)
						allowedRenderMode|=DzzD.RM_TEXTURE_BUMP|DzzD.RM_TEXTURE_MIPMAP|DzzD.RM_TEXTURE_BILINEAR;
						
				if(this.material.envTexture!=null)
					if(this.material.envTexture.pixels != null)
						allowedRenderMode|=DzzD.RM_TEXTURE_ENV|DzzD.RM_TEXTURE_MIPMAP|DzzD.RM_TEXTURE_BILINEAR;
						

			}
		}

		allowedRenderMode&=this.renderMode;
		allowedRenderMode&=this.objRef.renderMode;
		this.faceRenderMode=allowedRenderMode;

		this.currentTextureLevel=0;
		if((this.faceRenderMode & DzzD.RM_TEXTURE_MIPMAP) != 0)
			this.currentTextureLevel=this.textureLevel;

		//Read vertices in camera space
		Vertex3D p0=polRef.p0;
		Vertex3D p1=polRef.p1;
		Vertex3D p2=polRef.p2;
		double x1=p0.tX;
		double y1=p0.tY;
		double z1=p0.tZ;
		double x2=p1.tX;
		double y2=p1.tY;
		double z2=p1.tZ;
		double x3=p2.tX;
		double y3=p2.tY;
		double z3=p2.tZ;
		
		

		//Read vertices normals	for light	
		double p0nx=this.polRef.p0nx;
		double p0ny=this.polRef.p0ny;
		double p0nz=this.polRef.p0nz;
		double p1nx=this.polRef.p1nx;
		double p1ny=this.polRef.p1ny;
		double p1nz=this.polRef.p1nz;
		double p2nx=this.polRef.p2nx;
		double p2ny=this.polRef.p2ny;
		double p2nz=this.polRef.p2nz;
		
		if(Double.isNaN(p0nx) || Double.isNaN(p1nx) | Double.isNaN(p2nx))
		{
			return;
		}
			
		double p0tx=this.polRef.p0tx;
		double p0ty=this.polRef.p0ty;
		double p0tz=this.polRef.p0tz;
		double p1tx=this.polRef.p1tx;
		double p1ty=this.polRef.p1ty;
		double p1tz=this.polRef.p1tz;
		double p2tx=this.polRef.p2tx;
		double p2ty=this.polRef.p2ty;
		double p2tz=this.polRef.p2tz;


		double p0bx=this.polRef.p0bx;
		double p0by=this.polRef.p0by;
		double p0bz=this.polRef.p0bz;
		double p1bx=this.polRef.p1bx;
		double p1by=this.polRef.p1by;
		double p1bz=this.polRef.p1bz;
		double p2bx=this.polRef.p2bx;
		double p2by=this.polRef.p2by;
		double p2bz=this.polRef.p2bz;

		double den=1.0/(y1*(x3-x2) + y2*(x1-x3) + y3*(x2-x1));

		//Compute face plane equation in camera space
		double x=polRef.pa+p0.x;
		double y=polRef.pb+p0.y;
		double z=polRef.pc+p0.z;
			
		double pa=ox+this.axx*x+this.ayx*y+this.azx*z;
		double pb=oy+this.axy*x+this.ayy*y+this.azy*z;
		double pc=oz+this.axz*x+this.ayz*y+this.azz*z;
	
		double a=pa-p0.tX;
		double b=pb-p0.tY;
		double c=pc-p0.tZ;
		
		
		
		if((p0nx*polRef.pa+p0ny*polRef.pb+p0nz*polRef.pc)>0.99)
			if((p1nx*polRef.pa+p1ny*polRef.pb+p1nz*polRef.pc)>0.99)
				if((p2nx*polRef.pa+p2ny*polRef.pb+p2nz*polRef.pc)>0.99)				
					this.faceRenderMode|=DzzD.RM_LIGHT_FLAT;
		
		double d=-(a*p0.tX+b*p0.tY+c*p0.tZ);

		double iD=1.0/d;
		double iDiFocus=iD*iFocus;
		double AxiD=a*iD;
		double BxiD=b*iD;
		double CxiD=c*iD;	

		this.iZA=-a*iDiFocus;
		this.iZB=-b*iDiFocus;
		this.iZC=-c*iD;

		//Compute lighting vector (u,v,w) interpolation				
		double laxx=this.mesh3DLocalLight3DBuffer[0].axx;
		double laxy=this.mesh3DLocalLight3DBuffer[0].axy;
		double laxz=this.mesh3DLocalLight3DBuffer[0].axz;
		double layx=this.mesh3DLocalLight3DBuffer[0].ayx;
		double layy=this.mesh3DLocalLight3DBuffer[0].ayy;
		double layz=this.mesh3DLocalLight3DBuffer[0].ayz;
		double lazx=this.mesh3DLocalLight3DBuffer[0].azx;
		double lazy=this.mesh3DLocalLight3DBuffer[0].azy;
		double lazz=this.mesh3DLocalLight3DBuffer[0].azz;

		double ua0=0;
		double ua1=0;
		double ua2=0;
		double va0=0;
		double va1=0;
		double va2=0;
		double wa0=1;
		double wa1=1;
		double wa2=1;
		
		if((this.faceRenderMode&DzzD.RM_TEXTURE_BUMP)==0)
		{
				//Compute lighting vector at each vertices in object space
				ua0=laxx*p0nx+laxy*p0ny+laxz*p0nz;
				ua1=laxx*p1nx+laxy*p1ny+laxz*p1nz;
				ua2=laxx*p2nx+laxy*p2ny+laxz*p2nz;		
				va0=layx*p0nx+layy*p0ny+layz*p0nz;
				va1=layx*p1nx+layy*p1ny+layz*p1nz;
				va2=layx*p2nx+layy*p2ny+layz*p2nz;			
				wa0=lazx*p0nx+lazy*p0ny+lazz*p0nz;
				wa1=lazx*p1nx+lazy*p1ny+lazz*p1nz;
				wa2=lazx*p2nx+lazy*p2ny+lazz*p2nz;	
		}
		else
		{
				//Compute lighting vector at each vertices in tangent space		
				ua0=lazx*p0tx+lazy*p0ty+lazz*p0tz;
				va0=lazx*p0bx+lazy*p0by+lazz*p0bz;
				wa0=lazx*p0nx+lazy*p0ny+lazz*p0nz;		
				ua1=lazx*p1tx+lazy*p1ty+lazz*p1tz;
				va1=lazx*p1bx+lazy*p1by+lazz*p1bz;
				wa1=lazx*p1nx+lazy*p1ny+lazz*p1nz;		
				ua2=lazx*p2tx+lazy*p2ty+lazz*p2tz;
				va2=lazx*p2bx+lazy*p2by+lazz*p2bz;
				wa2=lazx*p2nx+lazy*p2ny+lazz*p2nz;		
		}


		//Compute interpolation parameter for u	
		double l1=0.5-0.49*ua0;
		double l2=0.5-0.49*ua1;
		double l3=0.5-0.49*ua2;	
		if(l1<0.0) l1=0.0;
		if(l2<0.0) l2=0.0;
		if(l3<0.0) l3=0.0;
		if(l1>1.0) l1=1.0;
		if(l2>1.0) l2=1.0;
		if(l3>1.0) l3=1.0;		
		iUBA = (y1*(l3-l2) + y2*(l1-l3) + y3*(l2-l1))  * den;
	    iUBB = (x1*(l2-l3) + x2*(l3-l1) + x3*(l1-l2))  * den;
		iUBC = (y1*(l2*x3-l3*x2) + y2*(l3*x1-l1*x3) + y3*(l1*x2-l2*x1) ) * den;				    
		iUBA-=(iUBC*AxiD);
		iUBB-=(iUBC*BxiD);
		iUBC=-iUBC*CxiD;
		iUBA*=iFocus;
		iUBB*=iFocus;		
		
		//Compute interpolation parameter for v		
		l1=0.5-0.49*va0;
		l2=0.5-0.49*va1;
		l3=0.5-0.49*va2;	
		if(l1<0.0) l1=0.0;
		if(l2<0.0) l2=0.0;
		if(l3<0.0) l3=0.0;
		if(l1>1.0) l1=1.0;
		if(l2>1.0) l2=1.0;
		if(l3>1.0) l3=1.0;		
					
		iVBA = (y1*(l3-l2) + y2*(l1-l3) + y3*(l2-l1))  * den;
	    iVBB = (x1*(l2-l3) + x2*(l3-l1) + x3*(l1-l2))  * den;
		iVBC = (y1*(l2*x3-l3*x2) + y2*(l3*x1-l1*x3) + y3*(l1*x2-l2*x1) ) * den;				    
		iVBA-=(iVBC*AxiD);
		iVBB-=(iVBC*BxiD);
		iVBC=-iVBC*CxiD;
		iVBA*=iFocus;
		iVBB*=iFocus;
		
		//Compute interpolation parameter for w		
		l1=0.5-0.49*wa0;
		l2=0.5-0.49*wa1;
		l3=0.5-0.49*wa2;	
		if(l1<0.0) l1=0.0;
		if(l2<0.0) l2=0.0;
		if(l3<0.0) l3=0.0;
		if(l1>1.0) l1=1.0;
		if(l2>1.0) l2=1.0;
		if(l3>1.0) l3=1.0;		
				
		iWBA = (y1*(l3-l2) + y2*(l1-l3) + y3*(l2-l1))  * den;
	    iWBB = (x1*(l2-l3) + x2*(l3-l1) + x3*(l1-l2))  * den;
		iWBC = (y1*(l2*x3-l3*x2) + y2*(l3*x1-l1*x3) + y3*(l1*x2-l2*x1) ) * den;				    
		iWBA-=(iWBC*AxiD);
		iWBB-=(iWBC*BxiD);
		iWBC=-iWBC*CxiD;
		iWBA*=iFocus;
		iWBB*=iFocus;				
		
		this.incUBDivZ=iUBA*iZoomX;
		this.incVBDivZ=iVBA*iZoomX;
		this.incWBDivZ=iWBA*iZoomX;
		
		
		Texture t=null;
		if(this.material!=null)
		{
			this.eTexturePixels=null;
			if(this.material.envTexture!=null && this.material.envTexture.pixels != null)
			{
				t=this.material.envTexture;
				//ENV TEXTURE
				this.eTexturePixels=t.pixels;
				this.eDecalLargeur=t.decalLargeur;
				this.eDecalHauteur=t.decalHauteur;
				this.eMaskHauteur=t.maskHauteur;								
				this.eMaskLargeur=t.maskLargeur;						
				this.eLargeurImage=t.largeurImage;	
				this.eHauteurImage=t.hauteurImage;		
				this.eNbMipMap=t.nbMipMap;	
				this.eMipMap=t.mipMap;		
			}
			
			this.bTexturePixels=null;
			if(this.material.bumpNormalTexture!=null && this.material.bumpNormalTexture.pixels != null)
			{
				t=this.material.bumpNormalTexture;
				//NORMAL/BUMP TEXTURE
				this.bTexturePixels=t.pixels;
				this.bDecalLargeur=t.decalLargeur;
				this.bDecalHauteur=t.decalHauteur;
				this.bMaskHauteur=t.maskHauteur;								
				this.bMaskLargeur=t.maskLargeur;						
				this.bLargeurImage=t.largeurImage;	
				this.bHauteurImage=t.hauteurImage;		
				this.bNbMipMap=t.nbMipMap;	
				this.bMipMap=t.mipMap;	
			}
			
			this.dTexturePixels=null;
			if(this.material.diffuseTexture!=null && this.material.diffuseTexture.pixels != null)
			{
				t=this.material.diffuseTexture;
				//DIFFUSE TEXTURE
				this.dTexturePixels=t.pixels;
				this.dDecalLargeur=t.decalLargeur;
				this.dDecalHauteur=t.decalHauteur;
				this.dMaskHauteur=t.maskHauteur;								
				this.dMaskLargeur=t.maskLargeur;						
				this.dLargeurImage=t.largeurImage;	
				this.dHauteurImage=t.hauteurImage;		
				this.dNbMipMap=t.nbMipMap;	
				this.dMipMap=t.mipMap;		
			}
					
			

			if(this.dTexturePixels!=null || this.bTexturePixels!=null)
			{
			
				double u0=polRef.u0*this.mapping.zoomU+this.mapping.ofsU;	
				double u1=polRef.u1*this.mapping.zoomU+this.mapping.ofsU;	
				double u2=polRef.u2*this.mapping.zoomU+this.mapping.ofsU;	
				double v0=polRef.v0*this.mapping.zoomV+this.mapping.ofsV;	
				double v1=polRef.v1*this.mapping.zoomV+this.mapping.ofsV;	
				double v2=polRef.v2*this.mapping.zoomV+this.mapping.ofsV;
								
		
				iUA = (y1*(u2-u1) + y2*(u0-u2) + y3*(u1-u0))  * den;
			    iUB = (-x1*(u2-u1) - x2*(u0-u2) - x3*(u1-u0))  * den;
				iUC = (y1*(u1*x3-u2*x2) + y2*(u2*x1-u0*x3) + y3*(u0*x2-u1*x1) ) * den;				    
				iUA-=(iUC*AxiD);
				iUB-=(iUC*BxiD);
				iUC=-iUC*CxiD;
				iUA*=iFocus;
				iUB*=iFocus;
								
				iVA = (y1*(v2-v1) + y2*(v0-v2) + y3*(v1-v0))  * den;
			    iVB = (-x1*(v2-v1) - x2*(v0-v2) - x3*(v1-v0))  * den;
				iVC = (y1*(v1*x3-v2*x2) + y2*(v2*x1-v0*x3) + y3*(v0*x2-v1*x1) ) * den;
				iVA-=(iVC*AxiD);
				iVB-=(iVC*BxiD);
				iVC=-iVC*CxiD;
				iVA*=iFocus;
				iVB*=iFocus;				
								    				
				//UPDATE TEXTURE/PIXEL RATIO (MIPMAP)		
				double px=1.0;
				double py=-iZA/iZB;
				double dx=px*this.zoomX;
				double dy=py*this.zoomY;
				if(dx<0.0) dx=-dx;
				if(dy<0.0) dy=-dy;
				
				double du=px*iUA+py*iUB;
				double dv=px*iVA+py*iVB;   													
				
				if(du<0)du=-du;
				if(dv<0)dv=-dv;
				du=du*t.largeurImage;
				dv=dv*t.hauteurImage;	
								
				//Update texture level using face normal
				this.currentTextureLevel+=c*2.5;
				this.dpDivZ=this.currentTextureLevel*(du+dv)/(dx+dy);
		
				this.incUDivZ=iUA*iZoomX;
				this.incVDivZ=iVA*iZoomX;
				
				//good one => this.dpDivZ=this.currentTextureLevel*(du+dv)/(dx+dy);
				
				//this.dpDivZ=this.currentTextureLevel*Math.abs(this.incUDivZ)+Math.abs(this.incVDivZ);
			}
		}
		
		
		this.drawFace3DPixels();
	}	
	
	private final void drawFace3DPixels()
	{	
			
		do
		{
			//Read face buffer		
			//Initialise y1,startXS & endXS to the horizontal segment to draw
			int startY=(this.renderPixelHeight-this.YBF-1)*this.renderPixelWidth;
			int startYB=this.YBF*this.renderPixelWidth;
			int startXS=this.XBF;
			this.startXY=startY+startXS;
			int startXYB=startYB+startXS;
			int endXS=zBuffer[startXYB];
			int poidDebut=(endXS>>16&0xFF);
			endXS&=0xFFFF;
			double y1=(0.5+this.YBF-this.renderPixelHeightDiv2)*this.iZoomY;	
			
					 
			//Initialise Z vars for current y
			double iZBC=y1 * iZB + iZC;
			double iZ0=iZBC-renderPixelWidthDiv2*iZA*iZoomX;
			double incIz=iZA*iZoomX;			
			
			//Initialise Light vars for current y
			double UBDivZY=y1*iUBB+iUBC;
			double VBDivZY=y1*iVBB+iVBC;	
			double WBDivZY=y1*iWBB+iWBC;

			
			//Initialise Mapping vars for current y
			double uDivZY=y1*iUB+iUC;
			double vDivZY=y1*iVB+iVC;
							

			//Loop in case of current segment have been clipped
			for(;startXS<endXS;)
			{
				int endX=endXS;
				int startX=startXS;
				double lenSegment=(endX-startX);
				
				
				double x1=1.0+startX-renderPixelWidthDiv2;
				double x2=-1.0+endX-renderPixelWidthDiv2;						
				x1*=iZoomX;
				x2*=iZoomX;
				//x1+=0.5;
				//x2+=0.5;
	
				double iZ=x1 * iZA + iZBC;
				double iZMax=iZ+lenSegment*incIz;
				double zMin=1.0/iZ;
				double zMax=1.0/iZMax;

				double iZSegStart=iZ;
				double iZSegEnd=iZMax;								
				double zSegStart=zMin;
				double zSegEnd=zMax;
				
				if(zMin>zMax)
				{
					double zTmp=zMin;
					zMin=zMax;
					zMax=zTmp;
				}
				
				//Clip segment to: Z1 <=> (2.0*Z1 OR 0.5*Z1)
				if(zMin!=zMax && lenSegment>8)
					if(zMin*4<zMax)
					{		
						if(incIz>0)	//Z Decrease
						{
							zMin=0.25*zSegStart;	
							zSegEnd=0.25*zSegStart;
							iZSegEnd=4*iZSegStart;
						}
						else		//Z Increase
						{
							zMax=4*zSegStart;
							zSegEnd=4*zSegStart;
							iZSegEnd=0.25*iZSegStart;
						}
						
						
						int endXZ=startX+(int)((iZSegEnd-iZSegStart)/incIz);
						
						if((endXZ-endX)>8)
						{
							if(endXZ>endX)
								endXZ=endX;
							endX=endXZ;
						}
						else
						{	
						 endX=startX+8;	
						}

						lenSegment=(endX-startX);
						iZSegEnd=iZSegStart+incIz*lenSegment;
						zSegEnd=1.0/iZSegEnd;						
						
					}
					
				double iLenSegment=1.0/lenSegment;
				if(lenSegment==1)
				{
				//	startXY++;
					//this.pixels[startXY++]=0xFF0000;
				}
				//System.out.println("lenSegment=1");
					
					
				//UPDATE END OF SEGMENT (CLIP IF NEEDED)	
				startXS=endX;
				int endXYC=startY+endX;
				
				//SET SCALLING FACTOR USING zMax
				double scale=0.1*zMax;
				
				/*
				 *INIT VARIABLE FOR INTERPOLATION
				 */
				 
				//Initialise Z interpolation vars
				double scaleIZ=2147483647.0*scale;   
				this.iZi=(int)(iZ*scaleIZ);
				this.incIzi=(int)(incIz*scaleIZ);	
				this.scaleZ=(int)(scale);
				
				if((this.faceRenderMode&DzzD.RM_LIGHT)!=0)
				{
					
					//Initialise Light interpolation vars
					double scaleUVW=2147483647.0*scale;
					this.incUBDivZi=(int)(incUBDivZ*scaleUVW);
					this.incVBDivZi=(int)(incVBDivZ*scaleUVW);
					this.incWBDivZi=(int)(incWBDivZ*scaleUVW);
					this.UBDivZi=(int)((x1*iUBA+UBDivZY)*scaleUVW);
					this.VBDivZi=(int)((x1*iVBA+VBDivZY)*scaleUVW);	
					this.WBDivZi=(int)((x1*iWBA+WBDivZY)*scaleUVW);
				}
				
							 	
				if((this.faceRenderMode&DzzD.RM_TEXTURE_DIFF)!=0 || 
				   (this.faceRenderMode&DzzD.RM_TEXTURE_BUMP)!=0)
				{
								 	
					//Initialise Mapping interpolation vars 
					double uDivZ=x1*iUA+uDivZY;
					double vDivZ=x1*iVA+vDivZY;
					double uStart=uDivZ*zSegStart;
					double uEnd=(uDivZ+incUDivZ*lenSegment)*zSegEnd;
					
					
					//SHIFT U MAPPING COORDINATE
					uEnd-=(int)uStart;
					uStart-=(int)uStart;
					uDivZ=uStart*iZSegStart;
					double uDivZEnd=uEnd*iZSegEnd;
					double newIncUDivZ=(uDivZEnd-uDivZ)*iLenSegment;
					
					
					//CALCULATE U SCALE
					this.scaleU=(int)(uEnd-uStart);
					if(scaleU<0) scaleU=-scaleU;
					if(scaleU==0) scaleU=1;	
					
					double vStart=vDivZ*zSegStart;
					double vEnd=(vDivZ+incVDivZ*lenSegment)*zSegEnd;
					
					
					//SHIFT V MAPPING COORDINATE
					vEnd-=(int)vStart;
					vStart-=(int)vStart;
					vDivZ=vStart*iZSegStart;
					double vDivZEnd=vEnd*iZSegEnd;
					double newIncVDivZ=(vDivZEnd-vDivZ)*iLenSegment;
					
					
					//CALCULATE V SCALE
					this.scaleV=(int)(vEnd-vStart);
					if(scaleV<0) scaleV=-scaleV;
					if(scaleV==0) scaleV=1;	
					
					double scaleUIZ=scaleIZ/scaleU;
					double scaleVIZ=scaleIZ/scaleV;
					
					
					//UPDATE UV INTERPOLATION VARIABLES											
					this.uDivZi=(int)(uDivZ*scaleUIZ);
					this.vDivZi=(int)(vDivZ*scaleVIZ);
					this.incUDivZi=(int)(newIncUDivZ*scaleUIZ);
					this.incVDivZi=(int)(newIncVDivZ*scaleVIZ);
					
					
					//CALCULATE TEXEL/PIXEL RATIO FOR FIRST PIXEL
					double ratiod=(dpDivZ*zSegStart);
					this.numMipMap=0;
					
					//GET MIPMAP LEVEL FOR THIS RATIO
					//TODO:ratio
					int ratio=(int)(ratiod);
					if(ratio>=0)
					{									
						if(ratio<this.log2.length)
							this.numMipMap=Drawer.log2[ratio];
						else
							this.numMipMap=32;
					}

				}
				
				for(;this.startXY<endXYC;)
				{
					this.endXY=endXYC;
					
					if((this.faceRenderMode&DzzD.RM_TEXTURE_DIFF)!=0 || 
					   (this.faceRenderMode&DzzD.RM_TEXTURE_BUMP)!=0)
					{
											
						//Clip MipMap level							
						if(this.numMipMap<0)
							this.numMipMap=0;
										
						if((this.faceRenderMode&DzzD.RM_TEXTURE_BUMP)!=0)
						{					
							this.normalPixels=bTexturePixels;
							this.decalLargeur=bDecalLargeur;
							this.decalHauteur=bDecalHauteur;
							this.maskHauteur=bMaskHauteur;								
							this.maskLargeur=bMaskLargeur;						
							this.largeurImage=bLargeurImage;	
							this.hauteurImage=bHauteurImage;
						}
												
						if((this.faceRenderMode&DzzD.RM_TEXTURE_DIFF)!=0)
						{					
							//Initialise texture information for current mipmap
							this.texturePixels=dTexturePixels;
							this.decalLargeur=dDecalLargeur;
							this.decalHauteur=dDecalHauteur;
							this.maskHauteur=dMaskHauteur;								
							this.maskLargeur=dMaskLargeur;						
							this.largeurImage=dLargeurImage;	
							this.hauteurImage=dHauteurImage;
						}
							
						this.endXMipMap=endX;
						
						//Set/Scale texture informations to current MipMap
						if(this.numMipMap>=1)
						{
							if(this.numMipMap>dNbMipMap)
								this.numMipMap=dNbMipMap;	
							if(dNbMipMap>0)
								if(dMipMap[this.numMipMap-1]!=null)
								{
									//Initialise texture information for current mipmap
									this.texturePixels=dMipMap[this.numMipMap-1];
									this.decalLargeur-=this.numMipMap;
									this.decalHauteur-=this.numMipMap;
									this.maskLargeur>>=this.numMipMap;						
									this.maskHauteur>>=this.numMipMap;
									this.largeurImage>>=this.numMipMap;		
									this.hauteurImage>>=this.numMipMap;
									
									//bump/normal texture
									if(this.normalPixels!=null)
										this.normalPixels=this.bMipMap[this.numMipMap-1];
										
										
								}
						}
						
						//Clip MipMap level
						int nMipMap=this.numMipMap;
						if(this.numMipMap>=dNbMipMap)
							this.numMipMap=dNbMipMap;
						
						//Calculate next MipMap begin offset
						if(incIz!=0.0)
						{
							if(iZ<iZMax)
								nMipMap=this.numMipMap;
							else
								nMipMap=this.numMipMap+1;
		
							double nextdp=1<<nMipMap;
							double nextIz=dpDivZ/nextdp;
							double nextX=(nextIz-iZ0)/incIz;
							//TODO: verif du +1
							this.endXMipMap=(int)nextX+1;
						}
						else					
							this.endXMipMap=endX;
						
						//Set next MipMap level
						if(iZ<iZMax)
							this.numMipMap--;
						else
							this.numMipMap++;
						
						//Update texture data
						this.decU=32-decalLargeur;	
						this.decV=32-decalHauteur;
						this.maskHauteur<<=decalLargeur;
						this.decV-=decalLargeur;
						this.maskUV=this.maskHauteur|this.maskLargeur;	
						this.decUBX=(this.decU-8);
						this.decVBX=(this.decV+decalLargeur)-8;
						this.uDiv2=1<<(this.decU-1);
						this.vDiv2=1<<(this.decV-1);

						
						//Clip rendered segment
						int endXYMipMap=startY+endXMipMap;
						
						if(endXYMipMap<=this.startXY) endXYMipMap=endXYC;
						if(endXYMipMap>endXYC) endXYMipMap=endXYC;
						
						//Test possible error
						if(maskLargeur==0)
							this.startXY=endXYMipMap;
							
						this.endXY=endXYMipMap;
					}						
						
						
						
						
					
					
					//	
					int zd=Drawer.iZ[(this.iZi&0x7FFF8000)>>15];				
					int zf=Drawer.iZ[(this.iZi+this.incIzi*(this.endXY-this.startXY)&0x7FFF8000)>>15];
				/*	if(zf==zd)
					{
						this.drawPixelsFullT();
						
						
						
						if((this.faceRenderMode&DzzD.RM_LIGHT)!=0)
						{
							
							
							
							
						}
						else
							
						this.pixels[startXY-1]=0xFFFFFF;
										
					
					}
					else*/
					{
						if((this.faceRenderMode&DzzD.RM_LIGHT)!=0)
							this.drawPixelsFull();
						else
							this.drawPixelsFull();
						
					}

					
				}
				//this.pixels[startXY-1]=0xFFFFFF00;
			}
					
			if(this.XBF>0 && ((this.antialias&1) != 0))
			{
				softBuffer[nbSoftPoint]=startY+this.XBF;
				softBufferR[nbSoftPoint++]=poidDebut;	
			}
			
		}
		while(this.nextSegment());
		
	}
	
	//Segment
	private int startXY;
	private int endXY;
	private int iZi;
	private int incIzi;
	private int scaleZ;
	
	//Texture normal/bump
	private int normalPixels[];
	
	//Texture environment
	private int envPixels[];	
	
	//Texture diffuse
	private int numMipMap;
	private int texturePixels[];
	private int decalLargeur;
	private int decalHauteur;
	private int maskHauteur;							
	private int maskLargeur;					
	private int largeurImage;
	private int hauteurImage;
	private int endXMipMap;
	private int maskUV;	
	
	private int decUBX;
	private int  decVBX;
	
	//Mapping
	private int uDivZi;
	private int incUDivZi;
	private int vDivZi;
	private int incVDivZi;
	private int scaleU;
	private int scaleV;
	private int decU;
	private int decV;
	private int uDiv2;//to center on pixel
	private int vDiv2;//to center on pixel
	
	//Light Vector
	private int UBDivZi;
	private int incUBDivZi;
	private int VBDivZi;
	private int incVBDivZi;
	private int WBDivZi;
	private int incWBDivZi;
	
	private int lastColor;
	private int co1=0;
	private int co2=0;
	private int co3=0;
	private int co4=0;

	private int no1=0;
	private int no2=0;
	private int no3=0;
	private int no4=0;
	
	private int envColor=0;
	//private int diffuseColor=0;
	private int alphaLevel=0;
	private int normal=0;
	private int pixelColor=0;
	private int u=0;
	private int v=0;
	
	
	//Runtime rendering value
	private int rZ;
	private int rDiffuseTexel;
	private int rNormalTexel;
	private int rEnvironmentTexel;
	private int rDiffuseLight;
	private int rSpecularColor;
	
	//UVBlin factors
	int f24;
	int f23;
	int f14;
	int f13;
	
	//Render flag
	boolean useDiffuseTexture;
	boolean useNormalTexture;
	boolean useEnvironmentTexure;
	
	
	private final int bilinUV(int no1,int no2,int no3,int no4)
	{
		int normal=((((no1&0xFF00FF)*this.f13+(no2&0xFF00FF)*this.f23+(no3&0xFF00FF)*this.f14+(no4&0xFF00FF)*this.f24)&0xFF00FF00)|
		(((no1&0x00FF00)*this.f13+(no2&0x00FF00)*this.f23+(no3&0x00FF00)*this.f14+(no4&0x00FF00)*this.f24)&0x00FF0000))>>8;
		return normal;
	}
	
	private final void computeTexture()
	{
		//BILIN FILTER on UV MAPPING
		int uz=(this.uDivZi>>12)*this.rZ*this.scaleU-this.uDiv2;
		int vz=(this.vDivZi>>12)*this.rZ*this.scaleV-this.vDiv2;
		this.uDivZi+=this.incUDivZi;
		this.vDivZi+=this.incVDivZi;

		u=uz>>decU&maskLargeur;
		v=vz>>decV&maskHauteur;							
		
		int uv1=v|u;
		int uv2=(uv1+1)&maskUV;
		int uv3=(uv1+largeurImage)&maskUV;
		int uv4=(uv3+1)&maskUV;
		int bX=(uz>>this.decUBX)&0xFF;
		int bY=(vz>>this.decVBX)&0xFF;
		
		//Comput bilin coeff
		this.f24=(bX*bY)>>8;
		this.f23=bX-f24;
		this.f14=bY-f24;
		this.f13=((bX^255)*(bY^255))>>8;//(f24^255);
		
		//f24&=0xFF;
		//f23&=0xFF;
		//f14&=0xFF;
		//f13&=0xFF;

		
		if(this.useNormalTexture)
			this.rNormalTexel=this.bilinUV(normalPixels[uv1],normalPixels[uv2],normalPixels[uv3],normalPixels[uv4]);
			
		if(this.useDiffuseTexture)
		{
			this.rDiffuseTexel=bilinUV(texturePixels[uv1],texturePixels[uv2],texturePixels[uv3],texturePixels[uv4]);
			this.pixelColor=this.rDiffuseTexel;
		}

	}
	
	private final void computeFastTexture()
	{
		//BILIN FILTER on UV MAPPING
		int uz=(this.uDivZi>>12)*this.rZ*this.scaleU-this.uDiv2;
		int vz=(this.vDivZi>>12)*this.rZ*this.scaleV-this.vDiv2;
		this.uDivZi+=this.incUDivZi<<1;
		this.vDivZi+=this.incVDivZi<<1;

		u=uz>>decU&maskLargeur;
		v=vz>>decV&maskHauteur;							
		
		int uv1=v|u;
		int uv2=(uv1+1)&maskUV;
		int uv3=(uv1+largeurImage)&maskUV;
		int uv4=(uv3+1)&maskUV;
		int bX=(uz>>this.decUBX)&0xFF;
		int bY=(vz>>this.decVBX)&0xFF;
		
		//Comput bilin coeff
		this.f24=(bX*bY)>>8;
		this.f23=bX-f24;
		this.f14=bY-f24;
		//this.f13=(bX^255)-f14;
		this.f13=((bX^255)*(bY^255))>>8;


		
		if(this.useNormalTexture)
			this.rNormalTexel=this.bilinUV(normalPixels[uv1],normalPixels[uv2],normalPixels[uv3],normalPixels[uv4]);
			
		if(this.useDiffuseTexture)
			this.pixelColor=this.rDiffuseTexel=bilinUV(texturePixels[uv1],texturePixels[uv2],texturePixels[uv3],texturePixels[uv4]);

	}	
	
	private final void computeLight()
	{
		//COMPUTE LIGHT VECTOR
		int WB=((this.WBDivZi>>13)*this.rZ>>23&0xFF)-128;
		int UB=((this.UBDivZi>>13)*this.rZ>>23&0xFF)-128;
		int VB=((this.VBDivZi>>13)*this.rZ>>23&0xFF)-128;
		this.UBDivZi+=this.incUBDivZi;
		this.VBDivZi+=this.incVBDivZi;				
		this.WBDivZi+=this.incWBDivZi;
		
		//COMPUTE VALUE TO NORMALIZE LIGHT VECTOR 
		int UVWN=Drawer.normalMap[(UB*UB+VB*VB+WB*WB)&0xFFFF]>>15;	

		if(this.useNormalTexture)				
		{												
			//READ NORMAL TEXTURE VECTOR							
			int R=((this.rNormalTexel&0xFF0000)>>16)-128;
			int V=((this.rNormalTexel&0xFF00)>>8)-128;
			int B=((this.rNormalTexel&0xFF))-128;

			UVWN=((R*UB+V*VB+B*WB)*UVWN)>>15;
			UVWN+=512;
		}
		else
		{
			UVWN=(WB*UVWN)>>8;
			UVWN+=512;
		}
		
		//READ MATERIAL RESPONSE FOR LIGHT VECTOR		
		int material=this.specularLightMap[UVWN];
		this.rSpecularColor=material&0xFFFFFF;
		this.rDiffuseLight=material>>>24;	
			
		
		
		if(this.useEnvironmentTexure)
		{
			//READ DIFFUSE TEXTURE
			int ue=(UB*UVWN)>>9;
			int ve=(VB*UVWN)>>9;
			
			ue+=128;
			ve+=128;
			
			this.rSpecularColor=0xFEFEFE&this.eTexturePixels[((ve&eMaskHauteur)<<eDecalLargeur)|ue&eMaskLargeur];
		}															
		
	}
	
	
	private final void computeFastLight()
	{
		//COMPUTE LIGHT VECTOR
		int WB=((this.WBDivZi>>13)*this.rZ>>23&0xFF)-128;
		int UB=((this.UBDivZi>>13)*this.rZ>>23&0xFF)-128;
		int VB=((this.VBDivZi>>13)*this.rZ>>23&0xFF)-128;
		this.UBDivZi+=this.incUBDivZi<<1;
		this.VBDivZi+=this.incVBDivZi<<1;				
		this.WBDivZi+=this.incWBDivZi<<1;
		
		//COMPUTE VALUE TO NORMALIZE LIGHT VECTOR 
		int UVWN=Drawer.normalMap[(UB*UB+VB*VB+WB*WB)&0xFFFF]>>15;	

		if(this.useNormalTexture)				
		{												
			//READ NORMAL TEXTURE VECTOR							
			int R=((this.rNormalTexel&0xFF0000)>>16)-128;
			int V=((this.rNormalTexel&0xFF00)>>8)-128;
			int B=((this.rNormalTexel&0xFF))-128;

			UVWN=((R*UB+V*VB+B*WB)*UVWN)>>15;
			UVWN+=512;
		}
		else
		{
			UVWN=(WB*UVWN)>>8;
			UVWN+=512;
		}
		
		//READ MATERIAL RESPONSE FOR LIGHT VECTOR		
		int material=this.specularLightMap[UVWN];
		this.rSpecularColor=material&0xFFFFFF;
		this.rDiffuseLight=material>>24&0xFF;		
		
		
		if(this.useEnvironmentTexure)
		{
			//READ DIFFUSE TEXTURE
			int ue=(UB*UVWN)>>9;
			int ve=(VB*UVWN)>>9;
			
			ue+=128;
			ve+=128;
			
			this.rSpecularColor=0xFEFEFE&this.eTexturePixels[((ve&eMaskHauteur)<<eDecalLargeur)|ue&eMaskLargeur];
		}							
		
	}	

	private final void drawPixelsFull()
	{
		this.rDiffuseLight=0xFF;
		this.rSpecularColor=0x0;
		this.pixelColor=this.rDiffuseTexel;
		try
		{
			this.useEnvironmentTexure=(this.faceRenderMode&DzzD.RM_TEXTURE_ENV)!=0;
			this.useDiffuseTexture=(this.faceRenderMode&DzzD.RM_TEXTURE_DIFF)!=0;
			this.useNormalTexture=(this.faceRenderMode&DzzD.RM_TEXTURE_BUMP)!=0;
			boolean renderTexture=this.useDiffuseTexture || this.useNormalTexture;
			boolean renderLight=(this.faceRenderMode&DzzD.RM_LIGHT)!=0;
			boolean renderAlpha=(this.alphaLevel!=0);
					
			//this.incIzi<<=1;
			//this.incUDivZi<<=1;
			//this.incVDivZi<<=1;
			//this.incUBDivZi<<=1;
			//this.incVBDivZi<<=1;
			//this.incWBDivZi<<=1;
			int n=0;
			
			//TODO: NEED OPTIMISE - IF MUST BE PUT OUTSIDE
			if(renderTexture)
			{
			
				if(renderLight)
				{
					while(this.startXY<this.endXY)
					{
						this.rZ=Drawer.iZ[this.iZi>>>16];
						this.iZi+=this.incIzi;
						this.computeTexture();
						this.computeLight();
						int diffuseColorLight=((((this.rDiffuseTexel&0xFF00FF)*this.rDiffuseLight)&0xFF00FF00|((this.rDiffuseTexel&0xFF00)*this.rDiffuseLight)&0xFF0000))>>8&0xFEFEFE;
						this.pixelColor=diffuseColorLight+this.rSpecularColor;
						this.pixelColor|=((pixelColor>>8)&0x010101)*0xFF;
						this.rPixels[startXY++]=this.pixelColor|0xFF000000;
					}
				}
				else
				{
					while(this.startXY<this.endXY)
					{
						this.rZ=Drawer.iZ[this.iZi>>>16];
						this.iZi+=this.incIzi;
						this.computeTexture();
						int diffuseColorLight=((((this.rDiffuseTexel&0xFF00FF)*this.rDiffuseLight)&0xFF00FF00|((this.rDiffuseTexel&0xFF00)*this.rDiffuseLight)&0xFF0000))>>8&0xFEFEFE;
						this.pixelColor=diffuseColorLight+this.rSpecularColor;
						this.pixelColor|=((pixelColor>>8)&0x010101)*0xFF;
						this.rPixels[startXY++]=this.pixelColor|0xFF000000;
					}
					
				}
			}
			else
			{
				if(renderLight)
				{
					while(this.startXY<this.endXY)
					{
						this.rZ=Drawer.iZ[this.iZi>>>16];
						this.iZi+=this.incIzi;
						this.computeLight();
						int diffuseColorLight=((((this.rDiffuseTexel&0xFF00FF)*this.rDiffuseLight)&0xFF00FF00|((this.rDiffuseTexel&0xFF00)*this.rDiffuseLight)&0xFF0000))>>8&0xFEFEFE;
						this.pixelColor=diffuseColorLight+this.rSpecularColor;
						this.pixelColor|=((pixelColor>>8)&0x010101)*0xFF;
						this.rPixels[startXY++]=this.pixelColor|0xFF000000;
					}
				}
				else
				{
					while(this.startXY<this.endXY)
					{
						this.rZ=Drawer.iZ[this.iZi>>>16];
						this.iZi+=this.incIzi;
						int diffuseColorLight=((((this.rDiffuseTexel&0xFF00FF)*this.rDiffuseLight)&0xFF00FF00|((this.rDiffuseTexel&0xFF00)*this.rDiffuseLight)&0xFF0000))>>8&0xFEFEFE;
						this.pixelColor=diffuseColorLight+this.rSpecularColor;
						this.pixelColor|=((pixelColor>>8)&0x010101)*0xFF;
						this.rPixels[startXY++]=this.pixelColor|0xFF000000;
					}
					
				}

				
			}
			/*
			while(this.startXY<this.endXY)
			{			
				//COMPUTE Z VALUE
				this.rZ=Drawer.iZ[this.iZi>>>16];
				this.iZi+=this.incIzi;

				if(renderTexture)
					this.computeTexture();
					
				//if(n++<3)	
				if(renderLight)
					this.computeLight();				
				*/
				
				/*
				if((n++&1)==0)
				{
					this.rZ=Drawer.iZ[this.iZi>>>16];//this.rZ=Drawer.iZ[(this.iZi&0x7FFF8000)>>15];
					this.iZi+=this.incIzi<<1;
					if(renderTexture)
						this.computeFastTexture();
						
					if(renderLight)
						this.computeFastLight();
				}
				*/
				
				
				//if(rDiffuseLight<50)
				//rDiffuseLight=50;
				
			/*	
				//HERE WE KNOW diffuseLight & specularColor
				int diffuseColorLight=((((this.rDiffuseTexel&0xFF00FF)*this.rDiffuseLight)&0xFF00FF00|((this.rDiffuseTexel&0xFF00)*this.rDiffuseLight)&0xFF0000))>>8&0xFEFEFE;
				this.pixelColor=diffuseColorLight+this.rSpecularColor;
				this.pixelColor|=((pixelColor>>8)&0x010101)*0xFF;
			*/	
			
				//ADD AMBIENT
				/*
				this.pixelColor&=0xFEFEFE;
				this.pixelColor+=0x400000;
				this.pixelColor|=((this.pixelColor>>8)&0x010101)*0xFF;
				*/
				
				
				//WRITE PIXEL
				/*this.rPixels[startXY++]=this.pixelColor|0xFF000000;
							
			}*/

		}
		catch(ArrayIndexOutOfBoundsException aioobe)
		{
			Log.log(aioobe);
		}
		catch(NullPointerException npe)
		{
			Log.log(npe);
			
		}	
		this.startXY=this.endXY;	
	}
	
//FOG
/*
int fog=256-((this.scaleZ*z)>>16);//;((((diffuseColor&0xFF00FF)*diffuseLight)&0xFF00FF00|((diffuseColor&0xFF00)*diffuseLight)&0xFF0000))>>8&0xFEFEFE;
if(fog<0) fog=0;
pixelColor=((((pixelColor&0xFF00FF)*fog)&0xFF00FF00|((pixelColor&0xFF00)*fog)&0xFF0000))>>8&0xFEFEFE;
*/


/*
//BLUR
lastColor=this.pixels[startXY];
pixelColor=((lastColor&0xFEFEFE)+(pixelColor&0xFEFEFE))>>1;
*/


	
	public final void antialiasPixels()
	{
		if((this.antialias&1)!=0)
		{
			for(int ns=0;ns<nbSoftPoint;ns++)
			{
				int ofs=this.softBuffer[ns];
				int p1=this.rPixels[ofs-1];	
				int p2=this.rPixels[ofs];
				int poidDebut=this.softBufferR[ns];
				p1=((((p1&0xFF00FF)*poidDebut)&0xFF00FF00|((p1&0xFF00)*poidDebut)&0xFF0000))>>8;
				poidDebut=255-poidDebut;					
				p2=((((p2&0xFF00FF)*poidDebut)&0xFF00FF00|((p2&0xFF00)*poidDebut)&0xFF0000))>>8;					
				int p12=((p1&0xFEFEFE)+(p2&0xFEFEFE));
				this.softBufferR[ns]=p12;
			}
			for(int ns=0;ns<nbSoftPoint;ns++)
			{
				int ofs=this.softBuffer[ns];
				this.rPixels[ofs]=this.softBufferR[ns];
			}
		}
			
		//if(this.antialias>1)
		//	this.pixels=pixels;
		
		if(this.antialias>1)
		{
			//pixels=this.aPixels;
	
			int antialias=this.antialias&0xFE;
			switch(antialias)
			{		
				case 2:
					for(int y=0;y<this.renderPixelHeight;y++)
					{
						int startY=y*(this.renderPixelWidth>>1);
						int startXY=startY;
						int endXY=startY+(this.renderPixelWidth>>1);
						int inputXY=startY<<1;
						
						while(startXY<endXY)
						{
							int p1=rPixels[inputXY]&0xFEFEFE;
							int p2=rPixels[inputXY+1]&0xFEFEFE;
							this.pixels[startXY]=(p1+p2)>>1;
							inputXY+=2;
							startXY++;
						}			
					}	
				break;
				
				case 4:
					for(int y=0;y<(this.renderPixelHeight>>1);y++)
					{
						int startY=y*this.renderPixelWidth;
						int startXY=startY;
						int endXY=startY+this.renderPixelWidth;
						int inputXY=startY<<1;
						while(startXY<endXY)
						{
							int p1=rPixels[inputXY]&0xFEFEFE;
							int p2=rPixels[inputXY+this.renderPixelWidth]&0xFEFEFE;
							
							int p=(p1+p2)>>1;
							
							this.pixels[startXY]=p;
							inputXY++;
							startXY++;
						}			
					}	
				break;	
						
				case 6:
					for(int y=0;y<(this.renderPixelHeight>>1);y++)
					{
						int startY=y*(this.renderPixelWidth>>1);
						int startXY=startY;
						int endXY=startY+(this.renderPixelWidth>>1);
						int inputXY=startY<<2;
						while(startXY<endXY)
						{
							
							int p1=rPixels[inputXY];
							int p2=rPixels[inputXY+1];
							int p3=rPixels[inputXY+this.renderPixelWidth];
							int p4=rPixels[inputXY+this.renderPixelWidth+1];
							
							int prb=(((p1&0xFF00FF)+(p2&0xFF00FF)+(p3&0xFF00FF)+(p4&0xFF00FF))>>2)&0xFF00FF;
							int pv=(((p1&0x00FF00)+(p2&0x00FF00)+(p3&0x00FF00)+(p4&0x00FF00))>>2)&0x00FF00;
							
							
							this.pixels[startXY]=prb|pv;
							inputXY+=2;
							startXY++;
						}			
					}	
				break;
			}
		}		
	}

	private final void initPixelsBuffer(int largeur,int hauteur)
	{
		this.useMIS=true;
		
		try 
		{
			Class c = Class.forName("java.awt.image.BufferedImage");
			PixelsBufferBI pbFront = new PixelsBufferBI(largeur,hauteur);
			this.image=pbFront.getImage();
			this.pixels=pbFront.getPixels();
			this.useMIS=false;
		}
		catch (ClassNotFoundException e)
		{
		}
   		

		if(this.useMIS)
		{			
			
			this.pixels = new int[largeur*hauteur];
			//DirectColorModel colorType=new DirectColorModel(32, 0xff0000, 0xff00, 0xff,0xff000000);
			DirectColorModel colorType=new DirectColorModel(24, 0xff0000, 0xff00, 0xff);
			//this.imageMemoire = new MemoryImageSource(largeur, hauteur,colorType, this.pixels, 0, largeur);
			this.imageMemoire = new MemoryImageSource(largeur, hauteur,colorType, this.pixels, 0, largeur);
			this.imageMemoire.setAnimated(true);
			this.imageMemoire.setFullBufferUpdates(false);
			this.image = Toolkit.getDefaultToolkit().createImage(imageMemoire);			
			this.useMIS=true;
		}
	}
	
	public final void drawPixelsOnCanvas(Canvas canvas)
	{
		Graphics g=canvas.getGraphics();
		
		if(this.useMIS)		
			this.imageMemoire.newPixels();

		// pulled this out of the g!=null test
		Render2D.PCanvas pCanvas = (Render2D.PCanvas)canvas;
		pCanvas.image = this.image;
		
		System.out.println("PCanvas.image: "+pCanvas.image);

//		((Render2D.PCanvas)canvas).image=this.image;						
		
		if(g!=null)
		{
			canvas.update(g);
		}
	}
	

	
}