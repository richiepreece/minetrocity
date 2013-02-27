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
 *  Used for accessing to a Face3DList.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IFace3D
 *  @see IMesh3DGenerator
 */
public interface IFace3DList 
{
	/**
	 * Gets Face3D for this Face3DList cell.
	 * <br>
	 * @return IFace3D for this FaceList cell or null if none
	 */	
	public IFace3D getFace3D();

	/**
	 * Gets next Face3DList cell.
	 * <br>
	 * @return Face3DList next Face3DList cell or null if none
	 */	
	public IFace3DList getNextFace3DList();
}