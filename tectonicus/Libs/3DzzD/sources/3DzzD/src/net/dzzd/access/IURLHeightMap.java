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
 *  Used for accessing to a URLHeightMap.
 *  <br>
 *  URLHeightMap is an HeightMap created by using a file located by an URL.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IMesh3D
 */
public interface IURLHeightMap extends IHeightMap
{
	/**
	 * Load this URLHeightMap internal 2d data array from a file.
	 * <br>
 	 * @param baseURL base url for file location
 	 * @param fileName file name
 	 * @param width width of the internal height array 
 	 * @param height height of the internal height array 
	 */
	public void load(String baseURL,String fileName,int width,int height);

	/**
	 * Sets this URLHeightMap internal 2d data array width.
	 * <br>
 	 * @param width width of the internal 2d data array
	 */
	public void setWidth(int width);

	/**
	 * Sets this URLHeightMap internal 2d data array height.
	 * <br>
 	 * @param height width of the internal 2d data array
	 */
	public void setHeight(int height);
}