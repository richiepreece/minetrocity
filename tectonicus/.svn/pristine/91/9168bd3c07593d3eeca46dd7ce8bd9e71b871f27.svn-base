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

/** 
 * Scene3DRenderCallBackExamine.
 * 
 *  @author Bruno Augier
 *  @version 1.0, 2007/12/27
 *  @since 1.0
 */
 package net.dzzd;

import net.dzzd.access.*;
import net.dzzd.*;

import java.awt.event.KeyEvent;



public class Scene3DRenderCallBackExamine extends Scene3DRenderCallBack
{
	IRender3D render;
	IScene3D scene;
	ICamera3D camera;
	IDirectInput input;
	
	
	public Scene3DRenderCallBackExamine()
	{
	}
	
	public void render3DstartCallBack(IScene3DRender r)
	{
		this.render=r.getRender3D();
		this.scene=r.getScene3D();
		this.input=this.render.getDirectInput();
		this.camera=this.scene.getCurrentCamera3D();
		this.render.setAntialiasLevel(7);	
		r.start();		
	}
	
	public void render3DSwitched(IScene3DRender r)
	{
		this.render=r.getRender3D();
		this.input=this.render.getDirectInput();
	}
	
	public void render3DStart(IScene3DRender r)
	{
		
	}
	

	boolean dragZoom=false;
	double dragZoomStartZoomX=0;
	double dragZoomStartZoomY=0;
	double dragZoomStartX=0;
	double dragZoomStartY=0;

	
	double dragStartX=0;
	double dragStartY=0;
	IScene3DObject dragObject=null;
	IAxis3D dragObjectAxisStart=null;
	IAxis3D dragObjectAxis=null;
	IPoint3D dragObjectRotation=null;
	
	public void render3DWorldSpace(IScene3DRender r)
	{
		Thread.yield();
		//Rotation
		if(this.input.isMouseB1())
		{
			
			if(this.dragObject==null)
			{
				//Start drag rotation
				this.dragObjectAxis=DzzD.newAxis3D();
				this.dragObjectAxisStart=DzzD.newAxis3D();
				this.dragObject=this.scene.getMesh3DById(0);
				this.dragObjectRotation=this.dragObject.getRotation();
				this.dragObjectAxisStart.copy(this.dragObject.getAxis3D());
				this.dragObjectAxisStart.toLocalAxis(this.camera.getAxis3D());
				this.dragObjectAxisStart.sub(this.dragObjectAxisStart.getOrigin());
				this.dragStartX=(double)this.input.getMouseX()/this.render.getWidth()-0.5;
				this.dragStartY=(double)this.input.getMouseY()/this.render.getHeight()-0.5;
			}
			else
			{
				//Currently drag rotation			
				double mx=(double)this.input.getMouseX()/this.render.getWidth()-0.5;
				double my=(double)this.input.getMouseY()/this.render.getHeight()-0.5;
				double vx=mx-this.dragStartX;
				double vy=my-this.dragStartY;
				double angle=Math.PI*Math.sqrt(vx*vx+vy*vy);
				this.dragObjectAxis.copy(this.dragObjectAxisStart);
				this.dragObjectAxis.rotate(angle,vy,vx,0);
				this.dragObjectAxis.toAxis(this.camera.getAxis3D());
				this.dragObjectAxis.getRotationXZY(this.dragObjectRotation);							
			}	
		}
		else
		{
			if(this.dragObject!=null)
			{
				//End drag rotation
				this.dragObject=null;
			}		
		}
		
		//Zoom
		if(this.input.isMouseB3())
		{
			if(!this.dragZoom)
			{
				//Start drag zoom
				this.dragZoom=true;
				this.dragZoomStartX=(double)this.input.getMouseX()/this.render.getWidth()-0.5;
				this.dragZoomStartY=(double)this.input.getMouseY()/this.render.getHeight()-0.5;
				this.dragZoomStartZoomX=this.camera.getZoomX();
				this.dragZoomStartZoomY=this.camera.getZoomY();
			}
			else
			{
				//Currently drag zoom
				double mx=(double)this.input.getMouseX()/this.render.getWidth()-0.5;
				double my=(double)this.input.getMouseY()/this.render.getHeight()-0.5;
				double vx=my-this.dragZoomStartX;
				double vy=my-this.dragZoomStartY;
				this.camera.setZoomX(this.dragZoomStartZoomX+vy);
				double ratio=((double)r.getRender3D().getWidth())/((double)r.getRender3D().getHeight());
				this.camera.setZoomY(ratio*this.camera.getZoomX());
			}
			
		}
		else
		{
			if(this.dragZoom)
			{
				//End drag zoom
				this.dragZoom=false;
				
			}
			
			
		}
		
		if(this.input.isKey(KeyEvent.VK_H))
			r.switchRender3D("JOGL");
		if(this.input.isKey(KeyEvent.VK_S))
			r.switchRender3D("SOFT");
		
			
		

		
		
	}
	public void render3DCameraSpace(IScene3DRender r){}
	public void render3DPixelsUpdate(IScene3DRender r){}
	public void render3DPixelsUpdated(IScene3DRender r){}
	public void render3DEnd(IScene3DRender r)
	{
		if(!this.input.isMouseB3() && !this.input.isMouseB1())
		{
			//this.render.setAntialiasLevel(7);
			DzzD.sleep(10);
			
			
		}
		else
		{
		//	this.render.setAntialiasLevel(1);
		}		
	}
}