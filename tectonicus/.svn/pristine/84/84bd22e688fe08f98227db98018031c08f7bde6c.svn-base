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

import net.dzzd.access.*;
import net.dzzd.utils.io.IOManager;

import java.io.*;
//import java.net.*;

public class URLHeightMap implements IURLHeightMap
{
	private int hMap[];
	private int hMapWidth;
	private int hMapHeight;
	
	public URLHeightMap()
	{
	}
	
	public void load(String baseURL,String fileName,int width,int height)
	{
		this.setWidth(width);
		this.setHeight(height);
		
		this.hMap=new int[this.hMapWidth*this.hMapHeight];
	    try
	    {
			/*URL u=new URL(baseURL+fileName);
			URLConnection uc=(URLConnection)u.openConnection();
			uc.setUseCaches(false);
			int size=uc.getContentLength();*/
			InputStream is = IOManager.openStream(baseURL+fileName, true);//uc.getInputStream();	    	
	    	for(int x=0;x<this.hMap.length;x++)
	    	{
	    		
	    		int haut=is.read();
	    		this.hMap[x]=255-haut;	
	    	}
	    	is.close();
	    }
	    catch(IOException ioe)
	    {
	    	
	    }	
	}

	public void setWidth(int width)
	{
		this.hMapWidth=width;
	}
	
	public void setHeight(int height)
	{
		this.hMapHeight=height;
	}
	
	public double getAt(double x,double y)		
	{
		//Evaluate 3D coordinate and object validity
		if(hMap==null) return 0.0;
		if(x<0.0)	return 0.0;			
		if(x>1.0)return 0.0;
		if(y<0.0)	return 0.0;			
		if(y>1.0)return 0.0;
	
		//Compute array index for given homogeneous coordinates with fixed decimal of 8 bits
		int tx=(int)(x*256.0*(this.hMapWidth-1));
		int ty=(int)(y*256.0*(this.hMapHeight-1));

		//Compute weight for heightmap index neighbords
		int p1x=tx%256;
		int p0x=256-p1x;
		int p1y=ty%256;
		int p0y=256-p1y;
		
		//Remove fixed decimal from heightmap index 
		tx>>=8;
		ty>>=8;

		//Read heigth at specified index and neighbord index
		int p0=this.hMap[tx+ty*this.hMapWidth];
		int p1=this.hMap[((tx+1)%this.hMapWidth)+ty*this.hMapWidth];
		int p2=this.hMap[tx+((ty+1)%this.hMapHeight)*this.hMapWidth];	
		int p3=this.hMap[((tx+1)%this.hMapWidth)+((ty+1)%this.hMapHeight)*this.hMapWidth];

		//Apply bilinear filtering
		int p01=p0*p0x+p1*p1x;
		p01>>=8;
		int p23=p2*p0x+p3*p1x;
		p23>>=8;
		int p0123=p01*p0y+p23*p1y;
		p0123>>=8;

		//Return homogeneous height value

		double h=(((double)p0123)/255.0);

		return h;
	}	
}