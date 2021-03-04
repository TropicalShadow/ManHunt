package com.bexwing.fuckingwhatever.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class AntiBlockBreaking implements Listener {

    @EventHandler()
    public void onBlazeSpawnerBreak(BlockBreakEvent event){
        if(!event.getBlock().getType().equals(Material.SPAWNER))return;
        CreatureSpawner block = (CreatureSpawner) event.getBlock().getState();
        if(!block.getSpawnedType().equals(EntityType.BLAZE))return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED+"Blaze spawners cannot be broken.");
    }

}
