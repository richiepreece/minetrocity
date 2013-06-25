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



package net.dzzd;

import net.dzzd.core.*;
import net.dzzd.access.*;
import net.dzzd.extension.loader.IExtensionLoader;
import net.dzzd.extension.loader.ExtensionLoader;


import java.awt.Component;

/** 
 * 3DzzD API general factory classes.
 * <p>
 *  <u><b>Overview:</b></u>
 * <p>
 *  This is 3DzzD API general factory 
 *   all constants used in 3DzzD API.
 * </p>
 * <p>
 *  You may use this class to create new 3DzzD API objects : <br>
 *  - Scene3DObject(Mesh,Camera,etc..)<br>
 *  - SceneObject(Material,Texture,etc..)<br>
 *  - Others(Face3D,Point3D,etc..)<br>
 *	 etc..<br>
 * </p>
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see DzzDApplet
 *	@see DzzDJSApplet
 */
public class DzzD
{
	/**
	 * Hidden constructor
	 */
	 private DzzD()
	 {
	 	
	 }
	
	/**
	 * Texture drawing mode : texture must be stretched. 
	 */
	final public static int TXT_STRETCH=0;	

	/**
	 * Texture drawing mode : texture must be tiled (repeat by x and y). 
	 */	
	final public static int TXT_TILE=1;
	
	/**
	 * Render3D background mode : Background is invisible. 
	 */
	final public static int BG_TRANSPARENT=0;

	/**
	 * Render3D background mode : Background use a solid color. 
	 */
	final public static int BG_COLOR=1;

	/**
	 * Render3D background mode : Background use a texture. 
	 */
	final public static int BG_TEXTURE=2;
	
	/**
	 * Progress state : unknow or not initialised. 
	 */
	final public static int PR_STATE_UNKNOW=0;
	
	/**
	 * Progress state : initialising/initialised. 
	 */
	final public static int PR_STATE_INIT=1;	
	
	/**
	 * Progress state : loading/loaded. 
	 */
	final public static int PR_STATE_LOAD=2;	

	/**
	 * Track3D interpolation key mode : no interpolation. 
	 */
	final public static int P4D_NONE=0;
	
	/**
	 * Track3D interpolation key mode : linear interpolation. 
	 */
	final public static int P4D_LINEAR=1;	
	
	/**
	 * Track3D interpolation key mode : cosin interpolation. 
	 */
	final public static int P4D_COSIN=2;	
	
	/**
	 * Track3D interpolation key mode : cubic interpolation. 
	 */
	final public static int P4D_CUBIC=3;		

	/**
	 * Render mode : enable/disable lighting. 
	 */
	final public static int RM_LIGHT=1;	
	
	/**
	 * Render mode : enable/disable materials. 
	 */
	final public static int RM_MATERIAL=2;

	/**
	 * Render mode : enable/disable diffuse texture. 
	 */
	final public static int RM_TEXTURE_DIFF=4;
	
	/**
	 * Render mode : enable/disable textures mipmap. 
	 */
	final public static int RM_TEXTURE_MIPMAP=8;
	
	/**
	 * Render mode : enable/disable bilinear filtering. 
	 */
	final public static int RM_TEXTURE_BILINEAR=16;
	
	/**
	 * Render mode : enable/disable detail textures. 
	 */
	final public static int RM_DETAIL_TEXTURE=32;
	
	/**
	 * Render mode : enable/disable lighting. 
	 */
	final public static int RM_LIGHT_FLAT=64;		
	
	/**
	 * Render mode : enable/disable bump texture. 
	 */
	final public static int RM_TEXTURE_BUMP=128;		
	
	/**
	 * Render mode : enable/disable env texture. 
	 */
	final public static int RM_TEXTURE_ENV=256;		
	
	/**
	 * Render mode : enable/disable fog
	 */
	final public static int RM_FOG=512;			

		
	/**
	 * Render mode : enable/disable all. 
	 */
	final public static int RM_ALL=0xFFFFFFFF-DzzD.RM_LIGHT_FLAT;
	
	/**
	 * Texture type : type RGB
	 */
	final public static int TT_RGB=1;
	
	/**
	 * Texture type : type ARGB
	 */
	final public static int TT_ARGB=2;	
	
	/**
	 * Texture type : type NORMAL RGB represent normal of the surface
	 */
	final public static int TT_NORMAL=3;	
	
	/**
	 * Texture type : type NORMAL RGB represent normal of the surface and A height
	 */
	final public static int TT_HNORMAL=4;		
	
	/**
	 * Texture type : type NORMAL RGB represent environment texture
	 */
	final public static int TT_ENV=5;			
	
	
	/**
	 * Jogl native source URL
	 */
	public static String extensionBaseURL=null;
		
	/**
	 * Allocate an return a new Render3D using the specified implementation.
	 *
	 * For now only predefined implementation name are allowed:<br>
	 * newRender3D("SOFT") : return the internal software renderer<br>
	 * newRender3D("JOGL") : return the internal JOGL hardware renderer <br>
	 * <br>
	 * if requested renderer is not available the internal software renderer is returned
	 *
	 * @param implementationName name of the implementation
	 * @return newly allocated Render3D
	 * @since 1.0
	 */
	public static IRender3D newRender3D(Class c,String implementationName,IProgressListener pl)
	{
		boolean nopl=false;
		if(pl==null)
		{
			pl=new ProgressListener();
			nopl=true;
		}
		pl.setStarted(true);
		if(implementationName.toUpperCase().equals("SOFT"))
			return new Render3DSW();
	
		if(implementationName.toUpperCase().equals("JOGL"))
		{
			try
			{
			
				IExtensionLoader loader=ExtensionLoader.getLoader(c);
				IRender3D r=(IRender3D)loader.loadExtension(DzzD.extensionBaseURL, 
															"DzzDExtensionJOGL.jar",
															"net.dzzd.extension.jogl.JOGLLoader",
															"DzzDExtension",
															pl,
															"net.dzzd.extension.jogl.Render3DJOGL");			
				
				//IRender3D r=(IRender3D)Class.forName("net.dzzd.extension.jogl.Render3DJOGL").newInstance();
				return r;
			}
			catch(Exception e)
			{
				pl.setProgress(pl.getMaximumProgress());
				pl.setFinished(true);
				pl.setError(true);
				e.printStackTrace();
				return null;
			}
			
		}
		pl.setProgress(pl.getMaximumProgress());
		pl.setFinished(true);
		pl.setError(false);
	
		return new Render3DSW();
	}
	
	
	public static IRender2D newRender2D(Class c,String implementationName,IProgressListener pl)
	{
		boolean nopl=false;
		if(pl==null)
		{
			pl=new ProgressListener();
			nopl=true;
		}
		pl.setProgress(0);
		pl.setFinished(false);
		pl.setError(false);
		if(implementationName.toUpperCase().equals("SOFT"))
			return new Render2DSW();
	
		if(implementationName.toUpperCase().equals("JOGL"))
		{
			try
			{
			
				IExtensionLoader loader=ExtensionLoader.getLoader(c);
				IRender2D r=(IRender2D)loader.loadExtension(DzzD.extensionBaseURL, 
															"DzzDExtensionJOGL.jar",
															"net.dzzd.extension.jogl.JOGLLoader",
															"DzzDExtension",
															pl,
															"net.dzzd.extension.jogl.Render2DJOGL");			
				return r;
			}
			catch(Exception e)
			{
				pl.setProgress(100);
				pl.setFinished(true);
				pl.setError(true);
				e.printStackTrace();
				return null;
			}
			
		}
		pl.setProgress(100);
		pl.setFinished(true);
		pl.setError(false);
	
		return new Render2DSW();
	}	

	/**
	 * Allocate an return a new URLTexture.
	 *
	 * @return newly allocated URLTexture
	 * @since 1.0
	 */
	public static IURLTexture newURLTexture()
	{
		IURLTexture obj=new URLTexture();
		return obj;
	}

	/**
	 * Allocate an return a new Material.
	 *
	 * @return newly allocated Material
	 * @since 1.0
	 */
	public static IMaterial newMaterial()
	{
		IMaterial obj=new Material();
		return obj;
	}
	
	/**
	 * Allocate an return a new MappingUV.
	 *
	 * @return newly allocated MappingUV
	 * @since 1.0
	 */
	public static IMappingUV newMappingUV()
	{
		IMappingUV obj=new MappingUV();
		return obj;
	}

	/**
	 * Allocate an return a new Mesh3D.
	 *
	 * @return newly allocated Mesh3D
	 * @since 1.0
	 */
	public static IMesh3D newMesh3D()
	{
		IMesh3D obj=new Mesh3D();
		return obj;
	}
	
	/**
	 * Allocate an return a new Mesh3D.
	 *
	 * @param vertice Vertex3D array 
	 * @param faces Face3D array  
	 *
	 * @return newly allocated Mesh3D
	 * @since 1.0
	 */
	public static IMesh3D newMesh3D(IVertex3D vertice[],IFace3D faces[])
	{
		IMesh3D obj=new Mesh3D(vertice,faces);
		return obj;
	}	

	/**
	 * Allocate an return a new Light3D.
	 *
	 * @return newly allocated Light3D
	 * @since 1.0
	 */
	public static ILight3D newLight3D()
	{
		ILight3D obj=new Light3D();
		return obj;
	}

	/**
	 * Allocate an return a new Camera3D.
	 *
	 * @return newly allocated Camera3D
	 * @since 1.0
	 */
	public static ICamera3D newCamera3D()
	{
		ICamera3D obj=new Camera3D();
		return obj;
	}

	/**
	 * Allocate an return a new Face3D.
	 *
	 * @return newly allocated Face3D
	 * @since 1.0
	 */
	public static IFace3D newFace3D()
	{
		IFace3D obj=new Face3D();
		return obj;
	}
	
	/**
	 * Allocate an return a new Face3D.
	 *
	 * @param v1 vertex 1
	 * @param v2 vertex 2
	 * @param v3 vertex 3
	 *
	 * @return newly allocated Face3D
	 * @since 1.0
	 */
	public static IFace3D newFace3D(IVertex3D v1,IVertex3D v2,IVertex3D v3)
	{
		IFace3D obj=new Face3D(v1,v2,v3);
		return obj;
	}	

	/**
	 * Allocate an return a new Point3D.
	 *
	 * @return newly allocated Point3D
	 * @since 1.0
	 */
	public static IPoint3D newPoint3D()
	{
		IPoint3D obj=new Point3D();
		return obj;
	}
	
	/**
	 * Allocate an return a new Vertex3D.
	 *
	 * @return newly allocated Vertex3D
	 * @since 1.0
	 */
	public static IVertex3D newVertex3D()
	{
		IVertex3D obj=new Vertex3D();
		return obj;
	}

	/**
	 * Allocate an return a new SolidSphere3D.
	 *
	 * @return newly allocated SolidSphere3D
	 * @since 1.0
	 */
	public static ISolidSphere3D newSolidSphere3D()
	{
		ISolidSphere3D obj=new SolidSphere3D();
		return obj;
	}

	/**
	 * Allocate an return a new SolidSphere3DResult.
	 *
	 * @return newly allocated SolidSphere3DResult
	 * @since 1.0
	 */
	public static ISolidSphere3DResult newSolidSphere3DResult()
	{
		ISolidSphere3DResult obj=new SolidSphere3DResult();
		return obj;
	}
	
	/**
	 * Allocate an return a new Track3D.
	 *
	 * @return newly allocated Track3D
	 * @since 1.0
	 */
	public static ITrack3D newTrack3D()
	{
		ITrack3D obj=new Track3D();
		return obj;
	}	

	/**
	 * Allocate an return a new Axis3D.
	 *
	 * @return newly allocated Axis3D
	 * @since 1.0
	 */
	public static IAxis3D newAxis3D()
	{
		IAxis3D obj=new Axis3D();
		return obj;
	}	

	/**
	 * Allocate an return a new Scene3DRender.
	 *
	 * @return newly allocated Scene3DRender
	 * @since 1.0
	 */
	public static IScene3DRender newScene3DRender()
	{
		IScene3DRender obj=new Scene3DRender();
		return obj;
	}	

	/**
	 * Allocate an return a new Scene3D.
	 *
	 * @return newly allocated Scene3D
	 * @since 1.0
	 */
	public static IScene3D newScene3D()
	{
		IScene3D obj=new Scene3D();
		return obj;
	}	
	
	/**
	 * Allocate an return a new DirectInput for the given component.
	 *
	 * @param component component to use for this directInput source
	 * @return newly allocated DirectInput
	 * @since 1.0
	 */
	public static IDirectInput newDirectInput(Component component)
	{
		IDirectInput obj=new DirectInput(component);
		return obj;
	}	

	/**
	 * Allocate an return a new Scene3DObjectAnimator.
	 *
	 * @return newly allocated Scene3DObjectAnimator
	 * @since 1.0
	 */
	public static IScene3DObjectAnimator newScene3DObjectAnimator()
	{
		IScene3DObjectAnimator obj=new Scene3DObjectAnimator();
		return obj;
	}		

	/**
	 * Allocate an return a new HeightMap3D.
	 *
	 * @param minLevel minimum tesselation level
	 * @param maxLevel maximum tesselation level
	 * @return newly allocated HeightMap3D
	 * @since 1.0
	 */
	public static IHeightMap3D newHeightMap3D(int minLevel,int maxLevel)
	{
		IHeightMap3D obj=new HeightMap3D(minLevel,maxLevel);
		return obj;
	}		

	/**
	 * Allocate an return a new URLHeightMap.
	 *
	 * @return newly allocated URLHeightMap
	 * @since 1.0
	 */
	public static IURLHeightMap newURLHeightMap()
	{
		IURLHeightMap obj=new URLHeightMap();
		return obj;
	}	
	
	/**
	 * Allocate an return a new Scene3DLoader
	 *
	 * @return newly allocated Scene3DLoader
	 * @since 1.0
	 */
	public static IScene3DLoader newScene3DLoader()
	{
		IScene3DLoader obj=new Scene3DLoader3DS();
		return obj;
	}		
	
	/**
	 * Allocate,load and return a new Font2D
	 * 
	 * @param baseURL URL location of the Font2D file
	 * @param fileName Font2D file name
	 * @return newly allocated Font2D
	 * @since 1.0
	 */
	public static IFont2D newFont2DFromURL(String baseURL,String fileName)
	{
		IFont2D obj=Font2D.load(baseURL,fileName);
		return obj;
	}			
 			
	/**
	 * Allocate and return a new Shape2D
	 * 
	 * @return newly allocated Shape2D
	 * @since 1.0
	 */
	public static IShape2D newShape2D()
	{
		IShape2D obj=new Shape2D();
		return obj;
	}			
 
 	/**
	 * Allocate and return a new Scene2D
	 * 
	 * @return newly allocated Scene2D
	 * @since 1.0
	 */
	public static IScene2D newScene2D()
	{
		IScene2D obj=new Scene2D();
		return obj;
	}			

 	/**
	 * Allocate and return a new Timer
	 * 
	 * @return newly allocated Timer
	 * @since 1.0
	 */
	public static ITimer newTimer()
	{

		return new net.dzzd.core.Timer();
	}		
	
 	/**
	 * Pause current thread for given millisecond time
	 * 
	 * @param ms time to pause current thread in ms
	 * @since 1.0
	 */
	public static void sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch(InterruptedException ie)
		{
		}
	}
}