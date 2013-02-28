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

public class PixelsDrawer2D extends Drawer2D implements IPixelsDrawer2D
{
	private int pixels[];			//Pixel buffer target
	private int pixelBufferWidth;	//Pixel buffer target width
	private int bufferWidth;		//Pixel buffer target width
	private int bufferHeight;		//Pixel buffer target height	
	private int buffer[];			//Segment buffer
	private double minX[];			//Temporary minimum segment start
	private double maxX[];			//Temporary maximum segment start
	private double symin;			//Temporary first y line to draw
	private double symax;			//Temporary last y line to draw
	
	//TODO: dont be afraid I am working on it ;-)
	
	public PixelsDrawer2D()
	{
		super();
		this.pixels=null;
	}

	public void setPixelsTarget(int[] pixels,int width,int height)
	{
		this.setPixelsTarget(pixels,width,height,width);		
	}
	 
	public void setPixelsTarget(int[] pixels,int width,int height,int pixelBufferWidth)
	{
		this.pixels=pixels;
		this.setSize(width,height);
		this.pixelBufferWidth=pixelBufferWidth;
		this.setClip(0,0,width-1,height-1);
		this.initShape2DBuffer(width,height);			
	}
	
	private void initShape2DBuffer(int bufferWidth,int bufferHeight)
	{
		this.bufferWidth=bufferWidth;
		this.bufferHeight=bufferHeight;			
		if(this.buffer==null || this.buffer.length!=bufferWidth*bufferHeight)
		{
			this.minX=new double[this.bufferHeight];
			this.maxX=new double[this.bufferHeight];
			this.buffer=new int[this.bufferWidth*this.bufferHeight];
		}
	}
	
	int lx=0;
	int ly=0;
	
	public void moveTo(double x,double y)
	{
		super.moveTo(x,y);
		this.lx=(int)x;
		this.ly=(int)y;
	}
	
	public void lineTo(double x,double y)
	{
		if(!this.sFlag) return;
		int nx=(int)x;
		int ny=(int)y;
		this.tracePixelTo(nx,ny);
		this.lx=nx;
		this.ly=ny;
		super.lineTo(x,y);
	}
	
	private void tracePixelTo(int ex,int ey)
	{
		int sx=this.lx;
		int sy=this.ly;
		
		int dx=ex-sx;
		int dy=ey-sy;
		
		if(dx==0 && dy==0)
			return;
		
		int x=sx;
		int y=sy;
					
		int stepX=1;
		int stepY=1;
		
		if(dx<0){dx=-dx;stepX=-1;};	
		if(dy<0){dy=-dy;stepY=-1;};
		
		dx++;
		dy++;
		
		int d=dx-dy;
		
		this.setPixel(x,y,this.color);
		
		while(ex!=x || ey!=y)
		{
			if(d>=0)
			{		
				x+=stepX;
				d-=dy;
			}

			if(d<0)
			{
				y+=stepY;
				d+=dx;				
			}
			this.setPixel(x,y,this.color);
		}			

	}		
		
	private void fillSolidCurrentShape2D()
	{
		
	}	
				
	public void fillSolid()
	{
		int iYMin=(int)this.yMin;
		int iYMax=(int)this.yMax;
		int iXMin=(int)this.xMin;
		int iXMax=(int)this.xMax;		
		int ofsY=iYMin*this.pixelBufferWidth;
		
		if(this.opacity==0xFF)
		{
			int color=0xFF000000|this.color;
			for(int y=iYMin;y<=iYMax;y++)
			{
				int ofsStart=iXMin+ofsY;
				int ofsEnd=iXMax+ofsY;
				for(int ofs=ofsStart;ofs<=ofsEnd;this.pixels[ofs++]=color);	
				ofsY+=this.pixelBufferWidth;
			}
		}
		else
		{
			int nColor=(((((this.color&0xFF00FF)*this.opacity)&0xFF00FF00)|(((this.color&0x00FF00)*this.opacity)&0x00FF0000))>>8);
			int pBack=this.opacity^0xFF;
			
			for(int y=iYMin;y<=iYMax;y++)
			{
				int ofsStart=iXMin+ofsY;
				int ofsEnd=iXMax+ofsY;				
				int ofs=ofsStart;
				while(ofs<=ofsEnd)
				{
					int bColor=this.pixels[ofs];
					bColor=(((((bColor&0xFF00FF)*pBack)&0xFF00FF00)|(((bColor&0x00FF00)*pBack)&0x00FF0000))>>8);
					this.pixels[ofs++]=nColor+bColor;
				}					
				ofsY+=this.pixelBufferWidth;
			}		
		}
	}

	public void line(double x1,double y1,double x2,double y2)
	{
		if(y1<this.yMin)
		{
			if(y2<this.yMin)
				return;
			if(y2!=y1)
				x1=x1+(this.yMin-y1)*(x2-x1)/(y2-y1);
			y1=this.yMin;
				
		}
		else
			if(y1>this.yMax)
			{
				if(y2>this.yMax)
					return;
				if(y2!=y1)
					x1=x1+(this.yMax-y1)*(x2-x1)/(y2-y1);	
				y1=this.yMax;
			}		
			
		if(y2<this.yMin)
		{
			if(y1<this.yMin)
				return;
			if(y2!=y1)
				x2=x1+(this.yMin-y1)*(x2-x1)/(y2-y1);
			y2=this.yMin;
				
		}
		else
			if(y2>this.yMax)
			{
				if(y1>this.yMax)
					return;
				if(y2!=y1)
					x2=x2+(this.yMax-y2)*(x2-x1)/(y2-y1);	
				y2=this.yMax;
			}
				
		if(x1>this.xMax && x2>this.xMax) return;	
		if(x1<this.xMin && x2<this.xMin) return;				
						
		if(x1<this.xMin)
		{
			if(x2<this.xMin)
				return;
			if(x2!=x1)
				y1=y1+(this.xMin-x1)*(y2-y1)/(x2-x1);
			x1=this.xMin;
				
		}
		else
			if(x1>this.xMax)
			{
				if(x2>this.xMax)
					return;
				if(x2!=x1)
					y1=y1+(this.xMax-x1)*(y2-y1)/(x2-x1);	
				x1=this.xMax;
			}		
			
		if(x2<this.xMin)
		{
			if(x1<this.xMin)
				return;
			if(x2!=x1)
				y2=y1+(this.xMin-x1)*(y2-y1)/(x2-x1);
			x2=this.xMin;
				
		}
		else
			if(x2>this.xMax)
			{
				if(x1>this.xMax)
					return;
				if(x2!=x1)
					y2=y2+(this.xMax-x2)*(y2-y1)/(x2-x1);	
				x2=this.xMax;
			}
					
		double dxf=x2-x1;
		double dyf=y2-y1;
		
		int color=this.color;
		
		if(Math.abs(dxf)>Math.abs(dyf))
		{
			int s=(int)x1;
			int e=(int)x2;
			
			if(Math.abs(e-s)>0)
			{
				double af=dyf/dxf;
				this.lineX(s,e,y1,af);
				return;
			}
			else
			{
				this.setPixel((int)x1,(int)y1,this.color);
				this.setPixel((int)x2,(int)y2,this.color);
			}

			
		}
		else
		{
			int s=(int)y1;
			int e=(int)y2;
			
			if(Math.abs(e-s)>0)
			{
				double af=dxf/dyf;
				this.lineY(s,e,x1,af);
				return;
			}
			else
			{
				this.setPixel((int)x1,(int)y1,this.color);
				this.setPixel((int)x2,(int)y2,this.color);
			}
			
		}
	}

	private void lineY(int s,int e,double x1,double af )	
	{
			int n=s;
			int step=(e>=s?1:-1);
			for(n=s;n!=e;n+=step)		
				this.setPixel((int)(x1+(n-s)*af),n,0xff0000);//this.color);
	}
	
	private void lineX(int s,int e,double y1,double af )	
	{
			int n=s;
			int step=(e>=s?1:-1);			
			for(n=s;n!=e;n+=step)					
				this.setPixel(n,(int)(y1+(n-s)*af),0x00FF00);//this.color);			
	}	
	
	public void setPixel(double x,double y,int color)
	{
		int iy=(int)y;
		int ix=(int)x;
		
		int ofsXY00=iy*this.pixelBufferWidth+ix;
		int ofsXY10=ofsXY00+1;
		int ofsXY01=ofsXY00+this.pixelBufferWidth;
		int ofsXY11=ofsXY01+1;

		int px=256-(((int)(x*256.0))&0xFF);	
		int py=256-(((int)(y*256.0))&0xFF);	
//px=256;
//py=256;
		int ppx=256-px;
		int ppy=256-py;
				
		int p00=(px*py)>>8;
		int p10=(ppx*py)>>8;
		int p01=(px*ppy)>>8;
		int p11=(ppx*ppy)>>8;
		
		int c00=this.pixels[ofsXY00];
		int c10=this.pixels[ofsXY10];
		int c01=this.pixels[ofsXY01];
		int c11=this.pixels[ofsXY11];
		
		int n00=(((((color&0xFF00FF)*p00)&0xFF00FF00)|(((color&0x00FF00)*p00)&0x00FF0000))>>8);
		int n10=(((((color&0xFF00FF)*p10)&0xFF00FF00)|(((color&0x00FF00)*p10)&0x00FF0000))>>8);
		int n01=(((((color&0xFF00FF)*p01)&0xFF00FF00)|(((color&0x00FF00)*p01)&0x00FF0000))>>8);
		int n11=(((((color&0xFF00FF)*p11)&0xFF00FF00)|(((color&0x00FF00)*p11)&0x00FF0000))>>8);

		p00=256-p00;
		p01=256-p01;
		p10=256-p10;
		p11=256-p11;
		
		c00=(((((c00&0xFF00FF)*p00)&0xFF00FF00)|(((c00&0x00FF00)*p00)&0x00FF0000))>>8);
		c10=(((((c10&0xFF00FF)*p10)&0xFF00FF00)|(((c10&0x00FF00)*p10)&0x00FF0000))>>8);
		c01=(((((c01&0xFF00FF)*p01)&0xFF00FF00)|(((c01&0x00FF00)*p01)&0x00FF0000))>>8);
		c11=(((((c11&0xFF00FF)*p11)&0xFF00FF00)|(((c11&0x00FF00)*p11)&0x00FF0000))>>8);

		this.pixels[ofsXY00]=n00+c00;		
		this.pixels[ofsXY10]=n10+c10;		
		this.pixels[ofsXY01]=n01+c01;		
		this.pixels[ofsXY11]=n11+c11;		
			
	}
	
	public void setPixel(int x,int y,int color)
	{
		int ofsXY=y*this.pixelBufferWidth+x;
		this.setPixel(ofsXY,color);	
	}	
	
	public int getPixel(int x,int y)
	{
		int ofsXY=y*this.pixelBufferWidth+x;
		return this.getPixel(ofsXY);	
	}	
		
	public void lineShape2D(double x1,double y1,double x2,double y2)
	{
		
		//Clip y value
		if(y1<this.yMin)
		{
			if(y2<this.yMin)
				return;
			if(y2!=y1)
				x1=x1+(this.yMin-y1)*(x2-x1)/(y2-y1);
			y1=this.yMin;
				
		}
		else
			if(y1>this.yMax)
			{
				if(y2>this.yMax)
					return;
				if(y2!=y1)
					x1=x1+(this.yMax-y1)*(x2-x1)/(y2-y1);	
				y1=this.yMax;
			}		
			
		if(y2<this.yMin)
		{
			if(y1<this.yMin)
				return;
			if(y2!=y1)
				x2=x1+(this.yMin-y1)*(x2-x1)/(y2-y1);
			y2=this.yMin;
				
		}
		else
			if(y2>this.yMax)
			{
				if(y1>this.yMax)
					return;
				if(y2!=y1)
					x2=x2+(this.yMax-y2)*(x2-x1)/(y2-y1);	
				y2=this.yMax;
			}
		
		//initialiase needed segment buffer lines for the y range
		if(y1<=symin)
		{
			int sy=(int)y1;
			int ey=(int)Math.min(symin-1,this.bufferHeight-1);
			for(int l=sy;l<=ey;l++)
			{
				this.buffer[l*this.bufferWidth]=this.bufferWidth;
				this.minX[l]=-(Double.MAX_VALUE-1);
				this.maxX[l]=Double.MAX_VALUE;
			}
			 symin=y1;
		}
		
		if(y1>=symax)
		{
			int sy=(int)Math.max(symax+1,0);
			int ey=(int)y1;
			for(int l=sy;l<=ey;l++)
			{
				this.buffer[l*this.bufferWidth]=this.bufferWidth;
				this.minX[l]=-(Double.MAX_VALUE-1);
				this.maxX[l]=Double.MAX_VALUE;
			}
			symax=y1;			
		}
		if(y2<=symin)
		{
			 int sy=(int)y2;
			 int ey=(int)Math.min(symin-1,this.bufferHeight-1);
			 for(int l=sy;l<=ey;l++)
			{
				this.buffer[l*this.bufferWidth]=this.bufferWidth;
				this.minX[l]=-(Double.MAX_VALUE-1);
				this.maxX[l]=Double.MAX_VALUE;
			}
			 symin=y2;
		}
		
		if(y2>=symax) 		
		{
			int sy=(int)Math.max(symax+1,0);
			int ey=(int)y2;
			for(int l=sy;l<=ey;l++)
			{
				this.buffer[l*this.bufferWidth]=this.bufferWidth;
				this.minX[l]=-(Double.MAX_VALUE-1);
				this.maxX[l]=Double.MAX_VALUE;
			}
			symax=y2;				
		}	
				
		//Compute delta x & y	
		double dxf=x2-x1;
		double dyf=y2-y1;
		
		//use local attribute for color
		//int color=this.color;
		
		//Compute rounded y values
		int iy1=(int)y1;
		int iy2=(int)y2;
		
		if(iy2==iy1)
		{
			return;
		}
		this.line(x1,y1,x2,y2);
	}
		
	private void setStartSegment(double x,double y)
	{	
		int ppx=((int)(x*256f))&0xFF;	
		int ppy=((int)(y*256f))&0xFF;	
		int px=ppx^0xFF;
		int py=ppy^0xFF;
		
		int p00=(px*py)>>8;
		int p10=((px^0xFF)*py)>>8;
		int p01=(px*(py^0xFF))>>8;
		int p11=((px^0xFF)*(py^0xFF))>>8;		
		int iy=(int)y;		
				
		if(x>this.xMax)
		{
			if(x>this.maxX[iy])
				return;
			else
			{					
				this.maxX[iy]=(double)x;				
				x=(int)this.xMax;
			}
		}				
			
		if(x<this.xMin)
		{
			if(x<this.minX[iy])
				return;
			else
			{
				this.minX[iy]=(double)x;				
				x=(int)this.xMin;
			}
		}

		int ix=(int)x;
		int ofsy=iy*this.bufferWidth;
		int ofsx=0;
		int nx=this.buffer[ofsy]&0x0000FFFF;

		
		while(true)
		{
			
			if(ix >= ofsx && ix<nx)
			{	
				int cparity=this.buffer[ofsy+ofsx];
				cparity&=0xC0000000;
				if(ix!=ofsx)
				{
					this.buffer[ofsy+ofsx]=ix|cparity;
					this.buffer[ofsy+ix]=nx|0x80000000;
				}						
				else			
					this.buffer[ofsy+ofsx]=nx|0x80000000|cparity;
				break;
			}
			
			ofsx=nx;
			
			if(ofsx == this.bufferWidth)
				break;
				
			nx=this.buffer[ofsx+ofsy]&0x0000FFFF;
		}		
	}
		
	private void setEndSegment(double x,double y)	
	{
		int ppx=((int)(x*256f))&0xFF;	
		int ppy=((int)(y*256f))&0xFF;	
		int px=ppx^0xFF;
		int py=ppy^0xFF;
		
		int p00=(px*py)>>8;
		int p10=((px^0xFF)*py)>>8;
		int p01=(px*(py^0xFF))>>8;
		int p11=((px^0xFF)*(py^0xFF))>>8;		
		int iy=(int)y;
		
		if(x>this.xMax)
		{
			if(x>this.maxX[iy])
				return;
			else
			{					
				this.maxX[iy]=(double)x;				
				x=(int)this.xMax;
			}
		}				
			
		if(x<this.xMin)
		{
			if(x<this.minX[iy])
				return;
			else
			{
				this.minX[iy]=(double)x;				
				x=(int)this.xMin;
			}
		}


		int ix=(int)x;
		int ofsy=iy*this.bufferWidth;
		int ofsx=0;
		int nx=this.buffer[ofsy]&0x0000FFFF;
		
		while(true)
		{
			if(ix >= ofsx && ix<nx)
			{	
				int cparity=this.buffer[ofsy+ofsx];
				cparity&=0xC0000000;
				if(ix!=ofsx)
				{
					this.buffer[ofsy+ofsx]=ix|cparity;
					this.buffer[ofsy+ix]=nx|0x40000000;
					
				}						
				else			
				{
					//if((cparity&0xC0000000)==0x80000000)
					this.pixels[iy*this.bufferWidth+ix]=0xFFFFFF00;
					this.buffer[ofsy+ofsx]=nx|0x40000000|cparity;
				}
				break;//return
			}
			
			ofsx=nx;
			
			if(ofsx == this.bufferWidth)
				break;
				
			nx=this.buffer[ofsx+ofsy]&0x0000FFFF;
		}				
	}
		
	private int getPixel(int ofsXY)
	{
		return this.pixels[ofsXY];
	}	
		
	private void setPixel(int ofsXY,int color)
	{
		this.pixels[ofsXY]=color;
	}				
	
}