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
import net.dzzd.DzzD;

/** 
 *  Class to manage Texture
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */

public class Texture extends SceneObject implements ITexture
{
	int largeur;				//Texture width
	int hauteur;				//Texture height
	int largeurImage;			//Texture width (nearest power of 2)
	int hauteurImage;			//Texture height (nearest power of 2)
	int decalLargeur;			//Width shift mask bit ln2(width nearest power of 2)
	int decalHauteur;			//Height shift mask bit ln2(height nearest power of 2)
	int maskLargeur;			//Width bit mask on an offset
	int maskHauteur;			//Height bit mask on an offset
	int pixels[];				//Pixels array 
	int[] mipMap[];				//Mipmap pixels arrays
	int nbMipMap;				//Nb MipMap
	int type;					//Texture type
	
	Texture ()
	{
		super();
		this.type=DzzD.TT_RGB;
		this.largeur=1;	
		this.hauteur=1;	
		this.largeurImage=1;	
		this.hauteurImage=1;
		this.maskLargeur=1;
		this.maskHauteur=1;
		this.decalLargeur=0;
		this.pixels=null;
		this.mipMap=new int[32][];
		this.nbMipMap=0;

	}
	
	public void setType(int type)
	{
		this.type=type;
	}
	
	public int getType()
	{
		return this.type;
	}	
	
	public void build()
	{
		super.build();
		this.buildMipMap();
		
		if(this.type==DzzD.TT_NORMAL || this.type==DzzD.TT_HNORMAL)
		{
			if(this.type==DzzD.TT_HNORMAL)
				this.buildNormal();
				
			this.normalize();
			this.buildMipMap();
			//this.normalize();
		}
		
	}
	
	public void buildNormal()
	{
		int mlMP=this.maskLargeur;
		int mhMP=this.maskHauteur;
		int pixA[]=this.pixels;
		int pixT[]=new int[this.pixels.length];
    	int lMP=this.largeurImage;
    	int hMP=this.hauteurImage;
    	for(int numMipMap=0;numMipMap<this.nbMipMap;numMipMap++)
    	{
    		for(int y=0;y<hMP;y++)
	    		for(int x=0;x<lMP;x++)
	    		{
	    			int ofsp=x+y*lMP;
	    			pixT[ofsp]=pixA[ofsp]<<8;
	    		}
	    			
    		for(int y=0;y<hMP;y++)
	    		for(int x=0;x<lMP;x++)
	    		{
	    			int ofsp1=((x-1)&mlMP)+((y-1)&mhMP)*lMP;
	    			int ofsp2=((x)&mlMP)+((y-1)&mhMP)*lMP;
	    			int ofsp3=((x+1)&mlMP)+((y-1)&mhMP)*lMP;
	    			int ofsp4=((x-1)&mlMP)+((y)&mhMP)*lMP;
	    			int ofsp5=((x)&mlMP)+((y)&mhMP)*lMP;
	    			int ofsp6=((x+1)&mlMP)+((y)&mhMP)*lMP;
	    			int ofsp7=((x-1)&mlMP)+((y+1)&mhMP)*lMP;
	    			int ofsp8=((x)&mlMP)+((y+1)&mhMP)*lMP;
	    			int ofsp9=((x+1)&mlMP)+((y+1)&mhMP)*lMP;
	    			
	    			int p1=(pixT[ofsp1]>>16)&0xFF;
	    			int p2=(pixT[ofsp2]>>16)&0xFF;
	    			int p3=(pixT[ofsp3]>>16)&0xFF;
	    			int p4=(pixT[ofsp4]>>16)&0xFF;
	    			int p5=(pixT[ofsp5]>>16)&0xFF;
	    			int p6=(pixT[ofsp6]>>16)&0xFF;
	    			int p7=(pixT[ofsp7]>>16)&0xFF;
	    			int p8=(pixT[ofsp8]>>16)&0xFF;
	    			int p9=(pixT[ofsp9]>>16)&0xFF;
	    			
	    			int v1=p5-p1;
	    			int v2=p5-p2;
	    			int v3=p5-p3;
	    			int v4=p5-p4;
	    			int v6=p5-p6;
	    			int v7=p5-p7;
	    			int v8=p5-p8;
	    			int v9=p5-p9;
	    			  			
	    			int R=v6-v4;//+((v3+v9)>>1)-((v1+v7)>>1);
	 				int V=v8-v2;//+((v3+v1)>>1)-((v7+v9)>>1);
	 				//R+=255;
	 				//V+=255;
	 				//R*=2;
	 				//V*=2;
	 				
	 				R>>=numMipMap+1;
	 				V>>=numMipMap+1;
	 				int B=255;
	 				
	 				
	 				
	 				/*
	 				System.out.println("RVB="+R+","+V+","+B);
	 				System.out.println("NORMAL="+(R*R+V*V+B*B));
					*/
					
					
	 				R+=128;
	 				V+=128;
	 				//B+=128;
	 				if(R>255) R=255;
	 				if(V>255) V=255;
	 				if(B>255) B=255;
	 				if(R<0) R=0;
	 				if(V<0) V=0;
	 				if(B<0) B=0;
	 				
	 				int A=pixT[ofsp5]&0xFF000000;
	 				pixA[ofsp5]=A|(R<<16)|(V<<8)|B;
	 				
	    		}
	    		pixA=this.mipMap[numMipMap];
	    		lMP=lMP>>1;
    			hMP=hMP>>1;
    			mlMP>>=1;
    			mhMP>>=1;
	    }		
	}	
	
	public void normalize()
	{
		int pixA[]=this.pixels;
    	int lMP=this.largeurImage;
    	int hMP=this.hauteurImage;
    	for(int numMipMap=0;numMipMap<this.nbMipMap;numMipMap++)
    	{
    		for(int y=0;y<hMP;y++)
	    		for(int x=0;x<lMP;x++)
	    		{
	    			int ofsp=x+y*lMP;
	 				int pix=pixA[ofsp];
	 				int B=((pix&0xFF))-128;
	 				int V=((pix>>8)&0xFF)-128;
	 				int R=((pix>>16)&0xFF)-128;

	 				
	 				
	 				//R>>=numMipMap;
	 				//V>>=numMipMap;
	 				//B<<=numMipMap;
	 				
	 				//if(B<1) B=1;
	 				//if(B>127) B=127;
	 				B=127;
	 				
	 				int A=pix&0xFF000000;
	 				int N=Drawer.normalMap[R*R+V*V+B*B];
	 				R*=N;
	 				V*=N;
	 				B*=N;
	 				R>>=23;
	 				V>>=23;
	 				B>>=23;
	 				
	 				//System.out.println("RVB="+R+","+V+","+B);
	 				//System.out.println("NORMAL="+(R*R+V*V+B*B));

	 				R+=128;
	 				V+=128;
	 				B+=128;
	 				if(R>255) R=255;
	 				if(V>255) V=255;
	 				if(B>255) B=255;
	 				if(R<0) R=0;
	 				if(V<0) V=0;
	 				if(B<0) B=0;
	 				
	 				
	 				pixA[ofsp]=A|(R<<16)|(V<<8)|B;
	 				
	    		}
	    		pixA=this.mipMap[numMipMap];
	    		lMP=lMP>>1;
    			hMP=hMP>>1;
	    }		
	}
	
 	public void buildMipMap()
	{
		
    	int lMP=this.largeurImage>>1;
    	int hMP=this.hauteurImage>>1;
    	int lastMipMap[]=pixels;
    	while((lMP>1) && (hMP>1))
    	{
    		int currentMipMap[]=new int[lMP*hMP];
    		for(int y=0;y<hMP;y++)
    			for(int x=0;x<lMP;x++)
    			{ 			
					int ofs=(x<<1)+((y<<1)*(lMP<<1));
					int ofsp=x+y*lMP;
					int mipPix=0;

					//Fastest method but maaybe decal texure (0.5)
					int p1=lastMipMap[ofs];
					int p2=lastMipMap[ofs+1];
					int p3=lastMipMap[ofs+(lMP<<1)];
					//int p4=nbMipMap*16+nbMipMap*16<<8;//lastMipMap[ofs+(lMP<<1)+1];
					int p4=lastMipMap[ofs+(lMP<<1)+1];
					int p12=((p1&0xFEFEFE)+(p2&0xFEFEFE))>>1;
					int p34=((p3&0xFEFEFE)+(p4&0xFEFEFE))>>1;
					mipPix=((p12&0xFEFEFE)+(p34&0xFEFEFE))>>1;
										
					 
					 if(this.type==DzzD.TT_ARGB || this.type==DzzD.TT_HNORMAL)
					 {
						int a1=p1>>24&0xFF;
						int a2=p2>>24&0xFF;
						int a3=p3>>24&0xFF;
						int a4=p4>>24&0xFF;	
						int a=(a1+a2+a3+a4)>>2;
						mipPix=mipPix|(a<<24);
					}
					currentMipMap[ofsp]=mipPix;
    			}
    			
    		this.mipMap[nbMipMap]=currentMipMap;
    		this.nbMipMap++;
    		
    		lastMipMap=currentMipMap;
	    	lMP=lMP>>1;
    		hMP=hMP>>1;
    	}	
    }
    
    public int getPixelsWidth()
    {
    	return this.largeurImage;
    }
    
    public int getPixelsHeight()
    {
    	return this.hauteurImage;
    }    
    
    public int[] getPixels()
    {
    	return this.pixels;
    }
}
