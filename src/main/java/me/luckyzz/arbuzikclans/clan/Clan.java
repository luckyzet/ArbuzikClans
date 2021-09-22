package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.date.DateFormat;
import me.luckkyyz.luckapi.util.date.FormatDate;

import java.time.LocalDateTime;

public interface Clan {

    int getId();

    LocalDateTime getDateCreated();

    default String getDateCreated(FormatDate format) {
        return DateFormat.dateTimeFormatter().format(getDateCreated(), format);
    }

    String getName();

    String renameClan(ClanMember member, String name);

    ClanMembers getMembers();

    // TODO: Rename by member

    void send(Message message);

}
