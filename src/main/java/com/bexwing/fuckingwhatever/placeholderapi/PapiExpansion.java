package com.bexwing.fuckingwhatever.placeholderapi;

import com.bexwing.fuckingwhatever.GameManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PapiExpansion extends PlaceholderExpansion {

    private HashMap<Player,Integer> cacheDistance = new HashMap<>();

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hunter";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TropicalShadow";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
    @Override
    public String onRequest(OfflinePlayer player, String identifier){
        GameManager gameManager = GameManager.getGameManager();
        Player hunter = (Player) player;
        if(!gameManager.getHunters().containsKey(hunter))
            return "First to kill the dragon wins!!! - ";
        // %hunter_hunting%
        if(identifier.equals("hunting")){
            if(gameManager.getGameState().equals(GameManager.GameState.PENDING)){
                return "The game hasn't started";
            }else if(gameManager.getGameState().equals(GameManager.GameState.END)){
                return "The game has ended.";
            }
            Player pray = gameManager.getHunters().get(hunter);
            if(pray==null)
                return "Finding Pray";
            if(hunter.getWorld() != pray.getWorld()){
                if(cacheDistance.containsKey(player)){
                    return pray.getName() + " lost "+cacheDistance.get(player) + " blocks away";
                }else{
                    return pray.getName() + " lost before we could track them";
                }
            }
            return pray.getName() +" "+ Math.round(pray.getLocation().distance(hunter.getLocation()))+" blocks away";
        }


        return null;
    }
}
