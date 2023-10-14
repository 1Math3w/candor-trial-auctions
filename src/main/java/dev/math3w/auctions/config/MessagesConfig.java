package dev.math3w.auctions.config;

import dev.math3w.auctions.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesConfig extends CustomConfig {
    public MessagesConfig(JavaPlugin plugin) {
        super(plugin, "messages");
    }

    public static String formatMessage(String message, MessagePlaceholder... placeholders) {
        for (MessagePlaceholder placeholder : placeholders) {
            message = message.replaceAll("%" + placeholder.getPlaceholder() + "%", placeholder.getValue());
        }

        return Utils.colorize(message);
    }

    public String getMessage(String path, MessagePlaceholder... placeholders) {
        return formatMessage(getConfig().getString(path, "&cMissing message: " + getFile().getName() + "/" + path), placeholders);
    }

    public void sendMessage(Player player, String path, MessagePlaceholder... placeholders) {
        String message = getMessage(path, placeholders);
        if (message.isEmpty()) return;
        player.sendMessage(message);
    }

    @Override
    protected void addDefaults() {
        addDefault("sell.no-item", "&cYou're not holding any item in your hand!");
        addDefault("sell.invalid-price", "&cThe price you entered is not valid!");
        addDefault("sell.success", "&aYour item has been successfully auctioned for %price%!");
        addDefault("buy.not-enough", "&cYou don't have enough money to purchase this item!");
        addDefault("buy.success", "&aYou have successfully purchased an item for %price%!");
    }
}
