package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.api.Service;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface ClanService extends Service {

    CompletableFuture<Clan> getClanName(String name);

    CompletableFuture<Clan> getClanPlayer(String player);

    default CompletableFuture<Clan> getClanPlayer(Player player) {
        return getClanPlayer(player.getName());
    }

    CompletableFuture<Clan> createClan(String name, Player owner);

}
