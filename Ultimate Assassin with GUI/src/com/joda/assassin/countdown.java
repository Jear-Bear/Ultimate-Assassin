package com.joda.assassin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class countdown
{
	public boolean counting = false;
	public void countDownTitle(int count, Plugin plug)
	{
		counting = true;
		Bukkit.getScheduler().runTaskTimer(plug, new Runnable()
	    {
	        int time = count;
	
	        @Override
	        public void run()
	        {
	            if (this.time == 0)
	            {
	            	counting = false;
	                return;
	            }
	           
	            for (Player p : Bukkit.getOnlinePlayers())
	            {
	            	p.sendTitle(time + "", "", 0, 20, 0);
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            }
	           
	            this.time--;
	        }
	    }, 0L, 20L);
	}
	
	public void countDownChat(int count, Plugin plug)
	{
		Bukkit.getScheduler().runTaskTimer(plug, new Runnable()
	    {
	        int time = count;
	
	        @Override
	        public void run()
	        {
	            if (this.time == 0)
	            {
	                return;
	            }
	           
	            Bukkit.broadcastMessage(time + "");
	            
	            for (Player p : Bukkit.getOnlinePlayers())
	            {
	                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            }
	           
	            this.time--;
	        }
	    }, 0L, 20L);
	}
	
	public boolean getCounting()
	{
		return counting;
	}
}