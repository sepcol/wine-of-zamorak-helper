package com.wineofzamorakhelper;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class TimerOverlay extends Overlay {
    private final Client client;
    private final WineOfZamorakHelperPlugin plugin;

    @Inject
    private TimerOverlay(Client client, WineOfZamorakHelperPlugin plugin) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isMonkOfZamorakNearby()) {
            return null;
        }

        for (TimedItem ti : plugin.getActiveItems()) {
            int remaining = plugin.getRemainingSeconds(ti);
            if (remaining <= 0) {
                continue;
            }

            WorldPoint wp = ti.getTile().getWorldLocation();
            LocalPoint lp = LocalPoint.fromWorld(client, wp);
            if (lp == null) {
                continue;
            }

            String text = remaining + "s";
            Point textLocation = Perspective.getCanvasTextLocation(client, graphics, lp, text, 0);

            if (textLocation != null) {
                OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.YELLOW);
            }
        }

        return null;
    }

}
