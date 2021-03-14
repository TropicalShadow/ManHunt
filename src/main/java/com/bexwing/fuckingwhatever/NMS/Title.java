package com.bexwing.fuckingwhatever.NMS;

import com.bexwing.fuckingwhatever.GameManager;
import com.bexwing.fuckingwhatever.ManHunt;
import com.bexwing.fuckingwhatever.utils.Logging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Title extends Reflection{
    private static Class<?> chatOutClass = null;
    private static Class<?> iChatBaseComponent = null;
    private static Class<?> packetPlayOutTitle = null;
    private static BukkitTask titleTask;

    public Title(){
        try {
            if(chatOutClass == null)chatOutClass = getNMSClass("PacketPlayOutChat");
            if(iChatBaseComponent==null)iChatBaseComponent = getNMSClass("IChatBaseComponent");
            if(packetPlayOutTitle==null)packetPlayOutTitle = getNMSClass("PacketPlayOutTitle");
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
    enum CountDownNumbers{
        SIXTY(60,ChatColor.DARK_RED+"1 Minute"),
        THIRTY(30,ChatColor.DARK_RED+"30 seconds"),
        TEN(10,ChatColor.DARK_RED+"10 seconds"),
        Five(5, ChatColor.DARK_RED+"\u2464"),
        FOUR(4,ChatColor.RED+"\u2463"),
        THREE(3,ChatColor.GOLD+"\u2462"),
        TWO(2,ChatColor.GOLD+"\u2461"),
        ONE(1,ChatColor.YELLOW+"\u2460"),
        ZERO(0,ChatColor.GREEN+"Start!"),
        NONE(-1,"");

        int number;
        String symbol;

         CountDownNumbers(int num,String sym){
            number = num;
            symbol = sym;
        }
        static String getSymbolFromInt(int number){
             String output = "";
             for (CountDownNumbers countDownNumbers : CountDownNumbers.values()){
                 if(countDownNumbers.number == number) output = countDownNumbers.symbol;
             }
             return output;
        }
        static ArrayList<Integer> getNumbers(){
            ArrayList<Integer> values = new ArrayList<>();
            for (CountDownNumbers value : values()) {
                values.add(value.number);
            }
            return values;
        }
    }
    static class CountDown extends BukkitRunnable{

        int countDown;
        public CountDown(int countDown){
            this.countDown = countDown;
        }
        @Override
        public void run() {
            int local = countDown;
            if(local <= 60 || local >= 0){
                if(local>=0){
                    ManHunt.getPlugin().getServer().getScheduler().runTaskAsynchronously(ManHunt.getPlugin(),()->{
                        if(local<-1)return;
                        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                            String toSend;
                            if(local>0){
                                toSend = local+" seconds left";
                            }else{
                                toSend = "START!";
                            }
                            GameManager.getGameManager().getHotbarTitle().sendActionMessage(player,local+" seconds left");
                        });
                    });
                }
                if(CountDownNumbers.getNumbers().contains(local)){

                    AtomicInteger stay = new AtomicInteger(20);
                    AtomicInteger fade = new AtomicInteger(0);
                    if(local>5){
                        stay.set(60);
                        fade.set(20);
                    }
                    Bukkit.getServer().getOnlinePlayers().forEach(player-> player.sendTitle(CountDownNumbers.getSymbolFromInt(local)," ",fade.get(), stay.get(), fade.get()));

                }
            }

            countDown--;
            if (countDown == -2) {
                GameManager.getGameManager().startGame();
                cancel();
            }
        }
    }

    public void startCountDown(){//1m 30s 10s 5..0
        new CountDown(60).runTaskTimerAsynchronously(ManHunt.getPlugin(),0,20);
    }
}
