package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.api.Service;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface ClanService extends Service {

    Collection<Clan> getAllClans();

    Clan getClan(int id);

    Clan getClan(String name);

    Clan getClanPlayer(String name);

    default Clan getClanPlayer(Player player) {
        return getClanPlayer(player.getName());
    }

    default boolean hasClan(String name) {
        return getClanPlayer(name) != null;
    }

    default boolean hasClan(Player player) {
        return hasClan(player.getName());
    }

    void createClan(Player player, String name);

}
