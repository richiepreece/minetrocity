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

/*
 * Purpose:
 * Provides a convient manager for repetitive IO tasks wrapped to the logger
 * Handles with the tricks and details for loading a resource from any reachable location
 *
 * A trick to fool the Java plugin to cache arbitrary data files:
 * - rename them to .class
 * - download them with caching enabled
 * - Java plugin will cache the file in the java cache directory (thinks its a class file)
 * - will return cached version if available to resourcepool, so other classes can access the datafile
 *
 * Author: Matthijs Blaas
 *
 */
package net.dzzd.utils.io;

import java.io.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import java.awt.Component;

import java.applet.Applet;
import java.applet.AudioClip;

import java.net.URL;
import java.net.URLConnection;

import net.dzzd.utils.Log;
import net.dzzd.access.IProgressListener;

import java.awt.image.*;


public class IOManager implements ImageObserver
{

	/*
	 * Sets up an URL connection given a setup
	 *
	 * @param url The URL to connect to
	 * @param allowUserInteraction Wether the user is allowed to talkback
	 * @param doInput Wether we can post to the URLconnection
	 * @param doOutput Wehter we can read from the URL
	 * @param useCaches Indicates whether we should cache contents read from the URL
	 */
	public synchronized static URLConnection openURLConnection( URL     url,
																boolean allowUserInteraction,
																boolean doInput,
																boolean doOutput,
																boolean useCaches) throws IOException
	{
		return openURLConnection(url,
								 allowUserInteraction,
								 doInput,
								 doOutput,
								 useCaches,
								 null,
								 null);
	}


	/*
	 * Sets up an URL connection given a setup
	 *
	 * @param url The URL to connect to
	 * @param allowUserInteraction Wether the user is allowed to talkback
	 * @param doInput Wether we can post to the URLconnection
	 * @param doOutput Wehter we can read from the URL
	 * @param useCaches Indicates whether we should cache contents read from the URL
	 * @param userAgent The useragent to identify us with on the URL
	 * @param contentType The contenttype we're going to send over the URLConnection
	 */
	public synchronized static URLConnection openURLConnection( URL     url,
																boolean allowUserInteraction,
																boolean doInput,
																boolean doOutput,
																boolean useCaches,
																String  userAgent,
																String  contentType) throws IOException
	{
		URLConnection uc;

		uc = url.openConnection();
		
		uc.setAllowUserInteraction( allowUserInteraction );
		uc.setDoInput( doInput );
		uc.setDoOutput( doOutput );
		uc.setUseCaches( useCaches );
		
		if(userAgent != null)
			uc.setRequestProperty("User-Agent", userAgent);
			
		if(contentType != null)
			uc.setRequestProperty("Content-Type", contentType);

		return uc;
	}


	/*
	 * Write's byte[] data to an outputstream
	 *
	 * @param out The outputstream to write to
	 * @param data The byte array to write
	 */
	public synchronized static void writeData(OutputStream out, byte[] data) throws Exception
	{
		DataOutputStream dos = null;
		
		try
		{
			dos = new DataOutputStream(new BufferedOutputStream(out));		
			dos.write(data,0,data.length);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if(dos!=null)
				dos.close();
		}
	}


	/*
	 * Write's byte[] data to an outputstream
	 *
	 * @param out The outputstream to write to
	 * @param data The byte array to write
	 */
	public synchronized static void writeString(OutputStream out, String sdata) throws Exception
	{
		DataOutputStream dos = null;
		
		try
		{
			dos = new DataOutputStream(new BufferedOutputStream(out));		
			dos.writeBytes(sdata);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if(dos!=null)
				dos.close();
		}
	}


	/*
	 * Read an URLConnection's header fields and store this information in
	 * an URLResourceInfo object
	 *
	 * @param c The URLConnection to read
	 * @param info The ResourceInfo object we will store the headers info in
	 */
	public static void getResourceInfo(URLConnection c, URLResourceInfo info)
	{
		info.reset();
		
		try	{
			info.headerStatus = c.getHeaderField(0);
		}
		catch(Exception e) {
		}
		try	{
			info.headerContentEncoding = c.getContentEncoding();
		}
		catch(Exception e) {
		}
		try	{
			info.headerContentLength = c.getContentLength();
		}
		catch(Exception e)	{
		}
		try	{
			info.headerContentType = c.getContentType();
		}
		catch(Exception e) {
		}
		try	{
			info.headerDate = c.getDate();
		}
		catch(Exception e) {
		}
	}


	/*
	 * Opens a stream to a resource (from an url or from a file in classpath)
	 *
	 * @param file The resource URI
	 * @param cache Wether we want to cache the resource when possible
	 */
	public synchronized static InputStream openStream(String filename, boolean cache)
	{
		return openStream(filename, IOManager.class, null, cache);
	}


	/*
	 * Opens a stream to a resource (from an url or from a file in classpath)
	 *
	 * @param file The resource URI
	 * @param loader The class that requests the resource (when URL fails class.getResource will be tried)
	 * @param info The ResourceInfo container
	 * @param cache Wether we want to cache the resource when possible
	 */
	public synchronized static InputStream openStream(String filename, Class loader, URLResourceInfo info, boolean cache)
	{
		URL url;
		URLConnection c = null;

		if(info == null)
			info = new URLResourceInfo();
			
		if(loader == null)
			loader = IOManager.class;


		// try to load the resource from an url
		try 
		{
				url = new URL(filename);
				c   = url.openConnection();		
				c.setUseCaches(cache);
		}
		catch(Exception e) 
		{
			Log.log(IOManager.class, e);
			try
			{
				url = loader.getResource(filename);
				c   = url.openConnection();
				
				c.setUseCaches(cache);
			}
			catch(Exception e2)
			{
				Log.log(IOManager.class, e2);
				return null;
			}
		}

		getResourceInfo(c, info);
		
		try
		{
			InputStream i = c.getInputStream();
			return i;
		}
		catch(IOException e)
		{
			Log.log(IOManager.class, e);
			return null;
		}
	}


	private synchronized static void closeStream(InputStream f)
	{
		try 
		{
			f.close();
		}
		catch(Exception e )
		{
			//Log.log(IOManager.class, e);
		}
	}


	/*
	 * Downloads raw data given a resource URI
	 *
	 * @param resource The resource URI
	 * @param pl The progresslistener
	 * @param cache Wether we want to cache the resource when possible
	 */
	public synchronized static byte[] downloadData(String resource, IProgressListener pl, boolean cache)
	{
		return downloadData(resource, IOManager.class, null, pl, cache);
	}


	/*
	 * Downloads raw data given a resource URI
	 *
	 * @param resource The resource URI
	 * @param loader The class that requests loading the resource
	 * @param info The ResourceInfo container
	 * @param pl The progresslistener
	 * @param cache Wether we want to cache the resource when possible
	 *
	 * @return byte[] The raw data downloaded
	 */
	public synchronized static byte[] downloadData(String resource, Class loader, URLResourceInfo info, IProgressListener pl, boolean cache)
	{
		InputStream is = null;
		DataInputStream dis = null;

		if(info == null)
			info = new URLResourceInfo();

		try 
		{
			is = openStream(resource, loader, info, cache);
			dis = new DataInputStream(is);
			if (dis==null)
			{
				return null;
			}
			
			if(pl != null)
			{
				pl.reset();
				pl.setName(resource);
				pl.setAction(IProgressListener.ACTION_FILE_DOWNLOAD);
				if(info.headerContentLength != -1)
					pl.setMaximumProgress(info.headerContentLength);
				pl.setUnit("KB");
			}

			//TCP packets would never be too much over 1024k so let's read 1024 blocks
			int length=1024;
			
			int totalAvailable=dis.available();
			if(totalAvailable>pl.getMaximumProgress())
				pl.setMaximumProgress(totalAvailable);			

			byte[] chunk = new byte[length];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			//read in bytes from the stream & update the progresslistener if available
			int bytesRead = 0;
			int nbRead=0;
			while((bytesRead = dis.read(chunk,0,length)) != -1)
			{
				nbRead+=bytesRead;
				totalAvailable=bytesRead+dis.available();
				if(totalAvailable>pl.getMaximumProgress())
					pl.setMaximumProgress(totalAvailable);
					
				bos.write(chunk,0,bytesRead);
				
				if(pl != null)
				{
					if(info.headerContentLength != -1)
						pl.setProgress(nbRead);
					else
						pl.setProgress(nbRead);
						//pl.setProgress(bytesRead/1000);
				}
				
				//Try to never eat all CPU
	 			try
				{
					//Try to never eat all CPU alos limit to 1Mo/s
					Thread.sleep(0);
					Thread.yield();
				}
				catch(InterruptedException ie)
				{
					ie.printStackTrace();
					return null;
				}
			}

			bos.flush();

			if(pl != null)
			{
				pl.setError(false);
				pl.setFinished(true);
			}			

			return bos.toByteArray();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			
			if(pl != null)
			{
				pl.setError(true);
				pl.setFinished(true);
			}
				
			return null;
		}
		finally
		{
			if(dis != null)
				closeStream(dis);
			if(is != null)
				closeStream(is);
			if(pl != null)	
				pl.setFinished(true);
		}
	}
	

	/**
	 * Loads a resource, when resource is found in pool it will be removed from the pool
	 *
	 * @param URI The resource URI
	 *
	 * @return byte[] The byte array for the resource
	 */	
	public synchronized static byte[] loadResource(String uri)
	{
		return loadResource(uri, false, null, true);
	}


	/**
	 * Loads a resource from the resource pool or URL
	 *
	 * @param URI The resource URI
	 *
	 * @return byte[] The byte array for the resource
	 */	
	public synchronized static byte[] loadResource(String uri, boolean cacheFile, IProgressListener pl, boolean removeFromPool)
	{
		return IOManager.downloadData(uri, pl, cacheFile);
	}


	/**
	 * Loads an image from the resourcepool, classpath/jar or url
	 * Tries different strategies to load
	 *
	 * When image is found in resource pool it will be removed from the pool
	 *
	 * @param uri The image URI
	 *
	 * @return Image An Image instance for the requested image
	 */	
	public synchronized static Image loadImage(String uri)
	{
		return loadImage(uri, null, null, true);
	}
	

	/**
	 * Loads an image from the resourcepool, classpath/jar or url
	 * Tries different strategies to load
	 *
	 * When image is found in resource pool it will be removed from the pool
	 *
	 * @param uri The image URI
	 * @param c The component requesting the image to load
	 *
	 * @return Image An Image instance for the requested image
	 */	
	public synchronized static Image loadImage(String uri, IProgressListener pl, Component c)
	{
		return loadImage(uri, pl, c, true);
	}
	

	/**
	 * Loads an image from the resourcepool, classpath/jar or url
	 * Tries different strategies to load
	 *
	 * @param uri The image URI
	 * @param c The component requesting the image
	 * @param removeFromPool Whether the Image should be removed from the ResourcePool when loaded
	 *
	 * @return Image An Image instance for the requested image
	 */	
	public synchronized static Image loadImage(String uri, IProgressListener pl, Component c, boolean removeFromPool)
	{
		Image img = null;
		
		Toolkit t =Toolkit.getDefaultToolkit();


		try
		{
			URL u = new URL(uri);
			img = t.getImage(u);
			t.prepareImage(img,-1,-1,null);
			
			int flag=0;
			do
	    	{
				try
			  	{
			  		Thread.sleep(100);
			  	}
			  	catch(InterruptedException ie)
			  	{
			  		return null;
			  	}
	    		
	    		
	    		flag=t.checkImage(img,-1,-1,null);  
	    				
	    	}
	    	while( (flag & ( ImageObserver.ALLBITS |  ImageObserver.ABORT |  ImageObserver.ERROR)) ==0);

			
			
		}
		catch(Exception e3)
		{
			
		}
		
		return img;
	}
	
	//for further implementation of progress on image loading an animated gif loading
	public boolean imageUpdate(Image img,int infoflags,int x,int y,int width,int height)
	{
		  	switch(infoflags)
		  	{
		  		case ImageObserver.WIDTH|ImageObserver.HEIGHT:
		  			//this.setMaximumProgress(width*height);
		  		//return true;
		  		
		  		
		  			//System.out.println("Image properties ok");
		  		return true;
		  		
		  		case ImageObserver.SOMEBITS:
		  			//this.setProgress(x+y*width);
		  			/* uncomment to simulate network latency
		  			try
				  	{
				  		Thread.sleep(1);
				  	}
				  	catch(InterruptedException ie)
				  	{
				  		return false;
				  	}
				  	*/

		  		case ImageObserver.PROPERTIES:
		  		
		  		return true;
		  		
		  		
		  		case ImageObserver.FRAMEBITS:
		  			//Prevent animated gif to hang return false
		  		return false;
		  		
		  		case ImageObserver.ALLBITS:
		  			//this.setProgress(this.getMaximumProgress());
		  			return false;
		  		  		
		  		case ImageObserver.ERROR:
		  			return false;
		  		
		  		case ImageObserver.ABORT:
		  			return false;  
		  	}
		return false;
	}
		
	
}