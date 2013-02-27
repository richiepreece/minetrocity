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
 *  Used for accessing to a Texture.
 *  <br>
 *  Texture are a 2D image represented by an array of pixels color.<br>
 *	<br>
 *  3DzzD use 32 bit integer to store pixels colors and 32 bit integer array to store Texture.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see ITexture
 *	@see IProgress
 *	@see DzzD
 */
public interface ITexture extends ISceneObject
{
	/**
	 * Compute MipMap for this Texture.
	 * <br>
	 * Internaly store multiple reduced version of pixels color array <br>
	 * by recursivly scaling this texture by 50% and storing resulting <br>
	 * pixels array internaly.
	 */
    public void buildMipMap();	
    
    /**
     * Gets this Texture pixel buffer
     *
     * @return this Texture pixels buffer
     */
    public int[] getPixels();

    /**
     * Gets this Texture pixel buffer width
     *
     * @return this Texture pixels buffer width
     */    
    public int getPixelsWidth();
    

    /**
     * Gets this Texture pixel buffer height
     *
     * @return this Texture pixels buffer height
     */    
    public int getPixelsHeight();

	/**
     * Sets this Texture type
     *
     * @param type this Texture type: DzzD.TT_RGB,DzzD.TT_ARGB,DzzD.TT_NORMAL,DzzD.TT_HNORMAL etc
     */     
	public void setType(int type);
	
	/**
     * Gets this Texture type
     *
     * @return this Texture type: DzzD.TT_RGB,DzzD.TT_ARGB,DzzD.TT_NORMAL,DzzD.TT_HNORMAL etc
     */        	
	public int getType();
}