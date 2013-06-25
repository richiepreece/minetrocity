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

import java.awt.*;
import java.applet.*;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Provide an easy way to create a 3D Frame.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/07
 *  @since 1.0
 *	@see IScene3DRenderCallBack,DzzDApplet
 */
 
public class DzzDFrame extends Frame implements AppletStub,AppletContext
{
	
    private Hashtable applets;
    private String status;
	
	/**
 	 * Construct a new empty DzzDFrame width a default size.
	 */	
	public DzzDFrame()
	{
		super();
		this.status="";
		this.applets=new Hashtable();
		this.setVisible(true);
	}
	
	/**
	 * Construct a new DzzDFrame with a first Applet
	 *
	 * @param applet applet to add to this frame
	 * @param appletName name for that applet
	 *
	 */
	DzzDFrame(Applet applet,String appletName)
	{
		super();
		this.setApplet(applet,appletName);
		
	}
		
	/**
	 * Add an Applet to this frame
	 *
	 * @param applet applet to add to this frame
	 * @param appletName name for that applet
	 */
	public void setApplet(Applet applet,String appletName)
	{
		this.applets.put(appletName,applet);
		applet.setStub(this);
		this.add(applet);
		applet.init();
	}	
	
	/**
	 * Remove an Applet from this frame
	 *
	 * @param appletName name of the applet to remove from this frame
	 */
	public void removeApplet(String appletName)
	{
		Applet a=this.getApplet(appletName);
	 	if(a==null)
	 		return;		
	 	this.applets.remove(appletName);
	 	a.destroy();
	 	this.remove(a);
	}		
	
	/**
	 * Get a DzzDApplet from this frame
	 *
	 * @param appletName name of the DzzDApplet to get from this frame
	 *
	 * @return DzzDApplet identified by the given name or null if not found
	 */
	public DzzDApplet getDzzDApplet(String appletName)
	{
		Applet a=this.getApplet(appletName);
	 	if(a==null)
	 		return null;	
	 	if(!(a instanceof DzzDApplet))	
	 		return null;
		return (DzzDApplet)a;
	}		
	
	/**
	 * Set an applet bounds
	 *
	 * @param name name of the applet
	 * @param left left border in pixel
	 * @param top top border in pixel
	 * @param width width  in pixel
	 * @param height height in pixel
	 */
	public void setBounds(String name,int left,int top,int width,int height)
	{
	 	Applet a=this.getApplet(name);
	 	if(a==null)
	 		return;
		a.setBounds(left,top,width,height);	
	}
	
	
	/**
	 * Applet Stub Interface
	 */
	public void appletResize(int width, int height) 
	{
	}
	
	public AppletContext getAppletContext() 
	{
		return this;
	}
	
	public URL getCodeBase() 
	{
		URL url=null;
		try
		{
			url=new URL("file:");
		}
		catch(MalformedURLException mue)
		{
		}
		catch(IOException ioe)
		{
		}
	
		return url;
	}
	
	public URL getDocumentBase() 
	{
		return getCodeBase();
	}
	
	public String getParameter(String name) 
	{
		return null;
	}
	
	public boolean isActive() 
	{
		return true;
	} 

	
	
	/**
	 * Applet Context Interface
	 */
	public Applet getApplet(String name) 
	{
		return (Applet)this.applets.get(name);
	}
	
	public Enumeration getApplets() 
	{
		return this.applets.elements();
	}
	
	public AudioClip getAudioClip(URL url) 
	{
		return Applet.newAudioClip(url) ;
	}
	
	public Image getImage(URL url) 
	{
		return Toolkit.getDefaultToolkit().getImage(url);
	}
	
	public InputStream getStream(String key) 
	{
		return null;
	}
	
	public Iterator getStreamKeys() 
	{
		return null;
	}
	
	public void setStream(String key, InputStream stream) 
	{
	}
	
	public void showDocument(URL url) 
	{
		try
		{
			Runtime.getRuntime().exec(url.toString());
		}
		catch(IOException ioe)
		{
		}
	}
	
	public void showDocument(URL url, String target) 
	{
		try
		{		
			Runtime.getRuntime().exec(url.toString());
		}
		catch(IOException ioe)
		{
		}
		
	}
	
	public void showStatus(String status) 
	{
		this.status=status;
	}	
}