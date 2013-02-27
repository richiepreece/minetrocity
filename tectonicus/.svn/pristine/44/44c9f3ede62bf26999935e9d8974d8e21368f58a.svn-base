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
 * Interface allowing access to extension loader for loading & instanciating extensions
 * <br><br>
 * Exensions that require special privileges must be loaded using such a loader
 * <br><br>
 */

package net.dzzd.extension.loader;

import net.dzzd.access.IProgressListener;

import java.net.URL;

public interface IExtensionLoader
{
	public void loadJar(final String cachePath, 
						final String remoteJarURL,
						final String fileName,
						final IProgressListener progressListener) throws Exception;

						
	public Object loadExtension(
						String extensionLoaderURL, 
						String jarFile, 
						String installerClass,
						String localDirectory,
						IProgressListener pl,
						String mainClassName) throws Exception;
}