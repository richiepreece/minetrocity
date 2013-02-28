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
 *  Used for accessing to a Scene3DRender.
 *
 *  @version 1.0
 *  @since 1.0
 *  @author Bruno Augier
 *
 *  Copyright Bruno Augier 2005 
 */
public interface IScene3DRender  
{
	/**
	 * Gets the current scene3D for this Scene3DRender.
	 *
	 * @return current scene3D or null if no scene3D has been set.
	 */
	public IScene3D getScene3D();
	
	/**
	 * Gets the current render3D for this Scene3DRender.
	 *
	 * @return current render3D or null if no render3D has been set.
	 */	
	public IRender3D getRender3D();

	/**
	 * Sets a new Scene3DRenderCallBack for this Scene3DRender.
	 *
	 * @param iScene3DRenderCallBack new event handler for this Scene3DRender, pass null to disable callback.
	 */	
	public void setScene3DRenderCallBack(IScene3DRenderCallBack iScene3DRenderCallBack);
	
	
	/**
	 * Render a single frame
	 *
	 * while rendering the current IScene3DRenderCallBack events are called
	 */
	public void render();
	
	/**
	 * Start this Scene3DRender thread.
	 *
	 * Once started the current scene3D will be rendered using the current render3D and will call the call back handler events methods when needed.
	 */	
	public void start();

	/**
	 * Stop this Scene3DRender thread.
	 */	
	public void stop();
	
	/**
	 * Pause this Scene3DRender thread.
	 */	
	public void pause();

	/**
	 * Gets the current average FPS.
	 * 
	 * @return current FPS, the returned value is the current frame rate per seconde multiplied by 100 25,56 fps will be return as 2556.
	 */
	public int getFPS100();
	
	/**
	 * Gets time for current frame
	 * <br>
	 * this method will intend to resolve time for the current frame<br>
	 * using advanced time algorithm even with an non-accurate timer <br>
	 * as System.currentTimeMillis();
	 *
	 * @return time expressed in ms for the current frame
	 */
	public long getTime();
	
	public long getFrameTime();
	
	/**
	 * Sets the maximum average FPS.
	 * 
	 * @param maxFPS maximum desired FPS, the passed value must be the frame rate per seconde multiplied by 100 25,56 fps will be return as 2556.
	 */	
	public void setMaxFPS100(int maxFPS);
	
	/**
	 * Immediatly replace the current Scene3D
	 *
	 * @param scene scene that will replace current one.
	 */
	public void setScene3D(IScene3D scene);

	
	/**
	 * Switch the current render3D implementation to the given one.<br>
	 * <br>
	 * This method will first try to instanciate the given render3D using the specified implmentation name, and the previous render3D size.<br>
	 * if render3D successfully instanciated the previous render3D will be removed from its parent component and replaced with the newly render3D.<br>
	 * if render3D not successfully instanciated no change will be preformed.<br>
	 * <br>
	 * <br>
	 * render3D switch is asyncronous and will be performed at the begining of next frame.<br> 
	 * 
	 * @param implementationName new render3D implementation name	
	 */
	 public void switchRender3D(String implementationName);

	/**
	 * Sets autoPlayAnimator flag.<br>
	 * <br>
	 * If autoPlayAnimator is set to true than after each render3DStart event<br> 
	 *  all SceneObject using an animator will be updated with its animator
	 * <br>
	 * default autoPlayAnimator value is true
	 *
	 * @param flag new value for autoPlayAnimator
	 */
	public void setAutoPlayAnimator(boolean flag);


	/**
	 * Gets autoPlayAnimator flag.<br>
	 * <br>
	 * If autoPlayAnimator is set to true than after each render3DStart event<br> 
	 *  all SceneObject using an animator will be updated with its animator
	 * <br>
	 * default autoPlayAnimator value is true
	 * 
	 * @return current value for autoPlayAnimator
	 */	
	public boolean getAutoPlayAnimator();
}
