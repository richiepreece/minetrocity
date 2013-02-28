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
 
import java.awt.*;
import java.applet.*;

import net.dzzd.access.*;
import net.dzzd.*;


/**
 * HelloWorld
 * 
 * This sample show how to :
 *  - setup a Scene3D
 *  - load a 3DS file into the Scene3D
 *  - setup a Render3D
 *  - compute and draw the Scene3D onto the Render3D
 */
public class HelloWorld extends Applet
{
	IRender3D render;
	IScene3D scene;
	GraphicsDevice[] devices;

	public void start()
	{
		//Ask 3DzzD factory for a fresh Scene3D
		this.scene=DzzD.newScene3D();	
		
		//Create a Scene3D loader and link it to a 3DS file
		IScene3DLoader loader=DzzD.newScene3DLoader();
		loader.loadScene3D(this.getCodeBase().toString()+"3D/","CUBE.3DS");
		
		//Add the loader to the scene
		this.scene.setScene3DLoader(loader);
		
		//Wait until all object & texture are loaded
		while(this.scene.getNbMonitoredSceneObject()!=0)
		{
		 this.scene.updateMonitoredSceneObjects();	
		 DzzD.sleep(10);
		}
		
		/*
		 * Set the active camera in the 3d scene
		 * 
		 * We use a camera that is inside the 3ds file
		 *
		 * 3DzzD always provide a default camera that you can set using :
		 *  this.scene.setCurrentCamera3DById(0);
		 */
		this.scene.setCurrentCamera3DByName("Camera01");
		
		//Ask 3DzzD factory for a software 3D Render
		this.render=DzzD.newRender3D(this.getClass(),"SOFT",null);
		
		//Add the Render3D canvas to the Applet Panel
		this.setLayout(null);
		this.add(this.render.getCanvas());
		
		//Set the Render3D size and enable maximum antialias
		this.render.setSize(this.getSize().width,this.getSize().height,7);
		
		//Set Camera Aspect ratio to 1:1
		this.scene.getCurrentCamera3D().setZoomY(((double)this.render.getWidth())/((double)this.render.getHeight()));	
		
		//Tell the Render3D wich camera it should use to render
		this.render.setCamera3D(this.scene.getCurrentCamera3D());
		
		//Render the frame
		this.renderSingleFrame();	

	}
	
	
	public void renderSingleFrame()
	{
		//Set the scene to world space
		this.scene.setScene3DObjectToWorld();
		
		//Set the scene to active camera space
		this.scene.setScene3DObjectToCamera();
		
		//Tell the 3D render to compute & draw the frame
		this.render.renderScene3D(this.scene);
	}
}
