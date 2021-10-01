package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMute;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMutes;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.DurationUtil;

import java.time.Duration;
import java.util.Map;

class ClanChatMutesImpl implements ClanChatMutes {

    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;

    private Clan clan;

    private final Map<String, ClanChatMute> muteMap;

    ClanChatMutesImpl(QueryExecutors executors, MessageConfig<Messages> messageConfig, Map<String, ClanChatMute> muteMap) {
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.muteMap = muteMap;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;

        muteMap.values().forEach(mute -> {
            if(mute instanceof TimeClanChatMute) {
                ((TimeClanChatMute) mute).setClan(clan);
            }
        });
    }

    @Override
    public ClanChatMute getMute(ClanMember member) {
        return muteMap.get(member.getName());
    }

    @Override
    public void mute(ClanMember muted, ClanMember whoMute, long timeMute, String reason) {
        if(!whoMute.hasPossibility(RankPossibility.MUTE)) {
            whoMute.apply(player -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player));
            return;
        }
        if(muted.equals(whoMute)) {
            whoMute.apply(player -> messageConfig.getMessage(Messages.YOURSELF).send(player));
            return;
        }
        if(muteMap.containsKey(muted.getName()) && muteMap.get(muted.getName()).checkActual()) {
            whoMute.apply(player -> messageConfig.getMessage(Messages.CLAN_CHAT_MUTED_ALREADY).send(player));
            return;
        }
        long timeWhenExpire = timeMute + System.currentTimeMillis();

        TimeClanChatMute mute = new TimeClanChatMute(executors, messageConfig, whoMute, muted, reason, timeWhenExpire);
        muteMap.put(mute.getMuted().getName(), mute);
        mute.setClan(clan);
        executors.async().update("INSERT INTO clanMutes VALUES (?, ?, ?, ?, ?)", clan.getId(), muted.getName(), whoMute.getName(), mute.getReason(), timeWhenExpire);

        whoMute.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTED_EXECUTOR).placeholder("%name%", muted.getName()));
        clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTED_LOCAL)
                .placeholder("%rank%", whoMute.getRank().getPrefix())
                .placeholder("%name%", whoMute.getName())
                .placeholder("%rank_muted%", muted.getRank().getPrefix())
                .placeholder("%name_muted%", muted.getName())
                .placeholder("%reason%", mute.getReason())
                .placeholder("%duration%", DurationUtil.formatDuration(Duration.ofMillis(timeMute))));
    }

    @Override
    public void unmuteSilently(ClanChatMute mute) {
        muteMap.remove(mute.getMuted().getName());

        executors.async().update("DELETE FROM clanMutes WHERE muted = ?", mute.getMuted().getName());
    }

    @Override
    public void unmuteSilently(ClanMember muted) {
        ClanChatMute mute = muteMap.get(muted.getName());
        if(mute == null) {
            return;
        }
        unmuteSilently(mute);
    }
}
