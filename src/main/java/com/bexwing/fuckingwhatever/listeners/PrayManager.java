package com.bexwing.fuckingwhatever.listeners;

import com.bexwing.fuckingwhatever.GameManager;
import com.bexwing.fuckingwhatever.utils.Logging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
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
            Logging.info("Comparing: "+hunter.getUniqueId().toString()+" - "+event.getPlayer().getUniqueId());
            if(hunter.getUniqueId().toString().equalsIgnoreCase(event.getPlayer().getUniqueId().toString())){
                Logging.info("REFRESHING HUNTER");
                GameManager.getGameManager().removeHunter(hunter);
                GameManager.getGameManager().addHunter(event.getPlayer());
                if(pray.isOnline())
                    GameManager.getGameManager().getHunters().replace(event.getPlayer(),pray);
            }
        });
    }
}
