package dev.math3w.auctions.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern hexColorPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    private Utils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }


    public static String colorize(String message) {
        Matcher matcher = hexColorPattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
            matcher = hexColorPattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
