package com.wineofzamorakhelper;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

import java.time.Instant;

public class TimedItem {
    private final Tile tile;
    private final TileItem item;
    private final Instant spawnTime;

    public TimedItem(Tile tile, TileItem item, Instant spawnTime) {
        this.tile = tile;
        this.item = item;
        this.spawnTime = spawnTime;
    }

    public Tile getTile() {
        return tile;
    }

    public TileItem getItem() {
        return item;
    }

    public Instant getSpawnTime() {
        return spawnTime;
    }
}

