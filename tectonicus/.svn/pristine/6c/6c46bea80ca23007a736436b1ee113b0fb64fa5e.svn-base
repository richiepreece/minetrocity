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

public interface IScene 
{
	/**
	 * Sets this scene internal buffer size.<br>
	 * <br>
	 * this method must be used to increase/reduce the scene internal buffers size.<br>
	 * <br>
	 * Default value are : <br>
	 *  IMaterial: 1024<br>
	 *  ITexture: 1024<br>
	 *  <br>
	 *  <br>
	 *  Note: This method will reset the scene by calling clearScene internally.
	 * @param maxMaterial maximum number of material allowed.
	 * @param maxTexture maximum number of texture allowed.
	 */
	public void setSceneBufferSize(int maxMaterial,int maxTexture);	
	
	/** Gets the number of ISceneObject for this scene.
	 *  <br>
	 *
	 *  @return number of ISceneObject
 	 */		
    public int getNbSceneObject();
    
	/** Gets the number of IMaterial for this scene.
	 *  <br>
	 *
	 *  @return number of IMaterial
 	 */	
    public int getNbMaterial();
    
	/** Gets the number of ITexture for this scene.
	 *  <br>
	 *
	 *  @return number of ITexture
 	 */	
    public int getNbTexture();

	/**
	 * Add the given SceneObject to this scene.<br>
	 * <br>
	 * @param object the new SceneObject to add.
	 */
	public void addSceneObject(ISceneObject object);
	
	/**
	 * Add multiple SceneObject to this scene.<br>
	 * <br>
	 * @param objects array containing SceneObject to add.
	 */	
	public void addSceneObjects(ISceneObject objects[]);	

	/**
	 * Add the given Material to this scene.<br>
	 * <br>
	 * @param m the new Material to add.
	 */
	public void addMaterial(IMaterial m);

	/**
	 * Add the given Texture to this scene.<br>
	 * <br>
	 * @param t the new Texture to add.
	 */
	public void addTexture(ITexture t);
	
	/**
	 * Remove the given Scene3DObject from this scene.<br>
	 * <br>
	 * this method will remove the given Scene3DObject and all its childrens.<br>
	 * <br>
	 * Material and Texture are not affected by this method.<br>
	 * 
	 * @param object the Scene3DObject to remove.
	 */	
	public void removeSceneObject(ISceneObject object);
	
  	/**
	 * Remove the Material having the given id from this scene.<br>
	 * 
	 * @param id id of the Material to remove.
	 */	
	public void removeMaterialById(int id);

	/**
	 * Remove the Texture having the given id from this scene.<br>
	 * 
	 * @param id id of the Texture to remove.
	 */	
	public void removeTextureById(int id);    

	/**
	 * Gets the Material having the given id from this scene.<br>
	 * 
	 * @param id id of the Material to get.
	 */	
	public IMaterial getMaterialById(int id);
	
	/**
	 * Gets the Material having the given name from this scene.<br>
	 * 
	 * @param name name of the Material to get.
	 */	
	public IMaterial getMaterialByName(String name);
	
	/**
	 * Gets the Texture having the given id from this scene.<br>
	 * 
	 * @param id id of the Texture to get.
	 */	
	public ITexture getTextureById(int id);
	
	/**
	 * Gets the Texture having the given name from this scene.<br>
	 * 
	 * @param name name of the Texture to get.
	 */	
	public ITexture getTextureByName(String name);

	/**
	 * Check all monitored SceneObject and update them or the scene3D if needed.<br>
	 * <br>
	 * if some monitored SceneObject have reached a new state this method will update them or the scene3D if needed.<br>
	 * <br>
	 * for example if some SceneObject(texture,3ds file) have reached the state loaded, they will be added to the scene.<br>
	 * <br>
	 */		
	public void updateMonitoredSceneObjects();

	/**
	 * Gets total number of SceneObject currently monitored.<br>
	 * <br>
	 * this will return the number of files currently loading as well as the number of sceneObject currently moving to an other state : loading,initilising,synchronising, etc..
	 * <br>
	 * @return total currently monitored sceneObject 
	 */
	public int getNbMonitoredSceneObject();
	
	/**
	 * Sets an object to monitor into the monitored object stack of this scene.
	 *
	 * @param obj MonitoredSceneObject to add into the monitored object stack of this scene
	 */
	public void startMonitorSceneObject(IMonitoredSceneObject obj);
	
	/**
	 * Remove an object from the monitored object stack of this scene.
	 *
	 * @param idx Index of the MonitoredSceneObject to remove from the monitored object stack of this scene
	 */
	 public void stopMonitorSceneObject(int idx);
	
	/**
	 * Gets a MonitoredSceneObject from the monitored object stack of this scene.<br>
	 * <br>
	 * @param Index of the MonitoredSceneObject to get from the monitored object stack of this scene
	 *
	 * @return MonitoredObject for the given index
	 */	
	public IMonitoredSceneObject getMonitoredSceneObject(int idx);

}