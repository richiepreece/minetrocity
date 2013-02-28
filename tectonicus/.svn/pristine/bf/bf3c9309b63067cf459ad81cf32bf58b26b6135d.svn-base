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


public class Scene2D extends Scene implements IScene2D
{
	protected Shape2D[] shapes2D;	
	protected int nbShape2D;			
	
	public Scene2D()
	{
		super();
		this.setScene2DBufferSize(1024);	
		this.clearScene2D();
	}	
	
	public void setScene2DBufferSize(int maxShape2D)
	{
		this.shapes2D=new Shape2D[maxShape2D];
		this.clearScene2D();
	}	
	
    public int getNbScene2DObject()
	{
		int nbScene2DObject=this.nbShape2D;
		return nbScene2DObject;
		
	}
	
    public int getNbShape2D()
	{
		return this.nbShape2D;
	}	
	
	public void addShape2D(IShape2D shape2D)
	{
		if(((Shape2D)shape2D).scene==this) return;
		((Shape2D)shape2D).scene=this;

		this.shapes2D[this.nbShape2D]=(Shape2D)shape2D;
		shape2D.setId(this.nbShape2D);
		this.startMonitorSceneObject((Shape2D)shape2D);
		shape2D.build();
		this.nbShape2D++;				
	}
	
	public void clearScene2D()
	{
		for(int nm=0;nm<this.nbShape2D;nm++)
			this.shapes2D[nm]=null;
		this.nbShape2D=0;
	}	
	
	public void addScene2DObject(IScene2DObject object)
	{
		if(object instanceof IShape2D)
		{
			this.addShape2D((IShape2D)object);
			return;
		}
	}	

	public void addScene2DObjects(IScene2DObject objects[])
	{
		for(int x=0;x<objects.length;x++)
			this.addScene2DObject(objects[x]);
	}
	
	public void removeScene2DObject(IScene2DObject object)
	{
		if(object==null)
			return;
		
		if(object instanceof IShape2D)
		{
			this.removeShape2DById(object.getId());
		}
					
	}
	
	public void removeShape2DById(int id)
	{
		if(this.shapes2D[id]==null)
			return;
		
		this.shapes2D[id]=null;	
		this.nbShape2D--;
		for(int n=id;n<this.getNbShape2D();n++)
		{
			this.shapes2D[n]=this.shapes2D[n+1];
			this.shapes2D[n].setId(n);	
		}		
	}	
	
	public IShape2D getShape2DById(int id)
	{
		return this.shapes2D[id];
	}	
	
	public IShape2D getShape2DByName(String name)
	{
		for(int x=0;x<this.nbShape2D;x++)
		{
			if(this.shapes2D[x]!=null)
				if(this.shapes2D[x].nom!=null)
					if(this.shapes2D[x].nom.equals(name))
						return this.shapes2D[x];
		}
		return null;
	}


	
	//HACK: something wrong this method have nothing to do here
	//public void render(IDrawer2D d){}
}