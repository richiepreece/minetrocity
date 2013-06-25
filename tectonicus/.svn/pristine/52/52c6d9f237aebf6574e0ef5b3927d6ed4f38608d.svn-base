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
 *  Used for accessing to a Mesh3DGenerator.
 *  <br>
 *  IMesh3DGenerator are able to generate Mesh3D.
 *  
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IMesh3D
 */
public interface IMesh3DGenerator
{
	/**
	 * Sets the size of the generated Mesh3D.
	 *
	 * @param min3DX bounding box xmin
	 * @param min3DY bounding box ymin
	 * @param min3DZ bounding box zmin
	 * @param max3DX bounding box xmax
	 * @param max3DY bounding box ymax
	 * @param max3DZ bounding box zmax
	 */
	public void setMesh3DBounds(double min3DX,double min3DY,double min3DZ,double max3DX,double max3DY,double max3DZ);

	/**
	 * Generate complete Face3D and Vertex3D arrays using the current quality.
	 * <br>
	 * @return Mesh3D generated
	 */
	public IMesh3D generate();

	/**
	 * Sets the quality to use for Mesh3D generation.
	 *
	 * @param quality new quality value
	 */
	public void setMesh3DGeneratorQuality(double quality);

	/**
	 * Gets the quality used for Mesh3D generation.
	 *
	 * @return quality value
	 */	
	public double getMesh3DGeneratorQuality();

	/**
	 * Gets total number of Face3D for last generation.
	 *
	 * @return total number of Face3D for last generation
	 */	
	public int getNbFace3D();

	/**
	 * Gets total number of Vertex3D for last generation.
	 *
	 * @return total number of Vertex3D for last generation
	 */		
	public int getNbVertex3D();

	/**
	 * Gets complete Vertex3D array.
	 *
	 * @return array containing all Vertex3D
	 */
	public IVertex3D[] getVertex3D();

	/**
	 * Gets complete Face3D array.
	 *
	 * @return array containing all Face3D
	 */
	public IFace3D[] getFace3D();

}