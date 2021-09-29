package me.luckyzz.arbuzikclans.clan.member;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public interface ClanMembers {

    Clan getClan();

    int getMaxMembers();

    default boolean hasMember(String name) {
        return getMember(name) != null;
    }

    default boolean hasMember(Player player) {
        return getMember(player) != null;
    }

    ClanMember getMember(String name);

    default Collection<ClanMember> getMembers(RankRole role) {
        return getAllMembers().stream()
                .filter(member -> member.getRank().getRole() == role)
                .collect(Collectors.toSet());
    }

    default ClanMember getMember(Player player) {
        return getMember(player.getName());
    }

    ClanMember addMemberSilently(Player player, RankRole role);

    void addMember(Player player, ClanMember member);

    void removeMember(ClanMember member);

    default void removeMember(String name) {
        removeMember(getMember(name));
    }

    default void removeMember(Player player) {
        removeMember(player.getName());
    }

    void removeMember(ClanMember member, ClanMember whoRemove);

    default void removeMember(String name, ClanMember whoRemove) {
        removeMember(getMember(name), whoRemove);
    }

    default void removeMember(Player player, ClanMember whoRemove) {
        removeMember(player.getName(), whoRemove);
    }

    Collection<ClanMember> getAllMembers();

}
