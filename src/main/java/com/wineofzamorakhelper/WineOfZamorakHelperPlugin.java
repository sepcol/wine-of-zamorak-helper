package com.wineofzamorakhelper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.NPC;
import net.runelite.api.TileItem;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@PluginDescriptor(
        name = "Wine of Zamorak Helper"
)
public class WineOfZamorakHelperPlugin extends Plugin {
    private static final int WINE_OF_ZAMORAK_ITEM_ID = 245;
    private static final int TIMER_DURATION = 23;
    private static final Set<Integer> MONK_OF_ZAMORAK_NPCS_IDS = Set.of(527, 3484, 8400);

    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private TimerOverlay overlay;
    @Inject
    private ItemManager itemManager;
    @Inject
    private InfoBoxManager infoBoxManager;

    private WineCounterBox wineCounterBox;

    @Getter
    private final List<TimedItem> activeItems = new ArrayList<>();
    private static final Set<NPC> MONKS_OF_ZAMORAK_NPCS = new HashSet<>();

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        activeItems.clear();

        if (wineCounterBox != null) {
            infoBoxManager.removeInfoBox(wineCounterBox);
            wineCounterBox = null;
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        if (MONK_OF_ZAMORAK_NPCS_IDS.contains(event.getNpc().getId())) {
            MONKS_OF_ZAMORAK_NPCS.add(event.getNpc());
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        if (MONK_OF_ZAMORAK_NPCS_IDS.contains(event.getNpc().getId())) {
            MONKS_OF_ZAMORAK_NPCS.remove(event.getNpc());
        }
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned event) {
        TileItem item = event.getItem();
        if (item.getId() != WINE_OF_ZAMORAK_ITEM_ID || !isMonkOfZamorakNearby()) {
            return;
        }

        activeItems.removeIf(activeItem -> activeItem.getTile().equals(event.getTile()));
    }

    @Subscribe
    public void onItemDespawned(ItemDespawned event) {
        TileItem item = event.getItem();
        if (!isMonkOfZamorakNearby() || item.getId() != WINE_OF_ZAMORAK_ITEM_ID) {
            return;
        }

        if (item.getId() == WINE_OF_ZAMORAK_ITEM_ID) {
            boolean alreadyExists = activeItems.stream()
                    .anyMatch(activeItem -> activeItem.getTile().equals(event.getTile()));
            if (!alreadyExists) {
                activeItems.add(new TimedItem(event.getTile(), item, Instant.now()));
            }
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
            return;
        }

        if (wineCounterBox != null) {
            infoBoxManager.removeInfoBox(wineCounterBox);
        }

        BufferedImage wineImage = itemManager.getImage(WINE_OF_ZAMORAK_ITEM_ID);
        wineCounterBox = new WineCounterBox(client, WINE_OF_ZAMORAK_ITEM_ID, wineImage, this);
        infoBoxManager.addInfoBox(wineCounterBox);
    }

    public int getRemainingSeconds(TimedItem ti) {
        long elapsed = Duration.between(ti.getSpawnTime(), Instant.now()).getSeconds();
        return (int) Math.max(0, TIMER_DURATION - elapsed);
    }

    public boolean isMonkOfZamorakNearby() {
        return !MONKS_OF_ZAMORAK_NPCS.isEmpty();
    }
}
