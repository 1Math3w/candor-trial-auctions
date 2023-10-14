package dev.math3w.auctions.items;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public final class AuctionItem {
    private final UUID playerUniqueId;
    private final String playerName;
    private final ItemStack item;
    private final int price;
    private int databaseId = -1;

    public AuctionItem(int databaseId, UUID playerUniqueId, String playerName, ItemStack item, int price) {
        this(playerUniqueId, playerName, item, price);
        this.databaseId = databaseId;
    }

    public AuctionItem(UUID playerUniqueId, String playerName, ItemStack item, int price) {
        this.playerUniqueId = playerUniqueId;
        this.playerName = playerName;
        this.item = item;
        this.price = price;
    }

    public int databaseId() {
        return databaseId;
    }

    public UUID playerUniqueId() {
        return playerUniqueId;
    }

    public String playerName() {
        return playerName;
    }

    public ItemStack item() {
        return item;
    }

    public int price() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AuctionItem) obj;
        return Objects.equals(this.playerUniqueId, that.playerUniqueId) &&
                Objects.equals(this.item, that.item) &&
                this.price == that.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUniqueId, item, price);
    }

    @Override
    public String toString() {
        return "AuctionItem[" +
                "playerUniqueId=" + playerUniqueId + ", " +
                "item=" + item + ", " +
                "price=" + price + ']';
    }

}
