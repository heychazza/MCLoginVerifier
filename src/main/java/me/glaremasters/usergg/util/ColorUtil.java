package me.glaremasters.usergg.util;

import org.bukkit.ChatColor;

public class ColorUtil {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
