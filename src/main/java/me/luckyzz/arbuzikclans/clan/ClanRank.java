package me.luckyzz.arbuzikclans.clan;

import java.util.List;

public interface ClanRank {

    enum Possibility {

        RENAME

    }

    Clan getClan();

    int getIndex();

    String getPrefix();

    void setPrefix(String prefix, ClanMember member);

    boolean hasPossibility(Possibility possibility);

}
