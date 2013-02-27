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

import java.awt.Component;
import java.awt.event.*;


public class DirectInput implements IDirectInput,MouseListener,MouseMotionListener,KeyListener
{
	private int mousePosX;				//Last know mouse pos x (relative to upper corner)
	private int mousePosY;				//Last know mouse pos y (relative to upper corner)
	private int mousePosDragX;			//Last drag start mouse pos x (relative to upper corner)
	private int mousePosDragY;			//Last drag start mouse pos y (relative to upper corner)
	private boolean mouseB1;			//Mouse b1 state 
	private boolean mouseB2;			//Mouse b2 state 
	private boolean mouseB3;			//Mouse b3 state 
	private long mouseB1Click;			//Mouse b1 last click
	private long mouseB2Click;			//Mouse b2 last click 
	private long mouseB3Click;			//Mouse b3 last click 
	private boolean drag;				//Mouse drag state : Bit 1 => started ; Bit 2 => ended		
	private int[] keyboard;				//Keyboard state array : Bit 1 => pressed ; Bit 2 => released	
	private long[] keyboardTime;		//Keyboard last event time in ms
	
	public DirectInput(Component c)
	{
        this.keyboard=new int[1024];
        this.keyboardTime=new long[1024];
		for(int x=0;x<keyboard.length;x++)			//Reset keyboard array 
		{
			this.keyboard[x]=0;		
			this.keyboardTime[x]=0;		
		}
	
        this.drag=false;							//Set drag flag to false
        this.mouseB1=false;							//Set mouse b1 flag to false
        this.mouseB2=false;							//Set mouse b2 flag to false
        this.mouseB3=false;							//Set mouse b3 flag to false
        
        this.mouseB1Click=0;						//Set mouse b1 click time to 0
        this.mouseB2Click=0;						//Set mouse b2 click time to 0
        this.mouseB3Click=0;						//Set mouse b3 click time to 0
        
        c.addMouseMotionListener(this);	//Set mouse motion listener
	    c.addMouseListener(this);		//Set mouse listener    
        c.addKeyListener(this);			//Set key listener		
		
	}

	public int getMouseX()
	 {
	 	return this.mousePosX;
	 }
	 
	public int getMouseY()
	 {
	 	return this.mousePosY;
	 }
	 
	public boolean isMouseB1()
	 {
	 	return this.mouseB1;
	 }
	 
	public boolean isMouseB1Click(long time)
	 {
	 	return ((System.currentTimeMillis()-this.mouseB1Click)<time);
	 }
	 
	public boolean isMouseB2Click(long time)
	 {
	 	return ((System.currentTimeMillis()-this.mouseB2Click)<time);
	 }

	public boolean isMouseB3Click(long time)
	 {
	 	return ((System.currentTimeMillis()-this.mouseB3Click)<time);
	 }
	 
	public boolean isMouseB2()
	 {
	 	return this.mouseB2;
	 }
	 
	public boolean isMouseB3()
	 {
	 	return this.mouseB3;
	 }	
	 
	public boolean isMouseDrag()
	 {
	 	return this.drag;
	 }	
	 	  
	public boolean isKey(int num)
	 {
	 		return (this.keyboard[num]==1);
	 }
	 
	public boolean isKey(int num,long maxTime)
	 {
	 	return (this.keyboard[num]==1)&&((System.currentTimeMillis()-this.keyboardTime[num])<maxTime);
	 }
	 
	  
	public int getMouseDragX()
	 {
	 	return this.mousePosDragX;
	 }

	public int getMouseDragY()
	 {
	 	return this.mousePosDragY;
	 }
	 
	 
	public void processKeyEvent(KeyEvent e)
	{
		if(e.getID()==KeyEvent.KEY_PRESSED)
			 this.keyPressed(e); 	
	
		if(e.getID()==KeyEvent.KEY_RELEASED)
			keyReleased(e) 	;
	}	

	public void keyPressed(KeyEvent e)  
	{
		this.keyboardTime[e.getKeyCode()&0x3FF]=System.currentTimeMillis();
		this.keyboard[e.getKeyCode()&0x3FF]=1;
	}	

	public void keyReleased(KeyEvent e) 
	{
		this.keyboardTime[e.getKeyCode()&0x3FF]=System.currentTimeMillis();
		this.keyboard[e.getKeyCode()&0x3FF]=0;
	}
	
	public void keyTyped(KeyEvent e) 
	{
	}
	
			
	public void processMouseMotionEvent(MouseEvent e)
	{
		this.mousePosX=e.getX();
		this.mousePosY=e.getY();
	}
	
	public void mouseMoved(MouseEvent e)
	{
		this.mousePosX=e.getX();	
		this.mousePosY=e.getY();
	}
	
	public void mouseDragged(MouseEvent e)
	{
		this.drag=true;
		this.mousePosX=e.getX();	
		this.mousePosY=e.getY();		
		if(!this.drag)
		{
			this.mousePosDragX=this.mousePosX;	
			this.mousePosDragY=this.mousePosY;	
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0)
			this.mouseB1Click=System.currentTimeMillis();
		if((e.getModifiers()&InputEvent.BUTTON2_MASK)!=0)
			this.mouseB2Click=System.currentTimeMillis();	
		if((e.getModifiers()&InputEvent.BUTTON3_MASK)!=0)
			this.mouseB3Click=System.currentTimeMillis();
		
	}
							   
	public void mouseEntered(MouseEvent e){} 
	
	public void mouseExited(MouseEvent e){} 
	
	public void mousePressed(MouseEvent e)
	{    
		if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0)
			this.mouseB1=true; 	
		if((e.getModifiers()&InputEvent.BUTTON2_MASK)!=0)
			this.mouseB2=true; 
		if((e.getModifiers()&InputEvent.BUTTON3_MASK)!=0)
			this.mouseB3=true;
			
		if(!this.drag)
		{
			this.mousePosDragX=this.mousePosX;	
			this.mousePosDragY=this.mousePosY;
			this.drag=true;		
		}
				
	}	
															 
	public void mouseReleased(MouseEvent e)
	{ 	   	
		if((e.getModifiers()&InputEvent.BUTTON1_MASK)!=0)
			this.mouseB1=false; 		
		if((e.getModifiers()&InputEvent.BUTTON2_MASK)!=0)
			this.mouseB2=false; 	
		if((e.getModifiers()&InputEvent.BUTTON3_MASK)!=0)
			this.mouseB3=false; 	
		if(this.drag)
			this.drag=false;		
	}		
}
