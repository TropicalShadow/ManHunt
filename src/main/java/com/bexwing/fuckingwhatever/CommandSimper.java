package com.bexwing.fuckingwhatever;

import com.bexwing.fuckingwhatever.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandSimper implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String cmd = command.getName();
        boolean isPlayer = (sender instanceof Player);
        if(cmd.equalsIgnoreCase("addhunter")){
            if(args.length <=0){
                StringUtils.coloriseAndSend("&b/addhunter &4&l>>&3NAME&4&l<<",sender);
                return true;
            }
            String username = args[0];
            Player newHunter = Bukkit.getPlayer(username);
            if(newHunter == null){
                StringUtils.coloriseAndSend("&b/addhunter &4&l>>&3UNKNOWN USERNAME&4&l<<",sender);
                return true;
            }
            GameManager.getGameManager().addHunter(newHunter);
            StringUtils.coloriseAndSend("&b"+username+" has been added to the hunters team!",sender);
            return true;
        }else if(cmd.equalsIgnoreCase("removehunter")){
            if(args.length <=0){
                StringUtils.coloriseAndSend("&b/removehunter &4&l>>&3NAME&4&l<<",sender);
                return true;
            }
            String username = args[0];
            Player newHunter = Bukkit.getPlayer(username);
            if(newHunter == null){
                StringUtils.coloriseAndSend("&b/removehunter &4&l>>&3UNKNOWN USERNAME&4&l<<",sender);
                return true;
            }
            if(!GameManager.getGameManager().getHunters().containsKey(newHunter)){
                StringUtils.coloriseAndSend("&4"+username+" is not on the hunters team.",sender);
                return true;
            }
            GameManager.getGameManager().removeHunter(newHunter);
            StringUtils.coloriseAndSend("&b"+username+" has been removed from the hunters team.",sender);
        }else if(cmd.equalsIgnoreCase("listhunters")){
            StringUtils.coloriseAndSend("&b----------------------------------------",sender);
            if(GameManager.getGameManager().getHunters().size()<=0){
                StringUtils.coloriseAndSend("&bThere are currently no hunters",sender);
            }
            GameManager.getGameManager().getHunters().keySet().forEach((hunter)->{
                StringUtils.coloriseAndSend("&b - "+hunter.getName(),sender);
            });
            StringUtils.coloriseAndSend("&b----------------------------------------",sender);
            return true;
        }else if(cmd.equalsIgnoreCase("togglegamestate")){
            GameManager.GameState finalState;
            GameManager.GameState currentState = GameManager.getGameManager().getGameState();
            switch(currentState){
                case PENDING:finalState= GameManager.GameState.PROGRESS;GameManager.getGameManager().startGame();break;
                case PROGRESS:finalState= GameManager.GameState.END;GameManager.getGameManager().endGame();break;
                case END: finalState = GameManager.GameState.PENDING;break;
                default: finalState = GameManager.GameState.PENDING;GameManager.getGameManager().endGame();break;
            }
            StringUtils.coloriseAndSend("&bGameState has been set to "+finalState.name(),sender);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> output = new ArrayList<>();
        if(command.getName().equalsIgnoreCase("removehunter") || command.getName().equalsIgnoreCase("addhunter"))
            return null;
        return output;
    }
}
