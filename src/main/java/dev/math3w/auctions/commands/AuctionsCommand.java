package dev.math3w.auctions.commands;

import dev.math3w.auctions.AuctionsPlugin;
import dev.math3w.auctions.config.MessagePlaceholder;
import dev.math3w.auctions.items.AuctionItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionsCommand implements CommandExecutor {
    private final AuctionsPlugin plugin;

    public AuctionsCommand(AuctionsPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length >= 1 && args[0].equalsIgnoreCase("sell")) {
            if (args.length < 2) {
                plugin.getMessagesConfig().sendMessage(player, "sell.invalid-price");
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().equals(Material.AIR)) {
                plugin.getMessagesConfig().sendMessage(player, "sell.no-item");
                return true;
            }

            int price;
            try {
                price = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                plugin.getMessagesConfig().sendMessage(player, "sell.invalid-price");
                return true;
            }

            AuctionItem auctionItem = new AuctionItem(player.getUniqueId(), player.getName(), item, price);

            player.getInventory().setItemInMainHand(null);
            plugin.getItemManager().addItem(auctionItem);
            plugin.getMessagesConfig().sendMessage(player, "sell.success", new MessagePlaceholder("price", String.valueOf(price)));
            return true;
        }

        plugin.getAuctionsMenu().open(player);
        return true;
    }
}
