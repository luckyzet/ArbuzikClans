package me.luckyzz.arbuzikclans.clan;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

class ClanMemberImpl implements ClanMember {

    private final ClanImpl clan;
    private final String name;

    ClanMemberImpl(ClanImpl clan, String name) {
        this.clan = clan;
        this.name = name;
    }

    ClanMemberImpl(ClanImpl clan, Player player) {
        this(clan, player.getName());
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanMemberImpl that = (ClanMemberImpl) o;
        return new EqualsBuilder()
                .append(clan, that.clan)
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clan)
                .append(name)
                .toHashCode();
    }
}
