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
 *  Used for accessing to a Point3D.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IVertex3D
 */
public interface IPoint3D
{
	
	/**
	 * Gets the x component.
	 * 
	 * @return x component
	 */		
	public double getX();
	
	/**
	 * Gets the y component.
	 * 
	 * @return y component
	 */			
	public double getY();

	/**
	 * Gets the z component.
	 * 
	 * @return z component
	 */		
	public double getZ();
	
	/**
	 * Sets the x component.
	 * 
	 * @param val x component value
	 */			
	public void setX(double val);

	/**
	 * Sets the y component.
	 * 
	 * @param val y component value
	 */			
	public void setY(double val);

	/**
	 * Sets the z component.
	 * 
	 * @param val z component value
	 */			
	public void setZ(double val);

	/**
	 * Sets the x,y,z components.
	 * 
	 * @param x x component value
	 * @param y y component value
	 * @param z z component value
	 */			
	public void set(double x,double y,double z);

	/**
	 * Copy values of a Point3D into this Point3D.
	 * 
	 * @param point3d source Point3D
	 * @return this point3D 
	 */			
	public IPoint3D copy(IPoint3D point3d);

	/**
	 * Add a Point3D to this Point3D.
	 * 
	 * @param p source Point3D 
	 * @return this point3D
	 */
	public IPoint3D add(IPoint3D p);

	/**
	 * Substract a Point3D to this Point3D.
	 * 
	 * @param p source Point3D 
	 * @return this point3D
	 */
	public IPoint3D sub(IPoint3D p);


	/**
	 * Normalize this Point3D.
	 * 
	 * divide all component of the vector represented by this point by its norme : sqrt(x*x+y*y+z*z)
	 * 
	 * @return this point3D normalized
	 */
	public IPoint3D normalize();

	/**
	 * Zoom this Point3D.
	 * 
	 * @param x x zoom factor
	 * @param y y zoom factor
	 * @param z z zoom factor
	 * @return this Point3D.
	 */
	public IPoint3D zoom(double x,double y,double z);	

	/**
	 * Gets a new instance of this Point3D.
	 * 
	 * @return newly allocated point3D with same values.
	 */
	public IPoint3D getClone();

	/** 
	 * Rotate the point around the x axis 
	 *
	 *  @param angle radian angle for the rotation 
	 *
 	 *  @return the same point rotated
 	 */
	public IPoint3D rotateX(double angle);
	
	/** 
	 * Rotate the point around the y axis 
	 *
	 *  @param angle radian angle for the rotation 
	 *
 	 *  @return the same point rotated
 	 */	
	public IPoint3D rotateY(double angle);
	
	/** 
	 * Rotate the point around the z axis 
	 *
	 *  @param angle radian angle for the rotation 
	 *
 	 *  @return the same point rotated
 	 */	
	public IPoint3D rotateZ(double angle);

	/** 
	 * Multiply all components (x,y,z) by n
	 *
	 *  @param n value to use for the multiplication
	 *
 	 *  @return same point with multiplied components
 	 */										
	public IPoint3D mul(double n);
	
	/** 
	 * Divide all components (x,y,z) by n
	 *
	 *  @param n value to use for the division
	 *
 	 *  @return same point with divided components
 	 */									
	public IPoint3D div(double n);
	
	/**
	 * Compute the cross product with another Point3D
	 *
	 * @param vector to use for computing cross product
	 * 
	 * @return cross product performed on the point
	 */
	public IPoint3D cross(IPoint3D p);

	/**
	 * Compute the dot product with another Point3D
	 *
	 * @param vector to use for computing cross product
	 * 
	 * @return dot product
	 */
	public double dot(IPoint3D p2);
	
	/** 
	 * Compute and return the length of the vector represented by this point
	 *
 	 *  @return length of the vector
 	 */							
	public double norm();

	/** 
	 * Compute and return the length of the vector represented by this point
	 *
 	 *  @return length of the vector
 	 */							
	public double length();
	
	
	/** 
	 * Compute and return the length²  of the vector represented by this point
	 *
 	 *  @return length² of the vector
 	 */								
	public double length2();
	
	/** 
	 * Return the distance with the point p
	 *
	 *  @param p a point 
	 *
 	 *  @return distance with the point
 	 */															
	public double dist(IPoint3D p);

}