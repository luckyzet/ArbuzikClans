package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMute;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMutes;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.Messages;

abstract class AbstractClanChatMute implements ClanChatMute {

    protected final QueryExecutors executors;
    protected final MessageConfig<Messages> messageConfig;

    protected Clan clan;

    protected final ClanMember whoMute, muted;
    protected final String reason;

    protected AbstractClanChatMute(QueryExecutors executors, MessageConfig<Messages> messageConfig, ClanMember whoMute, ClanMember muted, String reason) {
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.whoMute = whoMute;
        this.muted = muted;
        this.reason = reason;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public ClanMember getWhoMute() {
        return whoMute;
    }

    @Override
    public ClanMember getMuted() {
        return muted;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void unmuteSilently() {
        clan.getChatMutes().unmuteSilently(this);
    }
}
