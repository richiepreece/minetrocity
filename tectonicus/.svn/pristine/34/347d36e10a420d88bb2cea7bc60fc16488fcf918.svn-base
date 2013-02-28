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
 * Provide an easy way to implements 3D Applet.
 * 
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @updated Bruno Augier, 2007/12/27
 *  @since 1.0
 */

package net.dzzd;

import net.dzzd.access.*;

import java.awt.*;
import java.applet.*;



public class DzzDApplet extends Applet 
{
	private Frame frame ; 
	private Container parent;
	private boolean fullscreen=false;
	
	
				
	/**
	 * Gets applet base URL.
	 *
	 * @return a string representation of the location of this applet as an URL.
	 */		
	final public String getBaseURL()
	{
		return this.getCodeBase().toString();
	}	
		
    /**
	 * Gets Applet information.
	 * 
	 * @return a string with applet information.
     */
	final public String getAppletInfo()
	{
		return "Powered by 3DzzD Web 3D Engine, Copyright 2005-2007 Bruno Augier, http://dzzd.net/";
	}
	
	/**
	 * Gets this applet width.
	 *
 	 * @return applet width
 	 */			
	final public int getWidth()
	{
		return this.getSize().width;
	}


	/**
	 * Gets this applet height.
	 *
 	 * @return applet height
 	 */			
	final public int getHeight()
	{
		return this.getSize().height;
	}	 	
	
	
	/**
	 * Toggle Applet to fullscreen.
	 *
 	 * @param flag true to go into fullscreen or false to restore
 	 */			
	public void setFullScreen(boolean flag)
	{
		if(flag && this.fullscreen)
			return;
			
		if(flag)
		{
			if(this.parent==null)
				this.parent=getParent();
			this.frame= new Frame();
	  		this.frame.setUndecorated(true); 
	  		this.frame.add(this); 
	  		this.frame.setVisible(true);
	  		
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    GraphicsDevice[] devices = ge.getScreenDevices();
		    devices[0].setFullScreenWindow(this.frame);
		    this.fullscreen=true;
	    }
	    else
	    {
		    if(this.parent!=null)
		      	this.parent.add(this);
		    if(this.frame!=null)
		    	this.frame.dispose();
		    this.fullscreen=false;
		 }
	    this.setBounds(0,0,this.getParent().getSize().width,this.getParent().getSize().height);
	    
	    
	    this.requestFocus();
	}

	/**
	 * Gets if this Applet is fullscreen
	 *
 	 * @return true if applet is maximized to fullscreen
 	 */				
	public boolean isFullScreen()
	{
		return this.fullscreen;		
	}
}
