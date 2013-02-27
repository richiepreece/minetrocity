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

package net.dzzd.core;

import java.awt.*;
import java.awt.image.*;

final public class PixelsBufferBI
{
	Image image;
	int pixels[];
	public PixelsBufferBI(int largeur,int hauteur)
	{
			
			BufferedImage BIImage;
			//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			//GraphicsDevice gs = ge.getDefaultScreenDevice();
			//GraphicsConfiguration gc = gs.getDefaultConfiguration();
			BIImage = new BufferedImage(largeur, hauteur,BufferedImage.TYPE_INT_RGB);
			pixels=((DataBufferInt)(BIImage.getRaster().getDataBuffer())).getData(); 
			image=(Image)BIImage;			
	}
	public Image getImage()
	{
		return this.image;
	}
	public int[] getPixels()
	{
		return this.pixels;
	}
}
	
