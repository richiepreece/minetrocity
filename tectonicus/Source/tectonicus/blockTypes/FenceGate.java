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

import org.lwjgl.util.vector.Vector4f;

import tectonicus.BlockContext;
import tectonicus.BlockIds;
import tectonicus.BlockType;
import tectonicus.BlockTypeRegistry;
import tectonicus.configuration.LightFace;
import tectonicus.rasteriser.Mesh;
import tectonicus.raw.RawChunk;
import tectonicus.renderer.Geometry;
import tectonicus.texture.SubTexture;

public class FenceGate implements BlockType
{
	private final String name;
	private final SubTexture texture;
	
	public FenceGate(String name, SubTexture texture)
	{
		this.name = name;
		this.texture = texture;
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
		final int data = rawChunk.getBlockData(x, y, z);
		
		Mesh mesh = geometry.getMesh(texture.texture, Geometry.MeshType.Solid);
		
		Vector4f colour = new Vector4f(1, 1, 1, 1);
		
		final float topLight = world.getLight(rawChunk.getChunkCoord(), x, y, z, LightFace.Top);
		final float northSouthLight = world.getLight(rawChunk.getChunkCoord(), x, y, z, LightFace.NorthSouth);
		final float eastWestLight = world.getLight(rawChunk.getChunkCoord(), x, y, z, LightFace.EastWest);
		
		final int northId = world.getBlockId(rawChunk.getChunkCoord(), x, y, z+1);
		final int southId = world.getBlockId(rawChunk.getChunkCoord(), x, y, z-1);
		final int eastId = world.getBlockId(rawChunk.getChunkCoord(), x+1, y, z);
		final int westId = world.getBlockId(rawChunk.getChunkCoord(), x-1, y, z);
		
		final boolean open = (data & 0x04) == 0x04;
		final int direction = (data & 0x03);
		
		if ((direction == 1 || direction == 3) && (northId != BlockIds.WALL || southId != BlockIds.WALL) ) // south/north
		{
			// outside posts
			BlockUtil.addBlock(mesh, x, y, z, 7, 5,  0, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			BlockUtil.addBlock(mesh, x, y, z, 7, 5, 14, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// gates
			if (!open)
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 7, 12,  2,
													 2,  3, 12, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 7,  6,  2,
													 2,  3, 12, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 7,  9,  6,
													 2,  3,  4, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else if (direction == 1) // open north
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 1, 12,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 1,  6,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 1,  9,  0,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 1, 12, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 1,  6, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 1,  9, 14,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else // open south
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 9, 12,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 9,  6,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	13,  9,  0,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 9, 12, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 9,  6, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	13,  9, 14,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
		}
		else if ((direction == 0 || direction == 2) && (eastId != BlockIds.WALL || westId != BlockIds.WALL) )// east/west
		{
			// outside posts
			BlockUtil.addBlock(mesh, x, y, z,  0, 5, 7, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			BlockUtil.addBlock(mesh, x, y, z, 14, 5, 7, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// gates
			if (!open)
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 2, 12,  7,
													12,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 2,  6,  7,
													12,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 6,  9,  7,
													 4,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else if (direction == 0) // open west
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 0, 12,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 0,  6,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 0,  9, 13,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	14, 12,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	14,  6,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	14,  9, 13,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else // open east
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 0, 12,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 0,  6,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 0,  9,  1,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	14, 12,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	14,  6,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	14,  9,  1,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
		}
		else if ((direction == 1 || direction == 3) && (northId == BlockIds.WALL || southId == BlockIds.WALL) )  //south/north //If fence gate is connected to walls it needs to be lower
		{
			// outside posts
			BlockUtil.addBlock(mesh, x, y, z, 7, 2,  0, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			BlockUtil.addBlock(mesh, x, y, z, 7, 2, 14, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// gates
			if (!open)
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 7, 9,  2,
													 2,  3, 12, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 7,  3,  2,
													 2,  3, 12, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 7,  6,  6,
													 2,  3,  4, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else if (direction == 1) // open north
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 1, 9,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 1,  3,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 1,  6,  0,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 1, 9, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 1,  3, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 1,  6, 14,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else // open south
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 9, 9,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 9,  3,  0,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	13,  6,  0,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 9, 9, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 9,  3, 14,
													 6,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	13,  6, 14,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
		}
		else // east/west fence gate connected to walls
		{
			// outside posts
			BlockUtil.addBlock(mesh, x, y, z,  0, 2, 7, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			BlockUtil.addBlock(mesh, x, y, z, 14, 2, 7, 2, 11, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// gates
			if (!open)
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 2, 9,  7,
													12,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 2,  3,  7,
													12,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 6,  6,  7,
													 4,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else if (direction == 0) // open west
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 0, 9,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 0,  3,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 0,  6, 13,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	14, 9,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	14,  3,  9,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	14,  6, 13,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
			else // open east
			{
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	 0, 9,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	 0,  3,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	 0,  6,  1,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
				// Top bar
				BlockUtil.addBlock(mesh, x, y, z,	14, 9,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);
				
				// Bottom bar
				BlockUtil.addBlock(mesh, x, y, z,	14,  3,  1,
													 2,  3,  6, colour, texture, topLight, northSouthLight, eastWestLight);

				// Middle
				BlockUtil.addBlock(mesh, x, y, z,	14,  6,  1,
													 2,  3,  2, colour, texture, topLight, northSouthLight, eastWestLight);
			}
		}

		/*
		// Auto-connect to adjacent fences
		
		// Bars are two wide and three high
		
		// North
		final int northId = world.getBlockId(chunk.getChunkCoord(), x-1, y, z);
		if (northId == blockId)
		{
			// Top bar
			BlockUtil.addBlock(mesh, x, y, z,	0, 10, 7,
												8, 3, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// Bottom bar
			BlockUtil.addBlock(mesh, x, y, z,	0, 4, 7,
												8, 3, 2, colour, texture, topLight, northSouthLight, eastWestLight);
		}
		
		// South
		final int southId = world.getBlockId(chunk.getChunkCoord(), x+1, y, z);
		if (southId == blockId)
		{
			// Top bar
			BlockUtil.addBlock(mesh, x, y, z,	8, 10, 7,
												8, 3, 2, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// Bottom bar
			BlockUtil.addBlock(mesh, x, y, z,	8, 4, 7,
												8, 3, 2, colour, texture, topLight, northSouthLight, eastWestLight);
		}
		
		// East
		final int eastId = world.getBlockId(chunk.getChunkCoord(), x, y, z-1);
		if (eastId == blockId)
		{
			// Top bar
			BlockUtil.addBlock(mesh, x, y, z,	7, 10, 0,
												2, 3, 8, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// Bottom bar
			BlockUtil.addBlock(mesh, x, y, z,	7, 4, 0,
												2, 3, 8, colour, texture, topLight, northSouthLight, eastWestLight);
		}
		
		// West
		final int westId = world.getBlockId(chunk.getChunkCoord(), x, y, z+1);
		if (westId == blockId)
		{
			// Top bar
			BlockUtil.addBlock(mesh, x, y, z,	7, 10, 8,
												2, 3, 8, colour, texture, topLight, northSouthLight, eastWestLight);
			
			// Bottom bar
			BlockUtil.addBlock(mesh, x, y, z,	7, 4, 8,
												2, 3, 8, colour, texture, topLight, northSouthLight, eastWestLight);
		}
		*/
	}
}
