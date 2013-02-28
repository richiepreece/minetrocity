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

package net.dzzd.extension.jogl;

import net.dzzd.access.*;
import net.dzzd.core.*;
import net.dzzd.DzzD;
import net.dzzd.utils.*;

import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import java.nio.*;


public final class Render3DJOGL extends Render3D //ONLY FOR PASSIVE //implements GLEventListener 
{	
	private GLContext context;
	//private GLDrawable drawable;
	
	private GLCanvas canvas;
	private GL gl;
	private GLU glu;
	private IScene3D scene;
	private Point3D rotation;
	private float light0Pos[];
	private float light0Val[];
	private float matAmbient[];
	private float matDiffuse[];
	private float matSpecular[];
	
	private CompiledMesh3D compiledMeshes[];
    private CompiledMaterial compiledMaterials[];
    private CompiledTexture compiledTextures[];
	
	//Inner super class for compiled JOGL object
	class CompiledJOGL
	{
		int idJOGL;
		CompiledJOGL()
		{
			this.idJOGL=-1;
		}
	}
	
	//Inner class for compiled JOGL Mesh3DOctree
	class CompiledMesh3DOctree extends CompiledJOGL
    {
    	Mesh3DOctree tree;
    	
    	CompiledMesh3DOctree(Mesh3DOctree tree)
    	{
    		this.tree=tree;
    	}
    	
    }
    
    //Inner class for compiled JOGL Mesh3D
	class CompiledMesh3D extends CompiledJOGL
	{
		Mesh3D mesh;
		CompiledMesh3DOctree compiledMesh3DOctrees[];
		
		CompiledMesh3D(Mesh3D mesh)
		{
			super();
			this.mesh=mesh;
			
			if(mesh.getMesh3DOctree()!=null)
			{
				int nbChildrens=1+mesh.getMesh3DOctree().getNbChildren(true);
				this.compiledMesh3DOctrees=new CompiledMesh3DOctree[nbChildrens];
				IMesh3DOctree m[]=mesh.getMesh3DOctree().getMesh3DOctreeArray(new Mesh3DOctree[nbChildrens]);
				for(int x=0;x<nbChildrens;x++)
					this.compiledMesh3DOctrees[x]=new CompiledMesh3DOctree((Mesh3DOctree)m[x]);
				
			}
			
		}
	}

    //Inner class for compiled JOGL Light3D
	class CompiledLight3D extends CompiledJOGL
	{
		Light3D light;
		
		
		CompiledLight3D(Light3D light)
		{
			super();
			this.light=light;
		}
	}
	
	//Inner class for compiled JOGL Material
	class CompiledMaterial extends CompiledJOGL
	{
		Material material;
		
		CompiledMaterial(Material material)
		{
			super();
			this.material=material;
		}
	}
	
	//Inner class for compiled JOGL Texture
	class CompiledTexture extends CompiledJOGL
	{
		Texture texture;
		
		CompiledTexture(Texture texture)
		{
			super();
			this.texture=texture;
		}
	}	
	
	
	ITexture currentTexture=null;
	IMaterial currentMaterial=null;
	boolean textureEnabled=false;
	boolean lightEnabled=false;
	
	
	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public Render3DJOGL() throws Throwable
	{
		super();
		
		GLCapabilities glCaps = new GLCapabilities();
		
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);
		glCaps.setDoubleBuffered(true);
		glCaps.setHardwareAccelerated(true);

		this.canvas = new GLCanvas(glCaps);
		this.canvas.setAutoSwapBufferMode(false);
		this.glu=new GLU();
		
		//ONLY FOR ACTIVE
		this.context=this.canvas.getContext();
		this.gl = this.context.getGL();
		

		//ONLY FOR PASSIVE
		//this.canvas.addGLEventListener(this);
				
		this.directInput=new DirectInput(this.canvas);
		rotation=new Point3D();
		light0Pos=new float[4];
		light0Val=new float[4];
		matAmbient=new float[4];
		matDiffuse=new float[4];
		matSpecular=new float[4];
		
		this.initCompiledBuffers();
	}
	
	private void initCompiledBuffers()
	{
		this.compiledMeshes=new CompiledMesh3D[65536];
		this.compiledMaterials=new CompiledMaterial[1024];
		this.compiledTextures=new CompiledTexture[1024];
	}
	
	public void setSize(int viewPixelWidth,int viewPixelHeight,int maxAntialias)
	{
		super.setSize(viewPixelWidth,viewPixelHeight,maxAntialias);
		this.canvas.setSize(viewPixelWidth,viewPixelHeight);
	}		
	
	public void renderScene3D(IScene3D scene)
	{
		if(scene==null) return;
		this.scene=scene;
		
		//ONLY FOR PASSIVE
		//this.canvas.display();
		
		//ONLY FOR ACTIVE
		this.makeContentCurrent();
		super.renderScene3D(scene);	
	}

	protected void startFrame(IScene3D scene)
	{
		//Log.log(this.getClass(),"Start Frame (JOGL):");		
		this.initRender3DJOGL();
		this.setCamera3D(); 
		//Log.log(this.getClass(),"Start Frame (JOGL):");      
	}
	
	protected void renderFrame(IScene3D scene)
	{
		//Log.log(this.getClass(),"Render Frame (JOGL):");
		super.renderFrame(scene);
		//Log.log(this.getClass(),"Render Frame (JOGL) - OK");
	}
		
	protected void endFrame(IScene3D scene)
	{
		//Log.log(this.getClass(),"End Frame (JOGL) - OK");
		//ONLY FOR ACTIVE
		this.context.release();

		if(this.isScreenUpdateEnabled)
			this.canvas.swapBuffers();	
			
		//Log.log(this.getClass(),"End Frame (JOGL) - OK");	
	}
	
	protected void setCurrentMesh3D(Mesh3D mesh)
	{
		super.setCurrentMesh3D(mesh);
		
		this.cMesh3D.getAxis3D().getRotationXZY(rotation);
		float rx=(float)(rotation.getX()*180.0/Math.PI);
		float ry=(float)(rotation.getY()*180.0/Math.PI);
		float rz=(float)(rotation.getZ()*180.0/Math.PI);

		IPoint3D pos=this.cMesh3D.getAxis3D().getOrigin();
		float px=(float)pos.getX();
		float py=(float)pos.getY();
		float pz=(float)pos.getZ();

		IPoint3D pivot=this.cMesh3D.getPivot();
		float pivotx=(float)pivot.getX();
		float pivoty=(float)pivot.getY();
		float pivotz=(float)pivot.getZ();	
		
		gl.glLoadIdentity();
		gl.glScalef(1,1,-1);
		gl.glTranslatef(0,0,0);
		gl.glTranslatef(px,py,pz);
		gl.glRotatef(-ry,0,1,0);		
		gl.glRotatef(-rz,0,0,1);
		gl.glRotatef(-rx,1,0,0);
	}
		
	protected void setMesh3DToZBuffer(IMesh3D mesh)
	{
		//this.prepareMesh3DLocalLight3DBuffer(mesh.getScene3D(),(Mesh3D)mesh);
		this.setMesh3D(mesh);
	}
	
	protected void setMesh3DOctreeToZBuffer(IMesh3DOctree tree)
	{
		this.setMesh3DOctree(tree);
	}
	
	protected void prepareMesh3DLocalLight3DBuffer(IScene3D scene,Mesh3D ob)
	{
		super.prepareMesh3DLocalLight3DBuffer(scene,ob);
		this.setLights();
	}	
	
	private void setLights()
	{
		gl.glEnable(GL.GL_LIGHT0);
		
		light0Val[0]=1.0f;
		light0Val[1]=1.0f;
		light0Val[2]=1.0f;
		light0Val[3]=1.0f;
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE,light0Val,0);
		
		
		light0Val[0]=0.0f;
		light0Val[1]=0.0f;
		light0Val[2]=0.0f;
		light0Val[3]=1.0f;
		
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT,light0Val,0);
			
					
		light0Pos[0]=(float)-mesh3DLocalLight3DBuffer[0].azx;
		light0Pos[1]=(float)-mesh3DLocalLight3DBuffer[0].azy;
		light0Pos[2]=(float)-mesh3DLocalLight3DBuffer[0].azz;
		light0Pos[3]=0.0f;		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION,light0Pos,0);
				
		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE); 	
								
	}
		
	protected int setFaces3DToZBuffer(IFace3D faces[],int faceNum)
	{
		int nPol=faces.length; 
		int numP=0;	
		if(faceNum>=0)
		{
			numP=faceNum;
			nPol=faceNum+1;	
		}
		
		for(;numP<nPol;numP++)
		{
			
			IFace3D f=faces[numP];
			
			int allowedRenderMode=0;
			if(f.getMaterial()!=null)
			{
				allowedRenderMode|=DzzD.RM_LIGHT;
				if(f.getMaterial().getDiffuseTexture() !=null )
					allowedRenderMode|=DzzD.RM_TEXTURE_DIFF|DzzD.RM_TEXTURE_MIPMAP|DzzD.RM_TEXTURE_BILINEAR;
				if(f.getMaterial().getDiffuseTextureDetail() !=null )
					allowedRenderMode|=DzzD.RM_DETAIL_TEXTURE;
				
			}
			
			//System.out.println(this.cMesh3D);
			allowedRenderMode&=this.getRender3DModeFlags();
			allowedRenderMode&=this.cMesh3D.getRender3DModeFlags();
			int faceRenderMode=allowedRenderMode;
			
			
			this.setMaterial(f.getMaterial());				

			
			if(((faceRenderMode & DzzD.RM_LIGHT)!=0))
			{
				//if(!this.lightEnabled)
				{
					//this.setLights();
					gl.glEnable(GL.GL_LIGHTING);
					this.lightEnabled=true;
				}
			}
			else
			{
				//if(this.lightEnabled)
				{
					gl.glDisable(GL.GL_LIGHTING);
					this.lightEnabled=false;
				}				
			}
			//System.out.println(f.getMesh3D().getName());
			this.setTexture(f.getMaterial().getDiffuseTexture());		
			this.setFace3D(f);

		}
		return 1;
			
	}
		
	private void setFace3D(IFace3D f)
	{
			IPoint3D v0=f.getVertex3D0();
			IPoint3D v1=f.getVertex3D1();
			IPoint3D v2=f.getVertex3D2();							
				
			gl.glBegin(GL.GL_TRIANGLES);
			//if(litEnabled)
			gl.glNormal3f(f.getVertex3D0Nx(),f.getVertex3D0Ny(),f.getVertex3D0Nz());
			gl.glTexCoord2f(f.getMappingU(0),f.getMappingV(0));
			gl.glVertex3f((float)v0.getX(),(float)v0.getY(),(float)v0.getZ());
			
			//if(litEnabled)
			gl.glNormal3f(f.getVertex3D1Nx(),f.getVertex3D1Ny(),f.getVertex3D1Nz());
			gl.glTexCoord2f(f.getMappingU(1),f.getMappingV(1));
			gl.glVertex3f((float)v1.getX(),(float)v1.getY(),(float)v1.getZ());
			
			//if(litEnabled)
			gl.glNormal3f(f.getVertex3D2Nx(),f.getVertex3D2Ny(),f.getVertex3D2Nz());
			gl.glTexCoord2f(f.getMappingU(2),f.getMappingV(2));
			gl.glVertex3f((float)v2.getX(),(float)v2.getY(),(float)v2.getZ());
			gl.glEnd();		
	}
	
	protected void compileMesh3D(IMesh3D mesh)
	{
		Log.log("Compile Mesh3D (JOGL):" + mesh.getName());
		
		CompiledMesh3D compiledMesh = this.compiledMeshes[mesh.getId()];
		if(compiledMesh==null)
		{
			compiledMesh=new CompiledMesh3D((Mesh3D)mesh);
			this.compiledMeshes[mesh.getId()]=compiledMesh;	
		}
		int id=compiledMesh.idJOGL;
		this.cMesh3D=(Mesh3D)mesh;

		if(id<=0)
		{
			id=gl.glGenLists(1);
			compiledMesh.idJOGL=id;
		}
		
		if(mesh.getMesh3DOctree()==null)
		{
			gl.glNewList(id,GL.GL_COMPILE);
			this.setFaces3DToZBuffer(mesh.getFaces3D(),-1);
			gl.glEndList();			
		}
		Log.log("Compile Mesh3D (JOGL) - OK");
	}
	
	protected void compileMesh3DOctree(IMesh3DOctree tree)
	{
		Log.log("Compile Mesh3DOctree (JOGL):");
		/*
		Integer idProp=(Integer)tree.getProperty("IDOGL");

		int id=0;
		if(idProp !=null) 
			id=idProp.intValue();//releaseMesh3DOctree(tree);			
		if(id<=0)
		{
			id=gl.glGenLists(1);
			tree.setProperty("IDOGL",new Integer(id));
		}
				
		gl.glNewList(id,GL.GL_COMPILE);
		this.setFaces3DToZBuffer(tree.getFaces3D(),-1);
		gl.glEndList();	
		*/
		
		
		CompiledMesh3DOctree treec=this.compiledMeshes[tree.getMesh3D().getId()].compiledMesh3DOctrees[tree.getId()];
		int id=treec.idJOGL;			
		if(id<=0)
		{
			id=gl.glGenLists(1);
			treec.idJOGL=id;
		}
				
		gl.glNewList(id,GL.GL_COMPILE);
		this.setFaces3DToZBuffer(tree.getFaces3D(),-1);
		gl.glEndList();	
		Log.log("Compile Mesh3DOctree (JOGL) - OK");	
		
	}	
		
	protected void compileMaterial(IMaterial material)
	{
		Log.log("Compile Material (JOGL):" + material.getName());
		CompiledMaterial compiledMaterial = this.compiledMaterials[material.getId()];
		if(compiledMaterial==null)
		{
			compiledMaterial=new CompiledMaterial((Material)material);
			this.compiledMaterials[material.getId()]=compiledMaterial;
		}
		int id=compiledMaterial.idJOGL;			
		if(id<=0)
		{
			id=gl.glGenLists(1);
			compiledMaterial.idJOGL=id;
			//material.setProperty("IDOGL",new Integer(id));
		}
				
		gl.glNewList(id,GL.GL_COMPILE);
		IMaterial fm=material;//f.getMaterial();
		if(fm!=null)
		{
			//float mcolor[] = { 1.0f, 0.0f, 0.0f, 1.0f };
			//glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, mcolor);
			
			matAmbient[0]=0f;//((float)(fm.getAmbientColor()>>16&0xFF))*1.0f/255.0f;
			matAmbient[1]=0f;//((float)(fm.getAmbientColor()>>8&0xFF))*1.0f/255.0f;
			matAmbient[2]=0f;//((float)(fm.getAmbientColor()&0xFF))*1.0f/255.0f;
			matAmbient[3]=1;
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, matAmbient,0);
			
			
			if(fm.getDiffuseTexture()==null || fm.getDiffuseTexture().getPixels()==null)
			{
				matDiffuse[0]=((float)(fm.getDiffuseColor()>>16&0xFF))*1.0f/255.0f;
				matDiffuse[1]=((float)(fm.getDiffuseColor()>>8&0xFF))*1.0f/255.0f;
				matDiffuse[2]=((float)(fm.getDiffuseColor()&0xFF))*1.0f/255.0f;
				matDiffuse[3]=1;
				
			}
			else
			{
				matDiffuse[0]=1f;//((float)(fm.getDiffuseColor()>>16&0xFF))*1.0f/255.0f;
				matDiffuse[1]=1f;//((float)(fm.getDiffuseColor()>>8&0xFF))*1.0f/255.0f;
				matDiffuse[2]=1f;//((float)(fm.getDiffuseColor()&0xFF))*1.0f/255.0f;
				matDiffuse[3]=1;
			}
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, matDiffuse,0);
			
			float specPower=((float)fm.getSpecularLevel())*1.0f/255.0f;
			matSpecular[0]=specPower*((float)(fm.getSpecularColor()>>16&0xFF))*1.0f/255.0f;
			matSpecular[1]=specPower*((float)(fm.getSpecularColor()>>8&0xFF))*1.0f/255.0f;
			matSpecular[2]=specPower*((float)(fm.getSpecularColor()&0xFF))*1.0f/255.0f;
			matSpecular[3]=(1f);//
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, matSpecular,0);
			
			gl.glMateriali(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS,fm.getSpecularPower());
			
		}
		gl.glEndList();		
		Log.log("Compile Material (JOGL) - OK");
		
	}	
	
	protected void compileTexture(ITexture texture)
	{
		Log.log("Compile Texture (JOGL):" + texture.getName() + "("+texture.getId()+")");

		if(texture.getPixels()==null)
			return;
			
		CompiledTexture compiledTexture = this.compiledTextures[texture.getId()];
		if(compiledTexture==null)
		{
			compiledTexture=new CompiledTexture((Texture)texture);
			this.compiledTextures[texture.getId()]=compiledTexture;
		}
		int id=compiledTexture.idJOGL;	
		if(id<=0)
		{
			IntBuffer n=IntBuffer.allocate(1);
			gl.glGenTextures(1,n);
			n.rewind();
			id=n.get();
			compiledTexture.idJOGL=id;
			
		}			
		gl.glBindTexture(GL.GL_TEXTURE_2D ,id);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);	
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NICEST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NICEST);//GL_NEAREST);
		glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D,3,texture.getPixelsWidth(),texture.getPixelsHeight(),GL.GL_BGRA,GL.GL_UNSIGNED_BYTE,IntBuffer.wrap(texture.getPixels()));			
		Log.log("Compile Texture (JOGL) - OK");
	}
	
	private void initRender3DJOGL()
	{
 		if(this.isPixelUpdateEnabled)
 		{
		 
			if(scene.isBackgroundEnabled())
			{
				gl.glClearColor(
					  ((float)(scene.getBackgroundColor()>>16&0xFF))*1.0f/255.0f,
	                  ((float)(scene.getBackgroundColor()>>8&0xFF))*1.0f/255.0f,
	                  ((float)(scene.getBackgroundColor()&0xFF))*1.0f/255.0f,
	                  0f);
	        }
 		
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	
			gl.glDepthFunc(GL.GL_LESS);
		}
		else
		{
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);								
			gl.glDepthFunc(GL.GL_NEVER);
		}
	
		
		//SET PERSPECTIVE
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);		
		
		//SET ENABLE ZBUFFER TEST
		gl.glEnable(GL.GL_DEPTH_TEST );	
										
		//SET ENABLE CULL FRONT FACE
		gl.glEnable(GL.GL_CULL_FACE );									
		gl.glCullFace(GL.GL_FRONT);	
		
		this.currentTexture=null;
		this.currentMaterial=null;
		this.textureEnabled=false;
		this.lightEnabled=false;
			
		gl.glDisable(GL.GL_TEXTURE_2D);	
		gl.glDisable(GL.GL_LIGHTING);
	}

	private void setCamera3D()
	{		
		gl.glMatrixMode(GL.GL_PROJECTION);	
        gl.glLoadIdentity();				
		//double ratio2=this.zMin/this.focus;
		//gl.glFrustum(-1*ratio2,1*ratio2,-1*ratio*ratio2,1*ratio*ratio2,this.zMin,(float)Math.min(Float.MAX_VALUE,this.zMax)*ratio2);
		double ratio=this.zMin/this.focus;
		gl.glFrustum(-ratio/this.zoomX,ratio/this.zoomX,-ratio/this.zoomY,ratio/this.zoomY,this.zMin,(float)Math.min(Float.MAX_VALUE,this.zMax));
        gl.glMatrixMode(GL.GL_MODELVIEW);			
	}
	
	private void setLight3D(ILight3D light)
	{
		//TODO: Implement OGL light code here		
	}

	private void setMesh3D(IMesh3D mesh)
	{
		int idProp=this.compiledMeshes[mesh.getId()].idJOGL;
		gl.glCallList(idProp);
		
	}
		
	private void setMaterial(IMaterial material)
	{
		//if(this.currentMaterial==material)
		//	return;
		//this.currentMaterial=material;
		
		int idProp=this.compiledMaterials[material.getId()].idJOGL;
		gl.glCallList(idProp);
	}	
	
	private void setMesh3DOctree(IMesh3DOctree tree)
	{
		CompiledMesh3DOctree treec=this.compiledMeshes[tree.getMesh3D().getId()].compiledMesh3DOctrees[tree.getId()];
		int id=treec.idJOGL;	
		gl.glCallList(id);				
	}	
	
	private void setTexture(ITexture texture)
	{
		/*
		if(this.currentTexture==texture)
			return;
			*/
		this.currentTexture=texture;
		
		if(this.currentTexture==null || this.compiledTextures[this.currentTexture.getId()]==null)
		{
			gl.glDisable(GL.GL_TEXTURE_2D);
			this.textureEnabled=false;
			return;
		}
		
		//if(!this.textureEnabled)
		{
			gl.glEnable(GL.GL_TEXTURE_2D);
			this.textureEnabled=true;
		}
		//System.out.println(texture.getId());
		//System.out.println(texture.getName());
		int idProp=this.compiledTextures[this.currentTexture.getId()].idJOGL;
		gl.glBindTexture(GL.GL_TEXTURE_2D ,idProp);		
	}
	
	protected void disposeMesh3D(IMesh3D mesh)
	{
		Log.log("Dispose Mesh3D (JOGL) " + mesh.getName());
		this.releaseMesh3D(mesh);
		int n=mesh.getId();
		CompiledMesh3D nextCompiledMesh=this.compiledMeshes[n+1];
		do
		{
			this.compiledMeshes[n++]=nextCompiledMesh;
			nextCompiledMesh=this.compiledMeshes[n+1];
		}
		while(nextCompiledMesh!=null);
	}
	
	protected void disposeCamera3D(ICamera3D camera)
	{
		Log.log("Dispose Camera3D (JOGL) : " + camera.getName());
	}	
	
	protected void disposeLight3D(ILight3D light)
	{
		Log.log("Dispose Light3D (JOGL) : " + light.getName());
	}		
	
	protected void disposeTexture(ITexture texture)
	{
		Log.log("Dispose ITexture (JOGL) : " + texture.getName());
		this.releaseTexture(texture);
		int n=texture.getId();
		CompiledTexture nextCompiledTexture=this.compiledTextures[n+1];
		do
		{
			this.compiledTextures[n++]=nextCompiledTexture;
			nextCompiledTexture=this.compiledTextures[n+1];
		}
		while(nextCompiledTexture!=null);		
	}			
	
	protected void disposeMaterial(IMaterial material)
	{
		Log.log("Dispose Material (JOGL) : " + material.getName());
		this.releaseMaterial(material);
		int n=material.getId();
		CompiledMaterial nextCompiledMaterial=this.compiledMaterials[n+1];
		do
		{
			this.compiledMaterials[n++]=nextCompiledMaterial;
			nextCompiledMaterial=this.compiledMaterials[n+1];
		}
		while(nextCompiledMaterial!=null);
	}		
	
	private void releaseTexture(ITexture texture)
	{
		int n[]=new int[1];
		n[0]=this.compiledTextures[texture.getId()].idJOGL;
		gl.glDeleteTextures(1,IntBuffer.wrap(n));	
	}
	
	private void releaseMesh3D(IMesh3D mesh)
	{
		int idProp=this.compiledMeshes[mesh.getId()].idJOGL;
		gl.glDeleteLists(idProp,1);
		
	}
	
	private void releaseMesh3DOctree(IMesh3DOctree tree)
	{
		if(tree.getNbFace3D()==0)	
			return;
		CompiledMesh3DOctree treec=this.compiledMeshes[tree.getMesh3D().getId()].compiledMesh3DOctrees[tree.getId()];
		int id=treec.idJOGL;
		gl.glDeleteLists(id,1);		
	}	
	
	private void releaseMaterial(IMaterial material)
	{
		int idProp=this.compiledMaterials[material.getId()].idJOGL;
		gl.glDeleteLists(idProp,1);
		
	}	
	
	public String getImplementationName()
	{
		return "JOGL";
	}

	//ONLY FOR ACTIVE
	private void makeContentCurrent()
	{	
		try 
		{
			while (context.makeCurrent() == GLContext.CONTEXT_NOT_CURRENT) 
			{
				System.out.println("Context not yet current...");
				Thread.sleep(100);
			}
		}
		catch (InterruptedException e)
		{
			 e.printStackTrace(); 
		}
	} 	
	
	//ONLY FOR PASSIVE
    public void init(GLAutoDrawable glautodrawable)
    {
        this.gl = glautodrawable.getGL();
    }
	
	//ONLY FOR PASSIVE
	public void display(GLAutoDrawable drawable)
	{
		if(this.scene==null)
			return;
		super.renderScene3D(scene);		
	}
	
	//ONLY FOR PASSIVE
	public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) 
	{
	}

	//ONLY FOR PASSIVE
	public void displayChanged(GLAutoDrawable drawable, boolean b, boolean b1) 
	{
	}
		
}