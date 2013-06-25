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
 * An data encapsulation object to store information about a URL resource
 * 
 * Author: Matthijs Blaas
 *
 */
package net.dzzd.utils.io;


public class URLResourceInfo
{
	public String headerContentEncoding, headerStatus, headerContentType;
	public int headerContentLength;
	public long headerDate;
	
	
	public void URLResourceInfo()
	{
		this.reset();
	}
	
	public void reset()
	{
		headerContentEncoding="";
		headerStatus="";
		headerContentType="";
		headerContentLength=-1;
		headerDate=-1;
	}
	
	public String toString()
	{
		String str = "";
		
		str += "Status: "+headerStatus+"\n";
		str += "Content-encoding: "+headerContentEncoding+"\n";
		str += "Content-type: "+headerContentType+"\n";
		str += "Content-length: "+headerContentLength+"\n";
		str += "Timestamp: "+headerDate+"\n";
		
		return str;
	}
}
