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

package net.dzzd.access;

/** 
 *  Used for accessing to a MappingUV.
 *  <p>
 *   MappingUV specify how faces mapping u,v coordinates will be modified at render time.
 *  </p>
 *  <br>
 *  Available implemented transformations are : scale translation.
 *  <br>
 *  <br>
 *  <b>Ex.:</b> <br>
 *   - a U/V scaling factor of 3 means that the mapping coordinate U/V will be multiplied by 3 before apply texture using this mapping.
 *  <br>
 *   - a U/V offset of 0.5 means that 0.5 will be add to the mapping coordinate U/V before apply texture using this mapping.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IMaterial
 *	@see ITexture
 */
public interface IMappingUV
{
	/**
	 * Gets U mapping offset.
	 *
	 * @return U mapping offset
	 */		
    public float getUOffset();

	/**
	 * Gets V mapping offset.
	 *
	 * @return V mapping offset
	 */	
    public float getVOffset();

	/**
	 * Sets U mapping offset.
	 *
	 * @param val new U mapping offset
	 */		
    public void setUOffset(float val);

	/**
	 * Set V mapping offset.
	 *
	 * @param val new V mapping offset
	 */		
    public void setVOffset(float val);

	/**
	 * Gets U mapping zoom.
	 *
	 * @return U mapping zoom
	 */		
    public float getUZoom();

	/**
	 * Gets V mapping zoom.
	 *
	 * @return V mapping zoom
	 */	
    public float getVZoom();

	/**
	 * Sets U mapping zoom.
	 *
	 * @param val new U mapping zoom
	 */		
    public void setUZoom(float val);

	/**
	 * Sets V mapping zoom.
	 *
	 * @param val new V mapping zoom
	 */		
    public void setVZoom(float val);
}