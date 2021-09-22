package me.luckyzz.arbuzikclans.clan.rank;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommonRankPossibilities {

    private CommonRankPossibilities() {
        throw new UnsupportedOperationException();
    }

    public static final RankPossibilities OWNER = RankPossibilities.newBuilder()
            .addIndex(7)
            .create();

    public static final RankPossibilities VICE = RankPossibilities.newBuilder()
            .addIndex(6)
            .create();

    public static final RankPossibilities DEFAULT = RankPossibilities.newBuilder()
            .addIndexes(1, 2, 3, 4, 5)
            .create();

}
