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


public class Track3D extends Point3D implements ITrack3D
{
	private int defaultInterpolation;		//Default interpolation between key 0=none,1=linear,2=cosin,3=cubic
	private int nbKey;						//Nb of keys for this animation
	private long startTime;					//Start time in ms (-1 if none)
	private long loopTime;					//Looping time in ms (-1 if none)
	private long pauseTime;					//Pause time in ms (-1 if none)
	private ITimer timer;					//ITimer to use 
	private Track3DKey firstKey;			//Keys stack (first key)
	
		
	/**
	 * Create a new Track3D using its own internal ITimer	
	 */
	public Track3D()
	{
		this.defaultInterpolation=3;
		this.nbKey=0;
		this.loopTime=-1;
		this.startTime=-1;
		this.pauseTime=-1;
		this.firstKey=null;
		this.timer=new Timer();		
	}

	public int getNbKey()
	{
		return this.nbKey;
	}


	/**
	 * Return the starting time of this animation
	 *
	 * @return the starting time in ms of this animation
	 */
	public long getStartTime()
	{
		return this.startTime;
	}
	
	/**
	 * Return the pause time for this animation or 0 if not paused
	 *
	 * @return pause offset time
	 */
	public long getPauseTime()
	{
		return this.pauseTime;
	}
	
	/**
	 * Set the default interpolatin mode
	 *
	 * @param i default interpolation between key 0=none,1=linear,2=cosin,3=cubic
	 */
	public void setDefaultInterpolation(int i)
	 {
	 	this.defaultInterpolation=i;
	 }	 
	 
	/**
	 * Set timer to use with this interpolator
	 *
	 * @param t timer to use
	 */
	public void setTimer(ITimer t)
	 {
	 	this.timer=t;
	 }	 
	
	/**
	 * Set the loop time for this animation
	 *
	 * @param loopTime offset time for loop
	 */
	public void setLoop(long loopTime)
	{
		this.loopTime=loopTime;
	}
	
	/**
	 * Pause this animation
	 */
	public void pause()
	{
		if(this.pauseTime!=-1)
			return;
		this.pauseTime=this.timer.getTime()-this.startTime;
		this.playAt(this.pauseTime);
	}
	
	/** 
	 * Resume this animation at the last pauseTime (startTime is updated)
	 */
	public void resume()
	 {
	 	if(this.pauseTime==-1)
			return;
	 	this.startTime=this.timer.getTime()-this.pauseTime;
	 	this.pauseTime=0;
	 }

	/**
	 * Create a a new static key and add it to this animation
	 * 
	 * @param x x value for this new key	
	 * @param y z value for this new key	
	 * @param z z value for this new key	
	 * @param time time ofset for this new key	
	 * 
	 * @return index of the newly created key
	 */
	public int addKey(double x,double y,double z,long time)
	{
		return addKey(x,y,z,time,this.defaultInterpolation);
	}
	
	/**
	 * Create a a new static key and add it to this animation
	 * 
	 * @param x x value for this new key	
	 * @param y z value for this new key	
	 * @param z z value for this new key	
	 * @param time time ofset for this new key
	 * @param interpolation type of interpolation 0=none,1=linear,2=cosin,3=bicubic		
	 * 
	 * @return index of the newly created key
	 */
	public int addKey(double x,double y,double z,long time,int interpolation)
	{
		Track3DStaticKey nKey=new Track3DStaticKey();
		nKey.time=time;
		nKey.x=x;
		nKey.y=y;
		nKey.z=z;
		nKey.interpolation=interpolation;
		return this.addKey(nKey);
		
	}
	
	/**
	 * Create a a new linked key and add it to this animation
	 * 
	 * @param x x value for this new key	
	 * @param y z value for this new key	
	 * @param z z value for this new key	
	 * @param time time ofset for this new key	
	 * 
	 * @return index of the newly created key
	 */
	public int addKey(IPoint3D source,long time)
	{
		return addKey(source,time,this.defaultInterpolation);
	}

	/**
	 * Create a a new linked key and add it to this animation
	 * 
	 * @param source source for this linked key	
	 * @param time time offset for this new key
	 * @param interpolation type of interpolation 0=none,1=linear,2=cosin,3=bicubic		
	 * 
	 * @return index of the newly created key
	 */	
	public int addKey(IPoint3D source,long time,int interpolation)
	{
		Track3DLinkedKey nKey=new Track3DLinkedKey();
		nKey.setSource(source);
		nKey.time=time;
		nKey.interpolation=interpolation;
		return this.addKey(nKey);	
	}
	
	/**
	 * Remove a key from this animation
	 *
	 * @param n index of the key to remove from this animation
	 */	
	public void removeKey(int n)
	{
		if(n>=this.nbKey)
			return;
			
		if(this.firstKey==null)
			return;		

		Track3DKey last=null;
		Track3DKey current=this.firstKey;
		
		int nk=0;
		while(current!=null)
		{
			if(nk==n)
				break;
				
			last=current;
			current=current.nextKey;
			nk++;
		}
		
		if(last==null)
			this.firstKey=this.firstKey.nextKey;
		else
			last.nextKey=current.nextKey;
		this.nbKey--;
		return;
	}	
	
	/**
	 * Start or restart this animation
	 */
	public void start()
	{
		this.pauseTime=-1;
		this.startTime=this.timer.getTime();
	}
	
	/**
	 * Start or restart this animation
	 */
	public void start(long startTime)
	{
		this.pauseTime=-1;
		this.startTime=this.timer.getTime()+startTime;
	}	
	
	/**
	 * Compute and update the internal(x,y,z) value for the time given by the ITimer (internal or external)
	 *
	 * @param time time to compute x,y,z
	 *
	 */ 
	public void play()
	{	
		if(this.startTime==-1)
		 return;
		long time=this.timer.getTime()-this.startTime;
		if(this.pauseTime!=-1)
			time=this.pauseTime;
		this.playAt(time);
	}
	
	/**
	 * Stop this animator
	 */ 
	public void stop()
	{	
		this.startTime=-1;
		this.pauseTime=-1;
	}	
	
	/**
	 * Compute and update the internal(x,y,z) value for the given time
	 *
	 * @param time time to compute x,y,z
	 *
	 */ 
	public void playAt(long time)
	{

		if(this.loopTime!=-1)
			time%=this.loopTime;
		
		if(this.firstKey==null)
		{
			this.x=0;
			this.y=0;
			this.z=0;	
			return;
		}

		Track3DKey lastLast=null;
		Track3DKey last=null;
		Track3DKey current=this.firstKey;
		
		while(current!=null)
		{
			if(current.time>time)
				break;
			lastLast=last;
			last=current;
			current=current.nextKey;
		}

		if(last==null)
		{
				this.x=current.getX();
				this.y=current.getY();
				this.z=current.getZ();	
				return;		
		}
		
		if(current==null)
		{
				this.x=last.getX();
				this.y=last.getY();
				this.z=last.getZ();	
				return;		
		}
		
		double denomTime=current.time-last.time;
		double numTime=time-last.time;
		double ratioTime=numTime/denomTime;

		switch(current.interpolation)
		{
			case 0:	//None
				this.x=current.getX();
				this.y=current.getY();
				this.z=current.getZ();
			break;
			
			case 1:	//Linear
				this.x=last.getX()*(1-ratioTime)+current.getX()*ratioTime;
				this.y=last.getY()*(1-ratioTime)+current.getY()*ratioTime;
				this.z=last.getZ()*(1-ratioTime)+current.getZ()*ratioTime;
			break;
			
			case 2:	//Cosin
				double ft=ratioTime*Math.PI;
				double f=(1 - Math.cos(ft)) * 0.5;
				this.x=last.getX()*(1-f)+current.getX()*f;
				this.y=last.getY()*(1-f)+current.getY()*f;
				this.z=last.getZ()*(1-f)+current.getZ()*f;			
			break;

			case 3:	//Cubic
				double t=ratioTime;
				double t2=t*t;
				double t3=t2*t;
				
				double v0x=0;
				double v1x=0;
				double v2x=0;
				double v3x=0;
				double v0y=0;
				double v1y=0;
				double v2y=0;
				double v3y=0;
				double v0z=0;
				double v1z=0;
				double v2z=0;
				double v3z=0;	
					
				v0x=v1x=last.getX();
				v0y=v1y=last.getY();
				v0z=v1z=last.getZ();
				v3x=v2x=current.getX();
				v3y=v2y=current.getY();
				v3z=v2z=current.getZ();		
				
				if(lastLast!=null)
				{
					v0x=lastLast.getX();
					v0y=lastLast.getY();
					v0z=lastLast.getZ();
				}
				
				if(current.nextKey!=null)
				{
					v3x=current.nextKey.getX();
					v3y=current.nextKey.getY();
					v3z=current.nextKey.getZ();
				}
					
				double Px = (v3x - v2x) - (v0x - v1x);
				double Qx = (v0x - v1x) - Px;
				double Rx = v2x - v0x;
				double Sx = v1x;
			
				double Py = (v3y - v2y) - (v0y - v1y);
				double Qy = (v0y - v1y) - Py;
				double Ry = v2y - v0y;
				double Sy = v1y;
			
				double Pz = (v3z - v2z) - (v0z - v1z);
				double Qz = (v0z - v1z) - Pz;
				double Rz = v2z - v0z;
				double Sz = v1z;
				
				
				this.x=Px*t3 + Qx*t2 + Rx*t + Sx;
				this.y=Py*t3 + Qy*t2 + Ry*t + Sy;
				this.z=Pz*t3 + Qz*t2 + Rz*t + Sz;			
			break;
		}
			
		
	}
	
	/**
	 * Create a string representation of this animation
	 *
	 * @return a string that representing this animation
	 */
	public String toString()
	{
		String s="";
		Track3DKey current=this.firstKey;
		int n=0;
		while(current!=null)
		{
			s+="Key " + n + " : " + current.getX() + "," + current.getY() + "," + current.getZ() + ") at " + current.time + "\r\n";
			current=current.nextKey;
			n++;
		}	
		return s;
	}

	/**
	 * Add a new key to this animation
	 * 
	 * @param nKey key to add 
	 * 
	 * @return index of the newly created key
	 */		
	private int addKey(Track3DKey nKey)
	{
				
		if(this.firstKey==null)
		{
			this.firstKey=nKey;
			this.nbKey++;
			return 0;
		}
		
		Track3DKey last=null;
		Track3DKey current=this.firstKey;
		
		int nk=0;
		while(current!=null)
		{
			if(current.time>nKey.time)
				break;
				
			last=current;
			current=current.nextKey;
			nk++;
		}
		
		if(last==null)
			this.firstKey=nKey;
		else
			last.nextKey=nKey;
		nKey.nextKey=current;
		this.nbKey++;
		return nk;
		
		
	}

	/**
	 * Class used to store animation key
	 */
	private abstract class Track3DKey extends Point3D
	{
		protected Track3DKey nextKey;
		protected long time;
		protected int interpolation;
	}
	
	/**
	 * Class used to store fixed animation key
	 */
	private class Track3DStaticKey extends Track3DKey
	{
		Track3DStaticKey()
		{
			this.nextKey=null;
			this.time=0;
			this.x=0;
			this.y=0;
			this.z=0;			
			this.interpolation=0;
		}
	}
	
	/**
	 * Class used to store linked animation key
	 */
	private class Track3DLinkedKey extends Track3DKey
	{
		private IPoint3D source;

		Track3DLinkedKey()
		{
			this.nextKey=null;
			this.time=0;
			this.interpolation=0;
		}
		
		/**
		 * Set the source for this linked key
		 *
		 * @param s s the source
		 */	
		public void setSource(IPoint3D s)
		{
			this.source=s;
		}
		
		/**
		 * @return the x value
		 */	
		public double getX()
		{
			return this.source.getX();
		}
	
		/**
		 * @return the y value 
		 */	
		public double getY()
		{
			return this.source.getY();
		}
	
		/**
		 * @return the z value 
		 */	
		public double getZ()
		{
			return this.source.getZ();
		}
		
	}
	
	/**
	 * Default ITimer implementation in case of there is no other ITimer provided
	 *
	 * @return return the time value in ms 
	 */
	public long getTime()
	{
		return System.currentTimeMillis();	
	}

		
}