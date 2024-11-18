package net.bobthehuge.tardiscraft;

import org.apache.commons.lang3.tuple.Pair;

public class Tuple<L, R> {
    public final L left;
    public final R right;
    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
