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

/** 
 *  A class representing 3D camera 
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
public final class Camera3D extends Scene3DObject implements ICamera3D
{
	double focus;	//Focal length (default 47)
	double width;	//Camera screen width (default 47)
	double height;	//Camera screen height(default 47*3/4)
	double zoomX;	//ZoomX (default 1)
	double zoomY;	//ZoomY (default 1)
	double zMin;	//Z min (default 1)
	double zMax;	//Z max (default 1e256)
	int type;		//Caméra type 0=targeted 1=freehand
	IPoint3D target;	//Target point for targeted camera (null if none)
	double roll;	//Roll for targeted camera (rotation about z axis)
	double fov;
	public String toString()
	{
		String r="";
		r+=" FOV("+getFOV()+") ";
		r+=" focal("+this.focus+") ";
		r+=" size("+width+","+height+") ";
		r+=" zoom("+zoomX+","+zoomY+") ";
		r+=" clipZ("+zMin+","+zMax+") ";
		return r;	
	}
	
	public Camera3D()
	{
		super();
		this.type=1;
		this.width=1;
		this.height=3.0/4.0;
		this.zoomX=1;
		this.zoomY=1;
		this.focus=1;
		this.zMin=1;//this.focus;
		this.zMax=Float.MAX_VALUE;
		this.target=null;
		this.roll=0;
		this.setFOV(45);
		this.sphereBox=1.0;
	}
	
	public double getFOV()
	{		
		return this.fov;//360.0*Math.atan(width/(2.0*focus))/Math.PI;
	}

	public void setFOV(double val)
	{
		this.fov=val;
		//this.focus=width/(2.0*Math.tan(val*Math.PI/360.0));
	}
	
	public double getFocus()
	{
		return this.focus;
	}
	
	public double getWidth()
	{
		return this.width;
	}	
	
	public double getHeight()
	{
		return this.height;
	}	
	
	public double getZoomX()
	{
		return this.zoomX;
	}		
	
	public double getZoomY()
	{
		return this.zoomY;
	}	
	
	public double getZMax()
	{
		return this.zMax;
	}
	
	public double getZMin()
	{
		return this.zMin;
	}				
					
	public void setFocus(double val)
	{
		this.focus=val;
	}
	
	public void setWidth(double val)
	{
		this.width=val;
	}	
	
	public void setHeight(double val)
	{
		this.height=val;
	}	
	
	public void setZoomX(double val)
	{
		this.zoomX=val;
	}		
	
	public void setZoomY(double val)
	{
		this.zoomY=val;
	}	
	
	public void setZMax(double val)
	{
		this.zMax=val;
	}
	
	public void setZMin(double val)
	{
		this.zMin=val;
	}					
		
	public IScene3DObject getClone(boolean childrens)
	{
		Camera3D c = new Camera3D();
		c.copy(this);
		
		if(childrens)
		{
			for(Scene3DObject o=this.firstChild;o!=null;o=o.nextChild)
				c.addChild(o.getClone(childrens));
		}		
		
		return c;
	}
	
	public void copy(ICamera3D c)
	{
		this.setWidth(c.getWidth());
		this.setHeight(c.getHeight());
		this.setZMin(c.getZMin());
		this.setZMax(c.getZMax());
		this.setFocus(c.getFocus());
		this.setZoomX(c.getZoomX());
		this.setZoomY(c.getZoomY());
		if(c.getTarget()!=null)
			this.setTarget(c.getTarget().getClone());
		else
			this.setTarget(null);
		super.copy(c);
	}

	public void setTarget(IPoint3D target)
	{
		this.target=target;
	}

	public IPoint3D getTarget()
	{
		return this.target;
	}
}