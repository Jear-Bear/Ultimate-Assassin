package com.joda.assassin;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.BlockBreakEvent;


public class logDrop implements Listener {
	
	public logDrop(Main main){}
	
	@EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Block b = event.getBlock();
        final Material t = b.getType();
        if (t.toString().toLowerCase().contains("log")) {
            final Random r = new Random();
            final int randomInt = r.nextInt(100) + 1;
            if (randomInt > 0 && randomInt < 33) {
                if (randomInt <= 2) {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.WATER_BUCKET, 1));
                }
                else if (randomInt <= 8) {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.HAY_BLOCK, 1));
                }
                else if (randomInt <= 18) {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.CRAFTING_TABLE, 1));
                }
                else {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.LADDER, 3));
                }
            }
        }
    }


}
