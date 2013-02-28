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

import java.awt.image.PixelGrabber;

import net.dzzd.access.*;
import net.dzzd.utils.Log;
//import net.dzzd.utils.io.IOManager;
import java.awt.Toolkit;
import java.net.*;
import java.awt.image.*;


/** 
 *  Class to manage Texture
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */

//import java.net.*;
//import java.awt.*;
import java.awt.Image;
import net.dzzd.utils.io.IOManager;


public final class URLTexture extends Texture implements ImageObserver,IURLTexture,Runnable
{
	String baseURL;				//Texture base URL	
	String sourceFile;			//Source file name
	private Image imageLoad;	//Source picture
	private boolean reload; 	//True if texture need to be reloaded
	
	public URLTexture ()
	{
		super();
		this.sourceFile=null;
		this.baseURL=null;
		this.reload=true;
	}

	public void build()
	{
		
		if(this.reload)
			this.load();
		else
			super.build();
		
	}
	
	
	public void  run() 
    {
		if(this.sourceFile==null) return;
		if(this.baseURL==null) return;
		
		//Log.log("Loading : " + this.baseURL+this.sourceFile.replace(' ','+'));

		Toolkit t =Toolkit.getDefaultToolkit();
		try
		{	
			this.imageLoad=t.getImage(new URL(this.baseURL+this.sourceFile.replace(' ','+')));	
		}
		catch(MalformedURLException  mue)
		{
			Log.log(mue);
			this.endLoading(true);
			return;
		}
		
		if(this.imageLoad == null)
		{
			this.endLoading(true);
			return;
		}
				
		if(t.prepareImage(imageLoad,-1,-1,this))
		{
			this.endLoading(false);
			return;
		}

    	int flag=0;
    	do
    	{
			try
		  	{
		  		//System.out.println("check " + sourceFile);
		  		Thread.sleep(10);
		  		Thread.yield();
		  	}
		  	catch(InterruptedException ie)
		  	{
		  		this.endLoading(false);
		  	}
    		
    		
    		flag=t.checkImage(imageLoad,-1,-1,this);  
    				
    	}
    	while( (flag & ( ImageObserver.ALLBITS |  ImageObserver.ABORT |  ImageObserver.ERROR)) ==0);
    	
    	if((flag & ImageObserver.ALLBITS) !=0)
	   		this.endLoading(false);
		else
			this.endLoading(true);		
	}

   void endLoading(boolean error)
	{
		if(!error)
		{
			//Log.log("Loading finished : ("+this.baseURL+this.sourceFile+")");
			this.imageToTexturePixels();
			this.setError(false);
			this.setFinished(true);	
			this.build();
			return;
		}
		this.setError(true);
		this.setFinished(true);	

		Log.log("Loading error ("+this.baseURL+this.sourceFile+")");
	}

	public boolean imageUpdate(Image img,int infoflags,int x,int y,int width,int height)
	{
		  	switch(infoflags)
		  	{
		  		case ImageObserver.WIDTH|ImageObserver.HEIGHT:
		  			this.setMaximumProgress(width*height);
		  		return true;
		  		
		  		case ImageObserver.SOMEBITS:
		  			this.setProgress(x+y*width);
		  			 //uncomment to simulate network latency
		  			 
		  			 /*
		  			try
				  	{
				  		Thread.sleep(1);
				  	}
				  	catch(InterruptedException ie)
				  	{
				  		return false;
				  	}*/
				return true;
				  	

		  		case ImageObserver.PROPERTIES:
		  		
		  		return true;
		  		
		  		
		  		case ImageObserver.FRAMEBITS:
		  			//Prevent animated gif to hang return false
		  		return false;
		  		
		  		case ImageObserver.ALLBITS:
		  			this.setProgress(this.getMaximumProgress());
		  		return false;
		  		  		
		  		case ImageObserver.ERROR:
		  		return false;
		  		
		  		case ImageObserver.ABORT:
		  		return false;  
		  	}
		return false;
	}
	

   void prepareTextureSize()
	{
		this.largeur=this.imageLoad.getWidth(null);
		this.hauteur=this.imageLoad.getHeight(null);
		this.maskLargeur=1;
		this.decalLargeur=0;
		this.largeurImage=1;
		while(this.largeurImage<this.largeur)
		{
			this.largeurImage<<=1;
			this.decalLargeur++;
		}
		this.maskLargeur=this.largeurImage-1;
	
		this.maskHauteur=1;
		this.decalHauteur=0;
		this.hauteurImage=1;
		while(this.hauteurImage<this.hauteur)
		{
			this.hauteurImage<<=1;			
			this.decalHauteur++;
		}
		this.maskHauteur=this.hauteurImage-1;
		
		if(this.largeur!=this.largeurImage || this.hauteur!=this.hauteurImage)		
		{
			Image i=this.imageLoad.getScaledInstance(this.largeurImage,this.hauteurImage,Image.SCALE_SMOOTH);
			this.imageLoad = i;
			this.largeur=this.largeurImage;
			this.hauteur=this.hauteurImage;
		}
		
	}

   void imageToTexturePixels()
   {
   		this.prepareTextureSize();
		this.pixels=new int[this.largeurImage*this.hauteurImage];
		PixelGrabber pg= new PixelGrabber(this.imageLoad,0,0,this.largeur,this.hauteur,this.pixels,0,this.largeurImage);
    	try 
    	{
    		pg.grabPixels();
    	} 
    	catch (InterruptedException ie) 
    	{
    		ie.printStackTrace();
    	}				
		this.imageLoad=null;
		System.gc();
	}
   

    /*
     *INTERFACE ITexture
     */
    public void setSourceFile(String sourceFile)
    {
    	this.reload=true;
    	this.sourceFile=sourceFile;
    }
    
    public String getSourceFile()
    {
    	return this.sourceFile;
    }    
    
    public void setBaseURL(String baseURL)
    {
    	this.reload=true;
    	this.baseURL=baseURL;
    }
    
    public String getBaseURL()
    {
    	return this.baseURL;
    }
     
  	public void load (String baseURL,String sourceFile)
	{
		this.baseURL=baseURL;
		this.sourceFile=sourceFile;
		this.load();
	}
	  
  	public void load(String baseURL,String sourceFile,boolean useMipMap)
	{
		this.baseURL=baseURL;
		this.sourceFile=sourceFile;
		this.load();
	}
  
	public void load()
	{
		//Log.log("loading :"+this.sourceFile);
		this.reset();
		this.setAction(IProgressListener.ACTION_FILE_DOWNLOAD);
		this.reload=false;	
	 	Thread tr=new Thread(this);
	 	tr.start();
	}
	        	        
        
}
