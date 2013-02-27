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

public final class MappingUV implements IMappingUV
{
	float ofsU;							//U mapping offset
	float ofsV;							//V mapping offset
	float zoomU;						//U mapping zoom
	float zoomV;						//V mapping zoom
		
	public MappingUV()
	{
		this.ofsU=0.0f;
		this.ofsV=0.0f;
		this.zoomU=1.0f;
		this.zoomV=1.0f;
	}	
	
	/*
	 *INTERFACE IMapping3D
	 */ 

    public float getUOffset()
	{
		return this.ofsU;
	}

    public float getVOffset()
	{
		return this.ofsV;
	}

    public void setUOffset(float val)
	{
		this.ofsU=val;
	}

    public void setVOffset(float val)
	{
		this.ofsV=val;
	}

    public float getUZoom()
	{
		return this.zoomU;
	}

    public float getVZoom()
	{
		return this.zoomV;
	}

    public void setUZoom(float val)
	{
		this.zoomU=val;
	}

    public void setVZoom(float val)
	{
		this.zoomV=val;
	}
}
