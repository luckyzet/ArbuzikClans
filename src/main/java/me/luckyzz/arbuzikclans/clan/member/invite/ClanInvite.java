package me.luckyzz.arbuzikclans.clan.member.invite;

import me.luckkyyz.luckapi.util.player.PlayerFilters;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface ClanInvite {

    default Clan getClan() {
        return getMember().getClan();
    }

    String getName();

    default boolean isOnline() {
        Player player = getPlayer();
        return player != null && player.isOnline();
    }

    default Player getPlayer() {
        return PlayerFilters.byName(getName()).orElse(null);
    }

    default void apply(Consumer<Player> consumer) {
        if (!isOnline()) {
            return;
        }
        Player player = getPlayer().getPlayer();
        consumer.accept(player);
    }

    ClanMember getMember();

    boolean subtractTime();

    void acceptInvite();

    void denyInvite();

}
