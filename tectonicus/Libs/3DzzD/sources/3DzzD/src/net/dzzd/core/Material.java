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

public final class Material extends SceneObject implements IMaterial
{
	Texture diffuseTexture;				//Diffuse texture XRGB
	Texture alphaTexture;				//Diffuse texture AXXX (usually same as diffuseTexture) 
	Texture bumpNormalTexture;			//Bump/Normal texture HXYZ
	Texture envTexture;					//Environment texture (256*256 RGB)
	
	Texture diffuseTextureDetail;		//Diffuse detail texture
	int diffuseTextureDetailFactor;		//Diffuse detail texture factor

	MappingUV mapping;					//Mapping
	
	int ambientColor;	 				//Ambiante color RGB
	int diffuseColor;	 				//Diffuse color RGB
	int emissiveColor;	 				//Emissive color RGB
	int selfIlluminationLevel;			//Self illumination level 0 to 100
	int specularColor;	 				//Specular color RGB		
	int specularPower;	 				//Shiness power	0 to 100		
	int specularLevel;	 				//Shiness level	0 to 255			
	int alphaLevel;						//Transparency level 0 to 255: object global alpha level
	int alphaFalloff;	 				//(Not used) Transparency falloff -255 to 255 : alpha relative to object center
	boolean alphaEnabled;				//False if alpha must not be used
	
	
	public void build()
    {
    	super.build();
    }
	
	public Material()
	{
		super();
		this.diffuseTexture=null;
		this.diffuseTextureDetail=null;
		this.diffuseTextureDetailFactor=-5;
		this.mapping=null;
		this.ambientColor=0x888888;
		this.diffuseColor=0x888888;
		this.emissiveColor=0x000000;
		this.selfIlluminationLevel=0;
		this.specularColor=0x000000;
		this.specularPower=0;
		this.specularLevel=0;		
		this.alphaLevel=0;
		this.alphaFalloff=0;
		this.alphaEnabled=false;
	}	
	
		
	//Pubic INTERFACE IMaterial	
	public IMappingUV getMappingUV()
	{
		return this.mapping;
	}
		
	public int getAmbientColor()
	{
		return this.ambientColor;
	}	
	
	public int getDiffuseColor()
	{
		return this.diffuseColor;
	}
	
	public int getEmissiveColor()
	{
		return this.emissiveColor;
	}
	
	public int getSelfIlluminationLevel()
	{
		return this.selfIlluminationLevel;
	}

	
	public int getSpecularColor()
	{
		return this.specularColor;
	}
	
	public int getSpecularLevel()
	{
		return this.specularLevel;
	}
		
	public int getSpecularPower()
	{
		return this.specularPower;
	}		
	
	public int getAlphaLevel()
	{
		return this.alphaLevel;
	}
	
	public void setMappingUV(IMappingUV mapping)
	{
		this.mapping=(MappingUV)mapping;
	}

	public void setAmbientColor(int color)
	{
		this.ambientColor=color;
		this.build();
	}
	
	public void setDiffuseColor(int color)
	{
		this.diffuseColor=color;
		this.build();
	}
	
	public void setEmissiveColor(int color)
	{
		this.emissiveColor=color;
		this.build();
	}
	
	public void setSelfIlluminationLevel(int level)
	{
		this.selfIlluminationLevel=level;
		this.build();
	}	

	public void setSpecularColor(int color)
	{
		this.specularColor=color;
		this.build();
	}	
	
	public void setSpecularLevel(int level)
	{
		this.specularLevel=level;
		this.build();
	}			

	public void setSpecularPower(int power)
	{
		this.specularPower=power;
		this.build();
	}
	
	public void setAlphaLevel(int alpha)
	{
		this.alphaLevel=alpha;
		this.build();
	}

	public void setAlphaEnabled(boolean alphaEnabled)
	{
		this.alphaEnabled=alphaEnabled;
	}
	
	public boolean getAlphaEnabled()
	{
		return this.alphaEnabled;
	}	
	
	public boolean isUsingAlpha()
	{
		return this.alphaEnabled && (alphaTexture!=null || this.alphaLevel!=0);
	}
		
	public ITexture getDiffuseTexture()
	{
		return this.diffuseTexture;
	}
	
	public ITexture getBumpNormalTexture()
	{
		return this.bumpNormalTexture;
	}	
	
	public ITexture getEnvTexture()
	{
		return this.envTexture;
	}		
	 
	public ITexture getDiffuseTextureDetail()
	{
		return this.diffuseTextureDetail;
	}

	public void setDiffuseTexture(ITexture texture)
	{
		this.diffuseTexture=(Texture)texture;
	}
	
	public void setBumpNormalTexture(ITexture texture)
	{
		this.bumpNormalTexture=(Texture)texture;
	}	
	
	public void setEnvTexture(ITexture texture)
	{
		this.envTexture=(Texture)texture;
	}	

	public void setDiffuseTextureDetail(ITexture texture)
	{
		this.diffuseTextureDetail=(Texture)texture;
	}
	
	public void setDiffuseTextureDetailFactor(int diffuseTextureDetailFactor)
	{
		this.diffuseTextureDetailFactor=diffuseTextureDetailFactor;
	}

    public int getDiffuseTextureDetailFactor()
	{
		return this.diffuseTextureDetailFactor;
	}

	
}