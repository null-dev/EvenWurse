package com.adamki11s.pathing;

/**
 * SOURCE: https://bukkit.org/threads/lib-a-pathfinding-algorithm.129786/
 *
 * @author Adamki11s
 */
public enum PathingResult {

    SUCCESS(0),
    NO_PATH(-1);

    private final int ec;

    PathingResult(int ec) {
        this.ec = ec;
    }

    public int getEndCode() {
        return this.ec;
    }

}
