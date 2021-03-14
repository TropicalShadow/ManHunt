package com.bexwing.fuckingwhatever;

import com.bexwing.fuckingwhatever.listeners.AntiBlockBreaking;
import com.bexwing.fuckingwhatever.listeners.PrayManager;
import com.bexwing.fuckingwhatever.placeholderapi.PapiExpansion;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Marker;

public final class ManHunt extends JavaPlugin {

    private static GameManager gameManager = null;
    private static ManHunt INSTANCE;

    private CommandSimper commandSimper;


    @Override
    public void onEnable() {
        INSTANCE = this;
        if(gameManager != null){
            throw new RuntimeException("GameManager was not null on enable");
        }
        gameManager = new GameManager();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null)
            new PapiExpansion().register();
        commandSimper = new CommandSimper();
        addEventListener(new AntiBlockBreaking(),new PrayManager());
        registerCommands("addhunter","removehunter","listhunters","togglegamestate","start");
    }

    @Override
    public void onDisable() {
        GameManager.getGameManager().endGame();
        HandlerList.unregisterAll(this);
        gameManager = null;
        INSTANCE = null;
    }

    public void registerCommands(String... commands){
        for (String command : commands) {
            getCommand(command).setExecutor(commandSimper);
            getCommand(command).setTabCompleter(commandSimper);
        }
    }

    public void addEventListener(Listener... listeners){
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener,this);
        }
    }

    public static ManHunt getPlugin() {
        return INSTANCE;
    }
}
