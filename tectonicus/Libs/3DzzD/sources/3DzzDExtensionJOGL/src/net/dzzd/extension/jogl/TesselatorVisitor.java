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

/*
 * TesselatorVisitor.java
 *
 * Purpose:
 * The TesselatorVisitor abstracts the sending of a list of OpenGL
 * geometry to an OpenGL engine.
 * 
 * Author: Matthijs Blaas
 *
 */
package net.dzzd.extension.jogl;


interface TesselatorVisitor
{
    /**
     * Begins a primitive.
     * @param mode the drawing mode
     */
	void begin(int mode);
    
    /**
     * Adds a vertex.
     * @param coords the coordinates of the vertex.
     */
	void addVertex(double[] coords);
    
    /**
     * Ends a primitive.
     *
     */
	void end();
    
    /**
     * Reports an error.
     * @param errorCode the code
     */
	void error(int errorCode);
    
    /**
     * Combine two segments.
     * @param coords the coords
     * @param vertex_data the vertex data
     * @param weight the weight
     * @param dataOut
     */
	void combine(double[] coords, Object[] vertex_data, float[] weight, Object[] dataOut);
}
