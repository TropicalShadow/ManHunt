package com.bexwing.fuckingwhatever.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StringUtils {


    public static String colorise(String str){
        return ChatColor.translateAlternateColorCodes('&',str);
    }

    public static void coloriseAndSend(String str, CommandSender sendTo){
        sendTo.sendMessage(colorise(str));
    }
}
