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
 *  Used for accessing to a Scene3DLoader.
 *
 *  @author Bruno Augier
 *  @version 1.0, 24/07/2007
 *  @since 1.0
 *	@see IScene3D
 */
public interface IScene3DLoader extends IScene2DLoader
{
	
	/**
	 * Asynchroneously load a scene3D from a file using an URL.
	 * <br>
	 * this method can load 3d file from a network or a file.<br>
	 * <br>	
	 * external scene files, as textures files, are expected to be at the same baseURL location.<br>
	 * if none use : loadScene3D(String baseURL,String file,String ressourcPath) instead
	 * <br>
	 * <br>
	 * loading sample:<br>
	 *  loadScene3D("http://myserver.com/","my3dsfile.3ds");<br>
	 * <br>
	 * this function will return immediadly and loading state will be accessible via monitored objects.<br>
	 * <br>
	 * <br>
	 * it is possible to load multiple 3ds file if needed<br>
	 * <br>
	 * each loaded 3ds file will create a root mesh object with the 3ds file name as its name<br>
	 * <br>
	 * loading sample:<br>
	 *  loadScene3D("http://myserver.com/","my3dsfile.3ds");<br>
	 * 	loadScene3D("http://myserver.com/","myobjfile2.obj");<br>
	 *  .....once loaded, you can do the following to get the root object of all 3d object of one of the loaded file.<br>
	 *  getMesh3DByName("my3dsfile.3ds");<br>
	 *  or<br>
	 *  getMesh3DByName("myobjfile2.3ds");<br>
	 * <br>
	 * <br>
	 *
	 * @param baseURL the base URL location.
	 * @param file the file name.
	 */
	public void loadScene3D(String baseURL,String file);
	
	
	/**
	 * Asynchroneously load a scene3D from a file using an URL.
	 * <br>
	 * this method can load 3d file from a network or a file.<br>
	 * <br>	
	 * <br>
	 * <br>
	 * loading sample:<br>
	 *  loadScene3D("http://myserver.com/","my3dsfile.3ds");<br>
	 * <br>
	 * this function will return immediadly and loading state will be accessible via monitored objects.<br>
	 * <br>
	 * <br>
	 * it is possible to load multiple 3ds file if needed<br>
	 * <br>
	 * each loaded 3ds file will create a root mesh object with the 3ds file name as its name<br>
	 * <br>
	 * loading sample:<br>
	 *  loadScene3D("http://myserver.com/","my3dsfile.3ds");<br>
	 * 	loadScene3D("http://myserver.com/","myobjfile2.obj");<br>
	 *  .....once loaded, you can do the following to get the root object of all 3d object of one of the loaded file.<br>
	 *  getMesh3DByName("my3dsfile.3ds");<br>
	 *  or<br>
	 *  getMesh3DByName("myobjfile2.3ds");<br>
	 * <br>
	 * <br>
	 *
	 * @param baseURL the base URL location.
	 * @param file the file name.
	 */	
	public void loadScene3D(String baseURL,String file,String ressourcePath);
	
	/**
	 * Gets an array of all Scene3dObject loaded by this Scene3DLoader
	 *
	 * @return an array containing all Scene3DObject loaded (null if none)
	 */
	public IScene3DObject[] getScene3DObjects();
}