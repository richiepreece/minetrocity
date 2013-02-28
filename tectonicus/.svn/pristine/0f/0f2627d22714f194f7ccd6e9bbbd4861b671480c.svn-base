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

import java.awt.Canvas;



public class Drawer2D implements IDrawer2D
{
	protected double xMin;	//Clip x Minimum x coordinate for a pixels
	protected double xMax;	//Clip x Maximum x coordinate for a pixels
	protected double yMin;	//Clip y Minimum y coordinate for a pixels
	protected double yMax;	//Clip y Maximum y coordinate for a pixels
	protected int opacity;	//Current opaciy for drawing
	protected Font2D font;	//Current font for drawing (null if none)
	protected int color;	//Current color RGB for drawing without alpha (see opacity)
	protected int width;	//Drawer width in pixels
	protected int height;	//Drawer height in pixels
	
	protected double sx;		//Starting x pos for current shape
	protected double sy;		//Starting y pos for current shape
	protected double cx;		//Current x pos for shape
	protected double cy;		//Current y pos for shape
	protected boolean sFlag;	//True if a shape is started
	
	protected int mode;			//Drawing mode (PLAIN, etc...)

	
	public Drawer2D()
	{
		this.width=0;
		this.height=0;
		this.xMin=0;
		this.yMin=0;
		this.xMax=0;
		this.yMax=0;
		this.color=0xFF000000;
		this.opacity=0xFF;
		this.font=null;
		this.mode=1;
		
	}
	
	//TODO: TEMP FOR JOGL DEMO
	public Canvas getCanvas() {
		return null;
	}
	
	public void doRender() {
	}
	//////////////////////////////
	
	
	public final void setMode(int mode)
	{
		this.mode=mode;
	}
	
	public final void setClip(double xMin,double yMin,double xMax,double yMax)
	{
		this.xMin=Math.max(0,xMin);
		this.yMin=Math.min(this.width-1,yMin);
		this.xMax=Math.max(0,xMax);
		this.yMax=Math.min(this.height-1,yMax);		
	}
	
	public final void drawString(String str,double px,double py,double size)
	{
		if(this.font==null)
			return;
			
		double advance=0f;
		for(int nc=0;nc<str.length();nc++)
		{
			int nChar=str.charAt(nc)&0xFF;
			Shape2D s= this.font.getShape2D(nChar);
			
		/*	if(	(px+advance+this.font.getMaxX(nChar)*size>=this.xMin)&&
				(px+advance+this.font.getMinX(nChar)*size<=this.xMax)&&
				(py+this.font.getMaxY(nChar)*size>=this.yMin)&&
				(py+this.font.getMinY(nChar)*size<=this.yMax))
					drawShape2D(s,px+advance,py,size,size*this.font.getRatioHW());*/
			
			advance+=this.font.getAdvance(nChar)*size;
			drawShape2D(s,100+advance,100,100,10*this.font.getRatioHW());
		}
	}	

	public void setSize(int width,int height)
	{
		this.width=width;
		this.height=height;	
		this.setClip(this.xMin,this.yMin,this.xMax,this.yMax);	
	}

	public int getColor()
	{
		return this.color;
	}		

	public void setColor(int color)
	{
		this.opacity=(color>>24)&0xFF;
		this.color=color&0xFFFFFF;		
	}	
			
	public IFont2D getFont2D()
	{
		return this.font;
	}
	
	public void setFont2D(IFont2D font)
	{
		this.font=(Font2D)font;
	}
			
	public void setPixel(double x,double y,int color)
	{
				
	}
	
	public void setPixel(int x,int y,int color)
	{
				
	}		
	
	public void drawShape2D(IShape2D shape,double px,double py,double zoomX,double zoomY)
	{
		this.beginShape2D();
		shape.firstKey();
		while(!shape.isEnd())
		{	
			int type=shape.getCurrentKeyType();
			double[] coords=shape.getCurrentKeyValues();
			switch(type)
			{
				case 1:
					this.moveTo(coords[0]*zoomX+px,coords[1]*zoomY+py);
				break;
				
				case 2:
					this.lineTo(coords[0]*zoomX+px,coords[1]*zoomY+py);
				break;
				
				case 3:
				break;
								
				case 4:
					this.quadTo(coords[0]*zoomX+px,coords[1]*zoomY+py,coords[2]*zoomX+px,coords[3]*zoomY+py);
				break;
				
				case 5:
				
				break;	
			}
			shape.nextKey();
		}
		
		this.endShape2D();					
	}	
		
	public void beginShape2D()
	{
		this.sx=0;
		this.sy=0;
		this.cx=0;
		this.cy=0;
		this.sFlag=true;
	}
	
	public void moveTo(double x,double y)
	{
		if(!this.sFlag) return;
		this.sx=x;
		this.sy=y;
		this.cx=x;
		this.cy=y;
	}
	
	public void lineTo(double x,double y)
	{
		if(!this.sFlag) return;					
		this.cx=x;
		this.cy=y;	
	}
	
	public void quadTo(double x,double y,double x2,double y2)
	{
		if(!this.sFlag) return;	
		this.quadShape2D(this.cx,this.cy,x,y,x2,y2,0,1);
		this.lineTo(x2,y2);
	}
		
	public void endShape2D()
	{
		if(!this.sFlag) return;
		//	this.lineTo(this.sx,this.sy);			
		this.sFlag=false;
	}
	
	public void line(double x1,double y1,double x2,double y2)
	{
	}
	
	public void fillSolid()
	{
	}
				
	protected void lineShape2D(double x1,double y1,double x2,double y2)
	{

	}
		
	private static double fac[]={1,1,2,6,24};
	
	private double B(int n,int m,double t)
	{
		//TODO: can be improved 
		return this.C(n,m) * Math.pow(t,m) * Math.pow(1-t,n-m);
	}
	
	private double C(int n,int m)
	{
		//TODO: can be easily improved using a 2d lokkup table
		return fac[n]/(fac[m]*fac[n-m]);
	}

	private void quadShape2D(double x1,double y1,double x2,double y2,double x3,double y3,double t1,double t3)
	{	

		double t2=(t1+t3)*0.5;
		
		
		double B20=this.B(2,0,t1);
		double B21=this.B(2,1,t1);
		double B22=this.B(2,2,t1);
		double nx1=B20*x1+B21*x2+B22*x3;
		double ny1=B20*y1+B21*y2+B22*y3;
		
		B20=this.B(2,0,t2);
		B21=this.B(2,1,t2);
		B22=this.B(2,2,t2);
		double nx2=B20*x1+B21*x2+B22*x3;
		double ny2=B20*y1+B21*y2+B22*y3;		
		
		B20=this.B(2,0,t3);
		B21=this.B(2,1,t3);
		B22=this.B(2,2,t3);
		double nx3=B20*x1+B21*x2+B22*x3;
		double ny3=B20*y1+B21*y2+B22*y3;
		
		if((Math.abs(nx1-nx2)>1) || (Math.abs(ny1-ny2)>1))
		{
			this.quadShape2D(x1,y1,x2,y2,x3,y3,t1,t2);
			this.quadShape2D(x1,y1,x2,y2,x3,y3,t2,t3);
			return;
		}

		if((Math.abs(nx2-x1)>=1) || (Math.abs(ny2-y1)>=1))
			this.lineTo(nx2,ny2);
			
		if((Math.abs(nx2-x3)>=1) || (Math.abs(ny2-y3)>=1))
			this.lineTo(nx2,ny2);
	}		
	
	public void quad(double x1,double y1,double x2,double y2,double x3,double y3)
	{
		this.quad(x1,y1,x2,y2,x3,y3,0f,0.5f,1f);
	}
	
	private void quad(double x1,double y1,double x2,double y2,double x3,double y3,double t1,double t2,double t3)
	{
		//TODO: need to be remake as quadShape2D
		if(y1<this.yMin && y2<this.yMin && y3<this.yMin)
			return ;
			
		if(y1>this.yMax && y2>this.yMax && y3>this.yMax)
			return ;
			
		if(x1<this.xMin && x2<this.xMin && x3<this.xMin)
			return ;
			
		if(x1>this.xMax && x2>this.xMax && x3>this.xMax)
			return ;
			
			
		double B20=this.B(2,0,t1);
		double B21=this.B(2,1,t1);
		double B22=this.B(2,2,t1);
		double nx1=B20*x1+B21*x2+B22*x3;
		double ny1=B20*y1+B21*y2+B22*y3;
		
		B20=this.B(2,0,t2);
		B21=this.B(2,1,t2);
		B22=this.B(2,2,t2);
		double nx2=B20*x1+B21*x2+B22*x3;
		double ny2=B20*y1+B21*y2+B22*y3;		
		
		B20=this.B(2,0,t3);
		B21=this.B(2,1,t3);
		B22=this.B(2,2,t3);
		double nx3=B20*x1+B21*x2+B22*x3;
		double ny3=B20*y1+B21*y2+B22*y3;				
			
		if((Math.abs(nx1-nx2)<2) && (Math.abs(ny1-ny2)<2))
		{
			this.line(nx1,ny1,nx2,ny2);
		}
		else
		{
			this.quad(x1,y1,x2,y2,x3,y3,t1,(t1+t2)*0.5f,t2);
		}
		
		if((Math.abs(nx2-nx3)<2) && (Math.abs(ny2-ny3)<2))
		{
			this.line(nx2,ny2,nx3,ny3);
		}
		else		
		{
			this.quad(x1,y1,x2,y2,x3,y3,t2,(t2+t3)*0.5f,t3);			
		}		
		
	}
	
	public void renderScene2D(IScene2D s)
	{
	}
		
}