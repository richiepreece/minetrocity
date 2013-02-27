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


/** 
 *  This class represent a light in 3D space
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
public final class Light3D extends Scene3DObject implements ILight3D
{
	public int type;	//Type SPOT/OMNI/OTHER (Undocumented/Not used for now)	
	
	public Light3D()
	{
		super();
		this.type=0;		
	}

}
