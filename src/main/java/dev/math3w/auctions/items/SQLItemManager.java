package dev.math3w.auctions.items;

import dev.math3w.auctions.AuctionsPlugin;
import dev.math3w.auctions.databases.MySQLDatabase;
import dev.math3w.auctions.databases.SQLDatabase;
import dev.math3w.auctions.databases.SQLiteDatabase;
import dev.math3w.auctions.utils.Utils;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLItemManager implements ItemManager {
    protected final AuctionsPlugin plugin;
    private final SQLDatabase sqlDatabase;

    public SQLItemManager(AuctionsPlugin plugin) {
        this.plugin = plugin;

        SQLDatabase.Type sqlType = plugin.getDatabaseConfig().getSqlType();
        if (sqlType == SQLDatabase.Type.MYSQL) {
            sqlDatabase = new MySQLDatabase(plugin.getDatabaseConfig().getMySQLConfig());
        } else {
            sqlDatabase = new SQLiteDatabase(plugin.getDataFolder(), "orders");
        }
        createTable();
    }

    private void createTable() {
        String autoIncrementSyntax = sqlDatabase.getType() == SQLDatabase.Type.SQLITE ? "AUTOINCREMENT" : "AUTO_INCREMENT";

        try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS items (" +
                "id INTEGER PRIMARY KEY " + autoIncrementSyntax + ", " +
                "item TEXT NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "player_uuid VARCHAR(36) NOT NULL, " +
                "player_name VARCHAR(36) NOT NULL, " +
                "buyer VARCHAR(36) DEFAULT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> addItem(AuctionItem item) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("INSERT INTO items (item, price, player_uuid, player_name) " +
                    "VALUES (?, ?, ?, ?)")) {
                statement.setString(1, Utils.serializeItem(item.item()));
                statement.setInt(2, item.price());
                statement.setString(3, String.valueOf(item.playerUniqueId()));
                statement.setString(4, item.playerName());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> registerPurchase(AuctionItem item, Player buyer) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("UPDATE items SET buyer=? WHERE id=?")) {
                statement.setString(1, buyer.getUniqueId().toString());
                statement.setInt(2, item.databaseId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<List<AuctionItem>> getItems() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("SELECT * FROM items WHERE buyer IS NULL ORDER BY created_at DESC")) {
                List<AuctionItem> items = new ArrayList<>();
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        AuctionItem item = new AuctionItem(resultSet.getInt("id"),
                                UUID.fromString(resultSet.getString("player_uuid")),
                                resultSet.getString("player_name"),
                                Utils.deserializeItem(resultSet.getString("item")),
                                resultSet.getInt("price"));
                        items.add(item);
                    }
                }

                return items;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
