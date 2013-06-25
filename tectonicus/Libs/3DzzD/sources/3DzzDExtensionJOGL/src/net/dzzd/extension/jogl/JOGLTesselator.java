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

/*
 * Tesselator.java
 *
 * Purpose:
 * Wrapper on the GLU Tess Functions, used to tesselate arbitary shapes
 * 
 * Author: Matthijs Blaas
 *
 */
package net.dzzd.extension.jogl;


import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import javax.media.opengl.glu.GLUtessellatorCallbackAdapter;

import net.dzzd.access.IShape2D;


public class JOGLTesselator extends GLUtessellatorCallbackAdapter
{
    private GLUtessellator    tobj;
    private GLU               glu;
    private GL               gl;
    private double[]          point = new double[8];
    private TesselatorVisitor visitor;


    /**
     * Create a new TesselationManager object.
     * 
     * @param glu a GLU object
     */
    public JOGLTesselator(GLU glu,GL gl)
    {
        this.glu = glu;
        this.gl=gl;
        tobj = glu.gluNewTess();

        // Set up Tess Object
        glu.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, this);
        glu.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, this);
        glu.gluTessCallback(tobj, GLU.GLU_TESS_END, this);
        glu.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, this);
        glu.gluTessCallback(tobj, GLU.GLU_TESS_COMBINE, this);

        // Important: Specify the normal for the shape as a whole,
        // so that GLU doesn't bother calculating it. Since all our flat
        // shapes are in the z=0 plane, the normal is easy to calculate:
        glu.gluTessNormal(tobj, 0.0, 0.0, -1.0);

        // Ideally, the GLU tesselator would contain a hint to indicate
        // whether or not to use its greedy approach to pick the best mesh
        // type to use. For cases such as font outlines (which are cached) this
        // greedy approach makes sense. However, for immediate-mode rendering
        // the greedy approach is not useful...
    }
    
    
    /**
     * Tesselates the interior of a polygon defined by a 2d shape.
     * 
     * @param shape a 2d shape
     * @param visitor a TesselatorVisitor
     */
    public void tesselate(IShape2D shape, TesselatorVisitor visitor)
    {
        this.visitor = visitor;
        /*switch (path.getWindingRule()) {
        case PathIterator.WIND_EVEN_ODD:
            setWindingRule(GLU.GLU_TESS_WINDING_ODD);
            break;
        case PathIterator.WIND_NON_ZERO:
            setWindingRule(GLU.GLU_TESS_WINDING_NONZERO);
            break;
        }*/
        

        glu.gluTessBeginPolygon(tobj, (double[]) null);
		glu.gluTessBeginContour(tobj);
        
        /*boolean closed = true;
        int numCoords = 0;

        while (!path.isDone()) {
            switch (path.currentSegment(point)) {
            case PathIterator.SEG_MOVETO:
                if (!closed) {
                    glu.gluTessEndContour(tobj);
                }
                else
                    closed = false;
                glu.gluTessBeginContour(tobj);

            // System.out.println("NEW CONTOUR");
            // System.out.print("MOVE");

            // FALLTHROUGH 
            case PathIterator.SEG_LINETO:
                if (closed) {
                    glu.gluTessBeginContour(tobj);
                    closed = false;
                    // System.out.println("Lineto without moveto");
                }
                // System.out.println("LINETO " + point[0] + ", " + point[1]);
                double[] coord = new double[3]; // coords[numCoords];
                coord[0] = point[0];
                coord[1] = point[1];
                coord[2] = 0;
                glu.gluTessVertex(tobj, coord, 0, coord);
                numCoords++;
                break;
            case PathIterator.SEG_CLOSE:
                glu.gluTessEndContour(tobj);
                closed = true;
                break;
            }
            path.next();
        }*/
        
		shape.firstKey();
		while(!shape.isEnd())
		{	
			double[] sc = shape.getCurrentKeyValues();
			double[] coords = new double[3];
			
			// Added temp sanity checks, just to get shapes with linekeys working / drawable for the moment
			if(sc!=null)
			{
				if(sc.length>0)
					coords[0] = sc[0];
				else
					coords[0] = 0;
				if(sc.length>1)
					coords[1] = sc[1];
				else
					coords[1] = 0;
				if(sc.length>2)
					coords[2] = sc[2];
				else
					coords[2] = 0;
			}
			else
			{
				coords[0] = 0;
				coords[1] = 0;
				coords[2] = 0;
			}
			
			System.out.println("tesselating coords: "+coords[0]+","+coords[1]);
			glu.gluTessVertex(tobj, coords, 0, coords);
			shape.nextKey();
		}        

        
        /*if (!closed) {
            glu.gluTessEndContour(tobj);
        }*/
		glu.gluTessEndContour(tobj);        
        glu.gluTessEndPolygon(tobj);
    }

    /**
     * Tesselates the interior of a polygon defined by a list of points.
     * 
     * @param xPts a table of x coordinates
     * @param yPts a table of y coordinates
     * @param nPts the number of points to consider
     * @param visitor the TesselatorVisitor 
     */
    public void tesselate(	int[] xPts,
    						int[] yPts,
    						int nPts,
    						TesselatorVisitor visitor)
    {
        if (nPts < 3)
            return;

        this.visitor = visitor;
        setWindingRule(GLU.GLU_TESS_WINDING_ODD);
        
        glu.gluTessBeginPolygon(tobj, (double[]) null);
        glu.gluTessBeginContour(tobj);

        for (int i = 0; i < nPts; i++)
        {
            double[] coord = new double[3];
            coord[0] = xPts[i];
            coord[1] = yPts[i];
            coord[2] = 0;
            glu.gluTessVertex(tobj, coord, 0, coord);
        }

        glu.gluTessEndContour(tobj);
        glu.gluTessEndPolygon(tobj);
    }


    private void setWindingRule(int rule) {
        glu.gluTessProperty(tobj, GLU.GLU_TESS_WINDING_RULE, rule);
    }


    /**
     * Frees the OpenGL TessObj associated with this tesselator.
     */
    public void dispose() {
        glu.gluDeleteTess(tobj);
        glu = null;
    }


    public void begin(int mode) {
        visitor.begin(mode);
    }


    // GLUTesselatorCallback
    public void vertex(Object data)
    {
        if (data instanceof double[])
        {
            double[] d = (double[]) data;
            visitor.addVertex(d);
        }
    }


    public void end()
    {
        visitor.end();
    }


    public void error(int errorCode) {
        visitor.error(errorCode);
    }

    
    public void combine(double[] coords,
    					Object[] data,
    					float[] weight,
    					Object[] dataOut)
    {
    	visitor.combine(coords, data, weight, dataOut);
    }

    
    /**
     * Default implementation of the TesselatorVisitor combine method usable in
     * any implementations.
     * 
     * @param coords coordinates to combine
     * @param data associated data to combine
     * @param weight weights on the points
     * @param dataOut where the combined points will be returned
     */
    public static void defaultCombine(	double[] coords,
    									Object[] data,
    									float[] weight,
    									Object[] dataOut)
    {
		double[] vertex = new double[3];
		vertex[0] = coords[0];
		vertex[1] = coords[1];
		vertex[2] = coords[2];
		dataOut[0] = vertex;
    }


    /**
     * Default implementation of the TesselatorVisitor error method usable in
     * any implementations.
     * 
     * @param errorCode the GLU error code.
     */
    public static void defaultError(int errorCode) {
        System.err.println("Tess Error: " + errorCode);
    }
    


    /**
     * Fills a specified shape.
     * @param gl the GL context
     * @param shape the shape
     */
    public void fill(final GL gl, IShape2D shape)
    {
    	tesselate(shape, new TesselatorAdapter()
    	{
            public void begin(int mode) {
                gl.glBegin(mode);
            }
            
            public void addVertex(double[] coords) {
                gl.glVertex2dv(coords, 0);
            }
            
            public void end() {
                gl.glEnd();
            }
        });
	}
}