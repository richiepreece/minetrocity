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
 *  Used for accessing to a Mesh3D.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IFace3D
 *	@see IVertex3D
 *  @see IMesh3DGenerator
 */
public interface IMesh3D extends IScene3DObject
{
	/**
	 * Gets number of Face3D used by this Mesh3D.
	 * <br>
	 * for a generated Mesh3D this also include faces used by LOD.
	 * 
	 * @return all Face3D used by this Mesh3D
	 */
	public int getNbFace3D();
	
	/**
	 * Gets number of Vertex3D used by this Mesh3D.
	 * <br>
	 * for a generated Mesh3D this also include vertices used by LOD.
	 * 
	 * @return all Vertex3D used by this Mesh3D
	 */
	public int getNbVertex3D();

	/**
	 * Gets a Face3D of this Mesh3D.
	 * <br>
	 * for a generated Mesh3D this also include faces used by LOD.
	 * @param num face id to return
	 * @return Face3D having the given
	 */	
	public IFace3D getFace3D(int num);

	/**
	 * Gets Face3D array used by this mesh.
	 * <br>
	 * for a generated Mesh3D this array also include faces used by LOD.
	 * 
	 * @return number of Face3D used by this Mesh3D
	 */
	public IFace3D[] getFaces3D();

	/**
	 * Gets Vertex3D array used by this mesh.
	 * <br>
	 * for a generated Mesh3D this array also include vertices used by LOD.
	 * 
	 * @return number of Vertex3D used by this Mesh3D
	 */
	public IVertex3D[] getVertex3D();


	/**
	 * Flip all Face3D normal by reversing vertices order.
	 */
	public void flipNormals();

	/**
	 * Gets IMesh3DViewGenerator for this Mesh3D.
	 * <br>
	 * IMesh3DViewGenerator are able to construct a (LOD) Face3DList for a given view point.
	 * 
	 * @return current Mesh3DViewGenerator or null if none.
	 */
	public IMesh3DViewGenerator getMesh3DViewGenerator();

	/**
	 * Sets IMesh3DViewGenerator for this Mesh3D.
	 * <br>
	 * IMesh3DViewGenerator are able to construct a (LOD) Face3DList for a given view point.
	 * 
	 * @param viewGenerator new Mesh3DViewGenerator or null to remove.
	 */
	public void setMesh3DViewGenerator(IMesh3DViewGenerator viewGenerator);


	/**
	 * Gets IMesh3DCollisionGenerator for this Mesh3D.
	 * <br>
	 * IMesh3DCollisionGenerator are able to construct a (LOD) Face3DList for collision tests.
	 * 
	 * @return current Mesh3DCollisionGenerator or null if none.
	 */
	public IMesh3DCollisionGenerator getMesh3DCollisionGenerator();

	/**
	 * Sets IMesh3DCollisionGenerator for this Mesh3D.
	 * <br>
	 * IMesh3DCollisionGenerator are able to construct a (LOD) Face3DList for collision tests.
	 * 
	 * @param collisionGenerator new Mesh3DCollisionGenerator or null to remove.
	 */
	public void setMesh3DCollisionGenerator(IMesh3DCollisionGenerator collisionGenerator);

	/**
	 * Build an internal Mesh3DOctree.
	 * <br>
	 * Mesh3DOctree are useful on complex Mesh3D to improve rendering and collision detections performance.
	 */
	public void buildMesh3DOctree();

	/**
	 * Build Vertex3D and Face3D normals for this Mesh3D.
	 * 
     * vertex normal are computed using face plane normal and smoothingGroups.
     * this method will also remove invalid vertex (unused) and invalid face (not drawable).
     * you may have call removeDuplicatedVertex once to avoid smoothing groups error
     * 
     * before returning this method will call <code>buildVertexId</code> if some vertices have been removed
     * before returning this method will call <code>buildFacesId</code> if some faces have been removed
     *
	 */
	public void buildFacesNormals();

	/**
	 * Remove duplicated vertice.
	 * <br>
	 * vertice with exacly the same position are removed.
	 */
	public void removeDuplicateVertices();

	/**
	 * Build id for all Face3D of this Mesh3D.
	 */
	public void buildFaceId();

	/**
	 * Build id for all Vertex3D of this Mesh3D.
	 */
	public void buildVertexId();

	/**
	 * Build this Mesh3D.
	 * <br>
	 * Call all build function in the following oreder:<br>
	 * <br>
	 * this.buildVertexId();<br>
	 * this.buildFaceId();<br>
	 * this.buildFacesNormals();<br>
	 * this.buildSphereBoxAndCenter();
	 */
	public void build();

	/**
	 * Build this Mesh3D spherebox.
	 * <br>
	 * compute and update internal sphere box radius and Mesh3D center.
	 */
	public void buildSphereBoxAndCenter();
	
	/**
	 * Sets this Mesh3D material.
	 * 
	 * @param material new material or null to remove
	 */	
	public void setMaterial(IMaterial material);
	
	/**
	 * Sets castShadowFlag flag
	 * <br>
	 * if set to true and renderer is able to render shadow this object will be used as a Shadow caster
	 * 
	 * @param castShadowFlag true if this Mesh3D may cast shadow
	 */
	public void setCastShadow(boolean castShadowFlag);

	/**
	 * Gets castShadowFlag flag
	 * <br>
	 * if set to true and renderer is able to render shadow this object will be used as a Shadow caster
	 * 
	 * @return true if this Mesh3D may cast shadow
	 */	
	public boolean getCastShadow();
	
	/**
	 * Sets recvShadowFlag flag
	 * <br>
	 * if this flag is set to true and renderer is able to render shadow this object will be used as a Shadow recevier
	 * 
	 * @param setRecvShadow true if this Mesh3D may received shadow
	 */	
	public void setRecvShadow(boolean recvShadowFlag);
	
	/**
	 * Gets recvShadowFlag flag
	 * <br>
	 * if this flag is set to true and renderer is able to render shadow this object will be used as a Shadow recevier
	 * 
	 * @return true if this Mesh3D may received shadow
	 */	
	public boolean getRecvShadow();
	
	/**
	 * Gets Mesh3DOctree of this Mesh3D
	 *
	 * @return Mesh3DOctree for this Mesh3D or null if none
	 */
	public IMesh3DOctree getMesh3DOctree();
	
	
}