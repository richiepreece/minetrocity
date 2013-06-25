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
 *  Used for accessing to a HeightMap3D.
 *  <br>
 *  HeightMap3D is generated using an HeightMap
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IHeightMap
 *	@see IMesh3D
 *	@see IMesh3DGenerator
 */
public interface IHeightMap3D extends IMesh3DViewGenerator,IMesh3DGenerator,IMesh3DCollisionGenerator
{
	/**
	 * Sets the area of the height map to use.
     * <br>
	 * input area should be given in homogeneous value (as percent) <br>
	 * ex : 0,0,1,1 = full  and  0,0,0.5,0.5 = only upper-left corner
	 * 
	 * @param xMin minimum height map x pos
	 * @param yMin minimum height map y pos
	 * @param xMax maximum height map x pos
	 * @param yMax maximum height map y pos
	 */
	public void setHeightMapBounds(double xMin,double yMin,double xMax,double yMax);	

	/**
	 * Sets the HeightMap to use for tesselation.
	 * <br>
	 * if HeighMap not null, this method will call this.updateHeightMap() just before returning	
	 *
	 * @param hm HeightMap to use or null to remove current
	 */
	public void setHeightMap(IHeightMap hm);


	/**
	 * Indicate that the HeighMap has been just set or has to be (re)computed.
	 * <br>
 	 * should has been called when HeighMap changed and at least once before first call to generate
	 * <br>
	 * setHeightMap will call this method automatically<br>
	 */
	public void updateHeightMap();

}