package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMutes;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.Messages;

class TimeClanChatMute extends AbstractClanChatMute {

    private final long timeWhenExpire;

    TimeClanChatMute(QueryExecutors executors, MessageConfig<Messages> messageConfig, ClanMember whoMute, ClanMember muted, String reason, long timeWhenExpire) {
        super(executors, messageConfig, whoMute, muted, reason);
        this.timeWhenExpire = timeWhenExpire;
    }

    public long getTimeLeft() {
        return timeWhenExpire - System.currentTimeMillis();
    }

    @Override
    public boolean checkActual() {
        if(getTimeLeft() > 0) {
            return true;
        }
        unmuteSilently();
        return false;
    }
}
