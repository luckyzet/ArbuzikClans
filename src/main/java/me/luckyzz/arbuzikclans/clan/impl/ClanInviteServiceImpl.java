package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.event.ExtendedListener;
import me.luckkyyz.luckapi.util.scheduler.Scheduler;
import me.luckkyyz.luckapi.util.scheduler.SchedulerTicks;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInvite;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInviteService;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ClanInviteServiceImpl extends ExtendedListener implements ClanInviteService {

    private final ClanService clanService;
    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final Map<String, ClanInvite> inviteMap = new HashMap<>();

    private final Set<Scheduler> schedulers = new HashSet<>();

    public ClanInviteServiceImpl(Plugin plugin, ClanService clanService, ClanConfig config, MessageConfig<Messages> messageConfig) {
        super(plugin);
        this.clanService = clanService;
        this.config = config;
        this.messageConfig = messageConfig;
        schedulers.add(new ClanInviteTask(plugin));
    }

    private Collection<ClanInvite> getAllInvites() {
        return inviteMap.values();
    }

    @Override
    public void addInvite(Player player, ClanMember member) {
        if (!member.hasPossibility(RankPossibility.INVITE)) {
            member.apply(player1 -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player1));
            return;
        }
        if (clanService.hasClan(player)) {
            member.apply(player1 -> messageConfig.getAdaptiveMessage(Messages.ALREADY_CLAN_OTHER).placeholder("%name%", player.getName()).send(player1));
            return;
        }
        if (getAllInvites().stream().anyMatch(invite -> invite.getName().equals(player.getName()) && invite.getMember().equals(member))) {
            member.apply(player1 -> messageConfig.getMessage(Messages.CLAN_INVITE_ALREADY).send(player1));
            return;
        }
        if (member.getClan().getMembers().getMaxMembers() >= member.getClan().getMembers().getAllMembers().size()) {
            member.apply(player1 -> messageConfig.getMessage(Messages.CLAN_INVITE_FULL).send(player1));
            return;
        }

        ClanInvite invite = new ClanInviteImpl(this, messageConfig, player, member, config.getInt(Settings.CLAN_INVITE_TIME));
        inviteMap.put(invite.getName(), invite);

        invite.apply(player1 -> messageConfig.getAdaptiveMessage(Messages.CLAN_INVITE_SUCCESS_TARGET)
                .placeholder("%clan%", invite.getClan().getName())
                .placeholder("%name%", invite.getName()));
        member.apply(player1 -> messageConfig.getAdaptiveMessage(Messages.CLAN_INVITE_SUCCESS_EXECUTOR)
                .placeholder("%name%", member.getName()));
    }

    void removeInvite(ClanInvite invite) {
        inviteMap.remove(invite.getName(), invite);
    }

    @Override
    public ClanInvite getInvite(String name) {
        return inviteMap.get(name);
    }

    @Override
    public void cancel() {
        schedulers.forEach(Scheduler::cancel);
    }

    private class ClanInviteTask extends Scheduler {

        private ClanInviteTask(Plugin plugin) {
            super(plugin);
            runTaskTimerAsynchronously(SchedulerTicks.SECOND);
        }

        @Override
        public void run() {
            getAllInvites().removeIf(invite -> {
                if (!invite.subtractTime()) {
                    invite.apply(player -> messageConfig.getMessage(Messages.CLAN_INVITE_TIME_LEFT).send(player));
                    invite.getMember().apply(player -> messageConfig.getMessage(Messages.CLAN_INVITE_TIME_LEFT).send(player));
                    return true;
                }
                return false;
            });
        }
    }

}
