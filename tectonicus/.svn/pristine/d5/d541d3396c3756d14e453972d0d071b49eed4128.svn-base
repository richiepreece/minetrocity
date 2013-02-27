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

import java.util.*;

public class SceneObject extends MonitoredSceneObject implements ISceneObject
{
    IScene scene;
    String nom;
    int id;
    
	private Hashtable properties;
	private int build;

	SceneObject()
	{
       this.scene=null;
       this.nom = null;
       this.id = -1;
	   this.properties=new Hashtable();
	   this.build=-1;
	   this.setProgress(100);
	   this.setFinished(true);
	   this.setError(false);
	}

	public String getName()
	{
		return this.nom;
	}
	
	public void setName(String name)
	{
		this.nom=name;
	}

	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}

	public IScene getScene()
	{
		return this.scene;
	}
	
	public void setProperty(String key,Object value)
	{
		if(value==null)
			this.properties.remove(key);
		else
			this.properties.put(key,value);
	}
	
	public Object getProperty(String key)
	{
		return this.properties.get(key);
	}	
	
	public void clearProperties()
	{
		this.properties=new Hashtable();
	}
	
	public void build()
	{
		this.build++;
	}	
	
	public int getBuild()
	{
		return this.build;
	}
	
	public void setBuild(int buil)
	{
		this.build=build;
	}	
	
	public void copy(ISceneObject source)
	{
			super.copy(source);
			this.setName(source.getName());
			this.build++;
	}
	
}