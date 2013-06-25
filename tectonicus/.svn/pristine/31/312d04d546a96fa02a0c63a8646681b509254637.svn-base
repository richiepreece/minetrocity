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
import net.dzzd.*;

import java.awt.Container;

public final class Scene3DRender implements IScene3DRender,Runnable
{
    private ITimer iTimer;	//Internal timer
        
	private long startTime;
    private boolean runProcess;
	private boolean pauseProcess;
    private Thread process;
	
    private int numLoop;
	private int maxFPS100;
	private long fpsBuffer[];
	private String renderSwitchName;

	private boolean autoPlayAnimator;
	
	private AsyncSwitchRender render3DSwitcher;

	private IScene3DRenderCallBack iScene3DRenderCallBack;

	private IRender3D renderSwitch;
	private IRender3D render;
	private IScene3D scene;
	

	public Scene3DRender()
	{
		this.renderSwitchName=null;
		this.iTimer=DzzD.newTimer();

		this.fpsBuffer=new long[50];
		this.maxFPS100=4000;

		this.runProcess=false;
		this.pauseProcess=false;
		this.process=null;
		this.numLoop=0;

		this.render=DzzD.newRender3D(getClass(),"SOFT",null);
		this.scene=DzzD.newScene3D();
		this.iScene3DRenderCallBack=new Scene3DRenderCallBack();

		this.autoPlayAnimator=false;
	}
	
	public void setScene3D(IScene3D s)
	{
		this.scene=s;
	}
	
	public void switchRender3D(String render3DName)
	{
		if(this.render3DSwitcher!=null)
			return;
		this.render3DSwitcher=new AsyncSwitchRender(render3DName);
		Thread t=new Thread(this.render3DSwitcher);
		t.start();
	}
	
	private class AsyncSwitchRender extends ProgressListener implements Runnable,IProgressListener
	{
		IRender3D render;
		String render3DName;
		
		AsyncSwitchRender(String render3DName)
		{
			this.render3DName=render3DName;
			this.render=null;
			this.setProgress(0);
			this.setFinished(false);
			this.setError(false);
		}
		
		public IRender3D getRender()
		{
			return this.render;
		}
		
		public void run()
		{
			this.render=DzzD.newRender3D(getClass(),this.render3DName,null);
			if(this.render==null)
			{
				this.setError(true);
				this.setProgress(100);
				this.setFinished(true);
				return;
			}
			this.setError(false);
			this.setProgress(100);
			this.setFinished(true);			
		}
	}
	
	public long getFrameTime()
	{
		return 0;
	}
	
	public long getTime()
	{
		return	this.iTimer.getTime();
	}

	public IScene3D getScene3D()
	{
		return this.scene;
	}

	public IRender3D getRender3D()
	{
		return this.render;
	}
	
	public void setScene3DRenderCallBack(IScene3DRenderCallBack handler)
	{
		this.iScene3DRenderCallBack=handler;
		if(this.iScene3DRenderCallBack!=null)
			this.iScene3DRenderCallBack.render3DstartCallBack(this);		
	}

  	public void start() 
    {	
       if (this.process == null) 
       {
        	this.process = new Thread(this);				//Create main thread
        	try
        	{
        		this.process.setPriority(Thread.MAX_PRIORITY);	//Set thread priority          
        	}
        	catch(Throwable t)
        	{
        		//Log.log(this.getClass(),t);
        		t.printStackTrace();
        	}
        	this.runProcess=true;  							//Set running flag
			this.process.start();							//Start main thread
       }
       if (this.process != null) 
	   {       
       	this.runProcess=true;								//Set running flag   
	    this.pauseProcess=false;
	   }
    }

	public void pause()
	{
		this.pauseProcess=true;
	}	

    public void stop() 
    {
		this.pauseProcess=false; 	
		this.runProcess=false; 	
		if(this.process != null) 
		{    	
			try
			{
				
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException ie)
				{
					Log.log(ie);
				}
				if(this.process != null) 
					this.process.join();
				
			}
			catch(InterruptedException ie)
			{
				Log.log(ie);
			}
			catch(NullPointerException npe)
			{
				Log.log(npe);
			}
			
			this.process=null;
		}
     }

	public void setMaxFPS100(int maxFPS)
	{
		this.maxFPS100=maxFPS;
	}

	public int getFPS100()
	{
		if(this.numLoop==0)
			return 0;

		if(this.numLoop<this.fpsBuffer.length)
		{
			long t0=this.fpsBuffer[0];
			if((this.fpsBuffer[this.numLoop]-t0)==0)
				return 0;
			return (int)((100000L*this.numLoop)/(this.fpsBuffer[this.numLoop]-t0));
		}
		
		long t0=this.fpsBuffer[(this.numLoop+1)%this.fpsBuffer.length];
		if((this.iTimer.getTime()-t0)==0)
				return 0;
		return (int)((100000L*(this.fpsBuffer.length-1))/(this.fpsBuffer[(this.numLoop)%this.fpsBuffer.length]-t0));
		
	}	
	
	public void run() 
    {
    	Log.log("Start Scene3DRender: "+Thread.currentThread());
		long currentTime=0;
		long lastCurrentTime=0;
		long lastCurrentFrameTimeMs=0;
    	int overEatCPU=0;

		this.numLoop=0;
		this.startTime=this.iTimer.getTime();

		//Log.log(this,"startTime="+this.startTime);
		try
		{
	    	while(this.runProcess)
	    	{
				if(!this.pauseProcess)
				{
	    			
	    						
					//Make sure that we dont render twice a frame in the same timer ticks
				/*
					currentTime=this.iTimer.getTime()-this.startTime;
					while(currentTime==lastCurrentTime)
					{
						Thread.sleep(1);
						currentTime=this.iTimer.getTime()-this.startTime;
					}
					lastCurrentTime=currentTime;
				*/	
				
					currentTime=this.iTimer.getTime()-this.startTime;
					
					//Compute microsecond per frame using user selected FPS
					long frameTimeUs=100000000/this.maxFPS100;
					
					//Compute how many frame should have been rendered since start with frameTimeUs
					long nbFrameSinceStart=(1000*currentTime)/frameTimeUs;
					
					//Compute what should be the next frame millisecond rendering time
					long currentFrameTimeMs=((nbFrameSinceStart+1)*frameTimeUs)/1000;
					
					//lastCurrentFrameTimeMs=((nbFrameSinceStart)*frameTimeUs)/1000;
					
					//Ensure that we dont render twice the same frame
					//if(lastCurrentFrameTimeMs==currentFrameTimeMs)
					//	currentFrameTimeMs=lastCurrentFrameTimeMs+frameTimeUs/1000;
											
					//if((currentFrameTimeMs-lastCurrentFrameTimeMs)>(frameTimeUs/1000))
					//	currentFrameTimeMs=lastCurrentFrameTimeMs+frameTimeUs/1000;
					
					//lastCurrentFrameTimeMs=currentFrameTimeMs;
					/*
					long currentTime2=currentTime;
					while(currentTime<currentFrameTimeMs)
					{
						overEatCPU=0;
						Thread.sleep(1);
						if(this.iTimer.getTime()-this.startTime==currentTime2)
							currentTime++;
						else
						{
							currentTime=this.iTimer.getTime()-this.startTime;
							currentTime2=currentTime;
						}
					}
					*/
					this.fpsBuffer[this.numLoop%this.fpsBuffer.length]=currentTime+startTime;
					this.render();
					
					if(this.numLoop%400==0)
						System.out.println(this.getFPS100() +"    " +this.numLoop+" /" +nbFrameSinceStart);
						
						
/*
					if(overEatCPU++>5)
					{
					
						Thread.sleep(1);
						overEatCPU=0;
					}
					*/
					Thread.yield();	
						
					this.numLoop++;						
					
				}
				else
				{
					Thread.sleep(1);
					Thread.yield();
				}
			}
		}
		catch(InterruptedException ie)
		{
			Log.log(ie);					
		}
		this.pauseProcess=false;
		this.runProcess=false;
		this.process = null;
		Log.log("Stop Scene3DRender: "+Thread.currentThread());
    }
	
	public void render()
	{
		if(this.render3DSwitcher!=null && this.render3DSwitcher.getFinished())
		{
			if(!this.render3DSwitcher.getError())
			{
				IRender3D r=this.render3DSwitcher.getRender();
				Container parent=this.render.getCanvas().getParent();
				if(parent!=null)
					parent.remove(this.render.getCanvas());
				
				if(parent!=null)
					parent.add(r.getCanvas());
				r.setSize(this.render.getWidth(),this.render.getHeight());
				r.getCanvas().requestFocus();
				this.render=r;
				Log.log("SWITCHED TO " + this.render.getImplementationName());
			}
			this.render3DSwitcher=null;
			this.iScene3DRenderCallBack.render3DSwitched(this);
		}
		
		this.iScene3DRenderCallBack.render3DStart(this);			
		
		if(this.autoPlayAnimator)	
			this.scene.playScene3DObjectAnimator((int)(this.iTimer.getTime()-this.startTime));
			
		this.scene.setScene3DObjectToWorld();

		this.iScene3DRenderCallBack.render3DWorldSpace(this);	

		this.scene.setScene3DObjectToCamera();

		this.iScene3DRenderCallBack.render3DCameraSpace(this);

		this.render.setCamera3D(this.scene.getCurrentCamera3D());
		
		this.render.renderScene3D(this.scene);
		
		this.iScene3DRenderCallBack.render3DEnd(this);

		if(this.scene.getNbMonitoredSceneObject()!=0)
			this.scene.updateMonitoredSceneObjects();			
		
	}
	
	public void setAutoPlayAnimator(boolean flag)
	{
		this.autoPlayAnimator=flag;
	}

	public boolean getAutoPlayAnimator()
	{
		return this.autoPlayAnimator;
	}

}