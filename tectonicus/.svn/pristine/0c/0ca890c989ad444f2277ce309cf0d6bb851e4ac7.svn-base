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

package net.dzzd;

import net.dzzd.access.*;
import java.awt.Canvas;

import netscape.javascript.JSObject;

/** 
 * Provide an easy way to implements 3D Applet using only JavaScript.
 * <p>
 *  <u><b>Overview:</b></u>
 * </p>
 *  This class is provided to enable 3D Demo creation, without the need of java compiler, using only JavaScript and HTML.<br>
 * <br>
 * <b><u>HTML code sample:</u></b><br>
 * You can use the following HTML code to include a 3D scene in your web page.<br>
 * <p>
 * &lt;APPLET MAYSCRIPT <br>
 *  archive = "dzzd.jar" <br>
 *  code	= "dzzd.DzzDJSApplet.class"<br>
 *	width	= "600"<br>
 *	height	= "400"><br>
 *  &lt;PARAM NAME="JavaScriptInit" value="start"><br>
 * &lt;/APPLET><br>
 * </p>
 * <br>
 * <b><u>JavaScriptInit</u></b><br>
 * The "JavaScriptInit" parameter is used to tell the 3DzzD API to call the javascript function "start" once initialised.<br>
 * You may change the value of this parameter to set you own JavaScript function.<br>
 * Usually you will use this function to start loading a 3D scene and/or register other callback function as : render3DObjectMonitor,render3DWorldSpace.<br>
 * <br>
 * <b><u>JavaScript code sample:</u></b><br>
 * <code>
 * &lt;SCRIPT LANGUAGE="JavaScript"><br>
 * function start(applet)<br>
 * {<br>
 *  applet.getScene3DRender().getScene3D().loadScene3DFrom3DS(applet.getBaseURL(),"SCENE.3DS");<br>
 * }<br>
 * &lt;/SCRIPT><br>
 * </code>
 * <br>
 * This JavaScript code will load the 3DS file name "SCENE.3DS".<br>
 * <br>
 * The given sample expect that all files are in the same directory including dzzd.jar.<br> 
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IScene3DRender
 */
public final class DzzDJSApplet extends DzzDApplet implements IScene3DRenderCallBack
{
	private int nbStart=0;
	private int nbInit=0;
	private IScene3DRender scene3DRender;
	
	//Reference to JS window
	private JSObject htmlWin;
	private String render3DWorldSpace;
		
	/**
	 * Call a JavaScript function and catch exception
	 */
	private void callJS(String function,Object[] param)
	{
		try
		{
			this.htmlWin.call(function,param);
		}
		catch(Exception jse)
		{
		}		
	}

	/**
	 * Call JavaScript init function
	 */
	private void initJS()
	{
		try
		{
			this.htmlWin=JSObject.getWindow(this);
			this.render3DWorldSpace=null;	
			Object p[]=new Object[1];
			p[0]=this;		
			this.callJS(this.getParameter("JavaScriptInit"),p);
		}
		catch(UnsatisfiedLinkError ule)
		{
			this.htmlWin=null;
		}		
	}	

	/**
	 * Register the javascript function to call for world space event.<br>
	 * <br>
	 * You can use this function to register/replace or remove the function used for the world space event.<br>
	 * <br>
	 * use null value to remove the current function.
	 *
	 * @param fName the JavaScript function name
	 */			
	public void registerRender3DWorldSpace(String fName)
	{
		this.render3DWorldSpace=fName;
	}		

	public void init()
	{
		super.init();
		if(this.nbInit++==0)
		{
			this.initJS();
			DzzD.extensionBaseURL=this.getBaseURL()+"/LIB/";
        	this.scene3DRender=DzzD.newScene3DRender();
			this.setLayout(null);
			Canvas renderCanvas=this.scene3DRender.getRender3D().getCanvas();
			this.add(renderCanvas);
			this.scene3DRender.getRender3D().setScreenUpdateEnabled(false);
			this.scene3DRender.getRender3D().getCanvas().setVisible(false);
		}	

	}
	
	public void start()
	{
		super.start();
		if(this.nbStart++==0)
		{
			this.scene3DRender.setScene3DRenderCallBack(this);
			this.scene3DRender.start();
		}
		this.scene3DRender.getRender3D().setSize(this.getWidth(),this.getHeight());	
		
	}
	
	public void destroy()
	{
		this.scene3DRender.stop();
	}

	public void render3DstartCallBack(IScene3DRender r){}
	public void render3DStart(IScene3DRender r){}
	public void render3DWorldSpace(IScene3DRender r)
	{
		if(this.render3DWorldSpace==null)
			return;
		Object p[]=new Object[1];
		p[0]=this.scene3DRender;
		this.callJS(this.render3DWorldSpace,p);		
	}
	public void render3DCameraSpace(IScene3DRender r){}	
	public void render3DPixelsUpdate(IScene3DRender r){}
	public void render3DPixelsUpdated(IScene3DRender r){}
	public void render3DEnd(IScene3DRender r){}
	public void render3DSwitched(IScene3DRender r){}

}
