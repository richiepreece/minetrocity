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

import java.io.*;





public class Shape2D extends Scene2DObject implements IShape2D
{
	private int nbKey;
	private Shape2DKey firstKey;
	private Shape2DKey lastKey;
	private Shape2DKey currentKey;
	
	public void write(java.io.OutputStream out) throws IOException
	{
		DataOutputStream dos=new DataOutputStream(out);
		dos.writeInt(this.nbKey);
		this.firstKey();
		while(!this.isEnd())
		{
			this.currentKey.write(out);
			this.nextKey();
		}	
	}
		
	public void read(java.io.InputStream in) throws IOException
	{
		this.firstKey=null;
		this.lastKey=null;
		this.currentKey=null;
		DataInputStream dis=new DataInputStream(in);
		int nbKey=dis.readInt();
		for(int numk=0;numk<nbKey;numk++)
		{
			Shape2DKey nk=new Shape2DKey();
			nk.read(in);
			this.addKey(nk);
		}
		
	}
			
	public Shape2D()
	{
		this.nbKey=0;
		this.firstKey=null;
		this.currentKey=null;
		this.lastKey=null;
	}	
	
	public void firstKey()
	{
		this.currentKey=this.firstKey;
	}
	
	public void nextKey()
	{
		if(this.currentKey!=null)
			this.currentKey=this.currentKey.getNextKey();
	}		
	
	public boolean isEnd()
	{
		return (this.currentKey==null);	
	}				
	
	public int getCurrentKeyType()
	{
		if(this.currentKey==null)
			return -1;
		return this.currentKey.getType();
	}
	
	public double[] getCurrentKeyValues()
	{
		if(this.currentKey==null)
			return null;
		return this.currentKey.getValues();
	}
	
	public void addKey()
	{
		
		Shape2DKey nk=new Shape2DKey();
		
		if(this.firstKey==null)
		{
			this.firstKey=nk;
			this.lastKey=nk;
			this.currentKey=nk;
			this.nbKey++;
			return;
		}
		
		this.lastKey.setNextKey(nk);
		this.lastKey=nk;
		this.currentKey=nk;
		this.nbKey++;
	}
	
	public void setStartKey(double x1,double y1)
	{
		if(this.currentKey==null) this.addKey();
		this.currentKey.setStartKey(x1,y1);
	}
	
	public void setLineKey(double x1,double y1)
	{
		if(this.currentKey==null) this.addKey();
		this.currentKey.setLineKey(x1,y1);			
	}	

	public void setEndKey()
	{
		if(this.currentKey==null) this.addKey();
		this.currentKey.setEndKey();			
	}						
	
	public void setQuadKey(double x1,double y1,double x2,double y2)
	{
		if(this.currentKey==null) this.addKey();	
		this.currentKey.setQuadKey(x1,y1,x2,y2);		
	}
	
	public void setCubicKey(double x1,double y1,double x2,double y2,double x3,double y3)
	{
		if(this.currentKey==null) this.addKey();			
		this.currentKey.setCubicKey(x1,y1,x2,y2,x3,y3);
	}			

	private void addKey(Shape2DKey nk)
	{		
		if(this.firstKey==null)
		{
			this.firstKey=nk;
			this.lastKey=nk;
			this.currentKey=nk;
			this.nbKey++;
			return;
		}
		
		this.lastKey.setNextKey(nk);
		this.lastKey=nk;
		this.currentKey=nk;
		this.nbKey++;
	}		

	private class Shape2DKey 
	{
		private Shape2DKey nextKey;
		private int type;
		private double values[];
		
		public void write(java.io.OutputStream out) throws IOException
		{
			DataOutputStream dos=new DataOutputStream(out);
			
			dos.writeInt(this.type);
			int nbVal=0;
			switch(this.type)
			{
				case 1:	
				case 2:
					nbVal=2;
				break;
				case 3:
					nbVal=0;
				break;
				case 4:
					nbVal=4;
				break;
				case 5:
					nbVal=6;
				break;				
			}
			
			for(int v=0;v<nbVal;v++)
				dos.writeDouble(this.values[v]);
		}
		
		public void read(java.io.InputStream in) throws IOException
		{		
			DataInputStream dis=new DataInputStream(in);
			this.type=dis.readInt();			
			int nbVal=0;
			switch(this.type)
			{
				case 1:	
				case 2:
					nbVal=2;
				break;
				case 3:
					nbVal=0;
				break;
				case 4:
					nbVal=4;
				break;
				case 5:
					nbVal=6;
				break;				
			}
			this.values=new double[nbVal];
			for(int v=0;v<nbVal;v++)
				this.values[v]=dis.readDouble();	
			this.nextKey=null;
		}
 			
		Shape2DKey()
		{
			this.nextKey=null;	
			this.type=-1;
			this.values=null;	
		}
		
		public Shape2DKey getNextKey()
		{
			return this.nextKey;
		}

		public int getType()
		{
			return this.type;
		}
		
		public double[] getValues()
		{
			return this.values;
		}
		
		public Shape2DKey setNextKey(Shape2DKey nextKey)
		{
			return this.nextKey=nextKey;
		}			
		
		public void setStartKey(double x1,double y1)
		{
			this.type=1;
			this.values=new double[2];
			this.values[0]=x1;
			this.values[1]=y1;
		}
		
		public void setLineKey(double x1,double y1)
		{
			this.type=2;
			this.values=new double[2];
			this.values[0]=x1;
			this.values[1]=y1;
		}	

		public void setEndKey()
		{
			this.type=3;
			this.values=null;
		}						
		
		public void setQuadKey(double x1,double y1,double x2,double y2)
		{
			this.type=4;
			this.values=new double[4];
			this.values[0]=x1;
			this.values[1]=y1;
			this.values[2]=x2;
			this.values[3]=y2;
		}
		
		public void setCubicKey(double x1,double y1,double x2,double y2,double x3,double y3)
		{
			this.type=5;
			this.values=new double[6];
			this.values[0]=x1;
			this.values[1]=y1;
			this.values[2]=x2;
			this.values[3]=y2;
			this.values[4]=x3;
			this.values[5]=y3;
			
		}			
	}
}	