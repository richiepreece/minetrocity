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
 *  Used for accessing to a Face3D.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IMesh3D
 */
public interface IFace3D
{
	
	/**
	 * Gets this face material.
	 * 
	 * @return current face material or null if none
	 */	
	public IMaterial getMaterial();
	
	/**
	 * Sets this face material.
	 * 
	 * @param material new material or null to remove
	 */	
	public void setMaterial(IMaterial material);
	
	
	/**
	 * Gets interface the first vertex of this face.
	 *
	 * @return interface to access vertex
	 */	
	public IVertex3D getVertex3D0();
	
	/**
	 * Gets interface the second vertex of this face.
	 *
	 * @return interface to access vertex0
	 */		
	public IVertex3D getVertex3D1();
	
	/**
	 * Gets interface the third vertex of this face.
	 *
	 * @return interface to access vertex0
	 */		
	public IVertex3D getVertex3D2();
	
	/**
	 * Gets U mapping value for selected vertex.
	 *
	 * if param numVertex is out of the range 0-2 than 0 is returned
	 *
	 * @param numVertex vertex mapping coordinate to return
	 * @return U mapping coordinate for selected vertex
	 */	
	public float getMappingU(int numVertex);		
	
	/**
	 * Gets V mapping value for selected vertex.
	 *
	 * if param numVertex is out of the range 0-2 than 0 is returned
	 *
	 * @param numVertex vertex mapping coordinate to return
	 * @return V mapping coordinate for selected vertex
	 */	
	public float getMappingV(int numVertex);	
	
	/**
	 * Sets U mapping value for selected vertex.
	 *
	 * if param numVertex is out of the range 0-2 than it will return immediatly without doing anything
	 *
	 * @param numVertex vertex mapping coordinate to change
	 * @param val new U mapping coordinate for selected vertex
	 */	
	public void setMappingU(int numVertex,float val);		
	
	/**
	 * Sets V mapping value for selected vertex.
	 *
	 * if param numVertex is out of the range 0-2 than it will return immediatly without doing anything
	 *
	 * @param numVertex vertex mapping coordinate to change
	 * @param val new V mapping coordinate for selected vertex
	 */	
	public void setMappingV(int numVertex,float val);

	/**
	 * Gets the "a" parameter of the equation (a*x+b*y+c*z+d=0) for the plane that face lie on.
	 *
	 * The returned parameter is relative to object space.
	 *
	 * @return equation parameter value
	 */	
	public double getPA();

	/**
	 * Gets the "b" parameter of the equation (a*x+b*y+c*z+d=0) for the plane that face lie on.
	 *
	 * The returned parameter is relative to object space.
	 *
	 * @return equation parameter value
	 */
	public double getPB();

	/**
	 * Gets the "c" parameter of the equation (a*x+b*y+c*z+d=0) for the plane that face lie on.
	 *
	 * The returned parameter is relative to object space.
	 *
	 * @return equation parameter value
	 */
	public double getPC();

	/**
	 * Gets the "d" parameter of the equation (a*x+b*y+c*z+d=0) for the plane that face lie on.
	 *
	 * The returned parameter is relative to object space.
	 *
	 * @return equation parameter value
	 */
	public double getPD();

	/**
	 * Gets the radius of the surrounding sphere for this face relative to third vertex.
	 *
	 * This parameter is relative to object space.
	 *
	 * @return equation parameter value
	 */
	public double getSphereBox();

	/**
	 * Flip normal (reverse face vertices order).
	 */
	public void flipNormal();
	
	/**
	 * Gets the id.
	 *
	 * @return id of this face
	 */	
	public int getId();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return x component for vertex 0 normal
	 */		
	public float getVertex3D0Nx();

	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return y component for vertex 0 normal
	 */			
	public float getVertex3D0Ny();
		
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return z component for vertex 0 normal
	 */		
	public float getVertex3D0Nz();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return x component for vertex 1 normal
	 */		
	public float getVertex3D1Nx();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return y component for vertex 1 normal
	 */			
	public float getVertex3D1Ny();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return z component for vertex 1 normal
	 */		
	public float getVertex3D1Nz();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return x component for vertex 2 normal
	 */		
	public float getVertex3D2Nx();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return y component for vertex 2 normal
	 */			
	public float getVertex3D2Ny();
	
	/**
	 * Gets Vertex3D normal component.
	 *
	 * @return z component for vertex 2 normal
	 */		
	public float getVertex3D2Nz();
	
public IMesh3D getMesh3D();
}