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

import java.util.Vector;

public class Scene implements IScene
{
	protected Material[] materials3D;	
	protected int nbMaterial;			
	protected Texture[] textures;		
	protected int nbTexture;
	
	protected Vector monitoredSceneObjects;
	protected Vector removeSceneObjectConsumer;
	
	public Scene()
	{
		this.setSceneBufferSize(1024,1024);	
		this.clearScene();
		this.monitoredSceneObjects=new Vector();
		this.removeSceneObjectConsumer=new Vector();
	}
	
	public int getNbRemoveSceneObjectConsumer()
	{
		return removeSceneObjectConsumer.size();
	}
	
	public void addRemoveSceneObjectConsumer(IRemoveSceneObjectConsumer obj)
	{
		this.removeSceneObjectConsumer.addElement(obj);
	}
	
	public IRemoveSceneObjectConsumer getRemoveSceneObjectConsumer(int idx)
	{
		return (IRemoveSceneObjectConsumer)this.removeSceneObjectConsumer.elementAt(idx);
	}
	
	public void setSceneBufferSize(int maxMaterial,int maxTexture)
	{
		this.materials3D=new Material[maxMaterial];
		this.textures=new Texture[maxTexture];
		this.clearScene();
	}		
	
	public void clearScene()
	{
		
		for(int nm=0;nm<this.nbMaterial;nm++)
			this.materials3D[nm]=null;
		this.nbMaterial=0;
		
		for(int nt=0;nt<this.nbTexture;nt++)
			this.textures[nt]=null;
		this.nbTexture=0;
		
		Material material=new Material();
		material.setName("Default 3DzzD Material");
		this.addMaterial(material);
	}	
	
    public int getNbSceneObject()
	{
		int nbSceneObject=this.nbTexture;
		nbSceneObject+=this.nbMaterial;
		return nbSceneObject;
		
	}	
	
    public int getNbMaterial()
	{
		return this.nbMaterial;
	}

    public int getNbTexture()
	{
		return this.nbTexture;
	}

	public void addMaterial(IMaterial m)
	{
		if(((Material)m).scene==this) return;
		((Material)m).scene=this;

		this.materials3D[this.nbMaterial]=(Material)m;
		((Material)m).setId(this.nbMaterial);
		m.build();
		this.nbMaterial++;				
	}	

	public void addTexture(ITexture texture)
	{
		if(((Texture)texture).scene==this) return;
		((Texture)texture).scene=this;

		this.textures[this.nbTexture]=(Texture)texture;
		texture.setId(this.nbTexture);
		this.startMonitorSceneObject((Texture)texture);
		texture.build();
		this.nbTexture++;				
	}	
	
	public void addSceneObject(ISceneObject object)
	{
		if(object instanceof IMaterial)
		{
			this.addMaterial((IMaterial)object);
			return;
		}
		if(object instanceof ITexture)
		{
			this.addTexture((ITexture)object);
			return;
		}
	}	

	public void addSceneObjects(ISceneObject objects[])
	{
		for(int x=0;x<objects.length;x++)
			this.addSceneObject(objects[x]);
	 }

	public void removeSceneObject(ISceneObject object)
	{
		if(object==null)
			return;
		
		if(object instanceof ITexture)
		{
			this.removeTextureById(object.getId());
		}
		
		if(object instanceof IMaterial)
		{
			this.removeMaterialById(object.getId());
		}				
	}
	
	public void removeMaterialById(int id)
	{
		if(this.materials3D[id]==null)
			return;
		ISceneObject object=this.materials3D[id];
		//this.materials3D[id]=null;	
		this.nbMaterial--;
		for(int n=id;n<this.getNbMaterial();n++)
		{
			this.materials3D[n]=this.materials3D[n+1];
			this.materials3D[n].setId(n);	
		}
		for(int x=0;x<this.getNbRemoveSceneObjectConsumer();x++)
			this.getRemoveSceneObjectConsumer(x).removeSceneObject(object);
		
	}	
	
	public void removeTextureById(int id)
	{
		if(this.textures[id]==null)
			return;
		ISceneObject object=this.textures[id];
		//this.textures[id]=null;	
		this.nbTexture--;
		for(int n=id;n<this.getNbTexture();n++)
		{
			this.textures[n]=this.textures[n+1];
			this.textures[n].setId(n);	
		}	
		for(int x=0;x<this.getNbRemoveSceneObjectConsumer();x++)
			this.getRemoveSceneObjectConsumer(x).removeSceneObject(object);
	}	
	
	public IMaterial getMaterialById(int id)
	{
		return this.materials3D[id];
	}	
	
	public IMaterial getMaterialByName(String name)
	{
		for(int x=0;x<this.nbMaterial;x++)
		{
			if(this.materials3D[x]!=null)
				if(this.materials3D[x].nom!=null)
					if(this.materials3D[x].nom.equals(name))
						return this.materials3D[x];
		}
		return null;
	}

	public ITexture getTextureById(int id)
	{
		return this.textures[id];
	}
		
	public ITexture getTextureByName(String name)
	{
		for(int x=0;x<this.nbTexture;x++)
		{
			if(this.textures[x]!=null)
				if(this.textures[x].nom!=null)
					if(this.textures[x].nom.equals(name))
						return this.textures[x];
		}
		return null;
	}
	

	public int getNbMonitoredSceneObject()
	{
		return monitoredSceneObjects.size();
	}	
	
	public void updateMonitoredSceneObjects()
	{
		int nbLoadingObject=this.getNbMonitoredSceneObject();
		
		if(nbLoadingObject!=0)
		{
			for(int x=0;x<this.getNbMonitoredSceneObject();x++)
			{
				IMonitoredSceneObject cLoad=this.getMonitoredSceneObject(x);
				if(cLoad.getFinished())
				{
					if(cLoad.getError()==false)
					{
						if(cLoad instanceof ISceneLoader)
							this.addSceneObjects(((ISceneLoader)cLoad).getSceneObjects());						
					}
					this.stopMonitorSceneObject(cLoad);
				}
			}
			try
			{
				//Log.log(this.getClass(),"Monitoring object:");
				//for(int x=0;x<this.getNbMonitoredSceneObject();x++)
				//	Log.log(this.getClass(),getMonitoredSceneObject(x).getName());
				Thread.sleep(1);
				Thread.yield();
			}
			catch(InterruptedException ie)
			{
			}
		}
	}
	
	public void startMonitorSceneObject(IMonitoredSceneObject obj)
	{
		this.monitoredSceneObjects.addElement(obj);
	}
	
	public IMonitoredSceneObject getMonitoredSceneObject(int idx)
	{
		return (MonitoredSceneObject)this.monitoredSceneObjects.elementAt(idx);
	}
	
	public void stopMonitorSceneObject(int idx)
	{
		this.monitoredSceneObjects.removeElementAt(idx);
	}
	
	public void stopMonitorSceneObject(IMonitoredSceneObject obj)
	{
		this.monitoredSceneObjects.removeElement(obj);
	}
	
}