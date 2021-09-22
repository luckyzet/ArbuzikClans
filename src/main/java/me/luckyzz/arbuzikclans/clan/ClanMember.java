package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.util.player.PlayerFilters;
import me.luckyzz.arbuzikclans.clan.Clan;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface ClanMember {

    Clan getClan();

    String getName();

    default Player getPlayer() {
        return PlayerFilters.byName(getName()).orElse(null);
    }

    default boolean accept(Consumer<Player> consumer) {
        Player player = getPlayer();
        if(player == null) {
            return false;
        }
        consumer.accept(player);
        return true;
    }

}
