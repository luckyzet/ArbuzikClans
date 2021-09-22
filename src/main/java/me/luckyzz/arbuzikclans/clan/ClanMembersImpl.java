package me.luckyzz.arbuzikclans.clan;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class ClanMembersImpl implements ClanMembers {

    private final ClanImpl clan;
    private final Map<String, ClanMember> members;

    ClanMembersImpl(ClanImpl clan, Map<String, ClanMember> members) {
        this.clan = clan;
        this.members = members;
    }

    ClanMembersImpl(ClanImpl clan) {
        this(clan, new HashMap<>());
    }

    @Override
    public ClanImpl getClan() {
        return clan;
    }

    @Override
    public ClanMember getMember(String name) {
        return members.get(name);
    }

    @Override
    public Collection<ClanMember> getAllMembers() {
        return new HashSet<>(members.values());
    }

    @Override
    public void addOwnerMember(Player player) {
        // Check rank

        ClanMember member = new ClanMemberImpl(clan, player);
        members.put(member.getName(), member);

        clan.getClanService().getExecutors().async().update("INSERT INTO clanMembers VALUES (?, ?)", member.getName(), member.getClan().getId());
    }
}
