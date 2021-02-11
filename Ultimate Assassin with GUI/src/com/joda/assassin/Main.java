package com.joda.assassin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	public HashMap<Player, Integer> roundsWon;
	int score1 = 0;
	int score2 = 0;
	int x;
	int z;
	ArrayList<Player> players = new ArrayList<>();
	Location spawn;
	countdown cDown = new countdown();
	assassinCommand assassin = new assassinCommand(this);
	scoreboard score = new scoreboard(this);
	
	@Override
	public void onEnable()
	{
		assassin.makeInvs();
		roundsWon = new HashMap<>();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new assassinCommand(this), this);
		getServer().getPluginManager().registerEvents(new logDrop(this), this);
		getCommand("start").setExecutor(new assassinCommand(this));
	}

	public void buildScore(Player player, HashMap<Player, Integer> roundsWon, ArrayList<Player> players, int score1, int score2)
	{
		score.buildSidebar(player, roundsWon, players, score1, score2);
	}
	
	public void findSpawn(Player p)
	{
		x = getRand();
		z = getRand();
		spawn = p.getWorld().getHighestBlockAt(x, z).getLocation();
		if (spawn.getBlock().getBiome().toString().toLowerCase().contains("ocean"))
		{
			while (spawn.getBlock().getBiome().toString().toLowerCase().contains("ocean"))
			{
				x = getRand();
				z = getRand();
				spawn = p.getWorld().getHighestBlockAt(x, z).getLocation();
			}
		}
	}
	
	public void countdownTitle(int i)
	{
		cDown.countDownTitle(i, (Plugin) this);
	}
	
	public void countdownChat(int i)
	{
		cDown.countDownChat(i, (Plugin) this);
	}
	
	public boolean getCounting()
	{
		return cDown.getCounting();
	}
	public void nextRound(Player player)
	{
		assassin.startRound(player);
	}
	
	public int getRand() 
	{
        if (Math.random() > 0.5) return (int)(Math.random() * 8000.0);
        return (int)(Math.random() * -8000.0);
    }
}
