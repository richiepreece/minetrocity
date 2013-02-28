/*
 * Source code from Tectonicus, http://code.google.com/p/tectonicus/
 *
 * Tectonicus is released under the BSD license (below).
 *
 *
 * Original code John Campbell / "Orangy Tang" / www.triangularpixels.com
 *
 * Copyright (c) 2012, John Campbell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list
 *     of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice, this
 *     list of conditions and the following disclaimer in the documentation and/or
 *     other materials provided with the distribution.
 *   * Neither the name of 'Tecctonicus' nor the names of
 *     its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package tectonicus.blockTypes;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tectonicus.BlockContext;
import tectonicus.BlockType;
import tectonicus.BlockTypeRegistry;
import tectonicus.Chunk;
import tectonicus.configuration.LightFace;
import tectonicus.rasteriser.Mesh;
import tectonicus.rasteriser.MeshUtil;
import tectonicus.raw.RawChunk;
import tectonicus.renderer.Geometry;
import tectonicus.texture.SubTexture;

public class PressurePlate implements BlockType
{
	private final String name;
	private final SubTexture texture;
	private final SubTexture edgeTexture;
	
	public PressurePlate(String name, SubTexture texture)
	{
		this.name = name;
		this.texture = texture;
		
		final float vSize = 1.0f / 16.0f / 16.0f;
		this.edgeTexture = new SubTexture(texture.texture, texture.u0, texture.v0, texture.u1, texture.v0+vSize);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
	
	@Override
	public boolean isWater()
	{
		return false;
	}
	
	@Override
	public void addInteriorGeometry(int x, int y, int z, BlockContext world, BlockTypeRegistry registry, RawChunk rawChunk, Geometry geometry)
	{
		addEdgeGeometry(x, y, z, world, registry, rawChunk, geometry);
	}
	
	@Override
	public void addEdgeGeometry(final int x, final int y, final int z, BlockContext world, BlockTypeRegistry registry, RawChunk rawChunk, Geometry geometry)
	{
		final float height = 1.0f / 16.0f;
		final float border = 1.0f / 16.0f;  //used to make pressure plate slightly smaller than the block it sits on
		
		Mesh mesh = geometry.getMesh(texture.texture, Geometry.MeshType.Solid);
		
		final float lightness = Chunk.getLight(world.getLightStyle(), LightFace.Top, rawChunk, x, y, z);
		
		Vector4f white = new Vector4f(lightness, lightness, lightness, 1);
		
		// Top quad
		MeshUtil.addQuad(mesh, new Vector3f(x+border, y+height, z+border), new Vector3f(x+1-border, y+height, z+border), new Vector3f(x+1-border, y+height, z+1-border), new Vector3f(x+border, y+height, z+1-border), white, texture);
		
		// West edge
		MeshUtil.addQuad(mesh, new Vector3f(x+border, y+height, z+border), new Vector3f(x+border, y+height, z+1-border), new Vector3f(x+border, y, z+1-border), new Vector3f(x+border, y, z+border), white, edgeTexture);
		
		// East edge
		MeshUtil.addQuad(mesh, new Vector3f(x+1-border, y+height, z+1-border), new Vector3f(x+1-border, y+height, z+border), new Vector3f(x+1-border, y, z+border), new Vector3f(x+1-border, y, z+1-border), white, edgeTexture);
		
		// South edge
		MeshUtil.addQuad(mesh, new Vector3f(x+1-border, y+height, z+border), new Vector3f(x+border, y+height, z+border), new Vector3f(x+border, y, z+border), new Vector3f(x+1-border, y, z+border), white, edgeTexture);
		
		// North edge
		MeshUtil.addQuad(mesh, new Vector3f(x+border, y+height, z+1-border), new Vector3f(x+1-border, y+height, z+1-border), new Vector3f(x+1-border, y, z+1-border), new Vector3f(x+border, y, z+1-border), white, edgeTexture);
	}
	
}
