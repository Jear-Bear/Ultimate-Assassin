package com.joda.assassin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.event.block.Action;


public class assassinCommand implements CommandExecutor, Listener {
	boolean start = false;
	ItemStack[] Inv;
	ArrayList<String> lore = new ArrayList<>();
	boolean inGame = false;
	private Main main;
	int p1 = 0;
	int p2 = 0;
	int round = 0;
	Location spawn;
	Player hunter;
	Player target;
	double wbSize = 160.0;
	ItemStack star = new ItemStack(Material.NETHER_STAR);
	ItemMeta starMeta = star.getItemMeta();
	ArrayList<Player> ready = new ArrayList<>();
	
	public assassinCommand(Main main)
	{
		this.main = main;
		starMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Ready up");
		lore.add("right click to ready up");
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("start"))
		{
			if (sender instanceof Player)
			{
				makeInvs();
				if (main.players != null) main.players.clear();
				if (main.roundsWon != null) main.roundsWon.clear();
				Player play = (Player) sender;
				play.sendMessage("yes!");
				if (args.length == 2)
				{
					play.sendMessage("OK");
					for (Player p : Bukkit.getServer().getOnlinePlayers())
					{
						if (p.getName().equals(args[0]))
						{
							main.players.add(p);
						}
						if (p.getName().equals(args[1]))
						{
							main.players.add(p);
						}
					}
				}
				else play.sendMessage("Please enter a player name after the /start command!");
			}
			for (Player p : main.players)
			{
				Bukkit.broadcastMessage(p.getDisplayName());
				main.roundsWon.put(p, 0);
				main.buildScore(p, main.roundsWon, main.players, main.score1, main.score2);
			}
			startRound((Player) sender);
		}
		return false;
	}
	
	@EventHandler
    public void onDeath(PlayerDeathEvent e) {
		inGame = false;
		if (lore != null) lore.clear();
		e.getDrops().clear();
        e.setDeathMessage("");
        if (e.getEntity() instanceof Player)
        {
        	Player p = e.getEntity();
        	p.getWorld().getWorldBorder().reset();
        	if (main.players.get(1).equals(p))
        	{
        		Bukkit.broadcastMessage(ChatColor.YELLOW + main.players.get(0).getDisplayName() + " has won this round!");
        		main.roundsWon.put(main.players.get(0), main.roundsWon.get(main.players.get(0)) + 1);
        		main.players.get(0).getScoreboard().getTeam(main.players.get(0).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(0)) + "/20");
        		main.players.get(1).getScoreboard().getTeam(main.players.get(0).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(0)) + "/20");
        	}
        	else if (main.players.get(0).equals(p))
        	{
        		Bukkit.broadcastMessage(ChatColor.YELLOW + main.players.get(1).getDisplayName() + " has won this round!");
        		main.roundsWon.put(main.players.get(1), main.roundsWon.get(main.players.get(1)) + 1);
        		main.players.get(0).getScoreboard().getTeam(main.players.get(1).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(1)) + "/20");
        		main.players.get(1).getScoreboard().getTeam(main.players.get(1).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(1)) + "/20");
        	}
        	main.players.get(0).getInventory().clear();
        	main.players.get(0).getInventory().addItem(star);
        	main.players.get(1).getInventory().clear();
        	main.players.get(1).getInventory().addItem(star);
        	if (ready != null) ready.clear();
        }
    }
	
	@EventHandler
    public void onPlayerAttack(EntityDamageEvent event)
    {
    	if(event.getEntity() instanceof Player)
    	{
    		Player p = (Player) event.getEntity();
    		if (p.getInventory().contains(star))
    		{
    			event.setCancelled(true);
    		}
    	}
    }
	
	
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e)
	{
		e.setRespawnLocation(spawn);
		main.findSpawn(e.getPlayer());
		ItemStack star = new ItemStack(Material.NETHER_STAR);
    	ItemMeta starMeta = star.getItemMeta();
    	starMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Ready up");
    	starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		e.getPlayer().getInventory().addItem(star);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if (player.getItemInHand().getType().equals(Material.NETHER_STAR))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if (ready.contains(player))
				{
					Bukkit.broadcastMessage(player.getDisplayName() + " is " + ChatColor.RED + "not ready");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
					ready.remove(player);
				}
				else
				{
					Bukkit.broadcastMessage(player.getDisplayName() + " is " + ChatColor.GREEN + "ready");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
					if (!(ready.contains(player))) ready.add(player);
				}
			}
		}
		if (ready.contains(main.players.get(0)) && ready.contains(main.players.get(1)))
		{
			ready.clear();
			main.players.get(0).getInventory().clear();
			main.players.get(1).getInventory().clear();
			Bukkit.broadcastMessage(ChatColor.GOLD + "Starting next round in: ");
			main.countdownChat(3);
			Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
			{
				@Override
				public void run() {
					main.nextRound(player);
				}
			}, 60L);
		}
	}
	
	public void startRound(Player play)
	{
		inGame = true;
		if (spawn != null) spawn.getChunk().setForceLoaded(false);
		Bukkit.broadcastMessage(ChatColor.GREEN + "Loading map..");
		main.findSpawn(play);
		spawn = main.spawn;
		spawn.getChunk().setForceLoaded(true);
		spawn.getChunk().load(true);
		spawn.add(0, 2, 0);
		play.getWorld().setSpawnLocation(spawn);
        play.getWorld().getWorldBorder().setCenter(spawn.getBlockX(), spawn.getBlockZ());
        play.getWorld().getWorldBorder().setSize(160);
		round = main.roundsWon.get(main.players.get(0)) + main.roundsWon.get(main.players.get(1)) + 1;
		if (round == 21) return;
		String r = "Round " + ChatColor.GOLD.toString() + ChatColor.BOLD + round;
		String hunt = "You're the " + ChatColor.RED + "hunter";
		String targ = "You're the " + ChatColor.BLUE + "target";
		for (Player p : main.players)
		{
			p.setHealth(20.0);
			p.setFoodLevel(20);
			p.getInventory().setContents(Inv);
			if (round % 2 == 0)
			{
				hunter = main.players.get(0);
				target = main.players.get(1);
			}
			else
			{
				hunter = main.players.get(1);
				target = main.players.get(0);
			}
		}
		hunter.sendTitle(r, hunt, 20, 40, 20);
		target.sendTitle(r, targ, 20, 40, 20);
		hunter.getInventory().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        hunter.getInventory().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        hunter.getInventory().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        hunter.getInventory().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        target.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        target.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        target.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        target.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        hunter.teleport(spawn);
        target.teleport(spawn);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				hunter.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 220, 100));
				hunter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 100));
				hunter.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 100));
				start = true;
				main.countdownTitle(10);
			}
		}, 80L);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				start = false;
				hunter.playSound(hunter.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
				target.playSound(hunter.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
			}
		}, 280L);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				if (hunter.getInventory().contains(star) || target.getInventory().contains(star)) return;
				else shrinkBorder(play, 0L);
			}
		}, 1400L);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				if (hunter.getInventory().contains(star) || target.getInventory().contains(star)) return;
				else shrinkBorder(play, 0L);
			}
		}, 3800L);
	}
	
	public void makeInvs() {
        final ItemStack Axe = new ItemStack(Material.DIAMOND_AXE);
        Axe.addEnchantment(Enchantment.DIG_SPEED, 2);
        final ItemStack Shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        Shovel.addEnchantment(Enchantment.DIG_SPEED, 2);
        final ItemStack pickAxe = new ItemStack(Material.DIAMOND_PICKAXE);
        pickAxe.addEnchantment (Enchantment.DIG_SPEED, 2);
        Inv = new ItemStack[5];
        Inv[0] = pickAxe;
        Inv[1] = Axe;
        Inv[2] = Shovel;
        Inv[3] = new ItemStack(Material.COBBLESTONE, 16);
        Inv[4] = new ItemStack(Material.COOKED_BEEF, 16);
    }
	
	public void shrinkBorder(Player play, Long i)
	{
		Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				Bukkit.broadcastMessage("Border shrinking in:");
				for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			}
		}, i);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				main.countdownChat(3);
			}
		}, i + 20L);
		for (int a = (int) (i + 80); a < (int) (i + 1280); ++a) {
            Bukkit.getServer().getScheduler().runTaskLater((Plugin)main, (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!play.getInventory().contains(Material.NETHER_STAR))
                    {
                    	play.getWorld().getWorldBorder().setSize(wbSize);
                    	wbSize -= 0.05;
                    }
                    else return;
                }
            }, a);
            if (a == (int) (i + 1279))
            {
            	Bukkit.broadcastMessage("The border has stopped shrinking!"); 
            	for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }
	}
}
