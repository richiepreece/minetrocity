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
import net.dzzd.utils.*;
import net.dzzd.DzzD;



public class Scene3DObject extends Scene2DObject implements IScene3DObject
{
    Axis3D axes;
    SolidSphere3D collision; 
    double sphereBox ;
    Point3D position;
    Point3D rotation;
    Point3D pivot;
    Point3D center;
    Point3D zoom;
    
    Scene3DObject parent;
    Scene3DObject firstChild;
    Scene3DObject nextChild;

	Scene3DObjectAnimator animator;
	
	private boolean isVisible;
	private boolean isActive;
	private	boolean isSolid;
	
	protected int renderMode;


    
    Scene3DObject()
    {
		super();
    	this.collision=null;
        this.axes = new Axis3D();
        this.position = new Point3D();
        this.rotation = new Point3D();
        this.pivot = new Point3D();
        this.center = new Point3D();
        this.zoom = new Point3D(1,1,1);
        this.parent = null;
		this.animator =null;
		this.isVisible=true;
		this.isActive=true;
		this.isSolid=true;
		this.renderMode=DzzD.RM_ALL;	
    }

    Scene3DObject toLocalAxe(Axis3D axe3d)
    {
    	axes.toLocalAxis(axe3d);
		Point3D o=axes.origine;		
		Point3D ax=axes.axeX;
		Point3D ay=axes.axeY;
		Point3D az=axes.axeZ;
		double ox=o.x;		
		double oy=o.y;
		double oz=o.z;
		double axx=ax.x-ox;		
		double axy=ax.y-oy;
		double axz=ax.z-oz;
		double ayx=ay.x-ox;		
		double ayy=ay.y-oy;
		double ayz=ay.z-oz;			
		double azx=az.x-ox;		
		double azy=az.y-oy;
		double azz=az.z-oz;	
		double x=this.pivot.x;
		double y=this.pivot.y;
		double z=this.pivot.z;
		double wpx=ox+axx*x+ayx*y+azx*z;
		double wpy=oy+axy*x+ayy*y+azy*z;
		double wpz=oz+axz*x+ayz*y+azz*z;
        axes.getRotationXZY(this.rotation);
        this.position.set(wpx,wpy,wpz);
        return this;
    }

    Scene3DObject translate(double d, double d1, double d2)
    {
        axes.add(d, d1, d2);
        return this;
    }
    
   	/*
	 * Interface IScene3DObject
	 */
 
    public void zoom(double x,double y,double z)
    {
     	if(this.animator!=null)
    		this.animator.zoom(x,y,z);
	
    	//System.out.println(this.nom);
    	for(Scene3DObject o=this.firstChild;o!=null;o=o.nextChild)
    	{
    	//	System.out.println(o.getName());
    		o.zoom(x,y,z);
    		o.position.sub(this.pivot).zoom(x,y,z).add(this.pivot);
    	}
    }

	public void copy(IScene3DObject source)
	{
        this.position.copy(source.getPosition());
        this.rotation.copy(source.getRotation());
        this.pivot.copy(source.getPivot());
        this.center.copy(source.getCenter());
        this.zoom.copy(source.getZoom());
        this.sphereBox=source.getSphereBox();
        
        if(source.getScene3DObjectAnimator()!=null)
			this.setScene3DObjectAnimator(source.getScene3DObjectAnimator().getClone());
		else
			this.animator=null;
		
		this.isVisible=source.isVisible();
		this.isActive=source.isActive();
		this.isSolid=source.isSolid();
		//this.setBuild(source.getBuild());
		this.axes.copy(source.getAxis3D());
		super.copy(source);
		
	}

	
	public IScene3DObject getClone(boolean childrens)
	{
		IScene3DObject s=new Scene3DObject();
		s.copy(this);
		if(childrens)
		{
			for(Scene3DObject o=this.firstChild;o!=null;o=o.nextChild)
				s.addChild(o.getClone(childrens));
		}

		return s;
	}
	
	public IScene3D getScene3D()
	{
		return (IScene3D)this.scene;
	}
	
    public IPoint3D getPosition()
    {
    	return this.position;
    }
    
    public IPoint3D getRotation()
    {
    	return this.rotation;
    }    

 	public IPoint3D getPivot()
 	{
 		return this.pivot;
 	}
 	
 	public IPoint3D getZoom()
 	{
 		return this.zoom;
 	} 	
 
    public IAxis3D getAxis3D()
    {
    	return this.axes;
    }

	public IScene3DObject getParent()
	{
		return this.parent;
	}
	
	public void setParent(IScene3DObject newParent)
	{
		//if(newParent==null)
		{
			if(this.parent!=null)
				this.parent.removeChild(this);	
			//return;
		}
		this.parent=(Scene3DObject)newParent;
		newParent.addChild(this);
	}		

	public void addChild(IScene3DObject childToAdd)
	{
		if(childToAdd==null)
			return;
		//this.removeChild(childToAdd);
		Scene3DObject lastChild=null;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
		{
			if(childToAdd==child)
				return;
		}
		//Log.log(this.getName() + " ADD CHILD " + childToAdd.getName());

		((Scene3DObject)childToAdd).nextChild=this.firstChild;
		this.firstChild=(Scene3DObject)childToAdd;
		((Scene3DObject)childToAdd).parent=this;
	}

	public void removeChild(IScene3DObject childToRemove)
	{
		//Log.log(this.getName() + " DEL CHILD " + childToRemove.getName());
		Scene3DObject childPop=(Scene3DObject)childToRemove;
		childPop.parent=null;
		Scene3DObject lastChild=null;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
		{
			if(child==childPop)
			{
				if(lastChild==null)
					this.firstChild=childPop.nextChild;
				else
					lastChild.nextChild=childPop.nextChild;
				break;
			}
			lastChild=child;
		}
	}
	
	public IScene3DObject getFirstChild()
	{
		return this.firstChild;
	}
	
	public IScene3DObject getNextChild()
	{
		return this.nextChild;
	}	
	
	public double getSphereBox()
	{
		return this.sphereBox;
	}
	
	public void setSphereBox(double radius)
	{
		this.sphereBox=radius;
	}

	public IPoint3D getCenter()
	{
		return this.center;
	}
	
	public IScene3DObjectAnimator getScene3DObjectAnimator()
	{
		return this.animator;
	}

	public void setScene3DObjectAnimator(IScene3DObjectAnimator animator)
	{
		this.animator=(Scene3DObjectAnimator)animator;
	}

	public void playScene3DObjectAnimator()
	{
		if(this.animator==null)
			return;
		this.animator.play();

		this.position.copy(this.animator.getPosition());
		this.rotation.copy(this.animator.getRotation());

		if(this.parent!=null)
			this.position.add(this.parent.pivot);
	
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.playScene3DObjectAnimator();				
	}

	public void startScene3DObjectAnimator()
	{
		if(this.animator!=null)
			this.animator.start();
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.startScene3DObjectAnimator();			
	}

	public void startScene3DObjectAnimator(long start)
	{
		if(this.animator!=null)
			this.animator.start(start);
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.startScene3DObjectAnimator(start);			
	}

	public void startScene3DObjectAnimator(long start,long end)
	{
		if(this.animator!=null)
			this.animator.start(start,end);
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.startScene3DObjectAnimator(start,end);			
	}

	public void stopScene3DObjectAnimator()
	{
		if(this.animator!=null)
			this.animator.stop();
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.stopScene3DObjectAnimator();			
	}

	public void pauseScene3DObjectAnimator()
	{
		if(this.animator!=null)
			this.animator.pause();
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.pauseScene3DObjectAnimator();			
	}

	public void resumeScene3DObjectAnimator()
	{
		if(this.animator!=null)
			this.animator.resume();
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.resumeScene3DObjectAnimator();			
	}
	
	public void loopAtScene3DObjectAnimator(long time)
	{
		if(this.animator!=null)
			this.animator.loopAt(time);
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.loopAtScene3DObjectAnimator(time);			
	}	
	
	public boolean isVisible()
	{
		return this.isVisible;
	}
	
	public boolean isActive()
	{
		return this.isActive;
	}	
	
	public boolean isSolid()
	{
		return this.isSolid;
	}		
	
	public void setVisible(boolean flag)
	{
		this.isVisible=flag;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.setVisible(flag);			
	}		
	
	public void setActive(boolean flag)
	{
		this.isActive=flag;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.setActive(flag);			
	}		
	
	public void setSolid(boolean flag)
	{
		this.isSolid=flag;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.setSolid(flag);			
	}	
	
	public IScene3DObject setAxis3DToWorld()
    {
        axes.init();
        axes.set(pivot, position, rotation);
        for(Scene3DObject scene3dobject = parent; scene3dobject != null; scene3dobject = scene3dobject.parent)
        {
//        	Bench.axeSet++;
            axes.set(scene3dobject.pivot, scene3dobject.position, scene3dobject.rotation);
        }

        return this;
    }
    		
	public IRender3DMode getRender3DMode()
	{
		return this;
	}

	public void enableRender3DMode(int flag)
	{
		this.renderMode|=flag;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.enableRender3DMode(flag);			

	}

	public void disableRender3DMode(int flag)	
	{
		this.renderMode&=(flag^DzzD.RM_ALL);
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.disableRender3DMode(flag);			
		
	}

	public void setRender3DModeFlags(int flag)
	{
		this.renderMode=flag;
		for(Scene3DObject child=this.firstChild;child!=null;child=child.nextChild)
			child.setRender3DModeFlags(flag);			
		
	}	
	
	public int getRender3DModeFlags()
	{
		return this.renderMode;
	}	
	
    public void setPoint3DToWorld(IPoint3D ipoint3d)
    {
    	Point3D point3d=(Point3D)ipoint3d;
        point3d.add(-pivot.x, -pivot.y, -pivot.z);
        
        point3d.rotateX(rotation.x);        
        point3d.rotateZ(rotation.z);
        point3d.rotateY(rotation.y);
        
        point3d.add(position.x, position.y, position.z);
        for(Scene3DObject scene3dobject = parent; scene3dobject != null; scene3dobject = scene3dobject.parent)
        {
            point3d.add(-scene3dobject.pivot.x, -scene3dobject.pivot.y, -scene3dobject.pivot.z);
            point3d.rotateX(scene3dobject.rotation.x);
            point3d.rotateZ(scene3dobject.rotation.z);
            point3d.rotateY(scene3dobject.rotation.y);            
            point3d.add(scene3dobject.position.x, scene3dobject.position.y, scene3dobject.position.z);
        }

    }			

/*
	public ISolidSphere3DResult moveAsSolidSphere(double mx,double my,double mz,double radius,int maxLoop)
	{
		if(this.collision==null)
			this.collision=new SolidSphere3D();
		this.collision.setScene3D(this.scene);
		this.collision.setRadius(this.sphereBox);

		//SET TO OBJECT SPACE
		IAxis3D a=this.getAxis3D();
		IPoint3D ax=a.getAX();
		IPoint3D ay=a.getAY();
		IPoint3D az=a.getAZ();
		IPoint3D o=a.getOrigin();	
		double ox=o.getX();
		double oy=o.getY();
		double oz=o.getZ();
		double axx=ax.getX();
		double axy=ax.getY();
		double axz=ax.getZ();
		double ayx=ay.getX();
		double ayy=ay.getY();
		double ayz=ay.getZ();
		double azx=az.getX();
		double azy=az.getY();
		double azz=az.getZ();

		double x=this.center.getX();			
		double y=this.center.getY();
		double z=this.center.getZ();
		double xSrc=ox+axx*x+ayx*y+azx*z;
		double ySrc=oy+axy*x+ayy*y+azy*z;
		double zSrc=oz+axz*x+ayz*y+azz*z;
		this.collision.setSource(xSrc,ySrc,zSrc);

		this.collision.moveSlide(mx,my,mz,maxLoop);
		double rmx=this.collision.getSource().getX()-xSrc;
		double rmy=this.collision.getSource().getY()-ySrc;
		double rmz=this.collision.getSource().getZ()-zSrc;
		this.position.x+=rmx;
		this.position.y+=rmy;
		this.position.z+=rmz;
		
		return this.collision.getResult();
	}
	*/


}