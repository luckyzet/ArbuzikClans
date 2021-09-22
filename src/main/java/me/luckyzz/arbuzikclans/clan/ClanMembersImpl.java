package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class ClanMembersImpl implements ClanMembers {

    private final QueryExecutors executors;

    private ClanImpl clan;
    private final Map<String, ClanMemberImpl> members;

    ClanMembersImpl(QueryExecutors executors, Map<String, ClanMemberImpl> members) {
        this.executors = executors;
        this.members = members;
    }

    ClanMembersImpl(QueryExecutors executors, ClanImpl clan) {
        this(executors, new HashMap<>());
        setClan(clan);
    }

    void setClan(ClanImpl clan) {
        this.clan = clan;
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
        ClanMemberImpl member = new ClanMemberImpl(clan, player);
        members.put(member.getName(), member);
        executors.async().update("INSERT INTO clanMembers VALUES (?, ?)", member.getName(), member.getClan().getId());
    }
}
