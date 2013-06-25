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
 *  Used for accessing to a URLTexture.
 *  <p>
 *  URLTexture are texture that gets pixel from a local or remote image file by using an URL.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see ITexture
 *	@see IProgress
 *	@see DzzD
 */
public interface IURLTexture extends ITexture
{
	/**
	 * Sets source file.
	 * <br>
	 * Sets source image file name : image.gif 
	 *
	 * @param sourceFile image file name
	 */
    public void setSourceFile(String sourceFile);

	/**
	 * Gets source file.
	 * <br>
	 * Gets source image file name : image.gif 
	 *
	 * @return image file name
	 */   
 	public String getSourceFile();

	/**
	 * Sets base URL for the source file.
	 * <br>
	 * Sets base URL for image file : <br>
	 * - http://myserver.com/
	 * - file://c:/images/
	 * - etc...
	 *
	 * @param baseURL image file location as an URL
	 */   
    public void setBaseURL(String baseURL);
    
	/**
	 * Gets base URL for the source file.
	 * <br>
	 * Gets base URL for image file : <br>
	 * - http://myserver.com/
	 * - file://c:/images/
	 * - etc...
	 *
	 * @return image file location as an URL
	 */  
	public String getBaseURL();

	/**
	 * Sets base URL and image file name and asynchronously begin loading.
	 *
	 * @param baseURL image file location as an URL
	 * @param sourceFile image file name
	 */
  	public void load(String baseURL,String sourceFile);

	/**
	 * Start loading image asynchronously using using current base URL and source file .
	 */
	public void load();
	
}