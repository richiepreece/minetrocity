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
 *  Used for accessing a Scene3DObject.
 *  <br>
 *  - 3D scene objects are all positioned objects (visible or not) in the 3d scene.
 *  <br>
 *   eg.: Cameras,lights and meshes are all 3d scene object
 *  <br>  
 *  <br>
 *  - 3D scene objects can be hierarchically linked each other.
 *  <br><br>
 *  <u>Positioning 3D scene objects in 3D scene:</u><br>
 *   each object is positioned in the 3D scene by a pivot,position and rotation vector
 *    relative to its parent space.<br>
 *   
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IPoint3D
 */
public interface IScene3DObject extends IScene2DObject,IRender3DMode
{
	
	/**
	 * Gets Scene3D owner, null if object has not been added to a Scene3D.
	 *
	 * @return Scene3D owner, null if not in a Scene3D
	 */    					
	public IScene3D getScene3D();
		
	/**
	 * Gets a clone copy for this object.
	 * <br><br>
	 * in case of IMesh3D the returned instance share material but do not share vertex and face (original vertex and face are duplicated)<br>
	 *
	 * @param childrens true if chidren must be duplicated
	 *
	 * @return interface for a newly allocated instance copy of this object
	 */
	public IScene3DObject getClone(boolean childrens);
	
	/**
	 * Gets the current position => this object pivot 3d coordinates relative to this object parent space.
	 * 
	 * @return interface of vector representing current position
	 */	 	
    public IPoint3D getPosition();
    
	/**
	 * Gets the current rotation around pivot in the following order (rx,rz,ry).
	 * 
	 * component x of the returned vector is the rotation angle around the x axis.<br>
	 * component y of the returned vector is the rotation angle around the y axis.<br>
	 * component z of the returned vector is the rotation angle around the z axis.<br>
	 *
	 * @return interface of vector representing current rotation
	 */        
    public IPoint3D getRotation();
    
	/**
	 * Gets the current pivot point (in object space) for this object.
	 * 
	 * @return interface of vector representing current pivot point
	 */            
 	public IPoint3D getPivot();
 	
	/**
	 * Gets the current zoom of this Scene3DObject.
	 * 
	 * @return interface of vector representing current zoom
	 */            
 	public IPoint3D getZoom(); 	
 
	/**
	 * Zoom that object and all chidrens object by x,y,z factor.
	 * 
	 * @param x x zoom factor
	 * @param y y zoom factor
	 * @param z z zoom factor
	 */             	
	public void zoom(double x,double y,double z); 	
 	
	/**
	 * Gets object axis3D.
	 * 
	 * @return axis used by this object
	 */ 	
    public IAxis3D getAxis3D();
	
	/**
	 * Gets parent object.
	 * 
	 * @return parent object interface or null if no parent
	 */    	
	public IScene3DObject getParent();
	
	/**
	 * Sets parent object.
	 * 
	 * @param parent new parent object for this object (pass null for no parent)
	 */    		
	public void setParent(IScene3DObject parent);
	
	/**
	 * Add child to this object.
	 * 
	 * @param childToAdd new child to add to this object
	 */    
	public void addChild(IScene3DObject childToAdd);

	/**
	 * Remove child from this object.
	 * 
	 * @param childToRemove child to remove from this object
	 */    
	public void removeChild(IScene3DObject childToRemove);
	
	/**
	 * Gets the first child of this object.
	 * 
	 * @return first chid object for this object or null if none
	 */    
	public IScene3DObject getFirstChild();	
	
	/**
	 * Gets the next child for this parent object.
	 * 
	 * @return next child for that object parent object related to this object
	 */    
	public IScene3DObject getNextChild();	
	
	/**
	 * Gets object spherebox radius.
	 * <br>
	 * return object sphere box radius relative to object center and expressed in scene unit.
	 *
	 * @return sphere box radius in scene units
	 */    				
	public double getSphereBox();
	
	/**
	 * Sets object spherebox radius.
	 * <br>
	 * set object sphere box radius relative to object center and expressed in scene unit.
	 *
	 * @param radius new sphere box radius in scene units
	 */	
	public void setSphereBox(double radius);
	
	/**
	 * Gets the center (in object space) for this object.
	 * 
	 * @return this object center.
	 */    
	public IPoint3D getCenter();

	/**
	 * Gets Scene3DObjectAnimator.
	 * 
	 * @return current Scene3DObjectAnimator or null if none.
	 */    
	public IScene3DObjectAnimator getScene3DObjectAnimator();

	/**
	 * Sets Scene3DObjectAnimator.
	 * 
	 * @param animator Scene3DObjectAnimator to set for this Scene3DObject or null to remove/disable current Scene3DObjectAnimator.
	 */  
	public void setScene3DObjectAnimator(IScene3DObjectAnimator animator);

	/**
	 * Call this Scene3DObjectAnimator play method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().play()
	 */  
	public void playScene3DObjectAnimator();

	/**
	 * Call this Scene3DObjectAnimator start method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().start()
	 */  
	public void startScene3DObjectAnimator();

	/**
	 * Call this Scene3DObjectAnimator start method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().start()
	 */  
	public void startScene3DObjectAnimator(long start);

	/**
	 * Call this Scene3DObjectAnimator start method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().start()
	 */  
	public void startScene3DObjectAnimator(long start,long end);

	/**
	 * Call this Scene3DObjectAnimator stop method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().stop()
	 */  
	public void stopScene3DObjectAnimator();

	/**
	 * Call this Scene3DObjectAnimator pause method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().start()
	 */  
	public void pauseScene3DObjectAnimator();

	/**
	 * Call this Scene3DObjectAnimator resume method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().resume()
	 */  
	public void resumeScene3DObjectAnimator();
	
	/**
	 * Call this Scene3DObjectAnimator loopAt method (also call on chidrens).
	 * <br>
	 * To avoid animation of chidrens use : this.getScene3DObjectAnimator().resume()
	 */  
	public void loopAtScene3DObjectAnimator(long loop);	
	
	/**
	 * Gets the visible flag.
	 * <br>
	 * visible Scene3DObject are rendered on screen by Render3D.<br>
	 * <br>
	 * set this flag to false to disable rendering of it and all its childrens..
	 * 
	 * @return visible flag
	 */		
	public boolean isVisible();
	
	/**
	 * Gets the active flag.
	 * <br>
	 * active Scene3DObject are transformed in the different space by Scene3D world space,camera space.<br>
	 * <br>
	 * set this flag to false to disable tranformation on it and all its childrens.
	 * 
	 * @return active flag
	 */	
	public boolean isActive();
	
	/**
	 * Gets the solid flag.
	 * <br>
	 * solid Scene3DObject are used to perform collision by physics engine as SolidSphere3D .<br>
	 * <br>
	 * set this flag to false to disable collision on it and all its childrens.
	 * 
	 * @return solid flag
	 */		
	public boolean isSolid();
	
	/**
	 * Sets the visible flag.
	 * <br>
	 * visible Scene3DObject are rendered on screen by Render3D.<br>
	 * <br>
	 * set this flag to false to disable rendering of it and all its childrens..
	 * 
	 * @param flag visible flag
	 */		
	public void setVisible(boolean flag);
	
	
	/**
	 * Sets the active flag.
	 * <br>
	 * active Scene3DObject are transformed in the different space by Scene3D world space,camera space.<br>
	 * <br>
	 * set this flag to false to disable tranformation on it and all its childrens.
	 * 
	 * @param flag active flag
	 */		
	public void setActive(boolean flag);
	
	
	/**
	 * Sets the solid flag.
	 * <br>
	 * solid Scene3DObject are used to perform collision by physics engine as SolidSphere3D .<br>
	 * <br>
	 * set this flag to false to disable collision on it and all its childrens.
	 * 
	 * @param flag solid flag
	 */			
	public void setSolid(boolean flag);
	
	/**
	 * Sets this Scene3DObject Axis3D to world space.
	 * <br>
	 * Set this Scene3DObject Axis3D in world space using its parent position,pivot,rotation ant its own position,pivot and rotation.<br>
	 * <br>
	 * 
	 * @return this Scene3DObject
	 */		
	public IScene3DObject setAxis3DToWorld();
	
	/**
	 * Sets a Point3D given in this Scene3DObject space to world space.
	 * <br>
	 * Convert the given Point3D from object space to world space
	 * <br>
	 * @param point3d local Point3D to set to world space
	 */			
	public void setPoint3DToWorld(IPoint3D point3d);
	
	//HACK: does not mean anything now, will be removed
	/**
	 * Translate this object as a solid object in its parent object space by performing physic collisions.
	 * <br>
	 *  collision are performed using sphereBox (relative to object center) and current Render3D owner scene objects
	 *
	 * @param x x translation vector component
	 * @param y y translation vector component
	 * @param z z translation vector component
	 * @param radius sphere radius to use for collision tests (you may use this object sphereBox)
	 * @param maxLoop collision is a recursive process (rebounds) you can limit recursive depth with this parameter (you may use 4)
	 * @return collision result giving informations about the collision performed
	 *   					
	public ISolidSphere3DResult moveAsSolidSphere(double x,double y,double z,double radius,int maxLoop);
	*/
	
}