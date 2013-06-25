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
 * Used for accessing to a Scene3D.
 *  <br>
 *  provide simple methods to manage a 3D scene.
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *	@see IScene3DRender
 *
 *  Copyright Bruno Augier 2005 
 */
public interface IScene3D extends IScene2D 
{
	/**
	 * Sets this scene3D internal buffer size.<br>
	 * <br>
	 * this method must be used to increase/reduce the scene3D internal buffers size.<br>
	 * <br>
	 * Default value are : <br>
	 *  IMesh3D  : 4096<br>
	 *  ILight3D : 1024<br>
	 *  ICamera3D: 1024<br>
	 *  <br>
	 *  <br>
	 *  Note: This method will reset the scene3D by calling clearScene3D internally.
	 *
	 * @param maxMesh3D maximum number of mesh3D allowed.
	 * @param maxLight3D maximum number of light3D allowed.
	 * @param maxCamera3D maximum number of camera3D allowed.
	 */
	public void setScene3DBufferSize(int maxMesh3D,int maxLight3D,int maxCamera3D);

	/** Gets the number of Scene3DObject for this scene.
	 *  <br>
	 *
	 *  @return number of Scene3DObject
 	 */		
	public int getNbScene3DObject();
	
	/** Gets the number of IMesh3D for this scene.
	 *  <br>
	 *
	 *  @return number of IMesh3D
 	 */		
    public int getNbMesh3D();
    
	/** Gets the number of ILight3D for this scene.
	 *  <br>
	 *
	 *  @return number of ILight3D
 	 */	
    public int getNbLight3D();
    
	/** Gets the number of ICamera3D for this scene.
	 *  <br>
	 *
	 *  @return number of ICamera3D
 	 */	    
    public int getNbCamera3D();
    
	/**
	 * Add the given Mesh3D to this scene.<br>
	 * <br>
	 * @param m the new Mesh3D to add.
	 */
	public void addMesh3D(IMesh3D m);	

	/**
	 * Add the given Light3D to this scene.<br>
	 * <br>
	 * @param l the new Light3D to add.
	 */
	public void addLight3D(ILight3D l);

	/**
	 * Add the given Camera3D to this scene.<br>
	 * <br>
	 * @param c the new Camera3D to add.
	 */
	public void addCamera3D(ICamera3D c);

	/**
	 * Add the given Scene3DObject to this scene.<br>
	 * <br>
	 * @param object the new Scene3DObject to add.
	 */	
	public void addScene3DObject(IScene3DObject object);

	/**
	 * Add multiple Scene3DObject to this scene.<br>
	 * <br>
	 * @param objects3D array containing Scene3DObject to add.
	 */	
	public void addScene3DObjects(IScene3DObject objects3D[]);

	/**
	 * Remove the given Scene3DObject from this scene.<br>
	 * <br>
	 * this method will remove the given Scene3DObject and all its childrens.<br>
	 * <br>
	 * Material and Texture are not affected by this method.<br>
	 * 
	 * @param object the Scene3DObject to remove.
	 */	
	public void removeScene3DObject(IScene3DObject object);

	/**
	 * Remove the Mesh3D having the given id from this scene.<br>
	 * <br>
	 * this method will remove Mesh3D and all its childrens.<br>
	 * <br>
	 * Material and Texture are not affected by this method.<br>
	 * 
	 * @param id id of the Mesh3D to remove.
	 */	
	public void removeMesh3DById(int id);

	/**
	 * Remove the Light3D having the given id from this scene.<br>
	 * <br>
	 * this method will remove Light3D and all its childrens.<br>
	 * <br>
	 * Material and Texture are not affected by this method.<br>
	 * 
	 * @param id id of the Light3D to remove.
	 */	
	public void removeLight3DById(int id);

	/**
	 * Remove the Camera3D having the given id from this scene.<br>
	 * <br>
	 * this method will remove Camera3D and all its childrens.<br>
	 * <br>
	 * Material and Texture are not affected by this method.<br>
	 * 
	 * @param id id of the Camera3D to remove.
	 */	
	public void removeCamera3DById(int id);

	/**
	 * Clear this scene3D.<br>
	 * <br>
	 * remove all SceneObject including mesh,camera,light,material and texture.<br>
	 * <br>
	 * a new default camera and light will be created just before this method return.
	 */
	public void clearScene3D();
	
	/**
	 * Gets the Mesh3D having the given id from this scene.<br>
	 * 
	 * @param id id of the Mesh3D to get.
	 */	
	public IMesh3D getMesh3DById(int id);
	
	/**
	 * Gets the Mesh3D having the given name from this scene.<br>
	 * 
	 * @param name name of the Mesh3D to get.
	 */	
	public IMesh3D getMesh3DByName(String name);
	
	/**
	 * Gets the Camera3D having the given id from this scene.<br>
	 * 
	 * @param id id of the Camera3D to get.
	 */	
	public ICamera3D getCamera3DById(int id);
	
	/**
	 * Gets the Camera3D having the given name from this scene.<br>
	 * 
	 * @param name name of the Camera3D to get.
	 */	
	public ICamera3D getCamera3DByName(String name);
	
	/**
	 * Gets the Light3D having the given id from this scene.<br>
	 * 
	 * @param id id of the Light3D to get.
	 */	
	public ILight3D getLight3DById(int id);
	
	/**
	 * Gets the Light3D having the given name from this scene.<br>
	 * 
	 * @param name name of the Light3D to get.
	 */	
	public ILight3D getLight3DByName(String name);	
	
	/**
	 * Sets the Mesh3D name object to use as sky box.<br>
	 * <br>
	 * Mesh3D used as skybox will be rendered without lighting and always at position 0,0,0 relative to the camera.<br>
	 * <br>
	 * Typically this will be a 3D box with its pivot located at the center and faces normal reversed.<br>
	 * <br>
	 * Other object than 3d box can be used : sphere,dome...
	 * 
	 * @param name name of the Mesh3D to use as sky box.
	 */	
	public void setSkyBoxMesh3DByName(String name);

	/**
	 * Sets the Mesh3D id object to use as sky box.<br>
	 * <br>
	 * Mesh3D used as skybox will be rendered without lighting and always at position 0,0,0 relative to the camera.<br>
	 * <br>
	 * Typically this will be a 3D box with its pivot located at the center and faces normal reversed.<br>
	 * <br>
	 * Other object than 3d box can be used : sphere,dome...
	 * 
	 * @param id id of the Mesh3D to use as sky box.
	 */	
	public void setSkyBoxMesh3DById(int id);

	/**
	 * Gets the Mesh3D id object used as sky box.<br>
	 * <br>
	 * Mesh3D used as skybox will be rendered without lighting and always at position 0,0,0 relative to the camera.<br>
	 * <br>
	 * Typically this will be a 3D box with its pivot located at the center and faces normal reversed.<br>
	 * <br>
	 * Other object than 3d box can be used : sphere,dome...
	 * 
	 * @return id of the Mesh3D to use as sky box.
	 */	
	public int getSkyBoxMesh3DId();
	 
	/**
	 * Gets the Mesh3D name object used as sky box.<br>
	 * <br>
	 * Mesh3D used as skybox will be rendered without lighting and always at position 0,0,0 relative to the camera.<br>
	 * <br>
	 * Typically this will be a 3D box with its pivot located at the center and faces normal reversed.<br>
	 * <br>
	 * Other object than 3d box can be used : sphere,dome...
	 * 
	 * @return id of the Mesh3D to use as sky box.
	 */	
	public String getSkyBoxMesh3DName();	 

	/**
	 * Sets the Camera3D to use for camera space using its id.<br>
	 * <br>
	 * this camera will be used for camera space as well as to render the scene when scene is passed to a Render3D.
	 * 
	 * @param id id of the Camera3D to use for camera space.
	 */		
	public void setCurrentCamera3DById(int id);

	/**
	 * Sets the Camera3D to use for camera space using its name.<br>
	 * <br>
	 * this camera will be used for camera space as well as to render the scene when scene is passed to a Render3D.
	 * 
	 * @param name name of the Camera3D to use for camera space.
	 */		
	public void setCurrentCamera3DByName(String name);

	/**
	 * Gets the Camera3D used for camera space.<br>
	 * <br>
	 * this camera will be used for camera space as well as to render the scene when scene is passed to a Render3D.
	 * 
	 * @return the Camera3D used for camera space.
	 */		
	public ICamera3D getCurrentCamera3D();

	/**
	 * Sets all Scene3DObject to world space.<br>
	 * <br>
	 * all Scene3DObject will be passed to world space.
	 */		
	public void setScene3DObjectToWorld();

	/**
	 * Sets all Scene3DObject to camera space.<br>
	 * <br>
	 * setScene3DObjectToWorld must have been called at least once before each call to this method.<br>
	 * <br>
	 * all Scene3DObject will be passed to world space.
	 */		
	public void setScene3DObjectToCamera();

	/** Gets the backgroundEnabled flag .
	 *  <br>
	 *  set this flag to false to disable background filling.<br>
	 *
	 *  @return backgroundEnabled flag 	 
 	 */	
	public boolean isBackgroundEnabled();

	/** Sets the backgroundEnabled flag .
	 *  <br>
	 *  set this flag to false to disable background filling.<br>
	 *
	 *  @param flag backgroundEnabled flag 	 
 	 */		
	public void setBackgroundEnabled(boolean flag);	

	/**
	 * Sets background color.<br>
	 * <br>
	 * @param color new background color 
	 */
 	public void setBackgroundColor(int color);

	/**
	 * Gets background color.<br>
	 * <br>
	 * @return current background color 
	 */
	public int getBackgroundColor();

	/** Gets the fogEnabled flag .
	 *  <br>
	 *  set this flag to false to disable fog.<br>
	 *
	 *  @return fogEnabled flag 	 
 	 */	
	public boolean isFogEnabled();

	/** Sets the fogEnabled flag .
	 *  <br>
	 *  set this flag to false to disable fog filling.<br>
	 *
	 *  @param flag fogEnabled flag 	 
 	 */		
	public void setFogEnabled(boolean flag);	

	/**
	 * Sets fog color.<br>
	 * <br>
	 * @param color new fog color 
	 */
 	public void setFogColor(int color);

	/**
	 * Gets fog color.<br>
	 * <br>
	 * @return current fog color 
	 */
	public int getFogColor();

	/**
	 * Sets all Scene3DObject using there Scene3DObjectAnimator for the given time.<br>
	 * <br>
	 * call the Scene3DObjectAnimator.playAt method for each Scene3DObjectAnimator used by Scene3DObject of this scene3D.<br>
	 * <br>
	 * @param time time in keyframer that should be set for each Scene3DObject
	 */	
	public void playScene3DObjectAnimator(int time);
	
	/*
	
	/** Gets the ISceneObject list.
	 *  <br>
	 *
	 *  @return an array containing all ISceneObject of this Scene3DLoader 	 
 	 *		
	public IScene3DObject[] getScene3DObject();
	*/
	
	/** Sets a Scene3DLoader for this Scene3D
	 *  <br>
	 *  @param sceneLoader a Scene3DLoader that will be added to this scene once loaded
 	 */		
	public void setScene3DLoader(IScene3DLoader sceneLoader);
	
}
