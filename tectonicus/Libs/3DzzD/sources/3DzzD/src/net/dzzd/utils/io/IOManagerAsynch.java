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

package net.dzzd.utils.io;

import net.dzzd.access.*;



public class IOManagerAsynch implements Runnable
{
	byte[] data;
	String resource;
	Class loader;
	URLResourceInfo info;
	IProgressListener pl;
	boolean cache;
	public boolean running;
	
	public IOManagerAsynch()
	{
		
	}
	
	public IOManagerAsynch downloadData(String resource, Class loader, URLResourceInfo info, IProgressListener pl, boolean cache)
	{
		this.running=true;
		this.resource=resource;
		this.loader=loader;
		this.info=info;
		this.pl=pl;
		this.cache=cache;	
		Thread t=new Thread(this);		
		t.start();
		return this;
	}
	
	public void run()
	{
		
		this.data = IOManager.downloadData(resource,loader,info,pl,cache);
		this.running=false;
	}	
	
	public byte[] getData()
	{
		return this.data;
	}
}