package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.chat.ClanChat;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuestsService;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

class ClanMembersImpl implements ClanMembers {

    private final Plugin plugin;
    private final ClanService clanService;
    private final ClanConfig config;
    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;
    private final MemberDayQuestsService questsService;
    private final Map<String, ClanMember> memberMap;
    private Clan clan;

    ClanMembersImpl(Plugin plugin, ClanService clanService, ClanConfig config, QueryExecutors executors, MessageConfig<Messages> messageConfig, MemberDayQuestsService questsService, Map<String, ClanMember> memberMap) {
        this.plugin = plugin;
        this.clanService = clanService;
        this.config = config;
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.questsService = questsService;
        this.memberMap = memberMap;
    }

    @Override
    public int getMaxMembers() {
        return getClan().getUpgrades().getUpgrade(MemberUpgradeData.class)
                .map(upgrade -> ((MemberUpgradeData) upgrade.getData()).getAmount())
                .orElse(config.getInt(Settings.MAX_MEMBERS_DEFAULT));
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;

        memberMap.values().forEach(member -> {
            if (member instanceof ClanMemberImpl) {
                ((ClanMemberImpl) member).setClan(clan);
            }
        });
    }

    @Override
    public ClanMember getMember(String name) {
        return memberMap.get(name);
    }

    @Override
    public ClanMember addMemberSilently(Player player, RankRole role) {
        ClanRank rank = clan.getRanks().getRank(role);

        ClanMemberImpl newMember = new ClanMemberImpl(plugin, executors, messageConfig, player.getName(),
                rank,
                new ArrayList<>(),
                0);
        newMember.setClan(clan);
        newMember.changeQuestsSilently(questsService.getQuests(newMember).generateRandom(newMember));

        memberMap.put(newMember.getName(), newMember);
        executors.async().update("INSERT INTO clanMembers VALUES (?, ?, ?, ?)", newMember.getName(), newMember.getClan().getId(),
                newMember.getRank().getIndex(), newMember.getQuestsCompleted());

        return newMember;
    }

    @Override
    public void addMember(Player player, ClanMember member) {
        if (!member.hasPossibility(RankPossibility.INVITE)) {
            member.apply(player1 -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player1));
            return;
        }
        if (clanService.hasClan(player)) {
            messageConfig.getMessage(Messages.ALREADY_CLAN).send(player);
            member.apply(player1 -> messageConfig.getAdaptiveMessage(Messages.ALREADY_CLAN_OTHER).placeholder("%name%", player.getName()).send(player1));
            return;
        }
        if (member.getClan().getMembers().getMaxMembers() <= member.getClan().getMembers().getAllMembers().size()) {
            messageConfig.getMessage(Messages.CLAN_INVITE_ACCEPT_FULL_EXECUTOR).send(player);
            member.apply(player1 -> messageConfig.getAdaptiveMessage(Messages.CLAN_INVITE_ACCEPT_FULL_TARGET).placeholder("%name%", player.getName()).send(player1));
            return;
        }

        addMemberSilently(player, RankRole.DEFAULT);
        ClanMember newMember = memberMap.get(player.getName());
        clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_MEMBER_ADDED)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName())
                .placeholder("%target_rank%", newMember.getRank().getPrefix())
                .placeholder("%target_name%", newMember.getName()));
    }

    @Override
    public void removeMember(ClanMember member) {
        if (member.getRank().getRole() == RankRole.OWNER) {
            member.apply(player1 -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player1));
            return;
        }
        memberMap.remove(member.getName());
        executors.async().update("DELETE FROM clanMembers WHERE name = ?", member.getName());

        clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_MEMBER_LEFT)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName()));
    }

    @Override
    public void removeMember(ClanMember member, ClanMember whoRemove) {
        if (member.getName().equals(whoRemove.getName())) {
            whoRemove.apply(player1 -> messageConfig.getMessage(Messages.YOURSELF).send(player1));
            return;
        }
        if (!whoRemove.hasPossibility(RankPossibility.KICK_OTHER) || member.getRank().getRole() == RankRole.OWNER) {
            whoRemove.apply(player1 -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player1));
            return;
        }
        memberMap.remove(member.getName());
        executors.async().update("DELETE FROM clanMembers WHERE name = ?", member.getName());
        executors.async().update("DELETE FROM clanQuests WHERE name = ?", member.getName());

        Clan clan = member.getClan();
        ClanChat chat = clan.getChat();
        ClanRegion region = clan.getRegion();
        region.setAccessChestSilently(false, member);
        region.setAccessBlocksSilently(false, member);
        chat.setMutedSilently(member, false);

        clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_MEMBER_KICKED)
                .placeholder("%rank%", whoRemove.getRank().getPrefix())
                .placeholder("%name%", whoRemove.getName())
                .placeholder("%target_rank%", member.getRank().getPrefix())
                .placeholder("%target_name%", member.getName()));
    }

    @Override
    public Collection<ClanMember> getAllMembers() {
        return memberMap.values();
    }

}
