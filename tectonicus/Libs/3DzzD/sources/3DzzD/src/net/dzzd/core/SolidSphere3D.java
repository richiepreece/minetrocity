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

import net.dzzd.access.*;
import net.dzzd.utils.Log;
import net.dzzd.core.collision.*;
import net.dzzd.DzzD;

/** 
 *  Class providing many functions to perform physic collision & response.
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IScene3DObject
 *	@see IPoint3D
 */
public final class SolidSphere3D extends Scene3DObject implements ISolidSphere3D
{	
	private IScene3D scene;
	private double radius;
	private SolidSphere3DResult result;		//Result of the last collision test with a world	
	private	IPoint3D source;				//Temporary 3d point for collsions tests
	private SolidSphere3DResult meshResult;	//Result of the last collision test with a world	
	private	IPoint3D destination;			//Temporary 3d point for collsions tests
	private	SphereToEdgeImpact sie1;		//Temporary SphereToEdgeImpact object for collision stests
	private	SphereToEdgeImpact sie2;		//Temporary SphereToEdgeImpact object for collision stests
	private	SphereToEdgeImpact sie3;		//Temporary SphereToEdgeImpact object for collision stests
	
	
	/**
     * Construct a new SolidSphere3D object.
     *
	 * @param object object attache to this collision object
     */
	public SolidSphere3D()
	{
		super();
		this.radius=1;
		this.source=new Point3D();
		this.destination=new Point3D();
		this.result=new SolidSphere3DResult();
		this.meshResult=new SolidSphere3DResult();
		this.sie1=new SphereToEdgeImpact();
		this.sie2=new SphereToEdgeImpact();
		this.sie3=new SphereToEdgeImpact();		
	} 

	public final void setScene3D(IScene3D scene)
	{
		this.scene=scene;
	}

	public final void setRadius(double radius)
	{
		this.radius=radius;
	}

	public final double getRadius()
	{
		return this.radius;
	}

	public final ISolidSphere3DResult getResult()
	{
		return this.meshResult;
	}

	public final void setSource(double x,double y,double z)
	{
		this.source.set(x,y,z);
	}

	public final void setDestination(double x,double y,double z)
	{
		this.destination.set(x,y,z);
	}

	public final void setSource(IPoint3D source)
	{
		this.source=source;
	}

	public final void setDestination(IPoint3D destination)
	{
		this.destination=destination;
	}

	public final IPoint3D getSource()
	{
		return this.source;
	}

	public final IPoint3D getDestination()
	{
		return this.destination;
	}

	public final void moveSlide(double x,double y,double z,int maxLoop)
	{
		this.destination.set(x,y,z);
		this.destination.add(this.source);
		this.moveSlide(maxLoop);
	}
	
	public final ISolidSphere3DResult move()	
	{
		return this.moveSlide(1);
	}
	

	public final ISolidSphere3DResult moveSlide(int maxLoop)
	{
		return moveBounce(maxLoop,0.0);
		
	}
	
	
	
	public final ISolidSphere3DResult moveBounce(int maxLoop,double bounce)	
	{

		int numLoop=0;
	    while(numLoop<maxLoop)
		{
					
			this.sphereToWorldImpact(bounce);
			if(!meshResult.hit)
			{	
				this.source.copy(destination);
				return this.meshResult;
			}
			else
			{
				if(Double.isNaN(this.meshResult.displace.getX()))
					Log.log("errorx");
				if(Double.isNaN(this.meshResult.displace.getY()))
					Log.log("errory");
				if(Double.isNaN(this.meshResult.displace.getZ()))
					Log.log("errorz");
				source.add(meshResult.displace);
				//result.newDisplace.copy(result.slidePlane).mul(radius);
				
				//result.newDisplace.mul(result.slideDistanceOver);			
				destination.copy(this.source).add(meshResult.newDisplace);
					
					/*
										
				source.add(this.result.displace);	
				destination.copy(source);
				destination.add(this.result.newDisplace);
				*/
				
			}	
			numLoop++;
			//Log.log("disp="+this.result.newDisplace);
		}//while(this.result.newDisplace.norm()>0.01);
		return this.meshResult;	
	}

	private final void sphereToWorldImpact(double bounce)
	{
		IPoint3D s=this.source;
		IPoint3D d=this.destination;
		
		this.sx=s.getX();
		this.sy=s.getY();
		this.sz=s.getZ();
		this.dx=d.getX();
		this.dy=d.getY();
		this.dz=d.getZ();
		this.moveWorldX=dx-sx;
		this.moveWorldY=dy-sy;
		this.moveWorldZ=dz-sz;
		this.moveNorme=Math.sqrt(this.moveWorldX*this.moveWorldX+this.moveWorldY*this.moveWorldY+this.moveWorldZ*this.moveWorldZ);//a déplacer en début de fonc
		this.moveINorme=1.0/this.moveNorme;	
		this.meshResult.resetImpact();
		this.meshResult.impactDistance=this.moveNorme*this.moveNorme;
		if(this.moveNorme==0.0)
			return;
		//Log.log("\nmoveNorme="+moveNorme);
		int nb=scene.getNbMesh3D();
		for(int no=0;no<nb;no++)
			this.sphereToMeshImpact(this.scene.getMesh3DById(no),bounce);
		//if(this.meshResult.hit)
		//	System.out.println("FINAL "+this.scene.getMesh3DById(meshResult.meshId).getName()+" "+this.meshResult.impactDistance);
		//System.out.println("");
		//if(this.meshResult.impactDistance==2.4950104414815146)
		//DzzD.sleep(5000);
	}
	String name;
	
	private double sx;
	private double sy;
	private double sz;
	private double dx;
	private double dy;
	private double dz;	
	private double moveWorldX;
	private double moveWorldY;
	private double moveWorldZ;
	private double moveNorme;
	private double moveINorme;

	private final void sphereToMeshImpact(IMesh3D mesh,double bounce)
	{
		if(!mesh.isSolid())
			return;
	
		this.result.resetImpact();
		this.result.impactDistance=this.moveNorme*this.moveNorme;	
			

		//GET OBJECT LOCAL AXIS
		IAxis3D a=mesh.getAxis3D();
		IPoint3D ax=a.getAX();
		IPoint3D ay=a.getAY();
		IPoint3D az=a.getAZ();
		IPoint3D o=a.getOrigin();	
		double ox=o.getX();
		double oy=o.getY();
		double oz=o.getZ();
		double axx=ax.getX()-ox;
		double axy=ax.getY()-oy;
		double axz=ax.getZ()-oz;
		double ayx=ay.getX()-ox;
		double ayy=ay.getY()-oy;
		double ayz=ay.getZ()-oz;
		double azx=az.getX()-ox;
		double azy=az.getY()-oy;
		double azz=az.getZ()-oz;

		double x;			
		double y;
		double z;

		//SET DISPLACE VECTOR SOURCE TO OBJECT SPACE
		x=this.sx-ox;			
		y=this.sy-oy;
		z=this.sz-oz;
		double xSrc=axx*x+axy*y+axz*z;
		double ySrc=ayx*x+ayy*y+ayz*z;
		double zSrc=azx*x+azy*y+azz*z;


		//TEST IF OBJECT CAN BE HIT
		double moveSphereSize=this.radius+this.moveNorme+mesh.getSphereBox();

		IPoint3D center=mesh.getCenter();
		double dstx=center.getX()-xSrc;
		double dsty=center.getY()-ySrc;
		double dstz=center.getZ()-zSrc;
		if((dstx*dstx+dsty*dsty+dstz*dstz)>(moveSphereSize*moveSphereSize))
			return;
		
		
		
		//SET DISPLACE VECTOR DESTINATION TO OBJECT SPACE
		x=this.dx-ox;			
		y=this.dy-oy;
		z=this.dz-oz;
		double xDst=axx*x+axy*y+axz*z;
		double yDst=ayx*x+ayy*y+ayz*z;
		double zDst=azx*x+azy*y+azz*z;

		
		//TEST FACE FOR OBJECT
		boolean isHit=false;
		if(mesh.getMesh3DCollisionGenerator()!=null)
		{
			mesh.getMesh3DCollisionGenerator().generateForSolidSphere3DCollision(xSrc,ySrc,zSrc,xDst,yDst,zDst,this.radius);
			IFace3DList fList=mesh.getMesh3DCollisionGenerator().getCollisionFace3DList();
			isHit=this.sphereToFacesImpact(fList,radius,xSrc,ySrc,zSrc,xDst,yDst,zDst);
		}
		else
		{
			isHit=this.sphereToFacesImpact(mesh.getFaces3D(),radius,xSrc,ySrc,zSrc,xDst,yDst,zDst);
		}
	
		//If hit then update result
		if(isHit && result.impactDistance<meshResult.impactDistance)
		{

			x=result.sphereP.getX();
			y=result.sphereP.getY();
			z=result.sphereP.getZ();				
			double xSphereSrc=ox+axx*x+ayx*y+azx*z;
			double ySphereSrc=oy+axy*x+ayy*y+azy*z;
			double zSphereSrc=oz+axz*x+ayz*y+azz*z;
			
			x=result.planeP.getX();
			y=result.planeP.getY();
			z=result.planeP.getZ();			
			double planeIx=ox+axx*x+ayx*y+azx*z;
			double planeIy=oy+axy*x+ayy*y+azy*z;
			double planeIz=oz+axz*x+ayz*y+azz*z;
			
			x=result.slidePlane.getX();
			y=result.slidePlane.getY();
			z=result.slidePlane.getZ();			
			double slidePlaneIx=axx*x+ayx*y+azx*z;
			double slidePlaneIy=axy*x+ayy*y+azy*z;
			double slidePlaneIz=axz*x+ayz*y+azz*z;
			

 			result.sphereP.set(xSphereSrc,ySphereSrc,zSphereSrc);				//SET SPHERE INTERSECTION POINT
   			result.planeP.set(planeIx,planeIy,planeIz);			
   			result.slidePlane.set(slidePlaneIx,slidePlaneIy,slidePlaneIz);	
			result.displace.copy(result.planeP).sub(result.sphereP);
			
			result.slidePlaneOffset=-result.planeP.dot(result.slidePlane);	//SET SLIDING PLANE OFFSET
		
			/*
			result.sphereP.copy(result.slidePlane);
			result.sphereP.mul(-this.radius*1.001);
			result.sphereP.add(this.source);
			*/
			
			double over=this.destination.dot(result.slidePlane)+result.slidePlaneOffset;
			result.newDisplace.copy(result.slidePlane).mul(this.radius*1.001-over).mul(1.0+bounce).add(this.destination);
			result.newDisplace.sub(this.source).sub(result.displace);
			
			/*
			
			result.newDisplace.copy(result.slidePlane).mul(-this.radius*1.001).add(this.destination);
			
			
			result.slideDistanceOver=(result.slidePlane.dot(result.newDisplace)+result.slidePlaneOffset);
			
			
			
			
			
			result.newDisplace.copy(result.slidePlane).mul(-result.slideDistanceOver);
			result.newDisplace.add(this.destination).sub(this.source).add(result.sphereP);
			result.newDisplace.sub(result.planeP);
			
			result.meshId=mesh.getId();

			*/
						
			meshResult.copy(result);
			meshResult.meshId=mesh.getId();
			

		}
	}
	
	IPoint3D d1=DzzD.newPoint3D();
	IPoint3D d2=DzzD.newPoint3D();

	private final boolean sphereToFaceImpact(IFace3D face,double radius,double xSrc,double ySrc,double zSrc,double moveX,double moveY,double moveZ,double unitMoveX,double unitMoveY,double unitMoveZ)
	{
		boolean isHit=false;	

		//TEST IF FACE CAN BE HIT
		double moveSphereSize=this.radius+this.moveNorme+face.getSphereBox();
		IPoint3D center=face.getVertex3D2();
		double dstx=center.getX()-xSrc;
		double dsty=center.getY()-ySrc;
		double dstz=center.getZ()-zSrc;
		if((dstx*dstx+dsty*dsty+dstz*dstz)>(moveSphereSize*moveSphereSize))
			return false;

		//GET FACE PLANE EQUATION IN OBJECT SPACE
		double planeA=face.getPA();
		double planeB=face.getPB();
		double planeC=face.getPC();
		double planeD=face.getPD();
		
		//FIND THEORICAL SPHERE COLLIDE POINT AND NEW DISPLACEMENT VECTOR
		double xSphereSrc=xSrc-planeA*radius;
		double ySphereSrc=ySrc-planeB*radius;
		double zSphereSrc=zSrc-planeC*radius;
		
		double xSphereDst=xSphereSrc+moveX;
		double ySphereDst=ySphereSrc+moveY;
		double zSphereDst=zSphereSrc+moveZ;	
		
		//LOOK IF PLANE IS CROSSED
		double dstPlaneSphere=planeA*xSrc+planeB*ySrc+planeC*zSrc+planeD;
		double dstPlaneSphereSrc=planeA*xSphereSrc+planeB*ySphereSrc+planeC*zSphereSrc+planeD;
		double dstPlaneSphereDst=planeA*xSphereDst+planeB*ySphereDst+planeC*zSphereDst+planeD;
		double moveFrontPlane=planeA*unitMoveX+planeB*unitMoveY+planeC*unitMoveZ;
		
		//HANDLE SPECIAL CASE WHERE NO POSSIBLE COLLISION 
		//name=face.getMesh3D().getName();
		//Log.log(" - COMPUTE "+ name);
			
				
		//MOVE OUT OR ALONG THE PLANE ?
		if(moveFrontPlane>=0.0) return false ;
		
		//FRONT AND TOO FAR ?
		//if(dstPlaneSphereSrc>this.moveNorme) return false;
		if(dstPlaneSphereDst>0) return false;
		
		//BACK AND TOO FAR ?
		if(dstPlaneSphereSrc<=-this.radius) return false;
		
		//FIND PLANE INTERSECTION POINT
		double planeIx;
		double planeIy;
		double planeIz;	

		if(dstPlaneSphereSrc>0.0)		//SPHERE NOT SPAM IF SPHERE COLLISION POINT IS IN FRONT OF THE FACE
		{
			double dist=dstPlaneSphereSrc/(dstPlaneSphereSrc-dstPlaneSphereDst);
			planeIx=xSphereSrc+dist*moveX;
			planeIy=ySphereSrc+dist*moveY;
			planeIz=zSphereSrc+dist*moveZ;
		}
		else
		{
			planeIx=xSphereSrc-dstPlaneSphereSrc*planeA;
			planeIy=ySphereSrc-dstPlaneSphereSrc*planeB;
			planeIz=zSphereSrc-dstPlaneSphereSrc*planeC;
		}
		
		p.set(planeIx,planeIy,planeIz);
		
	//	if(false)
		if(pointInTriangle(p,face.getVertex3D0(),face.getVertex3D1(),face.getVertex3D2()))
		{
			//double	distance=dstPlaneSphereSrc;
			d1.set(planeIx,planeIy,planeIz);
			d2.set(xSphereSrc,ySphereSrc,zSphereSrc);
			double distance=d1.sub(d2).length2();
			if(dstPlaneSphereSrc<0.0)
				distance=dstPlaneSphereSrc;
				
			if(distance<=result.impactDistance)
			{		
				result.impactDistance=distance;
				result.sphereP.set(xSphereSrc,ySphereSrc,zSphereSrc);			//SET SPHERE INTERSECTION POINT
				result.planeP.set(planeIx,planeIy,planeIz);						//SET PLANE INTERSECTION POINT
				result.slidePlane.set(planeA,planeB,planeC);
				result.faceId=face.getId();
				isHit=true;
				result.hit=true; 
				return isHit;	
				
			}
			return isHit;
			//return isHit;	
		}
		
		SphereToEdgeImpact sie=null;
		
		sie1.doImpact(xSrc,ySrc,zSrc,radius,moveX,moveY,moveZ,face.getVertex3D0().getX(),face.getVertex3D0().getY(),face.getVertex3D0().getZ(),face.getVertex3D1().getX(),face.getVertex3D1().getY(),face.getVertex3D1().getZ());
		sie2.doImpact(xSrc,ySrc,zSrc,radius,moveX,moveY,moveZ,face.getVertex3D1().getX(),face.getVertex3D1().getY(),face.getVertex3D1().getZ(),face.getVertex3D2().getX(),face.getVertex3D2().getY(),face.getVertex3D2().getZ());
		sie3.doImpact(xSrc,ySrc,zSrc,radius,moveX,moveY,moveZ,face.getVertex3D2().getX(),face.getVertex3D2().getY(),face.getVertex3D2().getZ(),face.getVertex3D0().getX(),face.getVertex3D0().getY(),face.getVertex3D0().getZ());
		if(sie1.hit)//FAUX A REVOIR ==>  hitTime != collision time == pos on edge
			sie=sie1;   
		if(sie2.hit && (sie==null || sie2.hitTime<sie.hitTime))
			sie=sie2;   						
		if(sie3.hit && (sie==null || sie3.hitTime<sie.hitTime))
			sie=sie3;

		if(sie !=null && sie.hit && sie.hitTime<=1.0)
		{		
						
			double dx=(sie.hitSphereX-sie.hitX);
			double dy=(sie.hitSphereY-sie.hitY);
			double dz=(sie.hitSphereZ-sie.hitZ);
			double d=Math.sqrt(dx*dx+dy*dy+dz*dz);
			double id=1.0/d;
			//dx*=id;
			//dy*=id;
			//dz*=id;			
			//double dw=-(dx*sie.hitX+dy*sie.hitY+dz*sie.hitZ);
			if(dx*unitMoveX+dy*unitMoveY+dz*unitMoveZ>=0)
			{
			
				return false;
			}
			
			xSphereSrc=xSrc-dx*radius*id;
			ySphereSrc=ySrc-dy*radius*id;
			zSphereSrc=zSrc-dz*radius*id;
			
			planeIx=sie.hitX;
			planeIy=sie.hitY;
			planeIz=sie.hitZ;  	
			
			d1.set(planeIx,planeIy,planeIz);
			d2.set(xSphereSrc,ySphereSrc,zSphereSrc);
			double distance=d1.sub(d2).length2();
			
			
			if(sie.hitTime==0.0)
			{
				distance=-distance;//Math.sqrt(dx*dx+dy*dy+dz*dz)-radius;
			}
			
			
			//Log.log("******edge of " +" d="+ distance + " ("+ face.getMesh3D().getName() + ")");

			if(distance<=result.impactDistance )
			{
				result.impactDistance=distance;
					
				result.sphereP.set(xSphereSrc,ySphereSrc,zSphereSrc);		//SET SPHERE INTERSECTION POINT
				result.planeP.set(planeIx,planeIy,planeIz);					//SET PLANE INTERSECTION POINT
				result.slidePlane.set(dx*id,dy*id,dz*id);
				result.faceId=face.getId();
				isHit=true;
				result.hit=true; 	   						
			}   		   						
		}		

		return isHit;
	}

	private final boolean sphereToFacesImpact(IFace3DList faces,double radius,double xSrc,double ySrc,double zSrc,double xDst,double yDst,double zDst)
	{

		boolean isHit=false;
		int nl=0;
		double moveX=xDst-xSrc;
		double moveY=yDst-ySrc;
		double moveZ=zDst-zSrc;
		
		double unitMoveX=moveX*this.moveINorme;
		double unitMoveY=moveY*this.moveINorme;
		double unitMoveZ=moveZ*this.moveINorme;

		IFace3DList current=faces;
		
		while(current!=null)
		{
			if(sphereToFaceImpact(current.getFace3D(),this.radius,xSrc,ySrc,zSrc,moveX,moveY,moveZ,unitMoveX,unitMoveY,unitMoveZ))
				isHit=true;
			current=current.getNextFace3DList();
		}	
		return isHit;

	}
	
	private final boolean sphereToFacesImpact(IFace3D faces[],double radius,double xSrc,double ySrc,double zSrc,double xDst,double yDst,double zDst)
	{
		boolean isHit=false;
		
		double moveX=xDst-xSrc;
		double moveY=yDst-ySrc;
		double moveZ=zDst-zSrc;
		
		double unitMoveX=moveX*this.moveINorme;
		double unitMoveY=moveY*this.moveINorme;
		double unitMoveZ=moveZ*this.moveINorme;

		IFace3D face;
		int nbf=faces.length;
	
		for(int nf=0;nf<nbf;nf++)
		{
			//GET NEXT FACE
			face=faces[nf];
			if(sphereToFaceImpact(face,this.radius,xSrc,ySrc,zSrc,moveX,moveY,moveZ,unitMoveX,unitMoveY,unitMoveZ))
				isHit=true;
		
		}	
		return isHit;
	}


	private IPoint3D p=DzzD.newPoint3D();
	/*
	private IPoint3D cp1=DzzD.newPoint3D();
	private IPoint3D p1a=DzzD.newPoint3D();
	private IPoint3D cp2=DzzD.newPoint3D();
	private IPoint3D p2a=DzzD.newPoint3D();

	private final boolean sameSide(IPoint3D p1,IPoint3D p2,IPoint3D a,IPoint3D b)
{
	cp1.copy(b).sub(a);
	p1a.copy(p1).sub(a);
    cp1.cross(p1a);
    
	cp2.copy(b).sub(a);
	p2a.copy(p2).sub(a);
    cp2.cross(p2a);    
    
    //cp1 = CrossProduct(b-a, p1-a);
    //cp2 = CrossProduct(b-a, p2-a);
    if(cp1.dot(cp2) >= 0.0) 
    	return true;
    return false;
}

	private final boolean pointInTriangle(IPoint3D p,IPoint3D a,IPoint3D b,IPoint3D c)
{
    if(sameSide(p,a,b,c) && sameSide(p,b,a,c) && sameSide(p,c,a,b))
    	return true;
    return false;
}*/



	private IPoint3D v0=DzzD.newPoint3D();
	private IPoint3D v1=DzzD.newPoint3D();
	private IPoint3D v2=DzzD.newPoint3D();


private final boolean pointInTriangle(IPoint3D p,IPoint3D a,IPoint3D b,IPoint3D c)
{
	v0.copy(c);
	v1.copy(b);
	v2.copy(p);
	
	v0.sub(a);
	v1.sub(a);
	v2.sub(a);
	
	// Compute dot products
	double dot00 = v0.dot(v0);
	double dot01 = v0.dot(v1);
	double dot02 = v0.dot(v2); 
	double dot11 = v1.dot(v1);
	double dot12 = v1.dot(v2);

	
	// Compute barycentric coordinates
	double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
	double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
	double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
	
	// Check if point is in triangle
	return (u > 0) && (v > 0) && (u + v < 1);
}

}
