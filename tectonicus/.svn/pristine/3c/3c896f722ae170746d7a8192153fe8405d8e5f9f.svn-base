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
 * Scene3DRenderCallBackWalkthrough.
 * 
 *  @author Bruno Augier
 *  @version 1.0, 2007/12/27
 *  @since 1.0
 */
 package net.dzzd;

import net.dzzd.access.*;
import net.dzzd.*;

import java.awt.event.KeyEvent;

public class Scene3DRenderCallBackWalkthrough extends Scene3DRenderCallBack
{
	IRender3D render;
	IScene3D scene;
	ICamera3D camera;
	IDirectInput input;
	
	
	public Scene3DRenderCallBackWalkthrough()
	{
	}
	
	public void render3DstartCallBack(IScene3DRender r)
	{
		this.render=r.getRender3D();
		this.scene=r.getScene3D();
		this.input=this.render.getDirectInput();
		this.camera=this.scene.getCurrentCamera3D();
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


	boolean dragRotate=false;	
	double dragRotateStartRotateX=0;
	double dragRotateStartRotateY=0;
	double dragRotateStartX=0;
	double dragRotateStartY=0;
	
	public void render3DWorldSpace(IScene3DRender r)
	{
		//Move
		if(this.input.isMouseB1())
		{
			
			if(!this.dragRotate)
			{
				this.dragRotate=true;
				this.dragRotateStartX=(double)this.input.getMouseX()/this.render.getWidth()-0.5;
				this.dragRotateStartY=(double)this.input.getMouseY()/this.render.getHeight()-0.5;
				this.dragRotateStartRotateX=this.camera.getRotation().getX();
				this.dragRotateStartRotateY=this.camera.getRotation().getY();
				
				
			}
			else
			{
				//Currently drag rotation			
				double mx=(double)this.input.getMouseX()/this.render.getWidth()-0.5;
				double my=(double)this.input.getMouseY()/this.render.getHeight()-0.5;
				double vx=mx-this.dragRotateStartX;
				double vy=my-this.dragRotateStartY;
				double angleX=-Math.PI*vy;
				double angleY=-Math.PI*vx;
				this.camera.getRotation().setX(this.dragRotateStartRotateX+angleX);
				this.camera.getRotation().setY(this.dragRotateStartRotateY+angleY);
				
			}	
		}
		else
		{
			if(this.dragRotate)
			{
				//End drag rotation
				this.dragRotate=false;
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
				this.camera.setZoomX(this.dragZoomStartZoomX*1+vy);
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
		
		//Read camera axis	
		IAxis3D a=this.scene.getCurrentCamera3D().getAxis3D();
		IPoint3D az= DzzD.newPoint3D();
		az.copy(a.getAZ()).sub(a.getOrigin());
		//IPoint3D az=this.camAxis.getAZ();
		//IPoint3D ax=this.camAxis.getAX();
		IPoint3D ax= DzzD.newPoint3D();
		ax.copy(a.getAX()).sub(a.getOrigin());
		
		//Read mouse pos
		double mx=this.input.getMouseX();
		double my=this.input.getMouseY();

		//Rotate camera using mouse coordinate
		IPoint3D rot=this.scene.getCurrentCamera3D().getRotation();
		double rx=rot.getX(); 
		double ry=rot.getY();
		
		rot.setY(ry+(this.render.getWidth()*0.5-mx)*6000.0/20000000.0);
		double crx=rx+(this.render.getHeight()*0.5-my)*6000.0/20000000.0;
		if(crx>Math.PI*0.5*0.8)
			crx=Math.PI*0.5*0.8;
		if(crx<-Math.PI*0.5*0.8)
			crx=-Math.PI*0.5*0.8;
		rot.setX(crx);		
		
		//Compute move vector using key pressed
		double moveForward=0;
		double moveSlide=0;
		double speed=5;
		
		
		double mvx=0;
		double mvy=0;
		double mvz=0;

		if(this.input.isKey(KeyEvent.VK_UP))
				moveForward=speed;  
				
		if(this.input.isKey(KeyEvent.VK_DOWN))
				moveForward=-speed;
		
		if(this.input.isKey(KeyEvent.VK_RIGHT))
				moveSlide=speed*0.2; 
				
		if(this.input.isKey(KeyEvent.VK_LEFT))
				moveSlide=-speed*0.2; 				
				
		mvx=az.getX()*moveForward+ax.getX()*moveSlide;
		//mvy=az.getY()*moveForward+ax.getY()*moveSlide;
		mvz=az.getZ()*moveForward+ax.getZ()*moveSlide;
			
		
		IPoint3D pos=this.scene.getCurrentCamera3D().getPosition();
		double cx=pos.getX();
		double cy=pos.getY();
		double cz=pos.getZ();
		pos.setX(cx+mvx);
		pos.setY(cy+mvy);
		pos.setZ(cz+mvz);
	
		
		
	}
	public void render3DCameraSpace(IScene3DRender r){}
	public void render3DPixelsUpdate(IScene3DRender r){}
	public void render3DPixelsUpdated(IScene3DRender r){}
	public void render3DEnd(IScene3DRender r){}
}