package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

class ClanImpl implements Clan {

    private final ClanServiceImpl clanService;

    private final int id;
    private final LocalDateTime dateCreated;
    private String name;

    private final ClanMembersImpl members;

    ClanImpl(ClanServiceImpl clanService, int id, LocalDateTime dateCreated, String name, ClanMembersImpl members) {
        this.clanService = clanService;
        this.id = id;
        this.dateCreated = dateCreated;
        this.name = name;
        this.members = members;
    }

    ClanImpl(ClanServiceImpl clanService, int id, LocalDateTime dateCreated, String name, Player owner) {
        this.clanService = clanService;
        this.id = id;
        this.dateCreated = dateCreated;
        this.name = ColorUtils.color(name);

        members = new ClanMembersImpl(this);
        members.addOwnerMember(owner);
    }

    ClanServiceImpl getClanService() {
        return clanService;
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
        clanService.getExecutors().async().update("UPDATE clans SET name = ? WHERE id = ?", this.name, id);
        return old;
    }

    @Override
    public String renameClan(ClanMember member, String name) {
        String old = renameClanSilently(name);

        member.accept(player -> clanService.getMessageConfig().getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_LOCAL)
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", name).send(player));

        send(clanService.getMessageConfig().getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_LOCAL)
                .placeholder("%name%", member.getName())
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", name));
        clanService.getMessageConfig().getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_GLOBAL)
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
                .append(clanService, clan.clanService)
                .append(dateCreated, clan.dateCreated)
                .append(name, clan.name)
                .append(members, clan.members)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clanService)
                .append(id)
                .append(dateCreated)
                .append(name)
                .append(members)
                .toHashCode();
    }
}
