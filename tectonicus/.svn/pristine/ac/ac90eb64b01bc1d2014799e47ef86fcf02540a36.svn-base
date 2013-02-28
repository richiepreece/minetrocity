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
 *  Used for accessing a scene SceneObject.
 *  <br>
 *  Scene objects include all object that can be added and managed by a Scene3D : Texture,Mesh,Animation,Material,etc...
 *   
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *	@see IScene3D
 *	@see IScene3DObject
 */
public interface ISceneObject extends IMonitoredSceneObject
{
	/**
	 * Gets Scene owner, null if object has not been added to a Scene.
	 *
	 * @return Scene owner, null if not in a Scene
	 */    					
	public IScene getScene();

	/**
	 * Gets Id.
	 * <br>
	 * object identifier (Id) are used internaly by the renderer, you may not change it.
	 *
	 * @return object Id
	 */    	  		
	public int getId();
	
	/**
	 * Sets Id.
	 * <br>
	 * object identifier (Id) are used internaly by the renderer, you may not change it.
	 *
	 * @param id new object Id 
	 */    			
	public void setId(int id);

	/**
	 * Gets name.
	 * 
	 * @return object name
	 */    
	public String getName();
	
	/**
	 * Sets name.
	 * 
	 * @param name new object name
	 */    	
	public void setName(String name);
	
	/**
	 * Sets a property for this SceneObject.
	 * <br>
	 * use null as value to remove a single property using its key.
	 *
	 * @param key key that can be used to retrieve that property
	 * @param value object to store as a property for this SceneObject
	 */    	
	public void setProperty(String key,Object value);
	
	/**
	 * Sets a property for this SceneObject.
	 * 
	 * @param key to retrieve property for
	 * @return object that have been stored with the given key
	 */ 	
	public Object getProperty(String key);

	/**
	 * Remove all properties for this SceneObject.
	 * 
	 */ 		
	public void clearProperties();
	
	/**
	 * Build this object and update its internal built version.
	 * <br>
	 * internal built version maybe used by Render3D or other object to now if an object has been updated.
	 */ 	
	public void build();
	
	/**
	 * Gets this object built version.
	 * <br>
	 * if version returned of -1 means that object has never been built or must be rebuilt.
	 * 
	 * @return internal built version maybe used by Render3D or other object to now if an object has been updated.
	 */ 	
	public int getBuild();	
	
	/**
	 * Sets this object built version.
	 * <br>
	 * version -1 means that object has never been built or must be rebuilt.
	 * 
	 * @param build internal built version maybe used by Render3D or other object to now if an object has been updated.
	 */ 	
	public void setBuild(int build);		
	
	/**
	 * Copy source SceneObject in this SceneObject.
	 * <br>
	 * Copy most properties from source to destination.<br>
	 * <br>
	 * 
	 * @return this SceneObject with new properties copied from source
	 */			
	public void copy(ISceneObject source);	
	

}