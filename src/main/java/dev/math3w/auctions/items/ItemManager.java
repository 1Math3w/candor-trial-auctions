package dev.math3w.auctions.items;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ItemManager {
    CompletableFuture<Void> addItem(AuctionItem order);

    CompletableFuture<Void> registerPurchase(AuctionItem item, Player buyer);

    CompletableFuture<List<AuctionItem>> getItems();
}
