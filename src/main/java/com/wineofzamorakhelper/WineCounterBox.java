package com.wineofzamorakhelper;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class WineCounterBox extends InfoBox {
    private final Client client;

    private static final int INVENTORY_SIZE = 28;
    private final int itemId;

    public WineCounterBox(Client client, int itemId, BufferedImage image, Plugin plugin) {
        super(image, plugin);
        this.client = client;
        this.itemId = itemId;
    }

    @Override
    public String getText() {
        int count = getWineCount();
        return String.valueOf(count);
    }

    @Override
    public Color getTextColor() {
        int invCount = getInventoryCount();
        return invCount >= INVENTORY_SIZE ? Color.RED : Color.YELLOW;
    }

    private int getWineCount() {
        ItemContainer inv = client.getItemContainer(InventoryID.INVENTORY);
        if (inv == null) {
            return 0;
        }
        return (int) Arrays.stream(inv.getItems())
                .filter(i -> i != null && i.getId() == itemId)
                .mapToLong(Item::getQuantity)
                .sum();
    }

    private int getInventoryCount() {
        ItemContainer inv = client.getItemContainer(InventoryID.INVENTORY);
        if (inv == null) {
            return 0;
        }
        return (int) Arrays.stream(inv.getItems())
                .filter(i -> i != null && i.getId() != -1) // count only real items
                .count();
    }
}
