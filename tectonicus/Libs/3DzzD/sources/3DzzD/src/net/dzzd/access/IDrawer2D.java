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

import java.awt.Canvas;

public interface IDrawer2D
{
	// TEMP methods to test the JOGL renderer
	public void doRender();
	public Canvas getCanvas();
	////////////////////////////
	
	public void setSize(int width,int height);
	public void setClip(double xMin,double yMin,double xMax,double yMax);
	
	public int getColor();
	public void setColor(int color);
	
	public IFont2D getFont2D();
	public void setFont2D(IFont2D font);
	
	public void drawShape2D(IShape2D shape,double px,double py,double zoomX,double zoomY);
	public void drawString(String str,double px,double py,double size);
	
	public void beginShape2D();
	public void moveTo(double x,double y);
	public void lineTo(double x,double y);
	public void quadTo(double x,double y,double x2,double y2);
	public void endShape2D();
	
	public void line(double x1,double y1,double x2,double y2);
	public void quad(double x1,double y1,double x2,double y2,double x3,double y3);
	
	public void fillSolid();
	
	public void setPixel(double x,double y,int color);
	public void setPixel(int x,int y,int color);
	
	public void setMode(int mode);
	
	public void renderScene2D(IScene2D s);
}