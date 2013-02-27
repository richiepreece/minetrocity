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

import java.util.*;

public class Render implements IRemoveSceneObjectConsumer
{
	protected Hashtable compiledBuild;
	
	public Render()
	{
		this.compiledBuild=new Hashtable();
	}
	
	protected void compileTexture(ITexture texture)
	{
		Log.log("Compile Texture :" + texture.getName());
	}
	
	protected void compileMaterial(IMaterial material)
	{
		Log.log("Compile Material :" + material.getName());
	}	
	
	protected void disposeTexture(ITexture texture)
	{
		Log.log("Dispose Texture :" + texture.getName());
	}			
	
	protected void disposeMaterial(IMaterial material)
	{
		Log.log("Dispose Material :" + material.getName());	
	}	
			
	protected void compileSceneObject(IScene scene)
	{
		int nbTexture=scene.getNbTexture();
		for(int n=0;n<nbTexture;n++)
		{
			ITexture so=scene.getTextureById(n);
			Integer build=new Integer(so.getBuild());
			if(!build.equals(this.compiledBuild.get(so)))
			{
				this.compileTexture(so);
				this.compiledBuild.put(so,new Integer(so.getBuild()));
			}
		}
		
		int nbMaterial=scene.getNbMaterial();
		for(int n=0;n<nbMaterial;n++)
		{
			IMaterial so=scene.getMaterialById(n);
			Integer build=new Integer(so.getBuild());
			if(!build.equals(this.compiledBuild.get(so)))
			{
				this.compileMaterial(so);
				this.compiledBuild.put(so,new Integer(so.getBuild()));
			}
		}		
	}
	
	public void removeSceneObject(ISceneObject sceneObject)
	{
		if(sceneObject instanceof ITexture)
			this.disposeTexture((ITexture) sceneObject);
		if(sceneObject instanceof IMaterial)
			this.disposeMaterial((IMaterial) sceneObject);			
	}
	
	public void clearScene(IScene scene)
	{
		 
	}	
		
	//Initialise rendering process
	protected void startFrame(IScene2D scene)
	{
		
	}
	
	//Initialise rendering process
	protected void renderFrame(IScene scene)
	{

	}
	
	//Finalise rendering process
	protected void endFrame(IScene scene)
	{
		
	}
			
	public void renderScene(IScene scene)
	{
	}		
}
