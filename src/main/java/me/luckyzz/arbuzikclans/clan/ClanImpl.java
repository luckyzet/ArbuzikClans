package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

class ClanImpl implements Clan {

    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;

    private final int id;
    private final LocalDateTime dateCreated;
    private String name;

    private final ClanMembersImpl members;

    ClanImpl(MessageConfig<Messages> messageConfig, QueryExecutors executors, int id, LocalDateTime dateCreated, String name, ClanMembersImpl members) {
        this.messageConfig = messageConfig;
        this.executors = executors;
        this.id = id;
        this.dateCreated = dateCreated;
        this.name = name;
        this.members = members;
    }

    ClanImpl(MessageConfig<Messages> messageConfig, QueryExecutors executors, int id, LocalDateTime dateCreated, String name, Player owner) {
        this.messageConfig = messageConfig;
        this.executors = executors;
        this.id = id;
        this.dateCreated = dateCreated;
        this.name = ColorUtils.color(name);

        members = new ClanMembersImpl(executors, this);
        members.addOwnerMember(owner);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getName() {
        return name;
    }

    private String renameClanSilently(String name) {
        String old = this.name;
        this.name = ColorUtils.color(name);
        executors.async().update("UPDATE clans SET name = ? WHERE id = ?", this.name, id);
        return old;
    }

    @Override
    public String renameClan(ClanMember member, String name) {
        String old = renameClanSilently(name);

        member.accept(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_LOCAL)
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", name).send(player));

        send(messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_LOCAL)
                .placeholder("%name%", member.getName())
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", name));
        messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_GLOBAL)
                .placeholder("%name%", member.getName())
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", name)
                .broadcast();
        return old;
    }

    @Override
    public ClanMembers getMembers() {
        return members;
    }

    @Override
    public void send(Message message) {
        members.getAllMembers().forEach(member -> member.accept(message::send));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanImpl clan = (ClanImpl) o;
        return new EqualsBuilder()
                .append(id, clan.id)
                .append(messageConfig, clan.messageConfig)
                .append(executors, clan.executors)
                .append(dateCreated, clan.dateCreated)
                .append(name, clan.name)
                .append(members, clan.members)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(messageConfig)
                .append(executors)
                .append(id)
                .append(dateCreated)
                .append(name)
                .append(members)
                .toHashCode();
    }
}
