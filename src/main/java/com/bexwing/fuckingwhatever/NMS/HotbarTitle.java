package com.bexwing.fuckingwhatever.NMS;

import com.bexwing.fuckingwhatever.FuckingWhatever;
import com.bexwing.fuckingwhatever.GameManager;
import com.bexwing.fuckingwhatever.utils.Logging;
import com.bexwing.fuckingwhatever.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;

public class HotbarTitle extends Reflection{
    private Class<?> chatOutClass;
    private Class<?> iChatBaseComponent;
    private Class<?> packetPlayOutTitle;
    private BukkitTask titleTask;


    public HotbarTitle(){
        try {
            chatOutClass = getNMSClass("PacketPlayOutChat");
            iChatBaseComponent = getNMSClass("IChatBaseComponent");
            packetPlayOutTitle = getNMSClass("PacketPlayOutTitle");
            if(chatOutClass ==null ){
                Logging.warning("chatOut Is Null");
            }
            if(iChatBaseComponent ==null ){
                Logging.warning("iChatBaseComponent IS NULL");
            }
            if(packetPlayOutTitle ==null ){
                Logging.warning("packetPlayOutTitle IS NULL");
            }
        } catch (Exception e) {
            Logging.warning("Failed to register in Title");
        }
    }

    public void startTitle(){
        titleTask = Bukkit.getScheduler().runTaskTimerAsynchronously(FuckingWhatever.getPlugin(),()->{
            GameManager.getGameManager().getHunters().keySet().forEach((hunter)->{
                String str = StringUtils.colorise(PlaceholderAPI.setPlaceholders(hunter,"&b%hunter_hunting%"));
                sendActionMessage(hunter,str);
            });
        },0,(long)20/2);
    }
    public void stopTitle(){
        if(titleTask!=null){
            if(titleTask.isCancelled()){
                titleTask.cancel();
            }
        }
    }

    public void sendActionMessage(Player player, String message) {
        try{
            Object chatsTitle = iChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\",\"color\":\"blue\"}");
            Constructor<?> stitleConstructor = packetPlayOutTitle.getConstructor(packetPlayOutTitle.getDeclaredClasses()[0], iChatBaseComponent,
                    int.class, int.class, int.class);
            Object spacket = stitleConstructor.newInstance(packetPlayOutTitle.getDeclaredClasses()[0].getField("ACTIONBAR").get(null), chatsTitle,
                    0, 72000, 0);
            sendPacket(player,spacket);
        }catch (Exception ignored){}
    }
}
