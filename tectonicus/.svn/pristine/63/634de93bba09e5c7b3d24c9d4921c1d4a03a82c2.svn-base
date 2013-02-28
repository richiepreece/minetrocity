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

package net.dzzd.extension.jogl;

import net.dzzd.access.*;
import net.dzzd.core.*;
import net.dzzd.DzzD;


import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;



public final class Render2DJOGL extends Render2D //ONLY FOR PASSIVE //implements GLEventListener 
{	
	private GLContext context;
	private GLCanvas canvas;
	private GL gl;
	private GLU glu;
	private IScene2D scene;
	
	//Test
	private JOGLTesselator tess;

	
	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public Render2DJOGL() throws Throwable
	{
		super();
		
		GLCapabilities glCaps = new GLCapabilities();	
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);
		glCaps.setDoubleBuffered(true);
		glCaps.setHardwareAccelerated(true);

		this.canvas = new GLCanvas(glCaps);
		this.canvas.setAutoSwapBufferMode(false);
		this.glu=new GLU();
		
		//ONLY FOR ACTIVE
		this.context=this.canvas.getContext();
		this.gl = this.context.getGL();
		

		//ONLY FOR PASSIVE
		//this.canvas.addGLEventListener(this);
				
		//this.directInput=new DirectInput(this.canvas);
		
		
		//Test
		this.tess = new JOGLTesselator(this.glu,this.gl);
	}
	
	public void setSize(int viewPixelWidth,int viewPixelHeight)//,int maxAntialias)
	{
		//super.setSize(viewPixelWidth,viewPixelHeight,maxAntialias);
		this.canvas.setSize(viewPixelWidth,viewPixelHeight);
	}		
	
	/**
	 * Called when user request Scene2D to be render
	 * <br>
	 * must draw all Scene2DObject within Scene2D as all Shape2D, and other 2D visible object
	 * <br>
	 * build revision between jogl & Scene2D object should be checked to see if recompilation is needed.
	 * <br>
	 * refer to Render3D & Render3DJOGL for compilation & build revision
	 */
	public void renderScene2D(IScene2D scene)
	{	
		this.scene=scene;
		
		//ONLY FOR PASSIVE
		//this.canvas.display();
		
		//if(this.scene==null)
		//	return;
		
		
		//ONLY FOR ACTIVE
		this.makeContentCurrent();
		
		this.gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glClearColor(0.0f,0.0f,1.0f,0.0f);	//Background blue
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();	
        gl.glColor3f(1.0f, 0.0f, 0.0f);	//Color red
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(0,0,0.0f);
        gl.glVertex3f(1,0,0.0f);
        gl.glVertex3f(0,1,0.0f);        
        gl.glEnd();	
        
        for(int x=0;x<scene.getNbShape2D();x++)
        {
        	IShape2D shape2d=scene.getShape2DById(x);
        	System.out.println("There we should render (& compil if needed) the shape : " + shape2d.getName());
        	
        	//Test
        	gl.glColor3f(0.0f, 1.0f,0.0f);	//Color green
        	tess.fill(this.gl, shape2d);
        }
		//Should render whole scene2D here
		//super.renderScene3D(scene);	
		
		//ONLY FOR ACTIVE
		this.context.release();
		
		if(this.isScreenUpdateEnabled)
			this.canvas.swapBuffers();
	}
	
	
	//ONLY FOR ACTIVE
	private void makeContentCurrent()
	{	
		try 
		{
			while (context.makeCurrent() == GLContext.CONTEXT_NOT_CURRENT) 
			{
				System.out.println("Context not yet current...");
				Thread.sleep(100);
			}
		}
		catch (InterruptedException e)
		{
			 e.printStackTrace(); 
		}
	} 		


	//ONLY FOR PASSIVE
    public void init(GLAutoDrawable drawable)
    {
        this.gl = drawable.getGL();
    }
		
	public void display(GLAutoDrawable drawable)
	{		
		if(this.scene==null)
			return;
		super.renderScene2D(this.scene);		
	}
	
	public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) 
	{
		this.gl = drawable.getGL();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean b, boolean b1) 
	{
		this.gl = drawable.getGL();
	}
	

}


/*package net.dzzd.extension.jogl;

import net.dzzd.access.*;
import net.dzzd.core.*;

import java.awt.Canvas;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

// TODO: - move canvas, GL context handling & rendering to the root joglrenderer in 3dzzd
public class Drawer2DJOGL extends Drawer2D// implements GLEventListener 
{
	// temp var
	GLCanvas canvas = null;

	// OpenGL vars
	private GLDrawable drawable;	// the rendering 'surface'
	private GLCapabilities caps;	// this videocards capabilities
	private GLContext context;		// the rendering context (holds rendering state info)
	private GL gl;
	private GLU glu;
	private JOGLTesselator tess;
	private IScene2D scene2D;
	

	public Drawer2DJOGL()
	{
		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);
		//glCaps.setDoubleBuffered(true);

		this.canvas = new GLCanvas(glCaps);
		//this.canvas.setAutoSwapBufferMode(false);

		this.glu = new GLU();

	    this.drawable = GLDrawableFactory.getFactory().getGLDrawable(canvas, glCaps, null);
	    this.context  = drawable.createContext(null);
	    this.drawable.setRealized(true);
	}

	public Canvas getCanvas() {
		return canvas;
	}


	public void renderScene2D(IScene2D s)
	{
		waitUntilCurrent();
		
		this.scene2D = s;
		this.canvas.display();
		this.canvas.swapBuffers();
		
		this.context.release();
	}
	
		
	// Make the rendering context current for this thread
	private void waitUntilCurrent()
	{
		try
		{
			while (context.makeCurrent() == GLContext.CONTEXT_NOT_CURRENT)
			{
				System.out.println("Context not yet current...");
				Thread.sleep(1);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	public void resizeView()
	{
		gl.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(0.0, (double) canvas.getWidth(), 0.0, (double) canvas.getHeight());
	}


	//rendering initialization (similar to the init() callback  in GLEventListener)
	private void initRender()
	{
		waitUntilCurrent();
	
		gl   = context.getGL();
		glu  = new GLU();
		tess = new JOGLTesselator(glu,gl);

		resizeView();
		
        gl.glDisable(GL.GL_SCISSOR_TEST);
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glEnableClientState(GL.GL_VERTEX_ARRAY);

		// release the context, otherwise the AWT lock on X11 will not be released
		context.release();
	}


	public void drawShape2D(IShape2D shape,double px,double py,double zoomX,double zoomY)
	{
		if(gl==null)
		{
			initRender();
			System.out.println("gl==null, initrender called");
		}
		if(shape==null)
		{
			System.out.println("shape==null");
			return;
		}		

		tess.fill(gl, shape);
		
	    gl.glEnable(GL.GL_LINE_STIPPLE);
	
	    gl.glBegin(GL.GL_LINES);
	
	    gl.glVertex2f((0.0f), ( 125.0f));
	    gl.glVertex2f((250.0f), (125.0f));
	    gl.glEnd();	    
	    
	    gl.glDisable(GL.GL_LINE_STIPPLE);
	    gl.glFlush();
    		
		drawable.swapBuffers();
	}
}
*/