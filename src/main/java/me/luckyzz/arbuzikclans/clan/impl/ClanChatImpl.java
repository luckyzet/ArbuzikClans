package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.message.AdaptiveMessage;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.chat.ClanChat;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMute;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.DurationUtil;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

class ClanChatImpl implements ClanChat {

    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;
    private Clan clan;
    private boolean chatEnabled;

    ClanChatImpl(MessageConfig<Messages> messageConfig, QueryExecutors executors, boolean chatEnabled) {
        this.messageConfig = messageConfig;
        this.executors = executors;
        this.chatEnabled = chatEnabled;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public boolean isChatEnabled() {
        return chatEnabled;
    }

    @Override
    public void send(String message, ClanMember member) {
        if (!member.isOnline()) {
            member.apply(player -> messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(player));
            return;
        }
        Player player = member.getPlayer();
        if (!chatEnabled) {
            messageConfig.getMessage(Messages.CLAN_CHAT_DISABLED).send(player);
            return;
        }
        ClanChatMute mute = clan.getChatMutes().getMute(member);
        if (mute != null && mute.checkActual()) {
            messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTED)
                    .placeholder("%time%", DurationUtil.formatDuration(Duration.ofMillis(((TimeClanChatMute) mute).getTimeLeft())))
                    .placeholder("%reason%", mute.getReason())
                    .send(player);
            return;
        }
        if (message.isEmpty()) {
            messageConfig.getMessage(Messages.CLAN_CHAT_EMPTY).send(member.getPlayer());
            return;
        }
        AdaptiveMessage format = messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_FORMAT)
                .placeholder("%name%", member.getName())
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%message%", ColorUtils.color(message));
        clan.send(format);
    }

    @Override
    public void setChatEnabled(boolean can, ClanMember member) {
        if(!member.hasPossibility(RankPossibility.CHAT_ENABLE)) {
            member.apply(player -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player));
            return;
        }

        if (can == chatEnabled) {
            member.apply(player -> messageConfig.getMessage(Messages.SUCCESS_BUT_ALREADY).send(player));
            return;
        }
        chatEnabled = can;
        executors.async().update("UPDATE clanChats SET enabled = ? WHERE clan = ?", chatEnabled, clan.getId());

        if (chatEnabled) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_CHAT_ENABLED_EXECUTOR).send(player));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_ENABLED_LOCAL)
                    .placeholder("%name%", member.getName())
                    .placeholder("%rank%", member.getRank().getPrefix()));
        } else {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_CHAT_DISABLED_EXECUTOR).send(player));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_DISABLED_LOCAL)
                    .placeholder("%name%", member.getName())
                    .placeholder("%rank%", member.getRank().getPrefix()));
        }
    }

//    @Override
//    public void setMuted(ClanMember muted, boolean isMuted, ClanMember member) {
//        if (muted.equals(member)) {
//            member.apply(player -> messageConfig.getMessage(Messages.CLAN_CHAT_MUTE_NOT_YOURSELF).send(player));
//            return;
//        }
//        if (this.muted.contains(muted) == isMuted) {
//            member.apply(player -> messageConfig.getMessage(Messages.SUCCESS_BUT_ALREADY).send(player));
//            return;
//        }
//        if (isMuted) {
//            this.muted.add(muted);
//            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTE_ENABLED_EXECUTOR)
//                    .placeholder("%name%", muted.getName())
//                    .send(player));
//            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTE_ENABLED_LOCAL)
//                    .placeholder("%name%", member.getName())
//                    .placeholder("%rank%", member.getRank().getPrefix())
//                    .placeholder("%name_muted%", muted.getName())
//                    .placeholder("%rank_muted%", muted.getRank().getPrefix()));
//        } else {
//            this.muted.remove(muted);
//            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTE_DISABLED_EXECUTOR)
//                    .placeholder("%name%", muted.getName())
//                    .send(player));
//            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_CHAT_MUTE_DISABLED_LOCAL)
//                    .placeholder("%name%", member.getName())
//                    .placeholder("%rank%", member.getRank().getPrefix())
//                    .placeholder("%name_muted%", muted.getName())
//                    .placeholder("%rank_muted%", muted.getRank().getPrefix()));
//        }
//
//        String mutedString = this.muted.stream().map(ClanMember::getName).collect(Collectors.joining(","));
//        executors.async().update("UPDATE clanChats SET muted = ? WHERE clan = ?", mutedString, clan.getId());
//    }
//
//    @Override
//    public void setMutedSilently(ClanMember muted, boolean isMuted) {
//        if (isMuted) {
//            this.muted.add(muted);
//        } else {
//            this.muted.remove(muted);
//        }
//
//        String mutedString = this.muted.stream().map(ClanMember::getName).collect(Collectors.joining(","));
//        executors.async().update("UPDATE clanChats SET muted = ? WHERE clan = ?", mutedString, clan.getId());
//    }
}
