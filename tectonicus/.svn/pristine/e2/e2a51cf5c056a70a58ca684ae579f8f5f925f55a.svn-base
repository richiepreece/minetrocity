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

public class ProgressListener implements IProgressListener
{
	private String name;
	private String unit;
	private int action;
	private int progress;
	private int maximumProgress;
	private boolean started;
	private boolean finished;
	private boolean error;
	
	public ProgressListener()
	{
		reset();
	}
	
	public void setName(String name)
	{
		name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public void setAction(int state)
	{
		action = state;
	}
	
	public int getAction()
	{
		return action;
	}
	
	public void setUnit(String unit)
	{
		this.unit = unit;
	}


	public String getUnit()
	{
		return unit;
	}
	
	public void setMaximumProgress(int size)
	{
		this.maximumProgress=size;
	}

	public int getMaximumProgress()
	{
		return this.maximumProgress;
	}
	
	public void setProgress(int progress)
	{
		this.progress=progress;
	}	
	
	public int getProgress()
	{
		return this.progress;
	}
	
	public boolean getStarted()
	{
		return this.started;
	}
	
	public void setStarted(boolean flag)
	{
		this.started=flag;
	}
	
	public boolean getFinished()
	{
		return this.finished;
	}
	
	public void setFinished(boolean flag)
	{
		this.finished=flag;
	}

	public boolean getError()
	{
		return this.error;
	}
	
	public void setError(boolean flag)
	{
		this.error=flag;
	}	
	
	public void reset()
	{
		this.name="Unknown";
		this.unit="%";
		this.action=IProgressListener.ACTION_UNKNOWN;
		this.maximumProgress=100;
		this.progress=0;
		this.finished=false;
		this.started=false;
		this.error=false;	
	}
	
	public void copy(IProgressListener source)
	{
		this.setProgress(source.getProgress());
		this.setFinished(source.getFinished());
		this.setError(source.getError());		
	}
}

