package dev.math3w.auctions.menus;

import dev.math3w.auctions.AuctionsPlugin;
import dev.math3w.auctions.config.MessagePlaceholder;
import dev.math3w.auctions.items.AuctionItem;
import me.zort.containr.Component;
import me.zort.containr.Element;
import me.zort.containr.GUI;
import me.zort.containr.PagedContainer;
import me.zort.containr.internal.util.ItemBuilder;
import me.zort.containr.internal.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AuctionsMenu {
    private final AuctionsPlugin plugin;
    private final Element glassElement;

    public AuctionsMenu(AuctionsPlugin plugin) {
        this.plugin = plugin;
        glassElement = Component.element(ItemBuilder.newBuilder(Material.BLACK_STAINED_GLASS_PANE).withName(ChatColor.WHITE.toString()).build()).build();
    }

    public void open(Player player) {
        getGui().thenAccept(gui -> Bukkit.getScheduler().runTask(plugin, () -> gui.open(player)));
    }

    public CompletableFuture<GUI> getGui() {
        return plugin.getItemManager().getItems().thenApply(this::getGui);
    }

    public GUI getGui(List<AuctionItem> items) {
        return Component.gui()
                .title(plugin.getMessagesConfig().getMessage("menu.title"))
                .rows(6)
                .prepare((gui, player) -> {
                    setGlass(gui);

                    PagedContainer itemsContainer = Component.pagedContainer()
                            .size(7, 4)
                            .init(container -> {
                                for (AuctionItem auctionItem : items) {
                                    int price = auctionItem.price();

                                    container.appendElement(
                                            Component.element(ItemBuilder.newBuilder(auctionItem.item())
                                                            .appendLore("")
                                                            .appendLore(ChatColor.GRAY + "Price: " + ChatColor.GREEN + "$" + price)
                                                            .appendLore(ChatColor.GRAY + "Seller: " + ChatColor.GREEN + auctionItem.playerName())
                                                            .build())
                                                    .click(clickInfo -> {
                                                        Player buyer = clickInfo.getPlayer();

                                                        if (!plugin.getEconomy().has(buyer, price)) {
                                                            plugin.getMessagesConfig().sendMessage(buyer, "buy.not-enough");
                                                            return;
                                                        }

                                                        OfflinePlayer seller = Bukkit.getOfflinePlayer(auctionItem.playerUniqueId());

                                                        plugin.getEconomy().withdrawPlayer(buyer, price);
                                                        buyer.getInventory().addItem(auctionItem.item());
                                                        plugin.getItemManager().registerPurchase(auctionItem, buyer);
                                                        plugin.getMessagesConfig().sendMessage(buyer, "buy.success", new MessagePlaceholder("price", String.valueOf(price)));

                                                        plugin.getEconomy().depositPlayer(seller, price);
                                                        
                                                        open(player);
                                                    })
                                                    .build());
                                }
                                setPagingArrows(gui, container, 46, 52);
                            }).build();

                    gui.setContainer(10, itemsContainer);
                }).build();
    }

    private void setGlass(GUI gui) {
        gui.fillElement(glassElement, 0, 9);
        gui.fillElement(glassElement, 45, 54);
        gui.setElement(glassElement, 9);
        gui.setElement(glassElement, 17);
        gui.setElement(glassElement, 18);
        gui.setElement(glassElement, 26);
        gui.setElement(glassElement, 27);
        gui.setElement(glassElement, 35);
        gui.setElement(glassElement, 36);
        gui.setElement(glassElement, 44);
    }

    public void setPagingArrows(GUI gui, PagedContainer container, int previousSlot, int nextSlot) {
        if (container.getCurrentPageIndex() > 0) {
            gui.setElement(previousSlot, Component.element()
                    .click(info -> {
                        container.previousPage();
                        setPagingArrows(gui, container, previousSlot, nextSlot);
                        gui.update(info.getPlayer());
                    })
                    .item(Items.create(Material.ARROW, ChatColor.GREEN + "Previous page"))
                    .build());
        } else {
            gui.setElement(previousSlot, glassElement);
        }

        if (container.getCurrentPageIndex() < container.getMaxPageIndex()) {
            gui.setElement(nextSlot, Component.element()
                    .click(info -> {
                        container.nextPage();
                        setPagingArrows(gui, container, previousSlot, nextSlot);
                        gui.update(info.getPlayer());
                    })
                    .item(Items.create(Material.ARROW, ChatColor.GREEN + "Next page"))
                    .build());
        } else {
            gui.setElement(nextSlot, glassElement);
        }
    }
}
