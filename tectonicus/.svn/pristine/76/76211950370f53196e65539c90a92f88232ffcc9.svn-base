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
 *  Used for accessing to a Mesh3DOctree.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *  @see IMesh3D
 */
public interface IMesh3DOctree 
{
	/**
	 * Gets number of Face3D used by this Mesh3DOctree .
	 * <br>	 * 
	 * @return all Face3D used by this Mesh3DOctree 
	 */
	public int getNbFace3D();

	/**
	 * Gets Face3D array used by this Mesh3DOctree.
	 * <br>
	 * 
	 * @return number of Face3D used by this Mesh3DOctree 
	 */
	public IFace3D[] getFaces3D();

	/**
	 * Sets a property for this Mesh3DOctree .
	 * <br>
	 * use null as value to remove a single property using its key.
	 *
	 * @param key key that can be used to retrieve that property
	 * @param value object to store as a property for this Mesh3DOctree 
	 */    	
	//public void setProperty(String key,Object value);
	
	/**
	 * Sets a property for this Mesh3DOctree .
	 * 
	 * @param key to retrieve property for
	 * @return object that have been stored with the given key
	 */ 	
	//public Object getProperty(String key);

	/**
	 * Remove all properties for this Mesh3DOctree .
	 * 
	 */ 		
	//public void clearProperties();
	
	/**
	 * Build this object and update its internal built version.
	 * <br>
	 * internal built version maybe used by Render3D or other object to now if an object has been updated.
	 */ 	
	public void build();
	
	/**
	 * Gets this object built version.
	 * <br>
	 * if version returned of -1 means that object has never been built.
	 * 
	 * @return internal built version maybe used by Render3D or other object to now if an object has been updated.
	 */ 	
	public int getBuild();
	
	/**
	 * Gets a children of this Mesh3DOctree
	 *
	 * @param n index of the children to return, must range between 0 and 7
	 * 
	 * @return the Mesh3DOctree children for the given index or null if none
	 */
	public IMesh3DOctree getChildren(int n);

	/**
	 * Gets number of childrens for this Mesh3DOctree
	 *
	 * @param recursive true if return value must include childrens of childrens recursivly
	 *
	 * @return number of childrens for this Mesh3DOctree
	 */	
	public int getNbChildren(boolean recursive);	

	/**
	 * Create an array with all Mesh3DOctree including childrens
	 *
	 * 
	 * @return an array containing this Mesh3DOctree and all its childrens
	 */		
	public IMesh3DOctree[] getMesh3DOctreeArray(IMesh3DOctree[] octrees);
	
	
	public int getId();

	public IMesh3D getMesh3D();
}