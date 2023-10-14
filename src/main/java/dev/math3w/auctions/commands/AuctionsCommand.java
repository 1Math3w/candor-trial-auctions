package dev.math3w.auctions.commands;

import dev.math3w.auctions.AuctionsPlugin;
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

        if (args.length == 1 && args[0].equalsIgnoreCase("sell")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().equals(Material.AIR)) {
                return true;
            }

            player.getInventory().setItemInMainHand(null);
            //TODO list item

            return true;
        }

        //TODO open menu

        return true;
    }
}
