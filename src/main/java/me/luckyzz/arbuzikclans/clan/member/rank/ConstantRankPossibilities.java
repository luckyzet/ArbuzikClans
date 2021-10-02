package me.luckyzz.arbuzikclans.clan.member.rank;

public final class ConstantRankPossibilities {

    private ConstantRankPossibilities() {
        throw new UnsupportedOperationException();
    }

    public static final RankPossibilities OWNER = new RankPossibilities(RankPossibility.ALL);

    public static final RankPossibilities VICE = RankPossibilities.allExcept(RankPossibility.DISBAND, RankPossibility.MONEY_TAKE);

    public static final RankPossibilities DEFAULT = new RankPossibilities();

    public static RankPossibilities fromRole(RankRole role) {
        if (role == RankRole.VICE) {
            return VICE;
        }
        if (role == RankRole.OWNER) {
            return OWNER;
        }
        return DEFAULT;
    }

}
