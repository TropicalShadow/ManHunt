package com.bexwing.fuckingwhatever;

import com.bexwing.fuckingwhatever.NMS.HotbarTitle;
import com.bexwing.fuckingwhatever.NMS.Title;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

    public enum GameState {
        PENDING(0),
        PROGRESS(1),
        END(2);

        private final int value;

         GameState(int value){
            this.value = value;
         }

        public int getValue() {
            return value;
        }
    }

    private HashMap<Player,Player> hunters = new HashMap<>();
    private static GameManager gameManager = null;
    private BukkitTask trackingTask = null;
    private HotbarTitle hotbarTitle;
    private GameState gameState;



    public GameManager(){
        if(gameManager != null){
            throw new RuntimeException("Too many GameManager instances created");
        }
        gameManager = this;
        gameState = GameState.PENDING;
        hotbarTitle = new HotbarTitle();
    }

    public HotbarTitle getHotbarTitle(){
        return this.hotbarTitle;
    }


    public boolean startGame(){
        if(gameState.equals(GameState.PENDING)){
            gameState = GameState.PROGRESS;
            startTracking();
            hotbarTitle.startTitle();
            return true;
        }
        return false;
    }

    public boolean endGame(){
        if(gameState.equals(GameState.PROGRESS)){
            gameState = GameState.END;
            stopTracking();
            hotbarTitle.stopTitle();
            return true;
        }
        return false;
    }
    public boolean resetGame(){
        if(gameState.equals(GameState.END)){
            gameState = GameState.PENDING;
            stopTracking();
            hotbarTitle.stopTitle();
            return true;
        }
        return false;
    }
    public void addHunter(Player player){
        hunters.put(player,null);
        player.getInventory().removeItem(trackingCompassItemStack());
        player.getInventory().addItem(trackingCompassItemStack());
        player.sendMessage(Component.text(ChatColor.GOLD+"You have been added to the hunter group."));
        Location loc = new Location(Bukkit.getWorld("world"),-103,118,42);
        if(loc.getBlock().getType().equals(Material.BARRIER))
            player.teleportAsync(loc.add(0.5,2,0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);

    }
    public void removeHunter(Player player){
        hunters.remove(player);
        player.getInventory().removeItem(trackingCompassItemStack());
    }
    public Player getRandomPray(){
        Collection<Player> prays = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> !(GameManager.getGameManager().getHunters().containsKey(player) || GameManager.getGameManager().getHunters().containsValue(player))).collect(Collectors.toList());
        ArrayList<Player> listOfPrays = new ArrayList<Player>(prays);
        if(listOfPrays.size()<= 0)return null;
        Random r = new Random();
        return listOfPrays.get(r.nextInt(prays.size()));
    }

    public void startTracking(){
        trackingTask = Bukkit.getScheduler().runTaskTimer(ManHunt.getPlugin(),()->{
            ItemStack compass = trackingCompassItemStack();
            hunters.forEach((hunter,pray) -> {
                if(pray == null){
                    Player newPray = getRandomPray();
                    hunters.replace(hunter,newPray);
                    pray = newPray;
                }
                Inventory inv = hunter.getInventory();
                if(!inv.contains(compass)){
                    inv.addItem(compass);// This shouldn't be a issue but inventory may be full...
                }
                if(hunter.getWorld()!= pray.getWorld())return;
                hunter.setCompassTarget(pray.getLocation());
            });
        },0,60);
    }
    public void stopTracking(){
        if(trackingTask!=null){
            if(!trackingTask.isCancelled()){
                trackingTask.cancel();
                trackingTask = null;
            }
        }
    }
    public static ItemStack trackingCompassItemStack(){
        ItemStack item = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) item.getItemMeta();

        meta.lore(new ArrayList<>(Collections.singleton(Component.text("Compass Of Tracking"))));

        item.setItemMeta(meta);

        return item;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public HashMap<Player, Player> getHunters() {
        return hunters;
    }
}
