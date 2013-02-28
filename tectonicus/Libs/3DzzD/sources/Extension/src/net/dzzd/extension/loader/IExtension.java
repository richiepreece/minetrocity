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

/**
 * Interface that defines the load method all extension requiered to implement
 * <br><br>
 */

package net.dzzd.extension.loader;

import net.dzzd.access.IProgressListener;
public interface IExtension
{
	/**
	 * Calling this method on an extension will make it load itself, using the extensionloader specified.
	 * <br><br>
	 *  NB: that should be the only one public method in an extension loader.<BR>
	 *  exension requiering special right to be loaded must implement this interface
	 * <br><br>
	 * @param baseURL base URL from where extension will be loaded
	 * @param pluginJarFile main jar file containing this extension plugin
	 * @param localDirectory local directory to install downloaded extension files
	 * @param pl listener interface to survey extension loading status  
	 * @param el loader that will be used to download & install this extension
	 */
	public void load(String baseURL,
					 String localDirectory,
					 IProgressListener pl,
					 IExtensionLoader el) throws Exception;
	
}