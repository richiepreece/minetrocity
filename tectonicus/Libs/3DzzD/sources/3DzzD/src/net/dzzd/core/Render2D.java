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
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Cursor;
import java.util.*;
import java.awt.Color;
import java.awt.Image;



public class Render2D extends Render implements IRender2D
{
	protected PCanvas canvas;
	
	protected int viewPixelWidth;		//On screen rendered image width in pixel 
	protected int viewPixelHeight;		//On screen rendered image height in pixel 
	protected int antialias;		//Anti-alias level : 0=None, Bit 1 = Border, Bit 2 = Horizontal,Bit 4 = Vertical
	protected int maxAntialias;		//Maximum antialias level : 0=None, Bit 1 = Border, Bit 2 = Horizontal,Bit 4 = Vertical

	protected int 	minXValue;		//X minimum value 
	protected int 	maxXValue;		//Y maximum value 
	protected int 	minYValue;		//X minimum value 
	protected int 	maxYValue;		//Y maximum value 

	protected boolean isScreenUpdateEnabled;
	protected boolean isPixelUpdateEnabled;
	
	protected int numImage;
	protected boolean rendering;

	
		
	protected IDirectInput directInput;

	protected int render2DMode;		//Current render 2D mode

	class PCanvas extends Canvas //implements ComponentListener
	{
		public Image image;
		
		public PCanvas()
		{
			super();
		//	this.addComponentListener(this);
		}
		
		
		
		
		public void paint(Graphics g)
		{
			this.update(g);
				
		}
		
		public void update(Graphics g)
		{
			if(this.image!=null)
			 g.drawImage(this.image,0,0,null);
			//System.out.println("PCanvas.update");
			this.validate();
				//g.drawString("ok",100,100);
		}
		
		/*
		public void componentHidden(ComponentEvent e) 
		{
		
		}
		
		public void componentMoved(ComponentEvent e)
		{
		
		}
		
		public void componentResized(ComponentEvent e)
		{
			this.repaint();
		}
		
		public void componentShown(ComponentEvent e)
		{
		
		}		
		*/
	}
	

	
	public Render2D()
	{
		super();
		this.render2DMode=DzzD.RM_ALL;	
		this.canvas=new PCanvas();
		this.rendering=false;
		this.antialias=1;
		this.maxAntialias=1;
		this.directInput=null;
		this.numImage=0;
		this.isScreenUpdateEnabled=true;	
		this.isPixelUpdateEnabled=true;	
	}	
	
	
	public Canvas getCanvas()
	{
		return this.canvas;
	}
		
	public IDirectInput getDirectInput()
	{
		return this.directInput;
	}
	
	public void setSize(int viewPixelWidth,int viewPixelHeight,int maxAntialias)
	{
		//Log.log(this, "Render3D.setSize(int viewPixelWidth,int viewPixelHeight,int maxAntialias)");
		this.canvas.setSize(viewPixelWidth,viewPixelHeight);
		this.viewPixelWidth=viewPixelWidth;
		this.viewPixelHeight=viewPixelHeight;
		this.maxAntialias=maxAntialias;
		this.antialias=maxAntialias;
				
	}	
			
	public void setSize(int viewPixelWidth,int viewPixelHeight)
	{
		//Log.log(this, "Render3D.setSize(int viewPixelWidth,int viewPixelHeight)");
		this.setSize(viewPixelWidth,viewPixelHeight,this.maxAntialias);
	}		

	public void setAntialiasLevel(int level)
	{
		if(level==this.antialias)
			return;
			
		if(level > this.maxAntialias)
			this.setSize(this.viewPixelWidth,this.viewPixelHeight,level);
			

		this.antialias=level;
	}
	
	public int getWidth()
	{
		return this.viewPixelWidth;
	}

	public int getHeight()
	{
		return this.viewPixelHeight;
	}

	public void setCursor(Cursor cursor)
	{
	 	this.canvas.setCursor(cursor);
	}
	
	public boolean isScreenUpdateEnabled()
	{
		return this.isScreenUpdateEnabled;
	}
	
	public void setScreenUpdateEnabled(boolean flag)
	{
		this.isScreenUpdateEnabled=flag;
	}
	
	public boolean isPixelUpdateEnabled()
	{	
		return this.isPixelUpdateEnabled;
	}
	
	public void setPixelUpdateEnabled(boolean flag)
	{	
		this.isPixelUpdateEnabled=flag;
	}

	public String getImplementationName()
	{
		return "NONE";
	}

	protected void compileScene2DObject(IScene2D scene)
	{
		this.compileSceneObject(scene);		
	}
	
	public void removeSceneObject(ISceneObject sceneObject)
	{
		super.removeSceneObject(sceneObject);	
	}	
		
	protected void startFrame(IScene2D scene)
	{
		
	}
	
	protected void renderFrame(IScene2D scene)
	{
		
	}
	
	protected void endFrame(IScene2D scene)
	{
		
	}
			
	public void renderScene2D(IScene2D scene)
	{
		this.rendering=true;
		this.startFrame(scene);
		this.renderFrame(scene);
		this.endFrame(scene);
		this.numImage++;
		this.rendering=false;
	}	

	//Render2DMode interface
	public IRender2DMode getRender2DMode()
	{
		return this;
	}

	public void enableRender2DMode(int flag)
	{
		this.render2DMode|=flag;
	}

	public void disableRender2DMode(int flag)	
	{
		this.render2DMode&=(flag^DzzD.RM_ALL);
	}

	public void setRender2DModeFlags(int flag)
	{
		this.render2DMode=flag;
	}
	
	public int getRender2DModeFlags()
	{
		return this.render2DMode;
	}		

}