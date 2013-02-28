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


public final class Scene3D extends Scene2D implements IScene3D
{
	protected int maxMesh3D;				
	protected Mesh3D[] objects3D;		
	protected int nbMesh3D;				
	protected int maxLight3D;				
	protected Light3D[] lights3D;			
	protected int nbLight3D;				
	protected int maxCamera3D;			
	protected Camera3D[] cameras3D;		
	protected int nbCamera3D;		
	
	private int skyBoxMeshId;
	private Camera3D currentCamera3D;

	private int backgroundColor;
	private boolean isBackgroundEnabled;

	private int fogColor;
	private boolean isFogEnabled;


	public Scene3D()
	{
		super();
		this.setScene3DBufferSize(4096,1024,1024);	
		this.clearScene3D();
	}

	public void setScene3DBufferSize(int maxMesh3D,int maxLight3D,int maxCamera3D)
	{
		this.maxMesh3D=maxMesh3D;
		this.maxLight3D=maxLight3D;
		this.maxCamera3D=maxCamera3D;
		this.objects3D=new Mesh3D[maxMesh3D];	
		this.lights3D=new Light3D[maxLight3D];
		this.cameras3D=new Camera3D[maxCamera3D];
	}

    public int getNbScene3DObject()
	{
		int nbScene3DObject=this.nbCamera3D;
		nbScene3DObject+=this.nbLight3D;
		nbScene3DObject+=this.nbMesh3D;
		return nbScene3DObject;
		
	}

    public int getNbMesh3D()
	{
		return this.nbMesh3D;
	}

    public int getNbLight3D()
	{
		return this.nbLight3D;
	}

    public int getNbCamera3D()
	{
		return this.nbCamera3D;
	}

	public void addScene3DObject(IScene3DObject object)
	{
		if(object.getScene3D()==this)
			return;
			
		if(object instanceof Light3D)
		{
			this.addLight3D((ILight3D)object);
			return;
		}
			
		if(object instanceof Camera3D)
		{
			this.addCamera3D((ICamera3D)object);
			return;
		}
		
		if(object instanceof Mesh3D)
		{
			//System.out.println("####################################addMesh");
			this.addMesh3D((IMesh3D)object);
			return;
		}	
		
		for(IScene3DObject child=object.getFirstChild();child!=null;child=child.getNextChild())
			this.addScene3DObject(child);			

			
	}
	
	/*
	public void addSceneObject(ISceneObject object)
	{
		if(object instanceof IScene3DObject)
		{
			this.addScene3DObject((IScene3DObject)object);
			return;
		}
		super.addSceneObject(object);
	}
	*/
	
	public void addMesh3D(IMesh3D o)
	{
		if(((Mesh3D)o).scene==this) return;
		
		((Mesh3D)o).scene=this;
		
		this.objects3D[this.nbMesh3D]=(Mesh3D)o;
		((Mesh3D)o).setId(this.nbMesh3D);
		o.build();
		this.nbMesh3D++;
		for(IScene3DObject child=o.getFirstChild();child!=null;child=child.getNextChild())
			this.addScene3DObject(child);			
				
	}
	
	public void addLight3D(ILight3D l)
	{
		if(((Light3D)l).scene==this) return;
		((Light3D)l).scene=this;
		
		this.lights3D[this.nbLight3D]=(Light3D)l;
		((Light3D)l).setId(this.nbLight3D);
		l.build();
		this.nbLight3D++;
		for(IScene3DObject child=l.getFirstChild();child!=null;child=child.getNextChild())
			this.addScene3DObject(child);			
		
	}
	
	public void addCamera3D(ICamera3D l)
	{
		if(((Camera3D)l).scene==this) return;
		((Camera3D)l).scene=this;

		this.cameras3D[this.nbCamera3D]=(Camera3D)l;
		((Camera3D)l).setId(this.nbCamera3D);
		l.build();
		this.nbCamera3D++;				
		for(IScene3DObject child=l.getFirstChild();child!=null;child=child.getNextChild())
			this.addScene3DObject(child);			
		
	}	
	
	public void removeScene3DObject(IScene3DObject object)
	{
		if(object==null)
			return;
			
		if(object.getScene3D()!=this)
			return;
		
		for(IScene3DObject child=object.getFirstChild();child!=null;child=child.getNextChild())
				this.removeScene3DObject(child);	
		
		if(object instanceof IMesh3D)
		{
			this.removeMesh3DById(object.getId());
		}
		
		if(object instanceof ILight3D)
		{
			this.removeLight3DById(object.getId());
		}
		
		if(object instanceof ICamera3D)
		{
			this.removeCamera3DById(object.getId());
		}				
					
	}

	public void removeMesh3DById(int id)
	{
		IScene3DObject object=this.objects3D[id];
		if(object==null)
			return;	
			
		if(object.getScene3D()!=this)
			return;

			
		for(IScene3DObject child=object.getFirstChild();child!=null;child=child.getNextChild())
			this.removeScene3DObject(child);

		//this.objects3D[id]=null;
		
		this.nbMesh3D--;
		for(int n=object.getId();n<this.getNbMesh3D();n++)
		{
			this.objects3D[n]=this.objects3D[n+1];
			this.objects3D[n].setId(n);	
		}
		for(int x=0;x<this.getNbRemoveSceneObjectConsumer();x++)
			this.getRemoveSceneObjectConsumer(x).removeSceneObject(object);
	}	 
	
	public void removeLight3DById(int id)
	{
		IScene3DObject object=this.lights3D[id];
		if(object==null)
			return;	
			
		if(object.getScene3D()!=this)
			return;
		
			
		for(IScene3DObject child=object.getFirstChild();child!=null;child=child.getNextChild())
			this.removeScene3DObject(child);
			
		//this.lights3D[id]=null;
		this.nbLight3D--;
		for(int n=object.getId();n<this.getNbLight3D();n++)
		{
			this.lights3D[n]=this.lights3D[n+1];
			this.lights3D[n].setId(n);	
		}
		for(int x=0;x<this.getNbRemoveSceneObjectConsumer();x++)
			this.getRemoveSceneObjectConsumer(x).removeSceneObject(object);
	}	
	
	public void removeCamera3DById(int id)
	{
		IScene3DObject object=this.cameras3D[id];
		if(object==null)
			return;	
			
		if(object.getScene3D()!=this)
			return;
			
			
		for(IScene3DObject child=object.getFirstChild();child!=null;child=child.getNextChild())
			this.removeScene3DObject(child);

				
		if(this.currentCamera3D==this.cameras3D[id])
			this.currentCamera3D=null;
			
		//this.cameras3D[id]=null;
		this.nbCamera3D--;
		for(int n=object.getId();n<this.getNbCamera3D();n++)
		{
			this.cameras3D[n]=this.cameras3D[n+1];
			this.cameras3D[n].setId(n);	
		}
		for(int x=0;x<this.getNbRemoveSceneObjectConsumer();x++)
			this.getRemoveSceneObjectConsumer(x).removeSceneObject(object);		
	}	
	
	public void clearScene3D()
	{
		this.currentCamera3D=null;
		this.skyBoxMeshId=-1;
		this.backgroundColor=0xFFFFFF;
		this.isBackgroundEnabled=true;
		this.fogColor=0xFFFFFF;
		this.isFogEnabled=false;

		for(int no=0;no<this.nbMesh3D;no++)
			this.objects3D[no]=null;
		this.nbMesh3D=0;
				
		for(int nl=0;nl<this.nbLight3D;nl++)
			this.lights3D[nl]=null;
		this.nbLight3D=0;

		for(int nc=0;nc<this.nbCamera3D;nc++)
			this.cameras3D[nc]=null;
		this.nbCamera3D=0;
			
				
		
		Camera3D cam=new Camera3D();
		cam.setName("Default 3DzzD Camera3D");
		this.addCamera3D(cam);

		Light3D light=new Light3D();
		light.setName("Default 3DzzD Light3D");
		this.addLight3D(light);

		this.setCurrentCamera3DById(0);
		
	}
	 
	public IMesh3D getMesh3DById(int id)
	{
		return this.objects3D[id];
	}	
	
	public IMesh3D getMesh3DByName(String name)
	{
		for(int x=0;x<this.nbMesh3D;x++)
		{
			if(this.objects3D[x]!=null)
				if(this.objects3D[x].nom!=null)
					if(this.objects3D[x].nom.equals(name))
						return this.objects3D[x];
		}
		return null;
	}

	public ICamera3D getCamera3DById(int id)
	{
		return this.cameras3D[id];
	}	
	
	public ICamera3D getCamera3DByName(String name)
	{
		for(int x=0;x<this.nbCamera3D;x++)
		{
			if(this.cameras3D[x]!=null)
				if(this.cameras3D[x].nom!=null)
					if(this.cameras3D[x].nom.equals(name))
						return this.cameras3D[x];
		}
		return null;
	}
	
	public ILight3D getLight3DById(int id)
	{
		return this.lights3D[id];
	}	
	
	public ILight3D getLight3DByName(String name)
	{
		for(int x=0;x<this.nbCamera3D;x++)
		{
			if(this.lights3D[x]!=null)
				if(this.lights3D[x].nom!=null)
					if(this.lights3D[x].nom.equals(name))
						return this.lights3D[x];
		}
		return null;
	}
	
	public void setSkyBoxMesh3DByName(String name)
	{
		IMesh3D mesh=this.getMesh3DByName(name);
		if(mesh!=null)
			this.setSkyBoxMesh3DById(mesh.getId());
	}
	 	
	public void setSkyBoxMesh3DById(int num)
	{
		this.skyBoxMeshId=num;
	}

	public int getSkyBoxMesh3DId()
	{
		return this.skyBoxMeshId;
	}
	
	public String getSkyBoxMesh3DName()
	{
		IMesh3D m=this.getMesh3DById(this.skyBoxMeshId);
		if(m!=null)
			return m.getName();
		return null;
	}
	
	public void setCurrentCamera3DById(int cameraID)
	{
		this.currentCamera3D=this.cameras3D[cameraID];
	}		
		
	public void setCurrentCamera3DByName(String cameraName)
	{
		ICamera3D cam=this.getCamera3DByName(cameraName);
		if(cam!=null)
			this.setCurrentCamera3DById(cam.getId());
	}				
	
	public ICamera3D getCurrentCamera3D()
	{
		return this.currentCamera3D;
	}

	public void setScene3DObjectToWorld()
	{
        for(int no=0;no<this.nbMesh3D;no++)
        {
        	Scene3DObject ob=this.objects3D[no];
			if(ob==null)
				continue;
			if(!ob.isActive())
				continue;
			if(ob.parent==null)				
        		setToParentSpace(ob);
        }
        for(int no=0;no<this.nbCamera3D;no++)
        {
        	Scene3DObject ob=this.cameras3D[no];
			if(ob==null)
				continue;
			if(!ob.isActive())
				continue;
			if(ob.parent==null)
        		setToParentSpace(ob);
        }   
        for(int no=0;no<this.nbLight3D;no++)
        {
        	Scene3DObject ob=this.lights3D[no];
			if(ob==null)
				continue;
			if(!ob.isActive())
				continue;
			if(ob.parent==null)
        		setToParentSpace(ob);
        }   
    }

	public void setScene3DObjectToCamera()
	{
		this.setMesh3DToCamera();
		this.setCamera3DToCamera();
		this.setLight3DToCamera();	
	}

	public boolean isBackgroundEnabled()
	{	
		return this.isBackgroundEnabled;
	}
	
	public void setBackgroundEnabled(boolean flag)
	{	
		this.isBackgroundEnabled=flag;
	}	

	public void setBackgroundColor(int color)
	{
		this.backgroundColor=color;
	}

	public int getBackgroundColor()
	{
		return this.backgroundColor;
	}
	
	public boolean isFogEnabled()
	{	
		return this.isFogEnabled;
	}
	
	public void setFogEnabled(boolean flag)
	{	
		this.isFogEnabled=flag;
	}	

	public void setFogColor(int color)
	{
		this.fogColor=color;
	}

	public int getFogColor()
	{
		return this.fogColor;
	}	
	 				
	public void addScene3DObjects(IScene3DObject objects3D[])
	{
		for(int x=0;x<objects3D.length;x++)
			this.addScene3DObject(objects3D[x]);
	 }
	 	
	private void setToParentSpace(Scene3DObject ob)
	{
		if(ob==null)
			return;
		Axis3D axes=ob.axes;
		axes.init();
        axes.set(ob.pivot, ob.position, ob.rotation);
        if(ob.parent!=null)
        	axes.toAxis(ob.parent.axes);
		for(Scene3DObject o=ob.firstChild;o!=null;o=o.nextChild)
            setToParentSpace(o);
             
	}	
	
	private void setLight3DToCamera()
	{
		for(int nt=0;nt<this.nbLight3D;nt++)
		{
			Light3D li=this.lights3D[nt];
			if(li==null)
				continue;		
			if(!li.isActive())
				continue;	
			
			li.axes.toLocalAxis(this.currentCamera3D.axes);	
		}
	}

	private void setCamera3DToCamera()
	{
		for(int nc=0;nc<this.nbCamera3D;nc++)
		{
			Camera3D ca=this.cameras3D[nc];
			if(ca==null || ca==this.currentCamera3D)
				continue;	
			if(!ca.isActive())
				continue;	
			ca.axes.toLocalAxis(this.currentCamera3D.axes);	
		}
	}

	private void setMesh3DToCamera()
	{
		for(int no=0;no<this.nbMesh3D;no++)
		{
			Mesh3D ob=objects3D[no];
			if(ob==null)
				continue;
			if(!ob.isActive())
				continue;	
			ob.axes.toLocalAxis(this.currentCamera3D.axes);
		}		
	}

	private void playScene3DObjectAnimator(Scene3DObject so,int time)	
	{
		if(so==null)
			return;
		if(so.animator==null)
			return;
			
		if(so.animator.getStartTime()!=-1)
		{
			//System.out.print("obj " + so.getName() + "  ");
			so.animator.playAt(time);
			so.position.copy(so.animator.getPosition());
			so.rotation.copy(so.animator.getRotation());
			//System.out.println(so.position);
			if(so.parent!=null)
				so.position.add(so.parent.pivot);
		}
	}

	public void playScene3DObjectAnimator(int time)
	{
		for(int n=0;n<this.nbMesh3D;n++)
			this.playScene3DObjectAnimator(this.objects3D[n],time);
			
		for(int n=0;n<this.nbCamera3D;n++)
			this.playScene3DObjectAnimator(this.cameras3D[n],time);
		
		for(int n=0;n<this.nbLight3D;n++)
			this.playScene3DObjectAnimator(this.lights3D[n],time);
	}
		
	public void setScene3DLoader(IScene3DLoader sceneLoader)
	{
		this.startMonitorSceneObject(sceneLoader);
	}
		
	public void updateMonitoredSceneObjects()
	{
		int nbLoadingObject=this.getNbMonitoredSceneObject();
		
		if(nbLoadingObject!=0)
		{
			for(int x=0;x<this.getNbMonitoredSceneObject();x++)
			{
				IMonitoredSceneObject cLoad=this.getMonitoredSceneObject(x);
				if(cLoad.getFinished())
				{
					if(cLoad.getError()==false)
					{
						if(cLoad instanceof ISceneLoader)
							this.addSceneObjects(((ISceneLoader)cLoad).getSceneObjects());
						if(cLoad instanceof IScene3DLoader)
							this.addScene3DObjects(((IScene3DLoader)cLoad).getScene3DObjects());	
						
					}
					this.stopMonitorSceneObject(cLoad);
					
				}
			}
			try
			{
				//Log.log(getClass(),"Monitoring object:");
				//for(int x=0;x<this.getNbMonitoredSceneObject();x++)
				//	Log.log(this.getClass(),getMonitoredSceneObject(x).getName());
				Thread.sleep(1);
				Thread.yield();
			}
			catch(InterruptedException ie)
			{
			}
		}
	}

}