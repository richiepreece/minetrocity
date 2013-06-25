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
 *  Axis3D.Interface
 *  <p>
 *   Usualy Axis3D are used to define 3D scene objects local axis.<br><br>
 *   Axis3D can also be useful to compute 3D space math & transformations.
 *   (it is nearly the same than common 3D Matrix)
 *  </p>
 *  <center>
 *  <img src="IAxis3D_1.bmp">
 *  <br>
 *  <b>Overwiev of a 3D scene objects local axis :</b>
 *  </center>
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IScene3DObject
 *	@see IPoint3D
 */
public interface IAxis3D
{
	
	/**
	 * Gets a snapshot of the x axis (ax) in the current space.
	 * 
	 * @return interface to acces to x,y,z components of the x axis (ax)
	 */
	public IPoint3D getAX();
	

	/**
	 * Gets a snapshot of the y axis (ay) in the current space.
	 *
	 * @return interface to acces to x,y,z components of the y axis (ay)
	 */	
	public IPoint3D getAY();
	

	/**
	 * Gets a snapshot of the z axis (az) in the current space.
	 *
	 * @return interface to acces to x,y,z components of the z axis (az)
	 */		
	public IPoint3D getAZ();
	

	/**
	 * Gets origine for axis3D in the current space. 
	 *
	 * @return interface to acces to x,y,z components of the origine of the axis3D
	 */		
	public IPoint3D getOrigin();


	/**
	 * Copy value from an other axis3D. 
	 *
	 * @param source source axis3D to copy value from.
	 * @return this axis3D.
	 */		
	public IAxis3D copy(IAxis3D source);


	/**
	 * Rotate this axis3D around the X axis.
	 * <p>
	 * X axis means vector(1,0,0) and does not means this axis AX vector.<br><br>
	 *
	 * You may call myaxis.rotate(angle,myaxis.getAX()) to rotate "myaxis" around it own AX axis.
	 * </p>
	 * @param angle rotation angle expressed in radian.
	 * @return this axis3D.
	 */		
	public IAxis3D rotateX(double angle);


	/**
	 * Rotate this axis3D around the Y axis.
	 * <p>
	 * Y axis means vector(0,1,0) and does not means this axis AY vector.<br><br>
	 *
	 * You may call myaxis.rotate(angle,myaxis.getAY()) to rotate "myaxis" around it own AY axis.
	 * </p>
	 * @param angle rotation angle expressed in radian.
	 * @return this axis3D.
	 */		
	public IAxis3D rotateY(double angle);


	/**
	 * Rotate this axis3D around the Z axis.
	 * <p>
	 * Z axis means vector(0,0,1) and does not means this axis AZ vector.<br><br>
	 *
	 * You may call myaxis.rotate(angle,myaxis.getAZ()) to rotate "myaxis" around it own AZ axis.
	 * </p>
	 * @param angle rotation angle expressed in radian.
	 * @return this axis3D.
	 */			
	public IAxis3D rotateZ(double angle);


	/**
	 * Rotate this axis3D around and arbitrary axis.
	 *
	 * @param angle rotation angle expressed in radian.
	 * @param x rotation axis x component.
	 * @param y rotation axis y component.
	 * @param z rotation axis z component.
	 * @return this axis3D.
	 */			
	public IAxis3D rotate(double angle,double x,double y,double z);


	/**
	 * Rotate this axis3D around and arbitrary axis using a specific pivot point.
	 *
	 * @param angle rotation angle expressed in radian.
	 * @param px pivot point x position.
	 * @param py pivot point y position.
	 * @param pz pivot point z position.
	 * @param x rotation axis x component relative to pivot point.
	 * @param y rotation axis y component relative to pivot point.
	 * @param z rotation axis z component relative to pivot point.
	 * @return this axis3D.
	 */		
	public IAxis3D rotate(double angle,double px,double py,double pz,double x,double y,double z);


	/**
	 * Rotate this axis3D around and arbitrary axis.
	 *
	 * @param angle rotation angle expressed in radian.
	 * @param axis rotation axis.
	 * @return this axis3D.
	 */		
	public IAxis3D rotate(double angle,IPoint3D axis);


	/**
	 * Rotate this axis3D around and arbitrary axis using a specific pivot point.
	 *
	 * @param angle rotation angle expressed in radian.
	 * @param px pivot point x position.
	 * @param py pivot point y position.
	 * @param pz pivot point z position.
	 * @param axis rotation axis relative to pivot point.
	 * @return this axis3D.
	 */		
	public IAxis3D rotate(double angle,double px,double py,double pz,IPoint3D axis);


	/**
	 * Rotate this axis3D around and arbitrary axis using a specific pivot point.
	 *
	 * @param angle rotation angle expressed in radian.
	 * @param pivot pivot point.
	 * @param axis rotation axis relative to pivot point.
	 * @return this axis3D.
	 */		
	public IAxis3D rotate(double angle,IPoint3D pivot,IPoint3D axis);


	/**
	 * Add/Move this axis by a given vector.
	 *
	 * @param x vector x component.
	 * @param y vector y component.
	 * @param z vector z component.
	 * @return this axis3D.
	 */		
	public IAxis3D add(double x,double y,double z);


	/**
	 * Substract/Move this axis by a given vector.
	 *
	 * @param x vector x component.
	 * @param y vector y component.
	 * @param z vector z component.
	 * @return this axis3D.
	 */		
	public IAxis3D sub(double x,double y,double z);

	/**
	 * Add/Move this axis by a given 3d point.
	 *
	 * @param point vector.
	 * @return this axis3D.
	 */		
	public IAxis3D add(IPoint3D point);

	/**
	 * Substract/Move this axis by a given 3d point.
	 *
	 * @param point vector.
	 * @return this axis3D.
	 */		
	public IAxis3D sub(IPoint3D point);

	/**
	 * Update the given 3d point with the rotation of this axis.
	 * <p>
	 *  this function will update the given point3d with the rotations 
	 *   that must be applied to a new axis3D to get the same axis.<br><br>
	 *  
	 *  <u><b>ex:</b></u><br><br>
	 *
	 *	axis.getRotationXZY(rotation);<br><br>
	 *
	 *	To get a new axis3D aligned to this axis you can do the following:<br><br>
	 *
	 *  newAxis.rotateX(rotation.getX()).rotateZ(rotation.getZ()).rotateY(rotation.getY())<br><br>
	 *
	 *  <b>Note:</b> rotations must be applied in the following order : RX,RZ,RY.
	 * </p>
	 *
	 * @param rotation point that will received the axis3D rotation rx,ry,rz.
	 * @return this axis3D.
	 */		
	public IAxis3D getRotationXZY(IPoint3D rotation);
	
	/** Transform this axis values into the local axis a
	 *  @param a an axis to transform axis to
 	 *  @return same axis "viewed" by axis a
 	 */																		
	public IAxis3D toLocalAxis(IAxis3D a);
	
	/** Put this axis in the given axis space
	 *  @param a an axis to transform to its space
 	 *  @return same axis in given axis space
 	 */																		
	public IAxis3D toAxis(IAxis3D a);

	/**
	 * Initialise this axis3D.
	 * <p>
	 * this function will do the following<br>
	 *  - set Origin to (0,0,0)<br>
	 *  - set AX to (1,0,0)<br>
	 *  - set AY to (0,1,0)<br>
	 *  - set AZ to (0,0,1)<br>
	 * </p>
	 *
	 * @return this axis3D.
	 */		
	public IAxis3D init();
	
	/** Update this Axis3D using given pivot,position & rotation.
	 *
	 *  @param pivot pivot to use 
	 *  @param position position to use 
	 *  @param rotation rotation to use 
 	 *  @return this Axis3D updated
 	 */			
	public IAxis3D set(IPoint3D pivot,IPoint3D position,IPoint3D rotation);

	/** Update this Axis3D using given position & rotation.
	 *
	 *  @param position position to use 
	 *  @param rotation rotation to use 
 	 *  @return this Axis3D updated
 	 */			
	public IAxis3D set(IPoint3D position,IPoint3D rotation);

	
}