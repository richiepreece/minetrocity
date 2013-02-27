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

public class Scene3DObjectAnimator implements IScene3DObjectAnimator
{
	class KeyAnim
	{
		private int keyTime;
		private KeyAnim nextKey;
		
		KeyAnim(int keyTime)
		{
			this.keyTime=keyTime;
			this.nextKey=null;
		}
		
		public int getTime()
		{
			return this.keyTime;
		}

		public KeyAnim getNextKey()
		{
			return this.nextKey;
		}

		public void setNextKey(KeyAnim next)
		{
			this.nextKey=next;
		}				
	}
	
	class KeyRotation extends KeyAnim
	{
		
		private Point3D rotation;
		private Point3D axis;
		private double angle;
		
		KeyRotation(int keyTime,Point3D rotation,Point3D axis,double angle)	
		{
			super(keyTime);			
			this.rotation=rotation;
			this.axis=axis;	
			this.angle=angle;
		}
		
		public Point3D getRotation()
		{
			return this.rotation;
		}

		public Point3D getAxis()
		{
			return this.axis;
		}

		public double getAngle()
		{
			return angle;
		}
		
	}

	class KeyPosition extends KeyAnim
	{
		private Point3D position;
		
		KeyPosition(int keyTime,Point3D position)	
		{
			super(keyTime);		
			this.position=position;	
		}
		
		public Point3D getPosition()
		{
			return this.position;
		}
				
	}

	class KeyZoom extends KeyAnim
	{
		private Point3D zoomStart;
		private Point3D zoom;
		
		KeyZoom(int keyTime,Point3D zoomStart,Point3D zoom)	
		{
			super(keyTime);	
			this.zoomStart=zoomStart;	
			this.zoom=zoom;	
		}
		
		public Point3D getZoomStart()
		{
			return this.zoom;
		}

		public Point3D getZoom()
		{
			return this.zoom;
		}			
		
	}

	private Axis3D axis3D;
	private Point3D position;
	private Point3D rotation;
	private KeyPosition firstKeyPosition;
	private int nbKeyPosition;
	private KeyRotation firstKeyRotation;
	private int nbKeyRotation;
	private KeyZoom firstKeyZoom;
	private int nbKeyZoom;

	private ITimer timer;					//ITimer to use
	private long startTime;					//Start time in ms (-1 if none)
	private long endTime;					//End time in ms (-1 if none)
	private long loopAt;					//Looping time in ms (-1 if none)
	private long pauseTime;					//Pause time in ms (-1 if none)

	public Scene3DObjectAnimator()
	{
        this.position = new Point3D();
        this.rotation = new Point3D();
		this.axis3D=new Axis3D();
		this.firstKeyPosition=null;
		this.nbKeyPosition=0;
		this.firstKeyRotation=null;
		this.nbKeyRotation=0;
		this.firstKeyZoom=null;
		this.nbKeyZoom=0;

		this.loopAt=-1;
		this.startTime=-1;
		this.endTime=-1;
		this.pauseTime=-1;
		this.timer=new Timer();	
	}


	public ITimer getTimer()
	{
	 	return this.timer;
	}	
 
	public void setTimer(ITimer t)
	{
	 	this.timer=t;
	}	 

	public long getStartTime()
	{
		return this.startTime;
	}
	
	public long getPauseTime()
	{
		return this.pauseTime;
	}

	public void loopAt(long loopAt)
	{
		this.loopAt=loopAt;
	}

	public void pause()
	{
		if(this.pauseTime!=-1)
			return;
		this.pauseTime=this.timer.getTime()-this.startTime;
		this.playAt((int)this.pauseTime);
	}

	public void resume()
	{
	 	if(this.pauseTime==-1)
			return;
	 	this.startTime=this.timer.getTime()-this.pauseTime;
	 	this.pauseTime=0;
	}

	public void start()
	{
		this.pauseTime=-1;
		this.endTime=-1;
		this.startTime=this.timer.getTime();
	}

	public void start(long start)
	{
		this.pauseTime=-1;
		this.endTime=-1;
		this.startTime=this.timer.getTime()-start;
	}

	public void start(long start,long end)
	{
		this.pauseTime=-1;
		this.endTime=end;
		this.startTime=this.timer.getTime()-start;
	}

	public void play()
	{	
		if(this.startTime==-1)
		 return;
		long time=this.timer.getTime()-this.startTime;

		if(this.loopAt!=-1)
			time%=this.loopAt;
		if(this.pauseTime!=-1)
			time=this.pauseTime;
		if((this.endTime!=-1) && (time>this.endTime))
			time=this.endTime;

		this.playAt((int)time);
	}

	public void stop()
	{	
		this.startTime=-1;
		this.pauseTime=-1;
	}
	
	public int addKey(KeyAnim firstKeyAnim,KeyAnim nKey)
	{
		if(firstKeyAnim==null)
		{
			firstKeyAnim=nKey;
			return 0;
		}
		KeyAnim last=null;
		KeyAnim current=firstKeyAnim;
		int nk=0;
		while(current!=null)
		{
			if(current.getTime()>nKey.getTime())
				break;
			last=current;
			current=current.getNextKey();
			nk++;
		}
		if(last!=null)
			last.setNextKey(nKey);
		nKey.setNextKey(current);
		return nk;				
	}

	public int addKeyPosition(int keyTime,IPoint3D position)
	{
		KeyPosition nKey=new KeyPosition(keyTime,(Point3D)position);
		this.nbKeyPosition++;
		int nKeyNum=this.addKey(this.firstKeyPosition,nKey);
		if(nKeyNum==0)
			this.firstKeyPosition=nKey;
		return nKeyNum;			
	}

	public int addKeyRotation(int keyTime,IPoint3D rotation,IPoint3D axis,double angle)
	{
		KeyRotation nKey=new KeyRotation(keyTime,(Point3D)rotation,(Point3D)axis,angle);
		this.nbKeyRotation++;
		int nKeyNum=this.addKey(this.firstKeyRotation,nKey);
		if(nKeyNum==0)
			this.firstKeyRotation=nKey;
		return nKeyNum;			
	}
	
	public void addKeyZoom(int keyTime,IPoint3D zoomStart,IPoint3D zoom)
	{
	}
	
	public KeyAnim getKeyAt(KeyAnim firstKeyAnim,int time)
	{
		if(firstKeyAnim==null)
			return null;

		KeyAnim last=null;
		KeyAnim current=firstKeyAnim;
		int n=0;
		while(current!=null)
		{
			n++;
			if(current.getTime()>time)
				break;
			last=current;
			current=current.getNextKey();
		}
		return last;		
	}
	
	public void playRotationAt(int time)
	{		
		KeyAnim current=this.getKeyAt(this.firstKeyRotation,time);
		if(current==null)
		{
			this.rotation.set(0,0,0);
			return;
		}
		
		
		KeyAnim next=current.getNextKey();
		KeyRotation key=(KeyRotation)current;

		if(next==null)
		{
			this.rotation.copy(key.getRotation());
			return;
		}

		KeyRotation keyNext=(KeyRotation)next;
		double denomTime=next.getTime()-current.getTime();
		double numTime=time-current.getTime();
		double ratio=1.0-(numTime/denomTime);	
		
		Point3D cRotation=keyNext.getRotation();
		Point3D axis=keyNext.getAxis();
		double angle=keyNext.getAngle()*ratio;

		this.axis3D.init();	
		
		this.axis3D.rotateX(cRotation.x);		
		this.axis3D.rotateZ(cRotation.z);
		this.axis3D.rotateY(cRotation.y);		
		this.axis3D.rotate(-angle,axis.x,axis.y,axis.z);

		this.axis3D.getRotationXZY(this.rotation);
	}
	
	public void playPositionAt(int time)
	{
		KeyAnim current=this.getKeyAt(this.firstKeyPosition,time);
		if(current==null)
		{
			this.position.set(0,0,0);
			return;
		}
		
		KeyPosition key=(KeyPosition)current;
		this.position.copy(key.getPosition());

		KeyAnim next=current.getNextKey();
		if(next==null)
			return;
			

		KeyPosition keyNext=(KeyPosition)next;
		IPoint3D posNext=keyNext.getPosition();
		double denomTime=next.getTime()-current.getTime();
		double numTime=time-current.getTime();
		double ratio=numTime/denomTime;
		
		
		double dx=posNext.getX()-this.position.getX();
		double dy=posNext.getY()-this.position.getY();
		double dz=posNext.getZ()-this.position.getZ();
		this.position.add(dx*ratio,dy*ratio,dz*ratio);	
		
	}	
	
	public void playAt(int time)
	{
		this.playRotationAt(time);								
		this.playPositionAt(time);
	}
	
	public void zoom(double x,double y,double z)
	{
		
		for(KeyPosition kp=this.firstKeyPosition;kp!=null;kp=(KeyPosition)kp.getNextKey())
			kp.getPosition().zoom(x,y,z);
	}
	
	public IScene3DObjectAnimator getClone()
	{
		Scene3DObjectAnimator s=new Scene3DObjectAnimator();
		s.copy(this);
		return s;
		
	}
	
	
	public IPoint3D getPosition()
	{
		return this.position;
	}
	
	public IPoint3D getRotation()
	{
		return this.rotation;
	}	
	
	
	public void copy(IScene3DObjectAnimator sourceAnimator)
	{
		Scene3DObjectAnimator source=(Scene3DObjectAnimator)sourceAnimator;
		
		KeyPosition kp=source.firstKeyPosition;
		while(kp!=null)
		{
			this.addKeyPosition(kp.getTime(),kp.getPosition().getClone());
			kp=(KeyPosition)kp.getNextKey();
		}
		
		KeyRotation kr=source.firstKeyRotation;
		while(kr!=null)
		{
			this.addKeyRotation(kr.getTime(),kr.getRotation().getClone(),kr.getAxis().getClone(),kr.getAngle());
			kr=(KeyRotation)kr.getNextKey();
		}
		
		KeyZoom kz=source.firstKeyZoom;
		while(kz!=null)
		{
			this.addKeyZoom(kz.getTime(),kz.getZoomStart().getClone(),kz.getZoom().getClone());
			kz=(KeyZoom)kz.getNextKey();
		}		
		this.position.copy(source.getPosition());
		this.rotation.copy(source.getRotation());
		this.timer=source.getTimer();			
		this.startTime=source.getStartTime();			
		this.endTime=source.endTime;				
		this.loopAt=source.loopAt;				
		this.pauseTime=source.getPauseTime();
		
	}
	

}