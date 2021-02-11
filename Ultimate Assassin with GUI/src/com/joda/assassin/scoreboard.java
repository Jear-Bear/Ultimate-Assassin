package com.joda.assassin;

import java.util.ArrayList;
import java.util.HashMap; 

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class scoreboard implements Listener {
	
	public scoreboard(Main main){}
	
	public void buildSidebar(Player player, HashMap<Player, Integer> roundsWon, ArrayList<Player> players, int score1, int score2) {
		if (roundsWon.get(players.get(0)) != null) score1 = roundsWon.get(players.get(0));
		if (roundsWon.get(players.get(1)) != null) score2 = roundsWon.get(players.get(1));
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		@SuppressWarnings("deprecation")
		Objective obj = board.registerNewObjective("test", "dummy");
		obj.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "1v1 Ultimate Assassin");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Score p1Name = obj.getScore(ChatColor.AQUA + players.get(0).getDisplayName());
		p1Name.setScore(5);
		
		Team p1 = board.registerNewTeam(players.get(0).getDisplayName());
		p1.addEntry(ChatColor.RED.toString());
		p1.setPrefix("Rounds won: ");
		p1.setSuffix(ChatColor.GOLD + "" + score1 + "/20");
		obj.getScore(ChatColor.RED.toString()).setScore(4);
		
		Score gap = obj.getScore(" ");
		gap.setScore(3);
		
		Score p2Name = obj.getScore(ChatColor.AQUA + players.get(1).getDisplayName());
		p2Name.setScore(2);
		
		Team p2 = board.registerNewTeam(players.get(1).getDisplayName());
		p2.addEntry(ChatColor.AQUA.toString());
		p2.setPrefix("Rounds won: ");
		p2.setSuffix(ChatColor.GOLD + "" + score2 + "/20");
		obj.getScore(ChatColor.AQUA.toString()).setScore(1);
		
		player.setScoreboard(board);
	}

}
