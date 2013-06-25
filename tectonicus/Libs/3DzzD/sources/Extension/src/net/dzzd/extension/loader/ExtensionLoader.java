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
 * ExtensionLoader.java
 *
 * Purpose:
 * (Down)loads & caches jar files that contain extensions. Provides a method to load these extensions using
 * privileges outside the sandbox. Uses a custom classloader & securitymanager to load & resolve these runtime
 * downloaded classes.
 *
 * TODO:
 *		- do versioncontrol (check for timestamp whether to replace)
 *      - enhance security manager
 *      - implement progresslistener
 * 
 * Author: Matthijs Blaas
 *
 */
package net.dzzd.extension.loader;

import java.io.*;
import java.util.zip.*;
import java.util.Hashtable;
import java.util.Enumeration;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedAction;
import java.security.Permission;

import net.dzzd.access.*;
import net.dzzd.utils.Log;



public class ExtensionLoader extends ClassLoader implements IExtensionLoader
{
	// singleton instance of this loader
	private static ExtensionLoader loader = null;
	// A classpool that resides in memory, when a class is requested we'll look it up in here
	private Hashtable classPool;
	// The directory to which we download/cache our extensions
	private String extDir;


	// Creates an instance of this extensionloader with the calling class as a reference (which classloader we'll use to delegate requests we cannot resolve)
	private ExtensionLoader(Class parent) throws Exception
	{
		// Create a new URLClassloader which delegates requests to its parent classloader when it cant find classes
		super(parent.getClassLoader());
		// Make this classloader available to other classes by setting it in the current thread's context
		Thread.currentThread().setContextClassLoader( this );
		// We're in charge now!
		System.setSecurityManager(new MySecurityManager());
		// Allocate a classpool for this classloader (key=classname, value=class bytedata)
		classPool = new Hashtable();
		
		// construct the root directory to install extensions in
		/*
        String root;
        if (SystemInfo.isWindows())
        {
        	if(SystemInfo.isVista())
        	{
        		root = "AppData" + File.separator + "LocalLow";
        	}
        	else
        		root = "Application Data";
        }
        else if (SystemInfo.isMacOS())
        {
            root = "Library" + File.separator + "Application Support";
        }
        else // isLinux() or something wacky 
        {
            root = ".dzzd_extloader";
        }
        */
        
        extDir = System.getProperty("user.home") + File.separator;// + root + File.separator;
	}
	

	// Gets an interface to acces ExtensionLoader
	public static IExtensionLoader getLoader(final Class parent) throws Exception
	{
		if(loader != null) return loader;
		loader = null;
		
		return (ExtensionLoader)AccessController.doPrivileged
		(
			new PrivilegedExceptionAction()
			{
				public Object run() throws Exception
				{
					loader = new ExtensionLoader(parent);
					
					// Check if we were granted with the necesary privileges
			        SecurityManager sm = System.getSecurityManager();      
			        if (sm != null)
			        {
			            try
			            {
			                sm.checkWrite("ExtensionLoader");
			                sm.checkPropertiesAccess();
			            }
			            catch (SecurityException se)
			            {
			                throw new Exception("User rejected certificate");
			            }
			        }
					
					return loader;
				}
			}
		);
	}
	
	
	// Add a class to the classpool maintained by this classloader
	protected void addClass(String name, byte[] classData) throws Exception
	{
		if(classPool.containsKey(name)) {
			throw new Exception("ExtensionLoader: Cannot add class "+name+", already in classpool");
		}
		
		// Replace the file seperators with . as we'll be looking up against java packages
		// As on some platforms '/' or '\' is used while the file seperator is different, we check for these too
		String temp;
		temp = name.replace(File.separator.charAt(0), '.');
		temp = name.replace('\\', '.');
		temp = name.replace('/', '.');
		classPool.put(temp, classData);
	}
	


	// Read a timestamp from an index (idx) file
    protected long readTimeStamp(File indexFile)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(indexFile));
            try {
                String str = reader.readLine();
                return Long.parseLong(str);
            }
            finally {
                reader.close();
            }
        } catch (Exception ex) {
        }
        return -1;
    }

	// Write an index file with an timestamp so we know when to update a file
    protected void writeTimeStamp(File indexFile, long timestamp) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));
            try {
                writer.write("" + timestamp + "\n");
                writer.flush();
            } finally {
                writer.close();
            }
        } catch (Exception ex) {
        }
    }


	// Downloads a jar file over an URL to a local file
	protected void downloadJar(URL downloadURL, File jarFile, IProgressListener pl)
	{
	    BufferedOutputStream out    = null;
	    InputStream in              = null;
		URLConnection urlConnection = null;
	
	    try
	    {
	    	// setup in- & output streams
	    	urlConnection   = downloadURL.openConnection();
			out             = new BufferedOutputStream(new FileOutputStream(jarFile));
			in              = urlConnection.getInputStream();
			int len         = in.available();
			
			//progressListener.setTotal(urlConnection.getContentLength());
			Log.log("downloading jar with size: "+urlConnection.getContentLength());
	
			// sanity check
			if (len < 1) len = 1024;
			byte[] buffer = new byte[len];
			
			// read in bytes from the url
			while ( (len = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, len);
				//progressListener.update(len);
			}
	
			out.close();
			in.close();
		}
		catch (Exception e) {
		} 
		finally
		{
			// close the streams
			if (out != null) {
				try	{
					out.close();
				}
				catch (IOException ignore) {
				}
			}
			if (in != null)	{
				try	{
					in.close();
				}
				catch (IOException ignore) {
				}
			}
		}
		
	}


	// Adds a jarfile's class files to the classloaders classpool
	protected void addJar(File jarFile) throws Exception
	{
		byte[] chunk;
		InputStream is;
		ByteArrayOutputStream bos;
		
		// extract all classes from zip and add their bytecode to our classpool
		ZipFile zf = new ZipFile(jarFile);
		ZipEntry ze;
		
		// loop through the entries and add them to the classpool
	    for (Enumeration e = zf.entries(); e.hasMoreElements() ;)
	    {
			ze = (ZipEntry)e.nextElement();
			
			if (!ze.isDirectory())
			{
				// We're only interested in class entries
				if(ze.getName().endsWith(".class"))
				{
					Log.log("addjar: found class in jarfile: "+ze.getName());
					is = zf.getInputStream(ze);
					bos = new ByteArrayOutputStream();
					
					int length = is.available();
					if(length<1) length = 1024;
					
					chunk = new byte[length];
					int bytesRead = 0;
					while((bytesRead = is.read(chunk,0,length)) != -1) {
						bos.write(chunk,0,bytesRead);
					}
					
					is.close();
					bos.flush();
					
					// add this class to our classpath
					addClass(ze.getName(), bos.toByteArray());
					
					bos.reset();
				}
			}
		}
	}


	// (Down)load a given jar file and add it to our classloader
	public void loadJar(final String extName, 
						final String url,
						final String fileName,
						final IProgressListener pl) throws Exception
	{
		pl.setName(fileName);
		pl.setProgress(0);
		pl.setFinished(false);
		pl.setStarted(true);
		
		// Check & prepare install directory
		String installDirName = extDir + File.separator + extName;
		Log.log("extension installation directory: "+installDirName);
		File installDir = new File(installDirName);
		
		if (!installDir.exists()){
			if (!installDir.mkdirs()) {
				throw new Exception("ExtensionLoader.loadJar: Cannot create install directory: "+installDirName);
			}
		}
		

		// Get an URL to the file to download
    	URL downloadURL = new URL(url+fileName);

		// Get a handle to the local cached copy (if it exists)
		File jarFile = new File(installDirName, fileName);
		
		// Index file and its properties to validate cache entries timestamp with
		File   indexFile = null;
       	
       	// Get last datetime file was modified
		long   urlTimeStamp = downloadURL.openConnection().getLastModified();

		// Construct the filename for the index file
		String indexFileName = "";

		int idx = fileName.lastIndexOf(".");
		if (idx > 0) {
			indexFileName = fileName.substring(0, idx);
		}
        else {
        	indexFileName = fileName;
        }

        indexFileName = indexFileName + ".idx";
        Log.log("index filename: "+indexFileName);
		
		// Indicates whether we should update the file
		boolean isDirty = true;
			
		// Check if we should download or update or fetch the file from cache
		if(jarFile.exists())
		{
			Log.log("extensionfile already exists: "+fileName);        
	        
	        // Get a handle to the index filename
	        indexFile = new File(installDir, indexFileName);
			
			// Validate the index file
			if(indexFile.exists())
			{
				Log.log("indexfile already exists");
				long cachedTimeStamp = readTimeStamp(indexFile);
				
				isDirty = !(cachedTimeStamp == urlTimeStamp);
				Log.log("cached file dirty: "+isDirty+", url timestamp: "+urlTimeStamp+" cache stamp: "+cachedTimeStamp);
			}
			else
			{
				Log.log("indexfile doesn't exist, assume cache is dirty");
			}
		}


		// Check if we should cache/replace the extension file
		if(isDirty)
		{
            if(jarFile.exists())
            {
    	        if(indexFile != null && indexFile.exists()) {
    	        	Log.log("deleting old index file");
	            	indexFile.delete();
	            }

            	indexFile = new File(installDirName, indexFileName);
				Log.log("deleting old cached file");
            	jarFile.delete();
            }

			downloadJar(downloadURL, jarFile, pl);
			
        	// Get a handle to a new indexfile
        	indexFile = new File(installDir, indexFileName);				
			
			Log.log("writing timestamp to index file");
			writeTimeStamp(indexFile, urlTimeStamp);
		}
			
		// And finally add the class files from the extension to the classpool
		addJar(jarFile);
	}


	// We override the findclass method to search for classses in our loaded jar(zip)file
	protected Class findClass(String name)
	{
		// Add .class for lookup against our extracted zip file
		String fullName = name.concat(".class");
		
		if(classPool.containsKey(fullName)) {
			byte[] data = (byte[])classPool.get(fullName);
			return defineClass(name, data, 0, data.length);
		}
		
		return null;
	}


 	// Load Extension and return a newly instanciated class to access it
	public Object loadExtension(String baseURL,
								String jarFile,
								String extensionClass,
								String localDirectory,
								IProgressListener pl,
								String mainClassName) 
	{
		pl.setProgress(0);
		pl.setFinished(false);
		pl.setError(false);	
		
		try
		{		
			loader.loadJar(localDirectory, baseURL, jarFile, pl);
			Class c=loader.loadClass(extensionClass);
			if(c==null)
				Log.log("cannot find extension class : extensionClass");
			IExtension jl = (IExtension)c.newInstance();
			
			jl.load(baseURL, localDirectory, pl, loader);
			Object o=loader.loadClass(mainClassName).newInstance();
			return o;
		}
		catch(Exception e)
		{
				pl.setProgress(100);
				pl.setFinished(true);
				pl.setError(true);	
				e.printStackTrace();		
		}

		return null;
	}


	// TODO: add permission checks here, temporarily allows all loaded classes to do everything they want!
	//       make it only allow dzzd extensions (check package or something?)
	private class MySecurityManager extends SecurityManager
	{
		public MySecurityManager() {
			super();
		}

		public void checkPermission(Permission perm) throws SecurityException, NullPointerException {
		}

		public void checkPermission(Permission perm, Object context) throws SecurityException, NullPointerException {
		}
	}
}