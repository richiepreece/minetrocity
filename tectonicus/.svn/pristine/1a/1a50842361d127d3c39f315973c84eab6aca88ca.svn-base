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
import net.dzzd.utils.Log;
import net.dzzd.utils.io.IOManager;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
//import java.net.*;
import java.io.*;



public class Font2D implements IFont2D
{
	private Shape2D shapes[];
	private double advance[];
	private double minX[];
	private double minY[];
	private double maxX[];
	private double maxY[];
	private double maxWidth;
	private double maxHeight;
	private double ratioHW;

	public void write(java.io.OutputStream out) throws IOException
	{
		DataOutputStream dos=new DataOutputStream(out);
		dos.writeDouble(this.maxWidth);
		dos.writeDouble(this.maxHeight);
		dos.writeDouble(this.ratioHW);
		for(int n=0;n<256;n++)
		{
			dos.writeBoolean(shapes[n]!=null);
			if(shapes[n] != null)
			{
				dos.writeDouble(this.advance[n]);
				dos.writeDouble(this.minX[n]);
				dos.writeDouble(this.minY[n]);
				dos.writeDouble(this.maxX[n]);
				dos.writeDouble(this.maxY[n]);				
				shapes[n].write(out);
			}
				
		}
	}
		
	public void read(java.io.InputStream in) throws IOException
	{
		DataInputStream dis=new DataInputStream(in);
		this.maxWidth=dis.readDouble();
		this.maxHeight=dis.readDouble();
		this.ratioHW=dis.readDouble();
		
		for(int n=0;n<256;n++)
		{
			boolean isNotNull=dis.readBoolean();
			if(!isNotNull)
				shapes[n]=null;			
			else
			{
				this.advance[n]=dis.readDouble();
				this.minX[n]=dis.readDouble();
				this.minY[n]=dis.readDouble();
				this.maxX[n]=dis.readDouble();
				this.maxY[n]=dis.readDouble();
				Shape2D s=new Shape2D();
				s.read(in);
				shapes[n]=s;
			}
		}
		
	}
	
	public Font2D()
	{
		this.shapes=new Shape2D[256];
		this.advance=new double[256];
		this.minX=new double[256];
		this.maxX=new double[256];
		this.minY=new double[256];
		this.maxY=new double[256];		
		this.maxWidth=1;
		this.maxHeight=1;
		this.ratioHW=1;
	}
	
	public static Font2D load(String baseURL, String fileName)
	{
		Font2D f2d=null;
		
		try
		{
			//URL u=new URL(baseURL+"/"+fileName);
			InputStream is = IOManager.openStream(baseURL+"/"+fileName, false);//u.openStream();
			f2d=new Font2D();
			f2d.read(is);
			is.close();
		}
		catch(Exception e)
		{
			Log.log(e);
			return null;
		}	
		
		return f2d;
	}	
		
	public Shape2D getShape2D(int num)
	{
		return this.shapes[num];
	}
	
	public double getAdvance(int num)
	{
		return this.advance[num];
	}

	public double getMinX(int num)
	{
		return this.minX[num];
	}	
	
	public double getMinY(int num)
	{
		return this.minY[num];
	}	
	
	public double getMaxX(int num)
	{
		return this.maxX[num];
	}	
	
	public double getMaxY(int num)
	{
		return this.maxY[num];
	}			
	
	public double getRatioHW()
	{
		return this.ratioHW;
	}
	
	//No jvm 1.1 compatibility!
	public void createFont2D(Font f)
	{
		FontRenderContext frc=new FontRenderContext(null,false,false);
		Rectangle2D r=f.getMaxCharBounds(frc);
		this.maxWidth=(double)(r.getMaxX()-r.getMinX());
		this.maxHeight=(double)(r.getMaxY()-r.getMinY());
		char tc[]=new char[1];
		double zx=1f/this.maxWidth;
		double zy=1f/this.maxHeight;
		this.ratioHW=this.maxHeight/this.maxWidth;
		for(int nc=0;nc<256;nc++)
		{
			tc[0]=(char)nc;
			GlyphVector gv=f.createGlyphVector(frc,new String(tc));
					
			GlyphMetrics gm=gv.getGlyphMetrics(0);
			this.advance[nc]=gm.getAdvance()*zx;
			this.minX[nc]=(double)gv.getVisualBounds().getMinX()*zx;
			this.maxX[nc]=(double)gv.getVisualBounds().getMaxX()*zx;
			this.minY[nc]=(double)gv.getVisualBounds().getMinY()*zy;
			this.maxY[nc]=(double)gv.getVisualBounds().getMaxY()*zy;
			
			Shape s=gv.getOutline();		
			PathIterator pi=s.getPathIterator(null);
			double[] coords=new double[6];
			Shape2D s2d=new Shape2D();
			this.shapes[nc]=s2d;
			while(!pi.isDone())
			{
				int type=pi.currentSegment(coords);
				for(int np=0;np<6;np++)
				{
					if(np%2==0)	coords[np]*=zx;
					if(np%2==1)	coords[np]*=zy;
				}
				switch(type)
				{
					case PathIterator.SEG_QUADTO:
						s2d.addKey();
						s2d.setQuadKey(coords[0],coords[1],coords[2],coords[3]);
					break;
					
					case PathIterator.SEG_CUBICTO:
						s2d.addKey();
						s2d.setCubicKey(coords[0],coords[1],coords[2],coords[3],coords[4],coords[5]);
					break;	
									
					case PathIterator.SEG_MOVETO:
						s2d.addKey();
						s2d.setStartKey(coords[0],coords[1]);
					break;
					
					case PathIterator.SEG_LINETO:
						s2d.addKey();
						s2d.setLineKey(coords[0],coords[1]);			
					break;
					
					case PathIterator.SEG_CLOSE:
						s2d.addKey();
						s2d.setEndKey();				
					break;
				}			
				pi.next();
			}
		}
	}
		
	public double getMaxWidth()
	{
		return this.maxWidth;
	}
	
	public double getMaxHeight()
	{
		return this.maxHeight;
	}	

	public double getStringWidth(String str,double size)
	{
		double advance=0f;
		for(int nc=0;nc<str.length();nc++)
			advance+=this.getAdvance(str.charAt(nc))*size;
		return advance;
	}
	
}