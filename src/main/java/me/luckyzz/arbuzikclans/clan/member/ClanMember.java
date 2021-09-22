package me.luckyzz.arbuzikclans.clan.member;

import me.luckkyyz.luckapi.util.player.PlayerFilters;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.rank.ClanRank;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface ClanMember {

    Clan getClan();

    String getName();

    ClanRank getRank();

    void setRank(ClanRank rank, ClanMember member);

    default Player getPlayer() {
        return PlayerFilters.byName(getName()).orElse(null);
    }

    default void accept(Consumer<Player> consumer) {
        Player player = getPlayer();
        if(player == null) {
            return;
        }
        consumer.accept(player);
    }

    void sendChat(String message);

}
