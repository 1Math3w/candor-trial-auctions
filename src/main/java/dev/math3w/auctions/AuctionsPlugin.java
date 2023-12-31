package dev.math3w.auctions;

import dev.math3w.auctions.commands.AuctionsCommand;
import dev.math3w.auctions.config.DatabaseConfig;
import dev.math3w.auctions.config.MessagesConfig;
import dev.math3w.auctions.items.ItemManager;
import dev.math3w.auctions.items.SQLItemManager;
import dev.math3w.auctions.menus.AuctionsMenu;
import me.zort.containr.Containr;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AuctionsPlugin extends JavaPlugin {
    private Economy economy = null;
    private MessagesConfig messagesConfig;
    private DatabaseConfig databaseConfig;
    private ItemManager itemManager;
    private AuctionsMenu auctionsMenu;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getLogger().severe("No registered Vault provider found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Containr.init(this);

        economy = rsp.getProvider();

        getCommand("auction").setExecutor(new AuctionsCommand(this));

        messagesConfig = new MessagesConfig(this);
        databaseConfig = new DatabaseConfig(this);

        itemManager = new SQLItemManager(this);

        auctionsMenu = new AuctionsMenu(this);
    }

    public Economy getEconomy() {
        return economy;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public AuctionsMenu getAuctionsMenu() {
        return auctionsMenu;
    }
}