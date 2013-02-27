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

/*
 * 3DS Scene Loader 
 *
 * @author: bruno.augier@dzzd.net
 * @website : http://dzzd.net
 * @Updated : 09/2008, blender 3ds export reading , bug fix
 * @Updated : 08/2007, ressource path improvment
 * @Updated : 12/2007, cleaning
 */


import net.dzzd.access.*;
import net.dzzd.utils.Log;
import net.dzzd.utils.io.IOManagerAsynch;
import java.util.Hashtable;

import net.dzzd.DzzD;

 
import java.io.*;

final public class Scene3DLoader3DS extends MonitoredSceneObject implements IScene3DLoader,Runnable
{
	
	//3DS Chunks ID
  	private final static int FILE_VERSION=0x0002;  		// File version
  	private final static int KEYFRAME_VERSION=0x0005;  	// Keyframe version
  	private final static int RGBF=0x0010; 		// Chunk Color RGB (Float*3)
  	private final static int RGBB=0x0011; 		// Chunk Color RGB (Byte*3)
  	private final static int RGBBG=0x0012;		// Chunk Color RGB (Byte*3) gamma correct ??
    private final static int RGBFG=0x0013;		// Chunk Color RGB (Float*3) gamma correct ??
  	private final static int PERCENTI=0x0030; 	// Chunk Percent Int 
  	private final static int PERCENTF=0x0031; 	// Chunk Percent Float
	private final static int MAIN3DS=0x4D4D;  	// Chunk Main
	
	 private final static int EDIT3DS=0x3D3D;  		// Chunk Editor
	 private final static int MESH_VERSION=0x3D3E; 	// Mesh version
	  private final static int UNIT=0x0100; 				//Unit
	  private final static int BGCOLOR=0x1200; 				//Background color
	  private final static int SHADOW_MAP_SIZE=0x1420;		//Shadow map size (10-4096)	  
 	  private final static int OBJECT=0x4000;   			// Chunk Object
 	  private final static int OBJECT_HIDDEN=0x4010;   			// Object hidden flag
 	  private final static int OBJECT_DONT_CAST_SHADOW=0x4012;	//Object dont cast shadow
 	  private final static int OBJECT_DONT_RECV_SHADOW=0x4017;	//Object dont recv shadow
	   private final static int LIGHT=0x4600;   				// Chunk Light 	  
 	   private final static int CAMERA=0x4700;   				// Chunk Camera
 	    private final static int CAMERA_ATMOS_RANGE=0x4720;   		// Chunk Camera atmospheric effect range
	   private final static int TRIMESH=0x4100; 				// Chunck Scene3DObject (TRIMESH)
	   private final static int VERTEXL=0x4110; 				// Chunck Vertex List
	   private final static int FACEL=0x4120;   				// Chunk Face List
	   private final static int SMOOTHL=0x4150;					//Chunk smooth group list
	   private final static int FACEM=0x4130;				// Chunk Material Face Assignement	
	   private final static int MAPPINGVL=0x4140;			//Chunck Mapping Coordinate
	  private final static int OBJAXES=0x4160;				//Chunck Axes Object
	  private final static int MATERIAL=0xAFFF;  			//Chunck Material 
	   private final static int MATERIALNAME=0xA000;			//Chunck Material Name
	   private final static int MATERIALAMBIENTCOLOR=0xA010;	//Chunck Material Ambient Color		
	   private final static int MATERIALDIFFUSECOLOR=0xA020;	//Chunck Material Diffuse Color			
	   private final static int MATERIALSPECULARCOLOR=0xA030;	//Chunck Material Specular Color			
	   private final static int MATERIALSHINEPERCENT=0xA040;	//Chunck Material Specular Level
	   private final static int MATERIALSHINEFPERCENT=0xA041;	//Chunck Material Specular Power
	   private final static int MATERIALTRANSPERCENT=0xA050;	//Chunck Material Alpha Level
	   private final static int MATERIALTRANSFPERCENT=0xA052;	//Chunck Material Alpha	Power		
	   private final static int MATERIALILLUMPERCENT=0xA084;	//Chunck Material self illume		
	   private final static int MATERIALTEXTUREDIFF=0xA200;		//Chunck Material Diffuse Texture		
	   private final static int MATERIALTEXTURESPEC=0xA204;		//Chunck Material Specular Texture	
	   private final static int MATERIALTEXTUREOPAC=0xA210;		//Chunck Material Opacity Texture	
	   private final static int MATERIALTEXTUREENV=0xA220;		//Chunck Material Environment Texture		   
	   private final static int MATERIALTEXTUREBUMP=0xA230;		//Chunck Material Bump Texture		
	   private final static int MATERIALTEXTURESHIN=0xA33C;		//Chunck Material Shininess Texture	
	   private final static int MATERIALTEXTUREILLUM=0xA33D;		//Chunck Material Shininess Texture	
	    private final static int MAPPINGFILE=0xA300;			//Chunck Material Texture File				
	    private final static int MAPPINGOFFSETU=0xA358;			//Chunck Material Texture U offset
	    private final static int MAPPINGOFFSETV=0xA35A;			//Chunck Material Texture V offset
	    private final static int MAPPINGSCALEU=0xA354;			//Chunck Material Texture U offset
	    private final static int MAPPINGSCALEV=0xA356;			//Chunck Material Texture V offset	    
	 private final static int KEYF3DS=0xB000;					//Chunck KeyFrame (Animation)
	  private final static int KEYFOBJFRAME=0xB002;				//Key frame object
	  private final static int KEYFCAMFRAME=0xB003;				//Key frame camera
	  private final static int KEYFCAMTARGETFRAME=0xB004;		//Key frame camera target
	  private final static int KEYFCAMFOVFRAME=0xB023;		//Key frame camera target
	  private final static int KEYFLIGHTFRAME=0xB005;			//Key frame light
	  private final static int KEYFSPOTLIGHTFRAME=0xB007;		//Key frame spot light
	  private final static int KEYFSPOTLIGHTTARGETFRAME=0xB006;	//Key frame spot light
	  private final static int KEYFHEADERACTIVE=0xB008;			//Key frame active frames range
	  private final static int KEYFHEADERCURRENT=0xB009;		//Key frame active frame
	  private final static int KEYFHEADERGLOBAL=0xB00A;			//Key frame globalheader
	   private final static int KEYFHIER=0xB010;				//Key frame header
	   private final static int KEYFNAME=0xB011;				//Instance name
	   private final static int KEYFPIVOT=0xB013;			//Pivot
	   private final static int KEYFBOX=0xB014;				//Box
	   private final static int KEYFTRANS=0xB020;			//Translation
	   private final static int KEYFROT=0xB021;				//Rotation
	   private final static int KEYFZOOM=0xB022;			//zoom
	   private final static int KEYFFOV=0xB023;				//FOV
	   private final static int KEYFROLL=0xB024;			//roll (light/cam)
	   private final static int KEYFCOL=0xB025;				//color (light)
	   private final static int KEYFMORPH=0xB026;			//morph 
	   private final static int KEYFHIDE=0xB029;			//hide
	   private final static int KEYFID=0xB030;				//Id object
	
	
	
	
	private Scene3DObject objets3D[];
	private Point3D key3DTrans[];
	private Point3D key3DRotAxe[];
	private double key3DRotAng[];
	private Point3D key3DZoom[];
	
	private Vertex3D vertice3D[][];
	//private Light3D lights3D[];
	//private Camera3D cameras3D[];
	//private Material materials[];
	private Texture textures[];
	

	//Keyframes
	public Scene3DObjectAnimator animators[];
	
	
	private byte[] data;				//3DS file byte buffer
	private ByteArrayInputStream input;	//Reader for byte
	
	private String mappingFileName;
	
	private String ressourcePath;
	private String baseURL;
	private String fileName;
	
	
	private Vertex3D cPoints3D[];		//Temporary Vertex Aray
	private float mappingU[];			//Temporary U Mapping Array coordinate
	private float mappingV[];			//Temporary V Mapping Array coordinate
	private Face3D cPolygones3D[];	//Temporary Face Array
	private String lastObjectName;
	private int nbObjet;
	
	private Scene3DObject tObjets3D[];
	private Scene3DObject cObjet;
	private int cObjetId;
	private Axis3D cAxe;
	private int nbMaterial;
	private int nbTexture;
	private Material tMateriaux3D[];
	private Material cMateriau;
	private MappingUV cMapping;

	private int nbBytesDecoded;
	
	private int meshVersion;
	private int fileVersion;
	private float unit;
	private int backgroundColor;
	
	private boolean isKeyFrame;
	private int keyFrameVersion;
	private int keyFrameRevision;
	private String keyFrameFileName;
	private long KeyFrameNbFrame;
	
	private int chunkStack[];
	private int chunkStackProof;

	private Hashtable textureCache=new Hashtable();

	public String toString()
	{
		String r="";
		r+=" File Version     : " + this.fileVersion +"\n";
		r+=" Mesh Version     : " + this.meshVersion +"\n";
		r+=" Units            : " + this.unit +"\n";
		r+=" Background Color : " + this.backgroundColor +"\n";
		if(isKeyFrame)
		{
			r+=" Keyframe Version     : " + this.keyFrameVersion +"\n";
			r+=" Keyframe Revision     :" + this.keyFrameRevision +"\n";
			r+=" Keyframe File Name   : " + this.keyFrameFileName +"\n";
			r+=" Keyframe Nb Frames   : " + this.KeyFrameNbFrame +"\n";
		}
		
		return r;
	}		

	public Scene3DLoader3DS()
	{
		super();
	}
	
	public void loadScene3D(String baseURL,String nom)
	{
		
		this.loadScene3D(baseURL,nom,baseURL);
    }
    
    public void loadScene3D(String baseURL,String fileName,String ressourcePath)
    {
   		this.baseURL=baseURL;
   		this.ressourcePath=ressourcePath;
		this.fileName=fileName;	
		this.reset();
		Thread process=new Thread(this);
	 	process.start(); 	
    }

	public void run()
    {
		this.loadAndDecode();
    }
    
    private void pushChunk(int chk)
    {
		this.chunkStack[this.chunkStackProof++]=chk;
    }
    
    private int popChunk()
    {
		return this.chunkStack[--this.chunkStackProof];
    }  
    
    private int getLastChunk()
    {
    	return this.chunkStack[this.chunkStackProof-1];
    }  
    
    private int getLastParentChunk()
    {
    	return this.chunkStack[this.chunkStackProof-2];
    }      

	public void reset()
	{
		super.reset();
		this.nbBytesDecoded=0;
		this.meshVersion=0;
		this.fileVersion=0;
		this.unit=0;
		this.backgroundColor=0;
		
		
		this.key3DTrans=new Point3D[16384];
		this.key3DRotAxe=new Point3D[16384];
		this.key3DRotAng=new double[16384];
		this.key3DZoom=new Point3D[16384];
		this.isKeyFrame=false;
		

		//ANIMATION
		this.animators=new Scene3DObjectAnimator[16384];

		this.cObjetId=-1;
		
		this.nbObjet=0;
		this.tObjets3D=new Scene3DObject[16384];
		this.nbMaterial=0;
		this.tMateriaux3D=new Material[16384];	
		this.nbTexture=0;
		this.textures=new Texture[16384];	
		this.chunkStackProof=0;
		this.chunkStack=new int[1024];		
	}
	
	private void loadAndDecode()
 	{
 		//LOAD 3DS FILE
		this.setAction(IProgressListener.ACTION_FILE_DOWNLOAD);
		ProgressListener pl=new ProgressListener();
				
		IOManagerAsynch aLoader = new IOManagerAsynch();
		aLoader.downloadData(this.baseURL+this.fileName, getClass(), null, pl, false);
		while(aLoader.running || !pl.getFinished())
		{
			this.setProgress((50*(pl.getProgress())/pl.getMaximumProgress()));
			try
			{
				Thread.sleep(5);				
			}
			catch(InterruptedException ie)
			{
				pl.setFinished(true);
				pl.setError(true);
				return;
			}
		}
		
		this.data=aLoader.getData();
		if(pl.getError() || this.data==null)
		{
			this.setError(true);
			this.setFinished(true);
			return;
		}
		
		
		//DECODE 3DS FILE
		this.input= new ByteArrayInputStream(this.data);
		this.setProgress(50);
		this.setAction(IProgressListener.ACTION_FILE_DECOMPRESS);
		int offset=0;
		while(offset<data.length)	
			offset+=decodeChunk(0);			
		
		//CREATE SCENE FROM LOADED SCENE (INIT WITH KEYFRAME IF AVAILABLE)
		objets3D=new Scene3DObject[nbObjet+1];
		objets3D[0]=new Mesh3D();
		objets3D[0].setName(this.fileName);

		double zx=1;
		double zy=1;
		double zz=1;
		
		for(int nbo=0;nbo<nbObjet;nbo++)
		{
			cObjet=tObjets3D[nbo];
			objets3D[nbo+1]=cObjet;	
			
			
			Axis3D cAxe=cObjet.axes;
			Point3D o=cAxe.origine;		
			Point3D ax=cAxe.axeX;
			Point3D ay=cAxe.axeY;
			Point3D az=cAxe.axeZ;
		
			//READ ZOOM FROM AXIS
			zx=ax.norm();
			zy=ay.norm();
			zz=az.norm();
			ax.add(o);
			ay.add(o);
			az.add(o);
			
			//if(cObjet instanceof Camera3D)
			//	System.out.println(cObjet.getPosition().toString());
			
			//NORMALIZE AXIS
			cAxe.normalize();
			
			if(cObjet instanceof Mesh3D)
			{
				Point3D vertex[]=((Mesh3D)cObjet).vertices3D;
				for(int np=0;np<vertex.length;np++)
					vertex[np].toLocalAxe(cAxe).zoom(1.0/zx,1.0/zy,1.0/zz);	
			}
			
			ax.sub(o);
			ay.sub(o);
			az.sub(o);
			
			//KEEP AXZ (TO KNOW IF ZOOM NEGATIVE)
			Point3D axz=new Point3D();
			axz.copy(ax).cross(ay);	
			
			
			//IF ZOOM NEGATIVE INVERT ALL AXIS AND FLIP OBJECT'S FACES NORMALS
			if(az.dot(axz)<0.0)
			{
				ax.mul(-1.0);
				ay.mul(-1.0);
				az.mul(-1.0);
				zx=-zx;
			   	zy=-zy;
			   	zz=-zz;
			}			
			ax.add(o);
			ay.add(o);
			az.add(o);

			//SET OBJECT ROTATION AND PIVOT FROM AXIS
			cObjet.axes.getPosition(cObjet.position);
			cObjet.axes.getRotationXZY(cObjet.rotation);
			
			//SET OBJECT ZOOM
			cObjet.zoom.set(zx,zy,zz);
		}
		
		//SET OBJECT AXIS IN WORLD SPACE WITH PIVOT,POSITION,ROTATION
		for(int nbo=1;nbo<objets3D.length;nbo++)
		{
			cObjet=objets3D[nbo];
			cObjet.axes.init();
			cObjet.axes.set(cObjet.pivot,cObjet.position,cObjet.rotation);

		}
		
		
		//UPDATE OBJECT HIERARCHY
		for(int nbo=1;nbo<objets3D.length;nbo++)
		{
			cObjet=objets3D[nbo];
			if(cObjet.parent==null)
				cObjet.parent=objets3D[0];
			cObjet.setParent(cObjet.parent);
		}		
		
		//UPDATE OBJECT FOR HIERARCHY NEW
		for(int nbo=1;nbo<objets3D.length;nbo++)
		{
			cObjet=objets3D[nbo];
			//cObjet.axes.push();
			IAxis3D a=DzzD.newAxis3D();
			a.copy(cObjet.axes);

			
			if(cObjet.parent==null)
				cObjet.parent=objets3D[0];
			cObjet.toLocalAxe(cObjet.parent.axes);
		//	if(cObjet.parent instanceof ICamera3D)
			//cObjet.axes.pop();
			cObjet.axes.copy(a);
			
		}			
	
		try
		{
	
			//UPDATE ALL OBJECT WITH FIRST KEYFRAMER KEY
			if(this.isKeyFrame)
			{
				for(int nbo=1;nbo<objets3D.length;nbo++)
				{
					cObjet=objets3D[nbo];
					if(cObjet.id==-1)
						continue;
					IAxis3D a=DzzD.newAxis3D();
					a.copy(cObjet.axes);
					//cObjet.axes.push();
					Point3D rot=this.key3DRotAxe[cObjet.id];
					if(rot!=null)	
					{
						double rota=this.key3DRotAng[cObjet.id];
						cObjet.axes.init();
						cObjet.axes.rotate(rota,-rot.x,-rot.y,-rot.z);
						cObjet.axes.getRotationXZY(cObjet.rotation);
						//cObjet.axes.pop();
						cObjet.axes.copy(a);
					}
					
					
					Point3D trans=this.key3DTrans[cObjet.id];
					if(trans!=null)	
					{
						cObjet.position.set(trans.x,trans.y,trans.z);
						if(cObjet.parent!=null)
							cObjet.position.add(cObjet.parent.pivot);
		
					}
					
					Point3D zoom=this.key3DZoom[cObjet.id];
					if(zoom!=null)	
					{
						cObjet.zoom.set(zoom.x,zoom.y,zoom.z);
					
					}									
					cObjet.axes.copy(a);
					//cObjet.axes.pop();
				}
			}
					
			//Add animator to object
			for(int nbo=1;nbo<objets3D.length;nbo++)
			{
				
				cObjet=objets3D[nbo];
				if(cObjet.id==-1)
						continue;
				if(this.animators[cObjet.id]!=null)
					cObjet.setScene3DObjectAnimator(this.animators[cObjet.id]);
				
			}
			
		}
		catch(ArrayIndexOutOfBoundsException aioobe )
		{
			Log.log(aioobe);
		}
		catch(NullPointerException npe)
		{
			Log.log(npe);
		}

		
		for(int nbo=1;nbo<objets3D.length;nbo++)
		{
			cObjet=objets3D[nbo];
			Point3D zoom=cObjet.zoom;
			cObjet.zoom(zoom.x,zoom.y,zoom.z);
		}
		
		
		this.setFinished(true);
		this.setProgress(100);
		return;
		
	}
	
	public IScene3DLoader getScene3DLoader()
	{
		return this;
	}
	
	public IScene3DObject[] getScene3DObjects()
	{
		return objets3D;
	}
	
	public ISceneObject[] getSceneObjects()
	{
		SceneObject s[]=new SceneObject[this.nbMaterial+this.nbTexture];
		for(int x=0;x<this.nbMaterial;x++)
			s[x]=this.tMateriaux3D[x];
		for(int x=0;x<this.nbTexture;x++)
			s[this.nbMaterial+x]=this.textures[x];
			return s;
			
	}
	
	public IMaterial[] getMaterials()
	{
		Material m[]=new Material[this.nbMaterial];
		for(int x=0;x<this.nbMaterial;x++)
		{
			m[x]=this.tMateriaux3D[x];
		}
		return m;
	}
	
	public ITexture[] getTextures()
	{
		Texture t[]=new Texture[this.nbTexture];
		for(int x=0;x<this.nbTexture;x++)
		{
			t[x]=this.textures[x];
		}
		return t;
	}
		
	private int decodeChunk(int prof)
	{

		int offset=6;
		int id=getUShort();
		int longueur=getInt();
		if(longueur<0)
			return 0;
			
		this.pushChunk(id);
		
	/*
		for(int x=0;x<prof;x++)
			System.out.print(" ");
		Log.log(prof + " - ID=" + Integer.toHexString(id) + " LONGUEUR=" + longueur );	
	
	*/
		switch(id)
		{
			case FILE_VERSION: 
			{
		
				long ver=getUInt();
				this.fileVersion=(int)ver;
				//System.out.println("FILE VERSION="+this.fileVersion);
				offset+=4;
			}
			break;
			
			case KEYFRAME_VERSION:
			{
				long ver=getUInt();
				this.keyFrameVersion=(int)ver;
				offset+=4;
				//System.out.println("KEYFRAME VERSION="+this.keyFrameVersion);
			}
			break;
			
			case MESH_VERSION: 
			{
				long ver=getUInt();
				this.meshVersion=(int)ver;
				offset+=4;
				//System.out.println("MESH VERSION="+this.meshVersion);
				
			}	
			break;
			/*
			case 0x0100:
			{
				double scl=(double) getFloat();
				offset+=4;
				System.out.println("MASTER SCALE="+scl);
			}	
			break;
			*/
			
			case UNIT: //MASTER_SCALE 1.0 <=> 1==1inch
				this.unit=getFloat();
				offset+=4;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
			break;
			
			case BGCOLOR:
				do
				{
					int colorBId=getUShort();
					int lBColor=getInt();
					offset+=6;
					switch(colorBId)
					{
						case RGBF:	
						case RGBFG:
							this.backgroundColor=readFloatColor();
						offset+=12;
						break;
						case RGBB:	
						case RGBBG:
							this.backgroundColor=readByteColor();
						offset+=3;
						break;						
					}				
				}
				while(offset<longueur);
			break;
			
			case OBJECT:
				lastObjectName=getString();
				offset+=lastObjectName.length()+1;
				
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				
				//TODO: 3DS LOADING MAY BE IT IS NOT NEEDED
				if(cObjet instanceof Mesh3D)
				{
				
				//	((Mesh3D)cObjet).buildVertexId();
				//	((Mesh3D)cObjet).buildFaceId();
				//	((Mesh3D)cObjet).removeDuplicateVertices();
		
				//	((Mesh3D)cObjet).buildFacesNormals();
				//	((Mesh3D)cObjet).buildSphereBoxAndCenter();
					//Auto octree
				//	((Mesh3D)cObjet).buildMesh3DOctree();
				}

			//	if(cObjet!=null)
				//cObjet.build();
			break;
			
			case OBJECT_HIDDEN:
				((Mesh3D)cObjet).setVisible(false);
			break;
			
			case OBJECT_DONT_CAST_SHADOW:
				if(cObjet instanceof Mesh3D)
					((Mesh3D)cObjet).setCastShadow(false);
			break;
			case OBJECT_DONT_RECV_SHADOW:
				if(cObjet instanceof Mesh3D)
					((Mesh3D)cObjet).setRecvShadow(false);			
			break;	
					
			case CAMERA_ATMOS_RANGE:
				float nearAtmosRange=getFloat();
				float farAtmosRange=getFloat();
				offset+=8;
			break;

			case CAMERA:
			{
				cObjet=new Camera3D();
				cObjet.id=-1;
				cObjet.nom=lastObjectName;				
				cAxe=new Axis3D();
				cObjet.axes=cAxe;
				tObjets3D[nbObjet]=cObjet;
				nbObjet++;
				
				float x=getFloat();	
				float y=getFloat();
				float z=getFloat();
				float tx=getFloat();	
				float ty=getFloat();
				float tz=getFloat();
				float rz=getFloat();
				float focus=getFloat();
				//System.out.println("focal lenght="+focus);
				double FOV=180.0*2.0 * Math.atan(44.1828/(focus * 2))/Math.PI;
				//System.out.println("FOV CALC="+FOV);
				
				
				
				cObjet.position.set(x,z,y);
				//cObjet.axes.add(x,z,y);
				//Log.log(cObjet.position.toString());
				
				cObjet.axes.origine.set(x,z,y);
				cObjet.axes.axeZ.set(tx-x,tz-z,ty-y);
				cObjet.axes.axeX.set(ty-y,0,-(tx-x));
				
				cObjet.axes.axeY.copy(cObjet.axes.axeZ).cross(cObjet.axes.axeX);

				cObjet.axes.axeX.normalize();
				cObjet.axes.axeY.normalize();
				cObjet.axes.axeZ.normalize();
				
				cObjet.rotation.z=rz;
				cObjet.pivot.set(0,0,0);		
				
				if(this.meshVersion==0)
					focus=(float)((Camera3D)cObjet).width;
				((Camera3D)cObjet).focus=focus;//rotation.rz=rz;
				offset+=32;
			
				//tObjets3D[nbObjet]=new Scene3DObject();
				//nbObjet++;
			
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			}
			break;
			
			case LIGHT:
			{
				float x=getFloat();	
				float y=getFloat();
				float z=getFloat();
				offset+=12;			
							
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			}
			break;	
					
			case TRIMESH:
				cObjet=new Mesh3D();
				cObjet.nom=lastObjectName;
				cObjet.id=-1;		
				tObjets3D[nbObjet]=cObjet;
				nbObjet++;
				cAxe=new Axis3D();
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				cObjet.axes=cAxe;
			break;
			
			case VERTEXL:
				int nbV=getUShort();
				offset+=2;
				cPoints3D=new Vertex3D[nbV];
				mappingU=new float[nbV];
				mappingV=new float[nbV];
			
				for(int nV=0;nV<nbV;nV++)
				{
					float x=getFloat();	
					float y=getFloat();
					float z=getFloat();
					cPoints3D[nV]=new Vertex3D(x,z,y);
					offset+=12;					
				}
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;
			
			case FACEL:
				int nbP=getUShort();
				offset+=2;
				cPolygones3D=new Face3D[nbP];
				for(int nP=0;nP<nbP;nP++)
				{
					int p1=getUShort();	
					int p2=getUShort();	
					int p3=getUShort();
					int info=getUShort();
					Vertex3D p3D[]=new Vertex3D[3];
					p3D[0]=cPoints3D[p3];
					p3D[1]=cPoints3D[p2];
					p3D[2]=cPoints3D[p1];					
					cPolygones3D[nP]=new Face3D(p3D[0],p3D[1],p3D[2]);
					cPolygones3D[nP].u0=mappingU[p3];
					cPolygones3D[nP].v0=mappingV[p3];
					cPolygones3D[nP].u1=mappingU[p2];
					cPolygones3D[nP].v1=mappingV[p2];
					cPolygones3D[nP].u2=mappingU[p1];
					cPolygones3D[nP].v2=mappingV[p1];					
					offset+=8;					
				}
				
				((Mesh3D)cObjet).vertices3D=cPoints3D;
				((Mesh3D)cObjet).faces3D=cPolygones3D;
				
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;
			
			case SMOOTHL:
				int nbpoly=cPolygones3D.length;
				for(int nP=0;nP<nbpoly;nP++)
				{
					int nbSmooth=getInt();
					cPolygones3D[nP].smoothGroupMask=nbSmooth;
					offset+=4;
				}
			break;	
					
			case FACEM:
				String nom=getString();
				offset+=nom.length()+1;
				cMateriau=getTempMateriauByName(nom);
				int nbF=getUShort();
				offset+=2;
				for(int nF=0;nF<nbF;nF++)
				{
					int numF=getUShort();
					cPolygones3D[numF].material=cMateriau;
					offset+=2;
				}
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			
			break;	
			
			case MAPPINGVL:	
				int nbMV=getUShort();
				offset+=2;
				for(int nV=0;nV<nbMV;nV++)
				{
					float u=getFloat();	
					float v=getFloat();
					mappingU[nV]=u;
					mappingV[nV]=-v;
					offset+=8;					
				}
				while(offset<longueur)
					offset+=decodeChunk(prof+1);											
			break;	
						
			case OBJAXES:
				double x=getFloat();		
				double y=getFloat();	
				double z=getFloat();
				cAxe.axeX.set(x,z,y);				
							
				x=getFloat();	
				y=getFloat();
				z=getFloat();
				cAxe.axeZ.set(x,z,y);
				
				x=getFloat();
				y=getFloat();
				z=getFloat();
				cAxe.axeY.set(x,z,y);
				
				x=getFloat();
				y=getFloat();				
				z=getFloat();
				cAxe.origine.set(x,z,y);
				
				offset+=48;				
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
			break;
			
			case MATERIAL:
				cMateriau=new Material();
				tMateriaux3D[nbMaterial]=cMateriau;
				nbMaterial++;	
				while(offset<longueur)
					offset+=decodeChunk(prof+1);											
			break;
			
			case MATERIALNAME:
				cMateriau.nom=getString();
				offset+=cMateriau.nom.length()+1;					
				while(offset<longueur)
					offset+=decodeChunk(prof+1);	
																	
			break;	
			
			case MATERIALAMBIENTCOLOR:										
				do
				{
					int colorAId=getUShort();
					int lAColor=getInt();
					offset+=6;
					switch(colorAId)
					{
						case RGBF:	
						case RGBFG:
							cMateriau.ambientColor=readFloatColor();
						offset+=12;
						break;
						case RGBB:	
						case RGBBG:
							cMateriau.ambientColor=readByteColor();
						offset+=3;
						break;						
					}				
				}
				while(offset<longueur);		
			break;
			
			case MATERIALDIFFUSECOLOR:
				do
				{
					int colorDId=getUShort();
					int lDColor=getInt();
					offset+=6;
					switch(colorDId)
					{
						case RGBF:	
						case RGBFG:
							cMateriau.diffuseColor=readFloatColor();
						offset+=12;
						break;
						case RGBB:	
						case RGBBG:
							cMateriau.diffuseColor=readByteColor();
						offset+=3;
						break;						
					}
				}
				while(offset<longueur);							
			break;	
					
			case MATERIALSPECULARCOLOR:
				do
				{
			
					int colorSId=getUShort();
					int lSColor=getInt();
					offset+=6;
					switch(colorSId)
					{
						case RGBF:	
						case RGBFG:
							cMateriau.specularColor=readFloatColor();
						offset+=12;
						break;
						case RGBB:	
						case RGBBG:
							cMateriau.specularColor=readByteColor();
						offset+=3;
						break;						
					}
				}
				while(offset<longueur);												
			break;	
			
			case MATERIALSHINEPERCENT:
				int shineId=getUShort();
				int lShine=getInt();
				offset+=6;
				switch(shineId)
				{
					case PERCENTF:
						cMateriau.specularPower=readFloatPercent();
						offset+=4;
					break;
					case PERCENTI:
						cMateriau.specularPower=readIntPercent();
						offset+=2;
					break;
				}
			break;	
			
			case MATERIALILLUMPERCENT:
				int illumId=getUShort();
				int lIllum=getInt();
				offset+=6;
				switch(illumId)
				{
					case PERCENTF:
						cMateriau.selfIlluminationLevel=readFloatPercent();
						offset+=4;
					break;
					case PERCENTI:
						cMateriau.selfIlluminationLevel=readIntPercent();
						offset+=2;
					break;
				}
				//Log.log(cMateriau.nom+" emi="+emi);
			break;
					
			case MATERIALSHINEFPERCENT:
				int shineFId=getUShort();
				int lShineF=getInt();
				offset+=6;
				switch(shineFId)
				{
					case PERCENTF:
						cMateriau.specularLevel=readFloatPercent()*255/100;
						offset+=4;
					break;
					case PERCENTI:
						cMateriau.specularLevel=(readIntPercent()*255)/100;
						offset+=2;
					break;										
				}
			break;
			
			case MATERIALTRANSPERCENT:
				int transId=getUShort();
				int lTrans=getInt();
				offset+=6;
				switch(transId)
				{
					case PERCENTF:
						cMateriau.alphaLevel=readFloatPercent()*255/100;
						offset+=4;
					break;
					case PERCENTI:
						cMateriau.alphaLevel=(readIntPercent()*255)/100;
						offset+=2;
					break;										
				}
			break;	
					
			case MATERIALTRANSFPERCENT:
				int transFId=getUShort();
				int lTransF=getInt();
				offset+=6;
				switch(transFId)
				{
					case PERCENTF:
						cMateriau.alphaFalloff=readFloatPercent()*255/100;
						offset+=4;
					break;
					case PERCENTI:
						cMateriau.alphaFalloff=(readIntPercent()*255)/100;
						offset+=2;
					break;										
				}
			break;			
			
			case MATERIALTEXTUREDIFF:
				cMapping=new MappingUV();			
				cMateriau.mapping=cMapping;			
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;
			
			case MATERIALTEXTUREBUMP:
				cMapping=new MappingUV();				
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;			
			
			case MATERIALTEXTUREENV:
				cMapping=new MappingUV();
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;				
			
			
			case MATERIALTEXTUREOPAC:
				cMapping=new MappingUV();
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;				

			case MATERIALTEXTURESPEC:
				cMapping=new MappingUV();
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;				

			case MATERIALTEXTURESHIN:
				cMapping=new MappingUV();
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;				

			case MATERIALTEXTUREILLUM:
				cMapping=new MappingUV();
				while(offset<longueur)
					offset+=decodeChunk(prof+1);			
			break;				
			
			
			case MAPPINGFILE:
				String fileName=getString();
				//System.out.println("MAPPINGFILE="+fileName);
				offset+=fileName.length()+1;
				
				String textureCacheKey=this.ressourcePath+fileName;
				URLTexture nTexture=(URLTexture)textureCache.get(textureCacheKey);
				if(nTexture==null)
				{
					nTexture=new URLTexture();
					nTexture.nom=fileName;
					nTexture.sourceFile=fileName;
					nTexture.baseURL=this.ressourcePath;
					textureCache.put(textureCacheKey,nTexture);
					this.textures[this.nbTexture++]=nTexture;
				}
				 
				
				//There cache will cause trouble if same texture used for different kind of map
				if(this.getLastParentChunk()==MATERIALTEXTUREDIFF)
				{
					nTexture.type=DzzD.TT_RGB;
					cMateriau.diffuseTexture=nTexture;
				}
				if(this.getLastParentChunk()==MATERIALTEXTUREBUMP)
				{
					nTexture.type=DzzD.TT_HNORMAL;
					cMateriau.bumpNormalTexture=nTexture;
					
				}				
				if(this.getLastParentChunk()==MATERIALTEXTUREENV)
				{
					nTexture.type=DzzD.TT_ENV;
					cMateriau.envTexture=nTexture;
					
				}				
				while(offset<longueur)
					offset+=decodeChunk(prof+1);																	
			break;

			case MAPPINGOFFSETU:
				float offseMappingU=getFloat();
				offset+=4;
				cMapping.ofsU=offseMappingU;	
			break;

			case MAPPINGOFFSETV:
				float offseMappingV=getFloat();
				offset+=4;
				cMapping.ofsV=offseMappingV;
			break;
			
			/*
			 * KEYFRAME BEGIN
			 */
			case KEYFHIER:
				//System.out.println("KEYFHIER");
				lastObjectName=getString();		//Name in mesh section		
				//System.out.println("Mesh section name="+lastObjectName);
				offset+=lastObjectName.length()+1;
				
				int info1=getUShort();
				int info2=getUShort();
				int idObjetParent=getShort();
				
				
				//System.out.println("------- info1: "+info1);
				//System.out.println("------- info2: "+info2);
				//System.out.println("------- idObjetParent parent:("+idObjetParent+")");
				
				offset+=6;
				
				if((this.meshVersion>=3)&&((info1&0x4000)==0)&&(!lastObjectName.equals("$$$DUMMY")))
				{
					//Log.log("------------ Instance From Object" + lastObjectName);
					cObjet=(Scene3DObject)getTempObjetByName(lastObjectName).getClone(false);
					cObjet.nom="Instance" + nbObjet ;// lastObjectName;
					cObjet.id=this.cObjetId;
					tObjets3D[nbObjet]=cObjet;
					nbObjet++;
				}
				else
				{
					if(lastObjectName.equals("$$$DUMMY"))
					{
						//System.out.println("New object");					
						cObjet=new Mesh3D();
						cObjet.nom="$$$DUMMY";
						cObjet.id=this.cObjetId;
						tObjets3D[nbObjet]=cObjet;
						nbObjet++;	
					}
					else
					{
						
						this.cObjet=getTempObjetByName(lastObjectName);
						if(this.cObjet instanceof Camera3D)
						{
							if(getLastParentChunk()==KEYFCAMTARGETFRAME)
							{
								//System.out.println("TARGET FOR "+lastObjectName);
								//this.cObjet.setTarget(DzzD.newPoint3D());
							}							
						}
					}
				}
					
				//System.out.println("ID objet==" + cObjetId);

				this.cObjet.id=this.cObjetId;
				if(idObjetParent!=65535)
				{
				
					Scene3DObject parent=getTempObjetById(idObjetParent);
					if(parent!=null)
						this.cObjet.parent=parent;
					//System.out.println(cObjet.getName()+" is child of "+cObjet.getParent().getName());
				}
				/*
				if(this.cObjet.parent!=null && !lastObjectName.equals("$$$DUMMY"))
					System.out.println("-" + lastObjectName+"("+ this.cObjet.id +"):parent:("+idObjetParent+")"+this.cObjet.parent.nom);
				*/
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				
			break;
			
			case KEYFNAME:// - Instance objects name.
				String instanceName=getString();
				//Log.log("instanceName="+instanceName);
				cObjet.nom=instanceName;
				offset+=instanceName.length()+1;
			break;
			
			case KEYFID:
				int idObjet=getUShort();
				
				offset+=2;
				this.cObjetId=idObjet;
				//System.out.println("ID OBJ="+cObjetId);
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
			break;		
				
			case KEYFTRANS:
				
				//Read Track Header
				int info=getShort();
				offset+=2;
				for(int niu=0;niu<8;niu++)
				{
					//Read Unknow TrackHeader info 8 byte
					byte infoUnknow=(byte)getByte();
					offset+=1;
				}
				
				//Read number ok keys
				int nbKey=(int)getUInt();
				offset+=4;
				
				Point3D cPosition=new Point3D();
				Point3D lastPosition=new Point3D();
				if(this.animators[cObjetId]==null)
					this.animators[cObjetId]=new Scene3DObjectAnimator();
				
				//Read all track
				for(int xKey=0;xKey<nbKey;xKey++)
				{
					//Read the frame number for this key
					int nKey=(int)getUInt();
					offset+=4;
					
					//Read Spline flag
					int splinef=getShort();
					offset+=2;	
					
					//Read Spline info depending on spline flag
					if((splinef&1)!=0)
					{
						//Read tension
						getFloat();
						offset+=4;
					}
					if((splinef&2)!=0)
					{
						//Read continuity
						getFloat();
						offset+=4;
					}
					if((splinef&4)!=0)
					{
						//Read bias
						getFloat();
						offset+=4;
					}
					if((splinef&8)!=0)
					{
						//Read ease to
						getFloat();
						offset+=4;
					}
					if((splinef&16)!=0)
					{
						//Read ease from
						getFloat();
						offset+=4;
					}																				
						
					//Read this key pos (global coordinate									
					x=getFloat();
					y=getFloat();	
					z=getFloat();
					if(xKey==0)
						this.key3DTrans[cObjetId]=new Point3D(x,z,y);
					
					
					cPosition.set(x,z,y);
					Point3D position=new Point3D();
					position.copy(cPosition);
/*
					if(cObjetId!=-1)
					{
					
						System.out.println(tObjets3D[cObjetId].getName());	
						System.out.println("pos="+position.toString());
					}
					*/
						
					this.animators[cObjetId].addKeyPosition(nKey*30,position);	
					lastPosition.copy(cPosition);
										
					offset+=12;	
				}
			break;
			
			case KEYFROT:
				int infor=getShort();
				offset+=2;
				for(int niu=0;niu<8;niu++)
				{
					byte infoUnknow=(byte)getByte();
					offset+=1;
				}
				int nbKeyr=(int)getUInt();
				offset+=4;			


				Axis3D cAxis=new Axis3D();
				cAxis.init();
				Point3D cRotation=new Point3D();
				Point3D lastRotation=new Point3D();

				if(this.animators[cObjetId]==null)
					this.animators[cObjetId]=new Scene3DObjectAnimator();
								
				for(int xKey=0;xKey<nbKeyr;xKey++)
				{
					int nKey=(int)getUInt();
					
					offset+=4;
					int infoAcceleration=getShort();
					offset+=2;										
					double a=getFloat();
					offset+=4;
					x=getFloat();
					y=getFloat();	
					z=getFloat();
					
					if(a>Math.PI && xKey!=0)
						a=2.0*Math.PI-a;
					
					
					if(xKey==0)
					{
						this.key3DRotAng[cObjetId]=a;
						this.key3DRotAxe[cObjetId]=new Point3D(x,z,y);
					}
					
					
					cAxis.rotate(a,-x,-z,-y);
					cAxis.getRotationXZY(cRotation);

					Point3D rotation=new Point3D();
					rotation.copy(cRotation);

					this.animators[cObjetId].addKeyRotation(nKey*30,rotation,new Point3D(-x,-z,-y),a);
										
					offset+=12;	
				}
				

			break;
			
			case KEYFZOOM:
				int infoz=getShort();
				offset+=2;
				for(int niu=0;niu<8;niu++)
				{
					byte infoUnknow=(byte)getByte();
					offset+=1;
				}
				int nbKeyz=(int)getUInt();
				offset+=4;		
				for(int xKey=0;xKey<nbKeyz;xKey++)
				{
					int nKey=(int)getUInt();
					offset+=4;
					int infoAcceleration=getShort();
					offset+=2;										
					x=getFloat();
					y=getFloat();	
					z=getFloat();
					if(xKey==0)
						this.key3DZoom[cObjetId]=new Point3D(x,z,y);
					offset+=12;	
				}
			break;			
			
			
			case KEYFCAMFOVFRAME:
				int infof=getShort();
				offset+=2;
				for(int niu=0;niu<8;niu++)
				{
					byte infoUnknow=(byte)getByte();
					offset+=1;
				}
				int nbKeyf=(int)getUInt();
				offset+=4;		
				for(int xKey=0;xKey<nbKeyf;xKey++)
				{
					int nKey=(int)getUInt();
					offset+=4;
					int infoAcceleration=getShort();
					offset+=2;										
					float fov=getFloat();
					if(this.cObjet instanceof Camera3D)
					{
						((Camera3D)this.cObjet).setFOV(fov);
					}
					
					offset+=4;	
				}
			break;				
						
			
			case KEYFPIVOT:
				x=getFloat();
				y=getFloat();	
				z=getFloat();
				offset+=12;		
				this.cObjet.pivot.set(x,z,y);
			break;
			
			case KEYFBOX:
				double x1=getFloat();
				double y1=getFloat();	
				double z1=getFloat();
				offset+=12;		
				double x2=getFloat();
				double y2=getFloat();	
				double z2=getFloat();				
				offset+=12;		
				this.cObjet.center.set((x1+x2)*0.5,(z1+z2)*0.5,(y1+y2)*0.5);
			break;	
					
			case KEYF3DS:
				this.isKeyFrame=true;
				//System.out.println("KEYF3DS");
				this.cObjetId=0;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				break;
				
			case KEYFHEADERACTIVE:
				long startf=getUInt();
				long endf=getUInt();
				//System.out.println("KEYFRAME START FRAME="+ startf);
				//System.out.println("KEYFRAME END FRAME="+ endf);
				offset+=8;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				break;	
									
			case KEYFHEADERCURRENT:
				long currentf=getUInt();
				//System.out.println("KEYFRAME CURRENT FRAME="+currentf);
				offset+=4;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				break;	
									
			case KEYFHEADERGLOBAL:
				this.keyFrameRevision=getShort();
				this.keyFrameFileName=getString();
				this.KeyFrameNbFrame=getUInt();
				offset+=2;
				offset+=this.keyFrameFileName.length()+1;
				offset+=4;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				break;	
						
			case MAIN3DS:
				this.cObjetId=0;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
			break;
			
			case EDIT3DS:
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
			break;
			
			case KEYFOBJFRAME:
				this.cObjetId++;
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
			break;
			
			case KEYFCAMFRAME:
				this.cObjetId++;
				//System.out.println("-----CAMERA-------");
				//System.out.println("ID1="+cObjetId);
				while(offset<longueur)
					offset+=decodeChunk(prof+1);
				//System.out.println("-----END CAMERA-------");
			break;	
			
			
			case KEYFCAMTARGETFRAME:
				this.cObjetId++;
				//System.out.println("-----CAMERA TARGET-------");
				//System.out.println("ID1="+cObjetId);
				//MUST DO SPECIAL DECODE OF CHILD TO NOT OVERWRITE CAMERA TRACK POS/ROT
				
				//while(offset<longueur)
				//	offset+=decodeChunk(prof+1);
				//System.out.println("-----END CAMERA TARGET-------");
			break;	
								
			default:
				
			break;
		}
		this.setProgress(50+(50*this.nbBytesDecoded)/this.data.length);
		skip(id,offset,longueur-offset);
		this.popChunk();
		return longueur;
	}
	
	private int readByteColor()
	{
		
		int r=getUByte();	
		int g=getUByte();	
		int b=getUByte();
		return (r<<16)|(g<<8)|b;					
	}
	private int readFloatColor()
	{
		
		int r=(int)(getFloat()*255);	
		int g=(int)(getFloat()*255);	
		int b=(int)(getFloat()*255);	
		return (r<<16)|(g<<8)|b;	
	}
	private int readIntPercent()
	{
		
		return getUShort();	
	}
	private int readFloatPercent()
	{
		return (int)(getFloat());			
	}
	
	private Material getTempMateriauByName(String nom)
	{
		for(int nM=0;nM<tMateriaux3D.length;nM++)	
		{
			if(tMateriaux3D[nM]!=null)
				if(tMateriaux3D[nM].nom.equals(nom))
				{
					return tMateriaux3D[nM];			
				}
		}
		return null;	
	}
	
	private Scene3DObject getTempObjetByName(String nom)
	{
		for(int nO=0;nO<tObjets3D.length;nO++)	
		{
			if(tObjets3D[nO]!=null)
			{
				if(tObjets3D[nO].nom!=null)
				if(tObjets3D[nO].nom.equals(nom))
				{
					return tObjets3D[nO];			
				}
			}
		}
		return null;	
	}	
	
	private Scene3DObject getTempObjetById(int id)
	{
		for(int nO=tObjets3D.length-1;nO>=0;nO--)	
			if(tObjets3D[nO]!=null)
			{
				//System.out.println(tObjets3D[nO].nom+":"+tObjets3D[nO].id);
			
				if(tObjets3D[nO].id==id)
				{
					
					return tObjets3D[nO];			
				}
			}
		return null;	
	}		
		
	private int getByte()
	{
		this.nbBytesDecoded+=1;
		return input.read();	
	}

	private int getUByte()
	{
		int b=getByte();
		if(b<0)
			b=-b;
		return b;
	}
	
	private int getInt()
	{
		int a=(int)getByte();	
		int b=(int)getByte();	
		int c=(int)getByte();	
		int d=(int)getByte();	
		return (d<<24)|(c<<16)|(b<<8)|a;	
	}
	
	private long getUInt()
	{
		long a=(long)getUByte();	
		long b=(long)getUByte();	
		long c=(long)getUByte();	
		long d=(long)getUByte();	
		return (d<<24)|(c<<16)|(b<<8)|a;	
	}

	private int getShort()
	{
		int a=(int)getByte();	
		int b=(int)getByte();	
		return (b<<8)|a;	
	}
	
	private int getUShort()
	{
		int a=(int)getUByte();	
		int b=(int)getUByte();	
		return (b<<8)|a;	
	}	
	
	private float getFloat()
	{
		int a=(int)getByte();	
		int b=(int)getByte();	
		int c=(int)getByte();	
		int d=(int)getByte();	
		
		return Float.intBitsToFloat((d<<24)|(c<<16)|(b<<8)|a);	
	}	
	
	private String getString()
	{
		String s=new String();
		int x;
		x=getByte();
		while(x!=0)
		{
			s+=(char)x;
			x=getByte();				
		}
		return s;
	}

	private void skip(int chunk,int offset,int nb)
	{
		if(nb==0) return;
		//Log.log(this, "START SKIP "+nb +  " AT OFFSET " + offset +"   LAST CHUNK:" + Integer.toHexString(chunk));
		this.nbBytesDecoded+=nb;
		for(int n=0;n<nb;n++)
		{
			int x=getByte();
			//System.out.println((char)x+","+x);
		}
		//System.out.println("END SKIP");
		
		//input.skip(nb);	
	}	
	
	
}