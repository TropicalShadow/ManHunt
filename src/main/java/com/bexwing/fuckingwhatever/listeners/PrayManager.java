package com.bexwing.fuckingwhatever.listeners;

import com.bexwing.fuckingwhatever.GameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PrayManager implements Listener {



    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if(!GameManager.getGameManager().getHunters().containsValue(event.getPlayer()))return;
        GameManager.getGameManager().getHunters().forEach((hunter,pray)->{
            if(pray==event.getPlayer()){
                GameManager.getGameManager().getHunters().replace(hunter,null);
            }
        });
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        HashMap<Player, Player> copyCat = ( HashMap<Player, Player>) GameManager.getGameManager().getHunters().clone();
        copyCat.forEach((hunter,pray)->{
            if(hunter.getUniqueId().toString().equalsIgnoreCase(event.getPlayer().getUniqueId().toString())){
                GameManager.getGameManager().removeHunter(hunter);
                GameManager.getGameManager().addHunter(event.getPlayer());
                if(pray.isOnline())
                    GameManager.getGameManager().getHunters().replace(event.getPlayer(),pray);
            }
        });
    }
    @EventHandler
    public void sayHelloToMemberOnJoin(PlayerJoinEvent event){
        if(GameManager.getGameManager().getGameState().equals(GameManager.GameState.PENDING))
            event.getPlayer().sendTitle(ChatColor.DARK_PURPLE.toString()+ ChatColor.BOLD+"MANHUNT",ChatColor.WHITE+"Starting Soon!",20,20,20);
    }

    @EventHandler
    public void hunterMenu(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if(!GameManager.getGameManager().getHunters().containsKey(event.getPlayer()))return;
        if(!GameManager.trackingCompassItemStack().equals(event.getItem()))return;
        event.getPlayer().sendMessage(Component.text("TODO - OPEN MENU"));
        
    }
}
