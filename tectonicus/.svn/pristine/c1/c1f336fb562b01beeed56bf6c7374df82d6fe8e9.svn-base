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

package net.dzzd.utils;

/** 
 *  MathX class.
 *  <br>
 * Base class used by 3DzzD Web 3D Engine to include faster and/or smarter maths functions<br>
 * <br>
 * <u>Some results comparaison between Math.cos and MathX.cos function, MathX.cos seems to be 0.5 to 20 times faster than Math.cos function and give more accurate results especially for PI/2</u><br>
  *<br>
 * <B>-PI</B><br>
 * MathX.cos(-Math.PI) =-1.0<br>
 * Math.cos(-Math.PI)  =-1.0<br>
 *<br>
 * <B>PI</B><br>
 * MathX.cos(Math.PI) =-1.0<br>
 * Math.cos(Math.PI)  =-1.0<br>
 *<br>
 * Root(2)/2=0.7071067811865476<br>
 * <B>PI/4</B><br>
 * MathX.cos(Math.PI*0.25) =0.7071067811865476<br>
 * Math.cos(Math.PI*0.25)  =0.7071067811865476<br>
 *<br>
 * <B>PI/2</B><br>
 * MathX.cos(Math.PI*0.25) =0.0<br>
 * Math.cos(Math.PI*0.25)  =6.123233995736766E-17<br>
 *<br>
 * <B>-PI/4</B><br>
 * MathX.cos(-Math.PI*0.25)=0.7071067811865476<br>
 * Math.cos(-Math.PI*0.25) =0.7071067811865476<br>
 *<br>
 * <B>-PI/2</B><br>
 * MathX.cos(-Math.PI*0.5) =0.0<br>
 * Math.cos(-Math.PI*0.5)  =6.123233995736766E-17<br>
 *<br>
 *  @author Bruno Augier (DzzD)
 *  @version 1.0, 01/03/07
 *  @since 1.0
 * @see IRender3D
 * @see IMesh3D
 * @see DzzD
 * @see <a href="http://dzzd.net/">http://dzzd.net/</a>
 */

public class MathX
{
	public static double PI=Math.PI;

	private static double f2=-0.5;
	private static double f4=-f2/(3.0*4.0);
	private static double f6=-f4/(5.0*6.0);
	private static double f8=-f6/(7.0*8.0);
	private static double f10=-f8/(9.0*10.0);
	private static double f12=-f10/(11.0*12.0);
	private static double f14=-f12/(13.0*14.0);
	private static double f16=-f14/(15.0*16.0);
	//MORE PRECISE BUT NOT COMPATIBLE JVM MS =>//private static double f18=-f16/(17.0*18.0);
	//MORE PRECISE BUT NOT COMPATIBLE JVM MS =>//private static double f20=-f18/(19.0*20.0);
	private static double PI2=2.0*PI;
	private static double PI05=0.5*PI;
	

	/**
	* Compute and return sinus of its parameter using taylor serie
	* @param x angle in radian to 
	* @return sinus value for the given parameter
	*/
	public static final double sin(double x)
	{
		//return Math.sin(x);
		return cos(x-PI05);
	}

	/**
	* Compute and return cosinus of its parameter using taylor serie
	* @param x angle in radian to 
	* @return cosinus value for the given parameter
	*/	
    public static final double cos(double x)
    {
    	
    	
    	if(x<0.0) x=-x;
    	
    	
    	if(x<PI2) 
    	{
    		if(x<PI)
    		{	
				double x2=x*x;
				return 1.0+x2*(f2+x2*(f4+x2*(f6+x2*(f8+x2*(f10+x2*(f12+x2*(f14+x2*(f16))))))));
				//MORE PRECISE BUT NOT COMPATIBLE JVM MS => return 1.0+x2*(f2+x2*(f4+x2*(f6+x2*(f8+x2*(f10+x2*(f12+x2*(f14+x2*(f16+x2*(f18+x2*f20)))))))));    	
			}
			else
			{
				x-=PI;
				double x2=x*x;
				return -(1.0+x2*(f2+x2*(f4+x2*(f6+x2*(f8+x2*(f10+x2*(f12+x2*(f14+x2*(f16)))))))));
				//MORE PRECISE BUT NOT COMPATIBLE JVM MS => return -(1.0+x2*(f2+x2*(f4+x2*(f6+x2*(f8+x2*(f10+x2*(f12+x2*(f14+x2*(f16+x2*(f18+x2*f20))))))))));    	
			}
    	}
    	
    	
    	x%=PI2;
    	x-=PI;
		double x2=x*x;
		
		return -(1.0+x2*(f2+x2*(f4+x2*(f6+x2*(f8+x2*(f10+x2*(f12+x2*(f14+x2*f16))))))));
		//MORE PRECISE BUT NOT COMPATIBLE JVM MS => return -(1.0+x2*(f2+x2*(f4+x2*(f6+x2*(f8+x2*(f10+x2*(f12+x2*(f14+x2*(f16+x2*(f18+x2*f20))))))))));
		
	}	
		
}